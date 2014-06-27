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
package at.kc.tugraz.ss.serv.datatypes.learnep.impl.fct.misc;

import at.kc.tugraz.socialserver.utils.SSObjU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEp;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpCircle;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpEntity;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpVersion;
import at.kc.tugraz.ss.serv.datatypes.learnep.impl.fct.sql.SSLearnEpSQLFct;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import java.util.ArrayList;
import java.util.List;

public class SSLearnEpMiscFct{
  
  public static SSUri copyLearnEpForUser(
    final SSLearnEpSQLFct sqlFct,
    final SSUri           user,
    final SSUri           forUser,
    final List<SSUri>     entitiesToExclude,
    final SSUri           learnEpUri) throws Exception{
    
    try{
      
      if(SSObjU.isNull(sqlFct, user, forUser, learnEpUri)){
        throw new Exception("pars null");
      }
      
      final SSLearnEp learnEp = sqlFct.getLearnEpWithVersions(user, learnEpUri, true);
      SSUri           copyVersionUri;
      
      final SSUri copyLearnEpUri = 
        SSServCaller.learnEpCreate(
          forUser, 
          learnEp.label, 
          false);
      
      for(SSLearnEpVersion version : learnEp.versions){

        copyVersionUri = SSServCaller.learnEpVersionCreate(forUser, copyLearnEpUri, false);
        
        for(SSLearnEpCircle circle : version.circles){
          
          if(SSStrU.contains(entitiesToExclude, circle.id)){
            continue;
          }
            
          SSServCaller.learnEpVersionAddCircle(
            forUser, 
            copyVersionUri, 
            circle.label, 
            circle.xLabel, 
            circle.yLabel, 
            circle.xR, 
            circle.yR, 
            circle.xC, 
            circle.yC,
            false);
        }
        
        for(SSLearnEpEntity entity : version.entities){
          
          if(
            SSStrU.contains(entitiesToExclude, entity.id) ||
            SSStrU.contains(entitiesToExclude, entity.entity)){
            continue;
          }
                    
          SSServCaller.learnEpVersionAddEntity(
            forUser, 
            copyVersionUri, 
            entity.entity, 
            entity.x, 
            entity.y, 
            false);
        }
        
        if(version.learnEpTimelineState != null){
        
          SSServCaller.learnEpVersionSetTimelineState(
            forUser, 
            copyVersionUri, 
            version.learnEpTimelineState.startTime,
            version.learnEpTimelineState.endTime, 
            false);
        }
      }
      
      return copyLearnEpUri;

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public static void shareLearnEpWithUser(
    final SSLearnEpSQLFct sqlFct,
    final SSUri           user,
    final SSUri           forUser,
    final SSUri           learnEp,
    final SSUri           circle) throws Exception{
    
    try{
      
      if(SSObjU.isNull(sqlFct, user, forUser, learnEp, circle)){
        throw new Exception("pars null");
      }
      
      sqlFct.addLearnEp(learnEp, forUser);
      
      addLearnEpWithContentToCircle(
        sqlFct,
        user,
        learnEp,
        circle);
      
      SSServCaller.entityUsersToCircleAdd(
        user,
        circle,
        sqlFct.getLearnEpUserURIs(learnEp),
        false);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static void addLearnEpWithContentToCircle(
    final SSLearnEpSQLFct sqlFct,
    final SSUri           user,
    final SSUri           learnEp,
    final SSUri           circle) throws Exception{

    try{
      SSServCaller.entityEntitiesToCircleAdd(
        user,
        circle,
        getLearnEpContentURIs(sqlFct, learnEp),
        false);

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static List<SSUri> getLearnEpContentURIs(
    final SSLearnEpSQLFct sqlFct,
    final SSUri           learnEp) throws Exception{

    try{

      final List<SSUri>  learnEpContentUris = new ArrayList<>();
      SSLearnEpVersion   learnEpVersion;

      learnEpContentUris.add(learnEp);
      
      for(SSUri learnEpVersionUri : sqlFct.getLearnEpVersionURIs(learnEp)){
        
        learnEpContentUris.add(learnEpVersionUri);
        
        learnEpVersion = sqlFct.getLearnEpVersion(learnEpVersionUri, true);
          
        for(SSLearnEpCircle circle : learnEpVersion.circles){
          learnEpContentUris.add(circle.id);
        }
        
        for(SSLearnEpEntity entity : learnEpVersion.entities){
          learnEpContentUris.add(entity.id);
          
          learnEpContentUris.add(entity.entity);
        }
        
        if(learnEpVersion.learnEpTimelineState != null){
          learnEpContentUris.add(learnEpVersion.learnEpTimelineState.id);
        }
      }
      
      SSStrU.distinctWithoutNull2(learnEpContentUris);
      
      return learnEpContentUris;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}
