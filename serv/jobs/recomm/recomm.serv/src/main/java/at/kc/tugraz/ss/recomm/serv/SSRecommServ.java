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
package at.kc.tugraz.ss.recomm.serv;

import at.kc.tugraz.socialserver.utils.SSDateU;
import at.kc.tugraz.ss.conf.api.SSCoreConfA;
import at.kc.tugraz.ss.recomm.api.SSRecommClientI;
import at.kc.tugraz.ss.recomm.api.SSRecommServerI;
import at.kc.tugraz.ss.recomm.conf.SSRecommConf;
import at.kc.tugraz.ss.recomm.impl.SSRecommImpl;
import at.kc.tugraz.ss.recomm.serv.task.SSRecommTagsBaseLevelLearningWithContextBasedOnUserEntityTagTimestampUpdateTask;
import at.kc.tugraz.ss.recomm.serv.task.SSRecommTagsLanguageModelBasedOnUserEntityTagUpdateTask;
import at.kc.tugraz.ss.recomm.serv.task.SSRecommTagsThreeLayersBasedOnUserEntityTagCategoryTimestampUpdateTask;
import at.kc.tugraz.ss.recomm.serv.task.SSRecommTagsThreeLayersBasedOnUserEntityTagCategoryUpdateTask;
import at.kc.tugraz.ss.serv.serv.api.SSServA;
import at.kc.tugraz.ss.serv.serv.api.SSServImplA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import java.util.List;

public class SSRecommServ extends SSServA{
  
  public static final SSRecommServ inst = new SSRecommServ(SSRecommClientI.class, SSRecommServerI.class);
  
  protected SSRecommServ(
    final Class servImplClientInteraceClass, 
    final Class servImplServerInteraceClass){
    
    super(servImplClientInteraceClass, servImplServerInteraceClass);
  }
  
  @Override
  protected SSServImplA createServImplForThread() throws Exception{
    return new SSRecommImpl(servConf);
  }
  
  public void schedule() throws Exception{
    
    if(!servConf.use){
      return;
    }
    
    if(!((SSRecommConf)servConf).initAtStartUp){
      SSServCaller.recommTagsBaseLevelLearningWithContextBasedOnUserEntityTagTimestampUpdate();
      SSServCaller.recommTagsLanguageModelBasedOnUserEntityTagUpdate();
      SSServCaller.recommTagsThreeLayersBasedOnUserEntityTagCategoryUpdate();
      SSServCaller.recommTagsThreeLayersBasedOnUserEntityTagCategoryTimestampUpdate();
    }

    SSDateU.scheduleAtFixedRate(
      new SSRecommTagsBaseLevelLearningWithContextBasedOnUserEntityTagTimestampUpdateTask(),
      SSDateU.getDatePlusMinutes(((SSRecommConf)servConf).updateInterval),
      ((SSRecommConf)servConf).updateInterval * SSDateU.minuteInMilliSeconds);
    
    SSDateU.scheduleAtFixedRate(
      new SSRecommTagsLanguageModelBasedOnUserEntityTagUpdateTask(),
      SSDateU.getDatePlusMinutes(((SSRecommConf)servConf).updateInterval),
      ((SSRecommConf)servConf).updateInterval * SSDateU.minuteInMilliSeconds);
    
    SSDateU.scheduleAtFixedRate(
      new SSRecommTagsThreeLayersBasedOnUserEntityTagCategoryUpdateTask(),
      SSDateU.getDatePlusMinutes(((SSRecommConf)servConf).updateInterval),
      ((SSRecommConf)servConf).updateInterval * SSDateU.minuteInMilliSeconds);
     
    SSDateU.scheduleAtFixedRate(
      new SSRecommTagsThreeLayersBasedOnUserEntityTagCategoryTimestampUpdateTask(),
      SSDateU.getDatePlusMinutes(((SSRecommConf)servConf).updateInterval),
      ((SSRecommConf)servConf).updateInterval * SSDateU.minuteInMilliSeconds);
  }
  
  @Override
  protected void initServSpecificStuff() throws Exception{
    
    if(
      !servConf.use ||
      !((SSRecommConf)servConf).initAtStartUp){
      return;
    }
    
    SSServCaller.recommTagsBaseLevelLearningWithContextBasedOnUserEntityTagTimestampUpdate();
    SSServCaller.recommTagsLanguageModelBasedOnUserEntityTagUpdate();
    SSServCaller.recommTagsThreeLayersBasedOnUserEntityTagCategoryUpdate();
    SSServCaller.recommTagsThreeLayersBasedOnUserEntityTagCategoryTimestampUpdate();
  }
  
 @Override
  public SSCoreConfA getConfForCloudDeployment(
    final SSCoreConfA coreConfA, 
    final List<Class> configuredServs) throws Exception{
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}
