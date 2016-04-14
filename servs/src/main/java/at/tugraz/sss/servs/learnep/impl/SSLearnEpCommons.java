/**
 * Code contributed to the Learning Layers project
 * http://www.learning-layers.eu
 * Development is partly funded by the FP7 Programme of the European Commission under
 * Grant Agreement FP7-ICT-318209.
 * Copyright (c) 2016, Graz University of Technology - KTI (Knowledge Technologies Institute).
  
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

package at.tugraz.sss.servs.learnep.impl;

import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSServPar;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSEntity;
import at.tugraz.sss.servs.common.impl.SSServErrReg;
import at.tugraz.sss.servs.learnep.api.*;
import at.tugraz.sss.servs.learnep.datatype.*;
import java.util.*;

public class SSLearnEpCommons {
  
  public List<SSUri> getEntityURIsFromLearnEpCircle(
    final SSLearnEpCircle learnEpCircle) throws SSErr{
    
    try{
      
      final List<SSUri> learnEpEntityURIs = new ArrayList<>();
      
      for(SSEntity learnEpEntity : learnEpCircle.entries){
        
        if(((SSLearnEpEntity) learnEpEntity).entity == null){
          continue;
        }
        
        SSUri.addDistinctWithoutNull(
          learnEpEntityURIs,
          ((SSLearnEpEntity) learnEpEntity).entity.id);
      }
      
      return learnEpEntityURIs;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public List<SSEntity> getLearnEpCirclesWithEntries(
    final SSLearnEpServerI learnEpServ,
    final SSServPar        servPar, 
    final SSUri            user,
    final SSUri            learnEpVersion) throws SSErr{
    
    try{
      
      return learnEpServ.learnEpVersionCirclesWithEntriesGet(
        new SSLearnEpVersionCirclesWithEntriesGetPar(
          servPar,
          user,
          learnEpVersion,
          false, //withUserRestriction
          false)).circles; //invokeEntityHandlers
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public List<SSUri> getCircleURIsForLearnEpEntity(
    final List<SSEntity> learnEpCircles, 
    final SSUri          learnEpEntity) throws SSErr{
    
    try{
      
      final List<SSUri> result = new ArrayList<>();
      
      for(SSEntity learnEpCircle : learnEpCircles){
        
        if(SSStrU.contains(learnEpCircle.entries, learnEpEntity)){
          result.add(learnEpCircle.id);
        }
      }
      
      return result;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}
