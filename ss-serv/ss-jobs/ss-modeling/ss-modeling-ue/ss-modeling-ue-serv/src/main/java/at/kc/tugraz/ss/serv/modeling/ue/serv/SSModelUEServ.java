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
package at.kc.tugraz.ss.serv.modeling.ue.serv;

import at.kc.tugraz.socialserver.utils.SSDateU;
import at.kc.tugraz.ss.serv.modeling.ue.api.SSModelUEClientI;
import at.kc.tugraz.ss.serv.modeling.ue.api.SSModelUEServerI;
import at.kc.tugraz.ss.serv.modeling.ue.impl.SSModelUEImpl;
import at.kc.tugraz.ss.serv.modeling.ue.conf.SSModelUEConf;
import at.kc.tugraz.ss.serv.modeling.ue.datatypes.SSModelUEResource;
import at.kc.tugraz.ss.serv.modeling.ue.serv.task.SSModelUEUpdateTask;
import at.kc.tugraz.ss.serv.modeling.ue.utils.SSModelUEU;
import at.kc.tugraz.ss.serv.serv.api.SSServA;
import at.kc.tugraz.ss.serv.serv.api.SSServImplA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;

import java.util.HashMap;

public class SSModelUEServ extends SSServA{
  
  public static final HashMap<String, SSModelUEResource> resources = new HashMap<String, SSModelUEResource>();
  public static final SSModelUEServ                      inst      = new SSModelUEServ(SSModelUEClientI.class, SSModelUEServerI.class);
   
  protected SSModelUEServ(
    final Class servImplClientInteraceClass, 
    final Class servImplServerInteraceClass){
    
    super(servImplClientInteraceClass, servImplServerInteraceClass);
  }
  
  @Override
  protected SSServImplA createServImplForThread() throws Exception{
    return new SSModelUEImpl((SSModelUEConf) servConf, resources);
  }
  
  public void schedule() throws Exception{
    
    if(!servConf.use){
      return;
    }
    
    if(!((SSModelUEConf)servConf).initAtStartUp){
      SSModelUEU.init();
      
      SSModelUEU.lastUpdateTime = SSDateU.dateAsLong() - (SSDateU.dayInMilliSeconds * ((SSModelUEConf)servConf).daysToRetrieveEvents);
    }

    SSDateU.scheduleAtFixedRate(
      new SSModelUEUpdateTask(),
      SSDateU.getDateForTomorrowMorning(),
      SSDateU.dayInMilliSeconds);
  }

  @Override
  protected void initServSpecificStuff() throws Exception{
    
    if(
      !servConf.use ||
      !((SSModelUEConf)servConf).initAtStartUp){
      return;
    }
    
    SSModelUEU.init();

    SSModelUEU.lastUpdateTime = SSDateU.dateAsLong() - (SSDateU.dayInMilliSeconds * ((SSModelUEConf)servConf).daysToRetrieveEvents);
    
    SSServCaller.modelUEUpdate();
  }
}