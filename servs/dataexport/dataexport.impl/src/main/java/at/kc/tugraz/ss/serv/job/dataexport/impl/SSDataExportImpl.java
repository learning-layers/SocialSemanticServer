 /**
  * Code contributed to the Learning Layers project
  * http://www.learning-layers.eu
  * Development is partly funded by the FP7 Programme of the European Commission under
  * Grant Agreement FP7-ICT-318209.
  * Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
  * For a list of contributors see the AUTHORS file at the top-level directory of this distribution.
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package at.kc.tugraz.ss.serv.job.dataexport.impl;

import at.kc.tugraz.ss.circle.api.SSCircleServerI;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleGetPar;
import at.tugraz.sss.serv.SSDateU;
import at.tugraz.sss.serv.SSEncodingU;
import at.tugraz.sss.serv.SSFileU;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSServPar;
import at.kc.tugraz.ss.serv.job.dataexport.api.SSDataExportClientI;
import at.kc.tugraz.ss.serv.job.dataexport.api.SSDataExportServerI;
import at.kc.tugraz.ss.serv.job.dataexport.conf.SSDataExportConf;
import at.kc.tugraz.ss.serv.job.dataexport.datatypes.par.SSDataExportUserEntityTagsCategoriesTimestampsLinePar;
import at.kc.tugraz.ss.serv.job.dataexport.datatypes.par.SSDataExportUsersEntitiesTagsCategoriesTimestampsFilePar;
import at.kc.tugraz.ss.serv.job.dataexport.datatypes.par.SSDataExportUserRelationsPar;
import at.kc.tugraz.ss.serv.job.dataexport.datatypes.par.SSDataExportUsersEntitiesTagsCategoriesTimestampsFileFromCirclePar;
import at.kc.tugraz.ss.serv.job.dataexport.impl.fct.SSDataExportFct;
import at.kc.tugraz.ss.service.user.api.SSUserServerI;
import at.kc.tugraz.ss.service.user.datatypes.pars.SSUsersGetPar;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityCircle;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSUserRelationGathererI;
import at.tugraz.sss.serv.SSUsersResourcesGathererI;
import au.com.bytecode.opencsv.CSVWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSServContainerI;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServImplWithDBA;

public class SSDataExportImpl extends SSServImplWithDBA implements SSDataExportClientI, SSDataExportServerI{
  
  public SSDataExportImpl(final SSConfA conf) throws Exception{
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
  }
  
  @Override
  public void dataExportUsersEntitiesTagsCategoriesTimestampsFileFromCircle(
    final SSDataExportUsersEntitiesTagsCategoriesTimestampsFileFromCirclePar par) throws Exception{
    
    CSVWriter                                           fileWriter = null;
    FileOutputStream                                    out        = null;
    OutputStreamWriter                                  writer     = null;
    
    try{
      
      final Map<String, List<String>>           tagsPerEntities       = new HashMap<>();
      final Map<String, List<String>>           categoriesPerEntities = new HashMap<>();
      final List<String>                        lineParts             = new ArrayList<>();
      String                                    resourceString;
      
      out        = SSFileU.openOrCreateFileWithPathForWrite (SSFileU.dirWorkingDataCsv() + par.fileName);
      writer     = new OutputStreamWriter                   (out,    Charset.forName(SSEncodingU.utf8.toString()));
      fileWriter = new CSVWriter                            (writer, SSStrU.semiColon.charAt(0));
      
      final SSEntityCircle circle =
        ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circleGet(
          new SSCircleGetPar(
            null,
            null,
            par.user,
            par.circle,
            null, //entityTypesToIncludeOnly
            false,  //setTags
            null, //tagSpace
            false,  //withUserRestriction
            false)); //invokeEntityHandlers
      
      tagsPerEntities.clear();
      categoriesPerEntities.clear();
      
      tagsPerEntities.putAll(
        SSDataExportFct.getTagsOfUserPerEntities(
          par.user,
          null,
          null,
          par.circle));
      
      categoriesPerEntities.putAll(
        SSDataExportFct.getCategoriesPerEntities(
          par.user, //forUser
          null, //forUser
          null,
          par.circle));
      
      for(SSEntity entity : circle.entities){
        
        resourceString = SSStrU.toStr(entity.id);
        
        lineParts.clear();
        
        lineParts.add(SSStrU.toStr     (circle.id)); //user
        lineParts.add(resourceString);
        lineParts.add(SSStrU.toStr     (SSDateU.dateAsLong() / 1000)); //TODO: provide tag time stamps for tags
        
        if(
          tagsPerEntities.containsKey(resourceString)){
          lineParts.add(StringUtils.join(tagsPerEntities.get(resourceString),SSStrU.comma));
        }else{
          lineParts.add(SSStrU.empty);
        }
        
        if(
          categoriesPerEntities.containsKey(resourceString)){
          lineParts.add(StringUtils.join(categoriesPerEntities.get(resourceString),SSStrU.comma));
        }else{
          lineParts.add(SSStrU.empty);
        }
        
        fileWriter.writeNext((String[]) lineParts.toArray(new String[lineParts.size()]));
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      
      if(fileWriter != null){
        fileWriter.close();
      }else{
        
        if(writer != null){
          writer.close();
        }else{
          
          if(out != null){
            out.close();
          }
        }
      }
    }
  }
  
  @Override
  public void dataExportUsersEntitiesTagsCategoriesTimestampsFile(final SSServPar parA) throws Exception{
    
    final SSDataExportUsersEntitiesTagsCategoriesTimestampsFilePar par        = new SSDataExportUsersEntitiesTagsCategoriesTimestampsFilePar(parA);
    CSVWriter                                           fileWriter = null;
    FileOutputStream                                    out        = null;
    OutputStreamWriter                                  writer     = null;
    
    try{
      
      final Map<String, List<SSUri>>            usersResources        = new HashMap<>();
      final Map<String, List<String>>           tagsPerEntities       = new HashMap<>();
      final Map<String, List<String>>           categoriesPerEntities = new HashMap<>();
      final List<String>                        lineParts             = new ArrayList<>();
      final List<String>                        allUsers;
      SSUri                                     user;
      String                                    resourceString;
      
      out        = SSFileU.openOrCreateFileWithPathForWrite (SSFileU.dirWorkingDataCsv() + par.fileName);
      writer     = new OutputStreamWriter                   (out,    Charset.forName(SSEncodingU.utf8.toString()));
      fileWriter = new CSVWriter                            (writer, SSStrU.semiColon.charAt(0));
      
      if(!par.users.isEmpty()){
        allUsers = SSStrU.toStr(par.users);
      }else{
        
        try{
          allUsers =
            SSStrU.toStr(
              ((SSUserServerI) SSServReg.getServ(SSUserServerI.class)).usersGet(
                new SSUsersGetPar(
                  null,
                  null,
                  par.user, //user
                  null, //users
                  false))); //invokeEntityHandlers
          
        }catch(SSErr error){
          
          switch(error.code){
            case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); return;
            default: SSServErrReg.regErrThrow(error); return;
          }
        }
      }
      
      for(SSServContainerI serv : SSServReg.inst.getServsGatheringUsersResources()){
        ((SSUsersResourcesGathererI) serv.serv()).getUsersResources(allUsers, usersResources);
      }
      
      for(Map.Entry<String, List<SSUri>> resourcesForUser : usersResources.entrySet()){
        
        user = SSUri.get(resourcesForUser.getKey());
        
        tagsPerEntities.clear();
        categoriesPerEntities.clear();
        
        if(par.exportTags){
          
          tagsPerEntities.putAll(
            SSDataExportFct.getTagsOfUserPerEntities(
              user,
              user, //forUser
              resourcesForUser.getValue(),
              null)); //circle
        }
        
        if(par.exportCategories){
          categoriesPerEntities.putAll(
            SSDataExportFct.getCategoriesPerEntities(
              user,
              user, //forUser
              resourcesForUser.getValue(),
              null)); //circle
        }
        
        for(SSUri resource : resourcesForUser.getValue()){
          
          resourceString = SSStrU.toStr(resource);
          
          lineParts.clear();
          
          lineParts.add(SSStrU.toStr     (user));
          lineParts.add(SSStrU.toStr     (resource));
          lineParts.add(SSStrU.toStr     (SSDateU.dateAsLong() / 1000)); //TODO: provide tag time stamps for tags
          
          if(
            tagsPerEntities.containsKey(resourceString)){
            lineParts.add(StringUtils.join(tagsPerEntities.get(resourceString),SSStrU.comma));
          }else{
            lineParts.add(SSStrU.empty);
          }
          
          if(
            categoriesPerEntities.containsKey(resourceString)){
            lineParts.add(StringUtils.join(categoriesPerEntities.get(resourceString),SSStrU.comma));
          }else{
            lineParts.add(SSStrU.empty);
          }
          
          fileWriter.writeNext((String[]) lineParts.toArray(new String[lineParts.size()]));
        }
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      
      if(fileWriter != null){
        fileWriter.close();
      }else{
        
        if(writer != null){
          writer.close();
        }else{
          
          if(out != null){
            out.close();
          }
        }
      }
    }
  }
  
  @Override
  public void dataExportUserEntityTagsCategoriesTimestampsLine(final SSServPar parA) throws Exception{
    
    final SSDataExportUserEntityTagsCategoriesTimestampsLinePar par        = new SSDataExportUserEntityTagsCategoriesTimestampsLinePar(parA);
    CSVWriter                                           fileWriter = null;
    FileOutputStream                                    out        = null;
    OutputStreamWriter                                  writer     = null;
    
    try{
      
      final List<String> lineParts = new ArrayList<>();
      
      out        = SSFileU.openOrCreateFileWithPathForAppend  (SSFileU.dirWorkingDataCsv() + par.fileName);
      writer     = new OutputStreamWriter                     (out,    Charset.forName(SSEncodingU.utf8.toString()));
      fileWriter = new CSVWriter                              (writer, SSStrU.semiColon.charAt(0));
      
      lineParts.add(SSStrU.toStr     (par.forUser));
      lineParts.add(SSStrU.toStr     (par.entity));
      lineParts.add(SSStrU.toStr     (SSDateU.dateAsLong() / 1000)); //TODO: provide tag time stamps for tags
      
      if(!par.tags.isEmpty()){
        lineParts.add(StringUtils.join(par.tags, SSStrU.comma));
      }else{
        lineParts.add(SSStrU.empty);
      }
      
      if(!par.categories.isEmpty()){
        lineParts.add(StringUtils.join(par.categories, SSStrU.comma));
      }else{
        lineParts.add(SSStrU.empty);
      }
      
      fileWriter.writeNext((String[]) lineParts.toArray(new String[lineParts.size()]));
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      
      if(fileWriter != null){
        fileWriter.close();
      }else{
        
        if(writer != null){
          writer.close();
        }else{
          
          if(out != null){
            out.close();
          }
        }
      }
    }
  }
  
  @Override
  public void dataExportUserRelations(final SSServPar parA) throws Exception{
    
    CSVWriter fileWriter = null;
    
    try{
      
      final SSDataExportUserRelationsPar par           = new SSDataExportUserRelationsPar(parA);
      final Map<String, List<SSUri>>     userRelations = new HashMap<>();
      final List<String>                 lineParts     = new ArrayList<>();
      final List<String>                 allUsers;
      List<SSUri>                        users;
      
      try{
        allUsers =
          SSStrU.toStr(
            ((SSUserServerI) SSServReg.getServ(SSUserServerI.class)).usersGet(
              new SSUsersGetPar(
                null,
                null,
                null, //user
                null, //users
                false))); //invokeEntityHandlers
        
      }catch(SSErr error){
        
        switch(error.code){
          case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); return;
          default: SSServErrReg.regErrThrow(error); return;
        }
      }
      
      for(SSServContainerI serv : SSServReg.inst.getServsGatheringUserRelations()){
        ((SSUserRelationGathererI) serv.serv()).getUserRelations(allUsers, userRelations);
      }
      
      final FileOutputStream out =
        SSFileU.openOrCreateFileWithPathForWrite (
          SSFileU.dirWorkingDataCsv() + ((SSDataExportConf)conf).fileNameForUserRelationsExport);
      
      final OutputStreamWriter writer =
        new OutputStreamWriter(
          out,
          Charset.forName(SSEncodingU.utf8.toString()));
      
      fileWriter =
        new CSVWriter(
          writer,
          SSStrU.semiColon.charAt(0));
      
      for(Map.Entry<String, List<SSUri>> entry : userRelations.entrySet()){
        
        lineParts.clear();
        
        users = new ArrayList<>(entry.getValue());
        
        SSStrU.remove(users, entry.getKey());
        
        lineParts.add(entry.getKey());
        lineParts.add(StringUtils.join(users, SSStrU.comma));
        
        fileWriter.writeNext((String[]) lineParts.toArray(new String[lineParts.size()]));
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      
      if(fileWriter != null){
        fileWriter.close();
      }
    }
  }
  
  
}