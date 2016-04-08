/**
 * Code contributed to the Learning Layers project
 * http://www.learning-layers.eu
 * Development is partly funded by the FP7 Programme of the European Commission under
 * Grant Agreement FP7-ICT-318209.
 * Copyright (c) 2016, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
package at.tugraz.sss.servs.common.impl;

import at.tugraz.sss.servs.activity.api.SSActivityServerI;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.reg.SSServReg;
import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.servs.eval.api.SSEvalServerI;
import at.tugraz.sss.servs.mail.api.*;

public class SSServCommons {
  
  public SSMailServerI getMailServ() throws SSErr{
    
    try{
      return (SSMailServerI) SSServReg.getServ(SSMailServerI.class);
    }catch(SSErr error){
      
      switch(error.code){
        
        case servInvalid:{
          SSLogU.warn(error);
          return null;
        } 
        
        default:{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public SSActivityServerI getActivityServ() throws SSErr{
    
    try{
      return (SSActivityServerI) SSServReg.getServ(SSActivityServerI.class);
    }catch(SSErr error){
      
      switch(error.code){
        
        case servInvalid:{
          SSLogU.warn(error);
          return null;
        } 
        
        default:{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public SSEvalServerI getEvalServ() throws SSErr{
    
    try{
      return (SSEvalServerI) SSServReg.getServ(SSEvalServerI.class);
    }catch(SSErr error){
      
      switch(error.code){
        
        case servInvalid:{
          SSLogU.warn(error);
          return null;
        } 
        
        default:{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}