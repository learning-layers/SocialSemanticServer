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
package at.kc.tugraz.sss.app.datatypes.par;

import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSStrU;
import java.util.ArrayList;
import java.util.List;

public class SSAppsDeletePar extends SSServPar{
  
  public List<SSUri> apps = new ArrayList<>();

  public List<String> getApps() {
    return SSStrU.removeTrailingSlash(apps);
  }

  public void setApps(final List<String> apps) throws Exception {
    this.apps = SSUri.get(apps);
  }
  
  public SSAppsDeletePar(){}
    
  public SSAppsDeletePar(
    final SSUri       user, 
    final List<SSUri> apps, 
    final Boolean     withUserRestriction, 
    final Boolean     shouldCommit){
    
    super(SSServOpE.appsDelete, null, user);
    
    SSUri.addDistinctWithoutNull(this.apps, apps);
    
    this.withUserRestriction = withUserRestriction;
    this.shouldCommit        = shouldCommit;
  }
}