/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
package at.kc.tugraz.ss.serv.ss.auth.datatypes.pars;

import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSVarU;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSStrU;

public class SSAuthCheckCredPar extends SSServPar{

  public SSLabel label    = null;
  public String  password = null;
  
  public void setLabel(String label){
    try{ this.label = SSLabel.get(label); }catch(Exception error){}
  }
  
  public void setPassword(String password){
    this.password = password;
  }
  
  public String getLabel(){
    return SSStrU.toStr(label);
  }

  public String getPassword(){
    return password;
  }

  public SSAuthCheckCredPar(){}
    
  public SSAuthCheckCredPar(
    final SSServOpE op,
    final String    key,
    final SSUri     user,
    final SSLabel   label, 
    final String    password){
    
    super(op, key, user);
    
    this.label    = label;
    this.password = password;
  }
  
  public static SSAuthCheckCredPar get(final SSServPar par) throws Exception{
    
    try{
      
      if(par.clientCon != null){
        return (SSAuthCheckCredPar) par.getFromJSON(SSAuthCheckCredPar.class);
      }
      
      return new SSAuthCheckCredPar(
        par.op,
        par.key,
        par.user,
        (SSLabel) par.pars.get(SSVarU.label),
        (String)  par.pars.get(SSVarU.password));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
//  public SSAuthCheckCredPar(final SSServPar par) throws Exception{
//    
//    super(par);
//    
//    try{
//      
//      if(pars != null){
//        label         = (SSLabel) pars.get(SSVarU.label);
//        password      = (String)  pars.get(SSVarU.password);
//      }
//      
//      if(par.clientJSONObj != null){
//
//        try{
//          password  = par.clientJSONObj.get(SSVarU.password).getTextValue();
//        }catch(Exception error){}
//
//        final SSAuthCheckCredPar huha = (SSAuthCheckCredPar) SSJSONU.obj(par.clientJSONRequ, SSAuthCheckCredPar.class);
//        
//        try{ 
//          label     = SSLabel.get(par.clientJSONObj.get(SSVarU.label).getTextValue()); 
//        }catch(Exception error){}
//      }
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }
//  }
}