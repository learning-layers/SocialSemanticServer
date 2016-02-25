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
package at.tugraz.sss.servs.dataexport.impl;

import at.kc.tugraz.ss.category.api.*;
import at.kc.tugraz.ss.category.datatypes.*;
import at.kc.tugraz.ss.category.datatypes.par.*;
import at.tugraz.sss.conf.SSConf;
import at.tugraz.sss.serv.entity.api.SSEntityServerI;
import at.tugraz.sss.serv.datatype.par.SSCircleGetPar;
import at.tugraz.sss.serv.util.SSDateU;
import at.tugraz.sss.serv.util.SSEncodingU;
import at.tugraz.sss.serv.util.SSFileU;
import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.*;
import at.kc.tugraz.ss.serv.job.dataexport.api.SSDataExportClientI;
import at.kc.tugraz.ss.serv.job.dataexport.api.SSDataExportServerI;
import at.kc.tugraz.ss.serv.job.dataexport.conf.SSDataExportConf;
import at.kc.tugraz.ss.serv.job.dataexport.datatypes.par.SSDataExportUserEntityTagsCategoriesTimestampsLinePar;
import at.kc.tugraz.ss.serv.job.dataexport.datatypes.par.SSDataExportUsersEntitiesTagsCategoriesTimestampsFilePar;
import at.kc.tugraz.ss.serv.job.dataexport.datatypes.par.SSDataExportUserRelationsPar;
import at.kc.tugraz.ss.serv.job.dataexport.datatypes.par.SSDataExportUsersEntitiesTagsCategoriesTimestampsFileFromCirclePar;
import at.kc.tugraz.ss.service.tag.api.*;
import at.kc.tugraz.ss.service.tag.datatypes.*;
import at.kc.tugraz.ss.service.tag.datatypes.pars.*;
import at.kc.tugraz.ss.service.user.api.SSUserServerI;
import at.kc.tugraz.ss.service.user.datatypes.pars.SSUsersGetPar;
import at.tugraz.sss.serv.conf.api.SSConfA;
import at.tugraz.sss.serv.db.api.SSDBNoSQLI;
import at.tugraz.sss.serv.db.api.SSDBSQLI;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.datatype.SSCircle;
import at.tugraz.sss.serv.datatype.SSEntityContext;
import at.tugraz.sss.serv.reg.*;
import au.com.bytecode.opencsv.CSVWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.impl.api.SSServImplWithDBA;
import at.tugraz.sss.serv.datatype.par.*;
import java.io.IOException;

public class SSDataExportImpl
extends
  SSServImplWithDBA
