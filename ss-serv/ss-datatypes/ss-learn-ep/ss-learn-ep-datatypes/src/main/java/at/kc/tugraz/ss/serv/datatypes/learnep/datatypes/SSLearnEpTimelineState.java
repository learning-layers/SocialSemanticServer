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

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityA;
import java.util.HashMap;
import java.util.Map;

public class SSLearnEpTimelineState extends SSEntityA {

  public SSUri learnEpTimelineStateUri = null;
  public SSUri learnEpVersionUri       = null;
  public Long  startTime               = null;
  public Long  endTime                 = null;
  
  public static SSLearnEpTimelineState get(SSUri learnEpTimelineStateUri, SSUri learnEpVersionUri, Long startTime, Long endTime)throws Exception{
    return new SSLearnEpTimelineState(learnEpTimelineStateUri, learnEpVersionUri, startTime, endTime);
  }
  
  private SSLearnEpTimelineState(SSUri learnEpTimelineStateUri, SSUri learnEpVersionUri, Long startTime, Long endTime)throws Exception{
    
    super(learnEpTimelineStateUri);
    
    this.learnEpTimelineStateUri = learnEpTimelineStateUri;
    this.learnEpVersionUri       = learnEpVersionUri;
    this.startTime               = startTime;
    this.endTime                 = endTime;
  }
  
  @Override
  public Map<String, Object> jsonLDDesc(){
    
    Map<String, Object> ld = new HashMap<String, Object>();
    
    ld.put(SSVarU.learnEpTimelineStateUri, SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.startTime,               SSVarU.xsd + SSStrU.colon + SSStrU.valueLong);
    ld.put(SSVarU.endTime,                 SSVarU.sss + SSStrU.colon + SSStrU.valueLong);
    
    return ld;
  }
  
  /*************** getters to allow for json enconding ********************/
  public String getLearnEpTimelineStateUri() throws Exception {
    return SSUri.toStrWithoutSlash(learnEpTimelineStateUri);
  }
  
   public String getLearnEpVersionUri() throws Exception {
    return SSUri.toStrWithoutSlash(learnEpVersionUri);
  }

  public Long getStartTime() {
    return startTime;
  }

  public Long getEndTime() {
    return endTime;
  }
}
