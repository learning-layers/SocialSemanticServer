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
package at.tugraz.sss.serv.datatype.par;

import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.par.SSServPar;import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.*;
 import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.enums.SSSpaceE;
import java.util.ArrayList;
import java.util.List;

public class SSCircleGetPar extends SSServPar{
  
  public SSUri           circle                     = null;
  public List<SSEntityE> entityTypesToIncludeOnly   = new ArrayList<>();
  public Boolean         setTags                    = false;
  public SSSpaceE        tagSpace                   = null;
  public Boolean         setEntities                = false;
  public Boolean         setUsers                   = false;
  public Boolean         invokeEntityHandlers       = false;
  public Boolean         setProfilePicture          = false;
  public Boolean         setThumb                   = false;
 
  public void setCircle(final String circle) throws Exception{
    this.circle = SSUri.get(circle);
  }
  
  public String getCircle() throws Exception{
    return SSStrU.removeTrailingSlash(circle);
  }
  
  public void setEntityTypesToIncludeOnly(final List<String> entityTypesToIncludeOnly) throws Exception{
    this.entityTypesToIncludeOnly = SSEntityE.get(entityTypesToIncludeOnly); 
  }
  
  public List<String> getEntityTypesToIncludeOnly() throws Exception{
    return SSStrU.toStr(entityTypesToIncludeOnly);
  }
  
  public void setTagSpace(final String tagSpace) throws Exception{
    this.tagSpace = SSSpaceE.get(tagSpace);
  }
  
  public String getTagSpace() throws Exception{
    return SSStrU.toStr(tagSpace);
  }
  
  public SSCircleGetPar(){}
  
  public SSCircleGetPar(
    final SSUri           user,
    final SSUri           circle,
    final List<SSEntityE> entityTypesToIncludeOnly,
    final Boolean         setTags,
    final SSSpaceE        tagSpace,
    final Boolean         setEntities,
    final Boolean         setUsers,
    final Boolean         withUserRestriction,
    final Boolean         invokeEntityHandlers){
    
    super(SSVarNames.circleGet, null, user);

    this.circle                = circle;
    
    if(entityTypesToIncludeOnly != null){
      this.entityTypesToIncludeOnly.addAll(entityTypesToIncludeOnly);
    }

    this.setTags              = setTags;
    this.tagSpace             = tagSpace;
    this.setEntities          = setEntities;
    this.setUsers             = setUsers;
    this.withUserRestriction  = withUserRestriction;
    this.invokeEntityHandlers = invokeEntityHandlers;
  }
}