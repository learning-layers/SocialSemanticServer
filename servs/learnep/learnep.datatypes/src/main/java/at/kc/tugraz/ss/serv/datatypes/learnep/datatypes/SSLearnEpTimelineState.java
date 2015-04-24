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
package at.kc.tugraz.ss.serv.datatypes.learnep.datatypes;

import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import java.util.Map;

public class SSLearnEpTimelineState extends SSEntity {

  public SSUri learnEpVersion          = null;
  public Long  startTime               = null;
  public Long  endTime                 = null;
  
  public static SSLearnEpTimelineState get(
    final SSUri id, 
    final SSUri learnEpVersion, 
    final Long  startTime, 
    final Long  endTime) throws Exception{
    
    return new SSLearnEpTimelineState(id, learnEpVersion, startTime, endTime);
  }
  
  protected SSLearnEpTimelineState(
    final SSUri id,
    final SSUri learnEpVersion,
    final Long  startTime,
    final Long  endTime)throws Exception{
    
    super(id, SSEntityE.learnEpTimelineState);
    
    this.learnEpVersion          = learnEpVersion;
    this.startTime               = startTime;
    this.endTime                 = endTime;
  }
  
  @Override
  public Map<String, Object> jsonLDDesc(){
    
    final Map<String, Object> ld             = (Map<String, Object>) super.jsonLDDesc();
    
    ld.put(SSVarNames.learnEpVersion,          SSVarNames.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarNames.startTime,               SSVarNames.xsd + SSStrU.colon + SSStrU.valueLong);
    ld.put(SSVarNames.endTime,                 SSVarNames.sss + SSStrU.colon + SSStrU.valueLong);
    
    return ld;
  }
  
  /* json getters  */
  
   public String getLearnEpVersion() throws Exception {
    return SSStrU.removeTrailingSlash(learnEpVersion);
  }
}
