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
 package at.kc.tugraz.ss.service.filerepo.conf;

import at.kc.tugraz.socialserver.utils.SSFileU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.serv.serv.api.SSServConfA;
import at.kc.tugraz.ss.service.filerepo.datatypes.enums.SSFileRepoTypeE;

public class SSFileRepoConf extends SSServConfA{
  
  private   String             path         = null; //keep this one private, because yaml loader isn't able to call setPath(...) for public variables
  public    SSFileRepoTypeE    fileRepoType = null;
  public    String             user         = null;
  public    String             password     = null;

  public static SSFileRepoConf copy(final SSFileRepoConf orig){
    
    final SSFileRepoConf copy = (SSFileRepoConf) SSServConfA.copy(orig, new SSFileRepoConf());
    
    copy.path          = orig.path;
    copy.fileRepoType  = SSFileRepoTypeE.get(SSFileRepoTypeE.toStr(orig.fileRepoType));
    copy.user          = orig.user;
    copy.password      = orig.password;
    
    return copy;
  }
  
  public void setPath(final String path){
    
    this.path = path;
    
    if(SSStrU.isEmpty(this.path)){
      this.path = SSFileU.dirWorkingTmp();
    }
  }
  
  public String getPath(){
    return path;
  }
}