implements
  SSDataExportClientI,
  SSDataExportServerI{
  
  public SSDataExportImpl(final SSConfA conf) throws SSErr{
    
    super(conf, (SSDBSQLI) SSServReg.getServ(SSDBSQLI.class), (SSDBNoSQLI) SSServReg.getServ(SSDBNoSQLI.class));
  }
  
  @Override
  public void dataExportUsersEntitiesTagsCategoriesTimestampsFileFromCircle(
    final SSDataExportUsersEntitiesTagsCategoriesTimestampsFileFromCirclePar par) throws SSErr{
    
    CSVWriter                                           fileWriter = null;
    FileOutputStream                                    out        = null;
    OutputStreamWriter                                  writer     = null;
    
    try{
      
      final Map<String, List<String>>           tagsPerEntities       = new HashMap<>();
      final Map<String, List<String>>           categoriesPerEntities = new HashMap<>();
      final List<String>                        lineParts             = new ArrayList<>();
      String                                    resourceString;
      
      out        = SSFileU.openOrCreateFileWithPathForWrite (conf.getSssWorkDirDataCsv() + par.fileName);
      writer     = new OutputStreamWriter                   (out,    Charset.forName(SSEncodingU.utf8.toString()));
      fileWriter = new CSVWriter                            (writer, SSStrU.semiColon.charAt(0));
      
      final SSCircle circle =
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).circleGet(
          new SSCircleGetPar(
            par,
            par.user,
            par.circle,
            null, //entityTypesToIncludeOnly
            false,  //setTags
            null, //tagSpace
            true, //setEntities,
            false, //setUsers
            false,  //withUserRestriction
            false)); //invokeEntityHandlers
      
      tagsPerEntities.clear();
      categoriesPerEntities.clear();
      
      tagsPerEntities.putAll(
        getTagsOfUserPerEntities(
          par,
          par.user,
          null,
          null,
          par.circle));
      
      categoriesPerEntities.putAll(
        getCategoriesPerEntities(
          par,
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
        
        fileWriter.flush();
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      
      if(out != null){
        
        try{
          out.close();
        }catch(IOException ioError){
          SSLogU.warn(SSWarnE.outputStreamCloseFailed, ioError);
        }
      }
    }
  }
  
  @Override
  public void dataExportUsersEntitiesTagsCategoriesTimestampsFile(final SSDataExportUsersEntitiesTagsCategoriesTimestampsFilePar par) throws SSErr{
    
    CSVWriter                                           fileWriter = null;
    FileOutputStream                                    out        = null;
    OutputStreamWriter                                  writer     = null;
    
    try{
      final Map<String, List<SSEntityContext>>  usersEntities         = new HashMap<>();
      final List<String>                        lineParts             = new ArrayList<>();
      final List<String>                        allUsers;
      SSUri                                     user;
      
      out        = SSFileU.openOrCreateFileWithPathForWrite (conf.getSssWorkDirDataCsv() + par.fileName);
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
                  par,
                  par.user, //user
                  null, //users
                  null, //emals
                  false))); //invokeEntityHandlers
          
          SSStrU.remove(allUsers, SSConf.systemUserUri);
          
        }catch(SSErr error){
          
          switch(error.code){
            case servInvalid: SSLogU.warn(error); return;
            default: {
              SSServErrReg.regErrThrow(error);
              return;
            }
          }
        }
      }
      
      for(String userStr : allUsers){
        usersEntities.put(userStr, new ArrayList<>());
      }
      
      SSServReg.inst.getUsersResources(par, usersEntities);
      
      for(Map.Entry<String, List<SSEntityContext>> resourcesForUser : usersEntities.entrySet()){
        
        user = SSUri.get(resourcesForUser.getKey());
        
        for(SSEntityContext entity : resourcesForUser.getValue()){
          
          //user;entity;timestamp;content;context;
          lineParts.clear();
          
          //user
          lineParts.add(SSStrU.toStr(user));
          
          //entity
          lineParts.add(SSStrU.toStr(entity.id));
          
          //timestamp
          if(entity.timestamp != null){
            lineParts.add(SSStrU.toStr(entity.timestamp / 1000));
          }else{
            lineParts.add(SSStrU.empty);
          }
          
          //content
          switch(entity.context){
            
            case category:
            case tag:{
              lineParts.add(entity.content);
              break;
            }
            
            default:{
              lineParts.add(SSStrU.empty);
              break;
            }
          }
          
          //context
          lineParts.add(SSStrU.toStr(entity.context));
          
          fileWriter.writeNext((String[]) lineParts.toArray(new String[lineParts.size()]));
          
          fileWriter.flush();
        }
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      
      if(out != null){
        
        try{
          out.close();
        }catch(IOException ioError){
          SSLogU.warn(SSWarnE.outputStreamCloseFailed, ioError);
        }
      }
    }
  }
  
  @Override
  public void dataExportUserEntityTagsCategoriesTimestampsLine(final SSDataExportUserEntityTagsCategoriesTimestampsLinePar par) throws SSErr{
    
    CSVWriter                                           fileWriter = null;
    FileOutputStream                                    out        = null;
    OutputStreamWriter                                  writer     = null;
    
    try{
      
      final List<String> lineParts = new ArrayList<>();
      
      out        = SSFileU.openOrCreateFileWithPathForAppend  (conf.getSssWorkDirDataCsv() + par.fileName);
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
      
      fileWriter.flush();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      
      if(out != null){
        
        try{
          out.close();
        }catch(IOException ioError){
          SSLogU.warn(SSWarnE.outputStreamCloseFailed, ioError);
        }
      }
    }
  }
  
  @Override
  public void dataExportUserRelations(final SSDataExportUserRelationsPar par) throws SSErr{
    
    CSVWriter           fileWriter = null;
    FileOutputStream    out        = null;
    OutputStreamWriter  writer     = null;
    
    try{
      
      final Map<String, List<SSUri>>     userRelations = new HashMap<>();
      final List<String>                 lineParts     = new ArrayList<>();
      final List<String>                 allUsers;
      List<SSUri>                        users;
      
      try{
        allUsers =
          SSStrU.toStr(
            ((SSUserServerI) SSServReg.getServ(SSUserServerI.class)).usersGet(
              new SSUsersGetPar(
                par,
                null, //user
                null, //users
                null, //emals
                false))); //invokeEntityHandlers
        
      }catch(SSErr error){
        
        switch(error.code){
          case servInvalid: SSLogU.warn(error); return;
          default:{
            SSServErrReg.regErrThrow(error);
            return;
          }
        }
      }
      
      SSServReg.inst.getUserRelations(par, allUsers, userRelations);
      
      out =
        SSFileU.openOrCreateFileWithPathForWrite (
          conf.getSssWorkDirDataCsv() + ((SSDataExportConf)conf).fileNameForUserRelationsExport);
      
      writer =
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
        
        fileWriter.flush();
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      
      if(out != null){
        
        try {
          out.close();
        } catch (IOException ioError) {
          SSLogU.warn(SSWarnE.outputStreamCloseFailed, ioError);
        }
      }
    }
  }

  private Map<String, List<String>> getTagsOfUserPerEntities(
    final SSServPar      servPar,
    final SSUri          userUri,
    final SSUri          forUser,
    final List<SSUri>    entities,
    final SSUri          circle) throws SSErr{
    
    return SSTag.getTagLabelsPerEntities(
      ((SSTagServerI) SSServReg.getServ(SSTagServerI.class)).tagsGet(
        new SSTagsGetPar(
          servPar,
          userUri,
          forUser, //forUser
          entities, //entities
          null, //labels
          null, //labelSearchOp
          SSSpaceE.asListWithoutNull(), //spaces
          SSUri.asListNotNull(circle), //circles
          null, //startTime
          false))); //withUserRestriction
  }
  
  private Map<String, List<String>> getCategoriesPerEntities(
    final SSServPar      servPar,
    final SSUri          userUri,
    final SSUri          forUser,
    final List<SSUri>    entities,
    final SSUri          circle) throws SSErr{
    
    return SSCategory.getCategoryLabelsPerEntities(
      ((SSCategoryServerI) SSServReg.getServ(SSCategoryServerI.class)).categoriesGet(
        new SSCategoriesGetPar(
          servPar,
          userUri,
          forUser, //forUser
          entities, //entities
          null, //labels
          null, //labelSearchOp
          null, //spaces
          SSUri.asListNotNull(circle), //circles
          null, //startTime
          false))); //withUserRestriction
  }
}