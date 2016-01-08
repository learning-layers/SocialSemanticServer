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
package at.tugraz.sss.serv.conf.api;

import at.tugraz.sss.serv.util.*;
import java.util.ArrayList;
import java.util.List;

public abstract class SSConfA{

  protected static String       sssWorkDir                 = null;
  protected static String       sssWorkDirTmp              = null;
  protected static String       sssWorkDirDataCsv          = null;
  protected static String       localWorkPath              = null;
  
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
  
  public void setSssWorkDir(final String value){
    
    sssWorkDir = SSFileU.correctDirPath (value);
    
    if(SSStrU.isEmpty(sssWorkDir)){
      sssWorkDir = SSFileU.dirWorking();
    }
    
    sssWorkDirTmp     = sssWorkDir + SSFileU.dirNameTmp;
    sssWorkDirDataCsv = sssWorkDir + SSFileU.dirNameDataCsv;
    localWorkPath     = sssWorkDirTmp;
  }
  
  public String getSssWorkDir(){
    return sssWorkDir;
  }
  
  public String getSssWorkDirTmp(){
    return sssWorkDirTmp;
  }
  
  public String getSssWorkDirDataCsv(){
    return sssWorkDirDataCsv;
  }
  
  public String getLocalWorkPath(){
    return localWorkPath;
  }
}