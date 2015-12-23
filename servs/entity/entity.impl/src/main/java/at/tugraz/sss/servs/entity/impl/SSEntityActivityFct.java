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
package at.tugraz.sss.servs.entity.impl;

import at.kc.tugraz.ss.activity.api.SSActivityServerI;
import at.tugraz.sss.serv.util.SSLogU;
import at.kc.tugraz.ss.activity.datatypes.enums.SSActivityE;
import at.kc.tugraz.ss.activity.datatypes.par.SSActivityAddPar;
import at.tugraz.sss.serv.datatype.par.SSEntitySharePar;
import at.tugraz.sss.serv.datatype.SSTextComment;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.datatype.*;

public class SSEntityActivityFct{
  
  public static void shareEntityWithUsers(
    final SSEntitySharePar par) throws Exception{
    
    try{
      
     ((SSActivityServerI) SSServReg.getServ(SSActivityServerI.class)).activityAdd(
        new SSActivityAddPar(
          par.user, 
          SSActivityE.shareEntityWithUsers, 
          par.entity,
          par.users, 
          SSUri.asListNotNull(), 
          SSTextComment.asListWithoutNullAndEmpty(par.comment), 
          null, 
          par.shouldCommit));
     
    }catch(SSErr error){
      
      switch(error.code){
        case servServerNotAvailable: SSLogU.warn(error.getMessage()); break;
        default: SSServErrReg.regErrThrow(error);
      }

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static void shareEntityWithCircles(
    final SSEntitySharePar par) throws Exception{
    
    try{
      
      ((SSActivityServerI) SSServReg.getServ(SSActivityServerI.class)).activityAdd(
        new SSActivityAddPar(
          par.user, 
          SSActivityE.shareEntityWithCircles, 
          par.entity,
          null,
          SSUri.asListNotNull(par.circles),
          SSTextComment.asListWithoutNullAndEmpty(par.comment), 
          null, 
          par.shouldCommit));
      
    }catch(SSErr error){
      
      switch(error.code){
        case servServerNotAvailable: SSLogU.warn(error.getMessage()); break;
        default: SSServErrReg.regErrThrow(error);
      }

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static void setEntityPublic(
   final SSEntitySharePar par) throws Exception{
    
     try{
      
      ((SSActivityServerI) SSServReg.getServ(SSActivityServerI.class)).activityAdd(
        new SSActivityAddPar(
          par.user, 
          SSActivityE.setEntityPublic, 
          par.entity,
          null, 
          null, 
          SSTextComment.asListWithoutNullAndEmpty(par.comment),  
          null, 
          par.shouldCommit));
      
    }catch(SSErr error){
      
      switch(error.code){
        case servServerNotAvailable: SSLogU.warn(error.getMessage()); break;
        default: SSServErrReg.regErrThrow(error);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}