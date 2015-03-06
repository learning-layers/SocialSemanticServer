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
package at.kc.tugraz.ss.serv.serv.api;

import at.kc.tugraz.socialserver.utils.SSMethU;
import java.util.ArrayList;
import java.util.List;

public abstract class SSConfA{
  
  public Boolean       use                        = false;
  public Boolean       initAtStartUp              = false;
  public List<SSMethU> initAtStartUpOps           = new ArrayList<>();
  public Boolean       schedule                   = false;
  public Boolean       executeScheduleAtStartUp   = false;
  public List<SSMethU> scheduleOps                = new ArrayList<>();
  public List<Integer> scheduleIntervals          = new ArrayList<>();
  public Boolean       executeOpAtStartUp         = false;
  public SSMethU       op                         = null;

  public static SSConfA copy(
    final SSConfA orig, 
    final SSConfA copy){
    
    copy.use                    = orig.use;
    copy.initAtStartUp          = orig.initAtStartUp;
    
    if(orig.initAtStartUpOps == null){
      copy.initAtStartUpOps = new ArrayList<>();
    }else{
      copy.initAtStartUpOps.addAll(orig.initAtStartUpOps);
    }
    
    copy.schedule                 = orig.schedule;
    copy.executeScheduleAtStartUp = orig.executeScheduleAtStartUp;
    
    if(orig.scheduleOps == null){
      copy.scheduleOps = new ArrayList<>();
    }else{
      copy.scheduleOps.addAll(orig.scheduleOps);
    }
    
    if(orig.scheduleIntervals == null){
      copy.scheduleIntervals = new ArrayList<>();
    }else{
      copy.scheduleIntervals.addAll(orig.scheduleIntervals);
    }
    
    copy.executeOpAtStartUp = orig.executeOpAtStartUp;
    copy.op                 = orig.op;
    
    return copy;
  }
}
