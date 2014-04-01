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
package at.kc.tugraz.ss.serv.serv.api;

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.db.api.SSDBGraphI;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class SSServImplWithDBA extends SSServImplA{
  
  public final SSDBSQLI   dbSQL;
  public final SSDBGraphI dbGraph;

  public SSServImplWithDBA(final SSServConfA conf, final SSDBGraphI dbGraph, final SSDBSQLI dbSQL){
    
    super(conf);
    
    this.dbSQL   = dbSQL;
    this.dbGraph = dbGraph;
  }
  
  @Override
  protected void finalizeImpl() throws Exception{
//    ((SSServImplDBA)dbSQL).finalizeImpl();
  }
  
  @Override
  public List<SSMethU> publishClientOps(final Class clientInterfaceClass) throws Exception{

    List<SSMethU> clientOps = new ArrayList<SSMethU>();

    Method[] methods = clientInterfaceClass.getMethods();

    for(Method method : methods){
      clientOps.add(SSMethU.get(method.getName()));
    }

    return clientOps;
  }
  
  @Override
  public List<SSMethU> publishServerOps(final Class serverInterfaceClass) throws Exception{

    List<SSMethU> serverOps = new ArrayList<SSMethU>();

    Method[] methods = serverInterfaceClass.getMethods();

    for(Method method : methods){
      serverOps.add(SSMethU.get(method.getName()));
    }

    return serverOps;
  }
}
