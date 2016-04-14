/**
 * Code contributed to the Learning Layers project
 * http://www.learning-layers.eu
 * Development is partly funded by the FP7 Programme of the European Commission under
 * Grant Agreement FP7-ICT-318209.
 * Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
  
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

/**
 * @author Dieter Theiler
 */

package at.tugraz.sss.servs.dataexport.impl;

import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSServPar;
import at.tugraz.sss.servs.entity.datatype.SSWarnE;
import at.tugraz.sss.servs.entity.datatype.SSSpaceE;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.category.datatype.SSCategoriesGetPar;
import at.tugraz.sss.servs.category.datatype.SSCategory;
import at.tugraz.sss.servs.conf.SSConf;
import at.tugraz.sss.servs.entity.datatype.SSCircleGetPar;
import at.tugraz.sss.servs.util.SSDateU;
import at.tugraz.sss.servs.util.SSEncodingU;
import at.tugraz.sss.servs.util.SSFileU;
import at.tugraz.sss.servs.util.SSLogU;
import at.tugraz.sss.servs.dataexport.api.SSDataExportServerI;
import at.tugraz.sss.servs.dataexport.conf.SSDataExportConf;
import at.tugraz.sss.servs.dataexport.datatype.*;
import at.tugraz.sss.servs.tag.api.*;
import at.tugraz.sss.servs.tag.datatype.*;
import at.tugraz.sss.servs.user.datatype.SSUsersGetPar;
import at.tugraz.sss.servs.entity.datatype.SSEntity;
import at.tugraz.sss.servs.entity.datatype.SSCircle;
import at.tugraz.sss.servs.entity.datatype.SSEntityContext;
import au.com.bytecode.opencsv.CSVWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.common.impl.SSServErrReg;
import at.tugraz.sss.servs.category.api.*;
import at.tugraz.sss.servs.category.impl.*;
import at.tugraz.sss.servs.conf.*;
import at.tugraz.sss.servs.entity.impl.*;
import at.tugraz.sss.servs.tag.impl.*;
import at.tugraz.sss.servs.user.impl.*;
import java.io.IOException;
import java.util.*;

public class SSDataExportImpl
  extends SSEntityImpl
  implements SSDataExportServerI{
  
  public SSDataExportImpl() throws SSErr{
    super(SSCoreConf.instGet().getDataExport());
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
      
      out        = SSFileU.openOrCreateFileWithPathForWrite (SSConf.getSssWorkDirDataCsv() + par.fileName);
      writer     = new OutputStreamWriter                   (out,    Charset.forName(SSEncodingU.utf8.toString()));
      fileWriter = new CSVWriter                            (writer, SSStrU.semiColon.charAt(0));
      
      final SSCircle circle =
        circleGet(
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
      final SSUserImpl                          userServ              = new SSUserImpl();
      final List<String>                        allUsers;
      SSUri                                     user;
      
      out        = SSFileU.openOrCreateFileWithPathForWrite (SSConf.getSssWorkDirDataCsv() + par.fileName);
      writer     = new OutputStreamWriter                   (out,    Charset.forName(SSEncodingU.utf8.toString()));
      fileWriter = new CSVWriter                            (writer, SSStrU.semiColon.charAt(0));
      
      if(!par.users.isEmpty()){
        allUsers = SSStrU.toStr(par.users);
      }else{
        
        allUsers =
          SSStrU.toStr(
            userServ.usersGet(
              new SSUsersGetPar(
                par,
                par.user, //user
                null, //users
                null, //emals
                false))); //invokeEntityHandlers
        
        SSStrU.remove(allUsers, SSConf.systemUserUri);
      }
      
      for(String userStr : allUsers){
        usersEntities.put(userStr, new ArrayList<>());
      }
      
      getUsersResources.getUsersResources(par, usersEntities);
      
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
            lineParts.add(SSStrU.toStr(new Date().getTime() / 1000));
          }
          
          //content
          switch(entity.context){
            
            case category:{
              lineParts.add(SSStrU.empty);
              lineParts.add(entity.content);
              break;
            }
            
            case tag:{
              lineParts.add(entity.content);
              lineParts.add(SSStrU.empty);
              break;
            }
            
            default:{
              lineParts.add(SSStrU.empty);
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
      
      out        = SSFileU.openOrCreateFileWithPathForAppend  (SSConf.getSssWorkDirDataCsv() + par.fileName);
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
      final SSUserImpl                   userServ      = new SSUserImpl();
      final List<String>                 allUsers;
      List<SSUri>                        users;
      
      allUsers =
        SSStrU.toStr(
          userServ.usersGet(
            new SSUsersGetPar(
              par,
              null, //user
              null, //users
              null, //emals
              false))); //invokeEntityHandlers
        
      getUserRelations.getUserRelations(par, allUsers, userRelations);
      
      out =
        SSFileU.openOrCreateFileWithPathForWrite (
          SSConf.getSssWorkDirDataCsv() + ((SSDataExportConf)conf).fileNameForUserRelationsExport);
      
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
    
    final SSTagServerI tagServ = new SSTagImpl();
    
    return SSTag.getTagLabelsPerEntities(
      tagServ.tagsGet(
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
    
    final SSCategoryServerI categoryServ = new SSCategoryImpl();
    
    return SSCategory.getCategoryLabelsPerEntities(
      categoryServ.categoriesGet(
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