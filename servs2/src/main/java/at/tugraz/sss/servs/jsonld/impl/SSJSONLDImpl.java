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
package at.tugraz.sss.servs.jsonld.impl;

import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.enums.SSClientE;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.jsonld.SSJSONLDPropI;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.datatype.par.SSServPar; 
import at.tugraz.sss.serv.datatype.ret.SSServRetI; 
import at.tugraz.sss.serv.impl.api.*;
import at.tugraz.sss.servs.common.impl.SSUserCommons;
import at.tugraz.sss.servs.jsonld.api.*;
import at.tugraz.sss.servs.jsonld.conf.*;
import at.tugraz.sss.servs.jsonld.datatype.*;

public class SSJSONLDImpl 
extends SSServImplA 
implements 
  SSJSONLDClientI, 
  SSJSONLDServerI{

  final SSUserCommons userCommons;
  
  public SSJSONLDImpl(final SSJSONLDConf conf) throws SSErr{
    
    super(conf);
    
    this.userCommons = new SSUserCommons();
  }
  
  @Override
  public SSServRetI jsonLD(SSClientE clientType, SSServPar parA) throws SSErr{
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSJSONLDPar par = (SSJSONLDPar) parA.getFromClient(clientType, parA, SSJSONLDPar.class);
      
      return SSJSONLDRet.get(jsonLD(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public Object jsonLD(final SSJSONLDPar par) throws SSErr{
    
    try{
      
      Class<?> clz    = Class.forName(par.type);
      Object[] consts = clz.getEnumConstants();
      
      if(!SSObjU.isNull(consts)){
        return consts[0].getClass().getDeclaredMethod(SSStrU.toStr(SSVarNames.jsonLD)).invoke(consts[0]);
      }
      
      return ((SSJSONLDPropI) clz.newInstance()).jsonLDDesc();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}
