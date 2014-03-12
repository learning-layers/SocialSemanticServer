/**
 * Copyright 2013 Graz University of Technology - KTI (Knowledge Technologies Institute)
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

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.socialserver.utils.SSObjU;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.serv.jsonld.api.SSJSONLDClientI;
import at.kc.tugraz.ss.serv.jsonld.api.SSJSONLDServerI;
import at.kc.tugraz.ss.serv.jsonld.conf.SSJSONLDConf;
import at.kc.tugraz.ss.serv.jsonld.datatypes.par.SSJSONLDPar;
import at.kc.tugraz.ss.serv.jsonld.datatypes.par.ret.SSJSONLDDescRet;
import at.kc.tugraz.ss.serv.jsonld.datatypes.api.SSJSONLDPropI;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.serv.api.SSServImplMiscA;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SSJSONLDImpl extends SSServImplMiscA implements SSJSONLDClientI, SSJSONLDServerI{

  public SSJSONLDImpl(final SSJSONLDConf conf) throws Exception{
    super(conf);
  }
  
  @Override
  public List<SSMethU> publishClientOps() throws Exception{
    
    List<SSMethU> clientOps = new ArrayList<SSMethU>();
    
    Method[] methods = SSJSONLDClientI.class.getMethods();
    
    for(Method method : methods){
      clientOps.add(SSMethU.get(method.getName()));
    }
    
    return clientOps;
  }
  
  @Override
  public List<SSMethU> publishServerOps() throws Exception{
    
    List<SSMethU> serverOps = new ArrayList<SSMethU>();
    
    Method[] methods = SSJSONLDServerI.class.getMethods();
    
    for(Method method : methods){
      serverOps.add(SSMethU.get(method.getName()));
    }
    
    return serverOps;
  }
  
  @Override
  public void handleClientOp(SSSocketCon sSCon, SSServPar par) throws Exception{
    SSJSONLDClientI.class.getMethod(SSMethU.toStr(par.op), SSSocketCon.class, SSServPar.class).invoke(this, sSCon, par);
  }
  
  @Override
  public Object handleServerOp(SSServPar par) throws Exception{
    return SSJSONLDServerI.class.getMethod(SSMethU.toStr(par.op), SSServPar.class).invoke(this, par);
  }
  
  @Override
  public void jsonLD(SSSocketCon sSCon, SSServPar par) throws Exception{
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSJSONLDDescRet.get(jsonLD(par), par.op));
  }

  @Override
  public Object jsonLD(SSServPar parA) throws Exception{
    
    SSJSONLDPar par = new SSJSONLDPar(parA);
    
    Class<?> clz    = Class.forName(par.entityType);
    Object[] consts = clz.getEnumConstants();
    
    if(SSObjU.isNotNull(consts)){
      return consts[0].getClass().getDeclaredMethod(SSMethU.toStr(SSMethU.jsonLD)).invoke(consts[0]);
    }
    
    return ((SSJSONLDPropI) clz.newInstance()).jsonLDDesc();
  }
}
