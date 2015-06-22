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
package at.kc.tugraz.ss.serv.jsonld.impl;

import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSObjU;
import at.tugraz.sss.serv.SSSocketCon;
import at.kc.tugraz.ss.serv.jsonld.api.SSJSONLDClientI;
import at.kc.tugraz.ss.serv.jsonld.api.SSJSONLDServerI;
import at.kc.tugraz.ss.serv.jsonld.conf.SSJSONLDConf;
import at.kc.tugraz.ss.serv.jsonld.datatypes.par.SSJSONLDPar;
import at.kc.tugraz.ss.serv.jsonld.datatypes.par.ret.SSJSONLDDescRet;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSJSONLDPropI;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.caller.SSServCallerU;

public class SSJSONLDImpl extends SSServImplWithDBA implements SSJSONLDClientI, SSJSONLDServerI{

  public SSJSONLDImpl(final SSJSONLDConf conf) throws Exception{
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
  }
  
  @Override
  public void jsonLD(SSSocketCon sSCon, SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSJSONLDDescRet.get(jsonLD(parA), parA.op));
  }

  @Override
  public Object jsonLD(SSServPar parA) throws Exception{
    
    SSJSONLDPar par = new SSJSONLDPar(parA);
    
    Class<?> clz    = Class.forName(par.type);
    Object[] consts = clz.getEnumConstants();
    
    if(!SSObjU.isNull(consts)){
      return consts[0].getClass().getDeclaredMethod(SSStrU.toStr(SSServOpE.jsonLD)).invoke(consts[0]);
    }
    
    return ((SSJSONLDPropI) clz.newInstance()).jsonLDDesc();
  }
}
