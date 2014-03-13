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
package at.kc.tugraz.ss.serv.job.recomm.serv;

import at.kc.tugraz.socialserver.utils.SSDateU;
import at.kc.tugraz.ss.serv.job.recomm.conf.SSRecommConf;
import at.kc.tugraz.ss.serv.job.recomm.impl.SSRecommImpl;
import at.kc.tugraz.ss.serv.job.recomm.serv.task.SSRecommTagsBaseLevelLearningWithContextBasedOnUserEntityTagTimestampUpdateTask;
import at.kc.tugraz.ss.serv.job.recomm.serv.task.SSRecommTagsLanguageModelBasedOnUserEntityTagUpdateTask;
import at.kc.tugraz.ss.serv.job.recomm.serv.task.SSRecommTagsThreeLayersBasedOnUserEntityTagCategoryTimestampUpdateTask;
import at.kc.tugraz.ss.serv.job.recomm.serv.task.SSRecommTagsThreeLayersBasedOnUserEntityTagCategoryUpdateTask;
import at.kc.tugraz.ss.serv.serv.api.SSServA;
import at.kc.tugraz.ss.serv.serv.api.SSServImplA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;

public class SSRecommServ extends SSServA{
  
  public static final SSRecommServ inst = new SSRecommServ();
  
  private SSRecommServ(){}
  
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
      SSDateU.getDateForTomorrowMorning(),
      SSDateU.dayInMilliSeconds);
    
    SSDateU.scheduleAtFixedRate(
      new SSRecommTagsLanguageModelBasedOnUserEntityTagUpdateTask(),
      SSDateU.getDateForTomorrowMorning(),
      SSDateU.dayInMilliSeconds);
    
    SSDateU.scheduleAtFixedRate(
      new SSRecommTagsThreeLayersBasedOnUserEntityTagCategoryUpdateTask(),
      SSDateU.getDateForTomorrowMorning(),
      SSDateU.dayInMilliSeconds);
    
    SSDateU.scheduleAtFixedRate(
      new SSRecommTagsThreeLayersBasedOnUserEntityTagCategoryTimestampUpdateTask(),
      SSDateU.getDateForTomorrowMorning(),
      SSDateU.dayInMilliSeconds);
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
}
