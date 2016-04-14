/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
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

/**
 * @author Dieter Theiler
 */

package at.tugraz.sss.servs.conf;

import at.tugraz.sss.servs.util.SSStrU;
import java.util.ArrayList;
import java.util.List;

public abstract class SSConfA{

  public boolean         use                        = false;
  public boolean         initAtStartUp              = false;
  public boolean         schedule                   = false;
  public boolean         executeScheduleAtStartUp   = false;
  public List<String>    scheduleOps                = new ArrayList<>();
  public List<Integer>   scheduleIntervals          = new ArrayList<>();

  public static SSConfA copy(
    final SSConfA orig, 
    final SSConfA copy){
    
    copy.use                      = orig.use;
    copy.initAtStartUp            = orig.initAtStartUp;
    copy.schedule                 = orig.schedule;
    copy.executeScheduleAtStartUp = orig.executeScheduleAtStartUp;
    
    SSStrU.addDistinctNotNull(copy.scheduleOps, orig.scheduleOps);
    
    if(orig.scheduleIntervals == null){
      copy.scheduleIntervals = new ArrayList<>();
    }else{
      copy.scheduleIntervals.addAll(orig.scheduleIntervals);
    }
    
    return copy;
  }
}