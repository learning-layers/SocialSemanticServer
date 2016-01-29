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
package at.kc.tugraz.sss.video.datatypes.par;

import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.par.SSServPar; 
import at.tugraz.sss.serv.util.*;

public class SSVideoUserGetPar extends SSServPar{
  
  public SSUri    video                = null;
  
  public void setVideo(final String video) throws SSErr{
    this.video = SSUri.get(video);
  }
  
  public String getVideo(){
    return SSStrU.removeTrailingSlash(video);
  }
  
  public SSVideoUserGetPar(){/* Do nothing because of only JSON Jackson needs this */ }
  
  public SSVideoUserGetPar(
    final SSServPar servPar,
    final SSUri          user,
    final SSUri          video, 
    final boolean        withUserRestriction, 
    final boolean        invokeEntityHandlers){
    
    super(SSVarNames.videoGet, null, user, servPar.sqlCon);
    
    this.video                = video;
    this.withUserRestriction  = withUserRestriction;
    this.invokeEntityHandlers = invokeEntityHandlers;
  }
}