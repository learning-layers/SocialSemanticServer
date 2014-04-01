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
 package at.kc.tugraz.ss.service.broadcast.service;

import at.kc.tugraz.socialserver.service.broadcast.api.SSBroadcasterClientI;
import at.kc.tugraz.socialserver.service.broadcast.api.SSBroadcasterServerI;
import at.kc.tugraz.ss.serv.broadcast.impl.SSBroadcasterImpl;
import at.kc.tugraz.socialserver.service.broadcast.conf.SSBroadcasterConf;
import at.kc.tugraz.ss.serv.serv.api.SSServA;
import at.kc.tugraz.ss.serv.serv.api.SSServImplA;

public class SSBroadcasterServ extends SSServA{
  
 public static final SSServA  inst = new SSBroadcasterServ(SSBroadcasterClientI.class, SSBroadcasterServerI.class);
  
 protected SSBroadcasterServ(
    final Class servImplClientInteraceClass, 
    final Class servImplServerInteraceClass){
    
    super(servImplClientInteraceClass, servImplServerInteraceClass);
  }
  
  @Override
  protected SSServImplA createServImplForThread() throws Exception{
    return new SSBroadcasterImpl((SSBroadcasterConf)servConf);
  }

  @Override
  protected void initServSpecificStuff() throws Exception{
  }
}