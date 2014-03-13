/**
 * Copyright 2014 Graz University of Technology - KTI (Knowledge Technologies Institute)
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
package at.kc.tugraz.ss.serv.auth.serv;

import at.kc.tugraz.ss.serv.auth.conf.SSAuthConf;
import at.kc.tugraz.ss.serv.auth.impl.SSAuthImpl;
import at.kc.tugraz.ss.serv.serv.api.SSServA;
import at.kc.tugraz.ss.serv.serv.api.SSServImplA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;

public class SSAuthServ extends SSServA{
  
  public static final SSServA inst = new SSAuthServ();
  
  private SSAuthServ(){}
  
  @Override
  protected SSServImplA createServImplForThread() throws Exception{
    return new SSAuthImpl((SSAuthConf)servConf);
  }

  @Override
  protected void initServSpecificStuff() throws Exception{
    
    if(!servConf.use){
      return;
    }
    
    switch(((SSAuthConf)servConf).authType){
      case csvFileAuth: SSServCaller.authUsersFromCSVFileAdd(); break;
    }
  }
}