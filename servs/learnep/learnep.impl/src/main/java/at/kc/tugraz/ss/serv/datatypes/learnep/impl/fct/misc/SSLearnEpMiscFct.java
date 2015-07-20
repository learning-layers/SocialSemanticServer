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

import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpCircle;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpEntity;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpVersion;
import at.kc.tugraz.ss.serv.datatypes.learnep.impl.fct.sql.SSLearnEpSQLFct;
import at.kc.tugraz.ss.service.filerepo.api.SSFileRepoServerI;
import at.tugraz.sss.serv.SSImageE;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.servs.file.datatype.par.SSEntityFilesGetPar;
import at.tugraz.sss.servs.image.api.SSImageServerI;
import at.tugraz.sss.servs.image.datatype.par.SSImagesGetPar;
import java.util.ArrayList;
import java.util.List;

public class SSLearnEpMiscFct{
  
  public List<SSUri> getLearnEpContentURIs(
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
        
        for(SSLearnEpEntity learnEpEntity : learnEpVersion.learnEpEntities){
          learnEpContentUris.add   (learnEpEntity.id);
          learnEpContentUris.add   (learnEpEntity.entity.id);
          learnEpContentUris.addAll(getLearnEpEntityAttachedEntities(user, learnEpEntity.entity.id));
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
  
  public List<SSUri> getLearnEpEntityAttachedEntities(
    final SSUri  user,
    final SSUri  entity) throws Exception{
    
    try{
      final List<SSUri> attachedEntities = new ArrayList<>();
      
      attachedEntities.addAll(
        ((SSFileRepoServerI) SSServReg.getServ(SSFileRepoServerI.class)).filesGet(
          new SSEntityFilesGetPar(
            null,
            null,
            user,
            entity,
            true)));  //withUserRestcrition);
      
      attachedEntities.addAll(
        SSUri.getFromEntitites(
          ((SSImageServerI) SSServReg.getServ(SSImageServerI.class)).imagesGet(
            new SSImagesGetPar(
              null,
              null,
              user,
              entity, //entity
              SSImageE.thumb, //imageType
              false))));
      
      return attachedEntities;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}