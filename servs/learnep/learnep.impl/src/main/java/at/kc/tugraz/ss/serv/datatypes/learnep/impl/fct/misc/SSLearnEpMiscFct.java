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

import at.kc.tugraz.ss.circle.api.SSCircleServerI;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclePrivEntityAddPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEp;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpCircle;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpEntity;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpVersion;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpCreatePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionAddCirclePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionAddEntityPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionCreatePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionSetTimelineStatePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.impl.SSLearnEpImpl;
import at.kc.tugraz.ss.serv.datatypes.learnep.impl.fct.sql.SSLearnEpSQLFct;
import at.tugraz.sss.serv.SSObjU;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.caller.SSServCaller;
import java.util.ArrayList;
import java.util.List;

public class SSLearnEpMiscFct{
  
  public static SSUri copyLearnEpForUser(
    final SSLearnEpImpl   serv,
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
        serv.learnEpCreate(
          new SSLearnEpCreatePar(
            null, 
            null, 
            forUser, 
            learnEp.label, 
            learnEp.description, 
            false));
      
      for(SSLearnEpVersion version : learnEp.versions){

        copyVersionUri = 
          serv.learnEpVersionCreate(
            new SSLearnEpVersionCreatePar(
              null, 
              null, 
              forUser, 
              copyLearnEpUri, 
              false));
            
        for(SSLearnEpCircle circle : version.learnEpCircles){
          
          if(SSStrU.contains(entitiesToExclude, circle.id)){
            continue;
          }
           
          serv.learnEpVersionAddCircle(
            new SSLearnEpVersionAddCirclePar(
              null,
              null,
              forUser,
              copyVersionUri,
              circle.label,
              circle.xLabel,
              circle.yLabel,
              circle.xR,
              circle.yR,
              circle.xC,
              circle.yC,
              false));
        }
        
        for(SSLearnEpEntity entity : version.learnEpEntities){
          
          if(
            SSStrU.contains(entitiesToExclude, entity.id) ||
            SSStrU.contains(entitiesToExclude, entity.entity)){
            continue;
          }
          
          ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circlePrivEntityAdd(
            new SSCirclePrivEntityAddPar(
              null,
              null,
              forUser,
              entity.entity.id,
              SSEntityE.entity,
              null,
              null,
              null,
              false));
          
          serv.learnEpVersionAddEntity(
            new SSLearnEpVersionAddEntityPar(
              null, 
              null, 
              forUser, 
              copyVersionUri,  
              entity.entity.id, 
              entity.x, 
              entity.y, 
              false));
        }
        
        if(version.learnEpTimelineState != null){
          
          serv.learnEpVersionSetTimelineState(
            new SSLearnEpVersionSetTimelineStatePar(
              null,
              null,
              forUser,
              copyVersionUri,
              version.learnEpTimelineState.startTime,
              version.learnEpTimelineState.endTime,
              false));
        }
      }
      
      return copyLearnEpUri;

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public static List<SSUri> getLearnEpContentURIs(
    final SSUri           user,
    final SSLearnEpSQLFct sqlFct,
    final SSUri           learnEp) throws Exception{

    try{

      final List<SSUri>  learnEpContentUris = new ArrayList<>();
      SSLearnEpVersion   learnEpVersion;

      learnEpContentUris.add(learnEp);
      
      for(SSUri learnEpVersionUri : sqlFct.getLearnEpVersionURIs(learnEp)){
        
        learnEpContentUris.add(learnEpVersionUri);
        
        learnEpVersion = sqlFct.getLearnEpVersion(learnEpVersionUri, true);
          
        for(SSLearnEpCircle circle : learnEpVersion.learnEpCircles){
          learnEpContentUris.add(circle.id);
        }
        
        for(SSLearnEpEntity entity : learnEpVersion.learnEpEntities){
          learnEpContentUris.add(entity.id);
          
          learnEpContentUris.add(entity.entity.id);
          
          for(SSUri file : SSServCaller.entityFilesGet(user, entity.entity.id)){
            learnEpContentUris.add(file);
          }
          
          for(SSUri thumb : SSServCaller.entityThumbsGet(user, entity.entity.id)){
            learnEpContentUris.add(thumb);
          }
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
