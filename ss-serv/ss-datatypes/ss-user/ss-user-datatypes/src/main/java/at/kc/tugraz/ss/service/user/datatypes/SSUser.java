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
 package at.kc.tugraz.ss.service.user.datatypes;

import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.socialserver.utils.*;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityA;
import java.util.HashMap;
import java.util.Map;

public class SSUser extends SSEntityA {

  public         SSUri           uri     = null;
  public         String          label   = SSStrU.empty;
  
  @Override
  public Object jsonLDDesc(){
    
    Map<String, Object> ld = new HashMap<String, Object>();
    
    ld.put(SSVarU.uri,     SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.label,   SSVarU.xsd + SSStrU.colon + SSStrU.valueString);
    
    return ld;
  }
   
  public static SSUser get(String user) throws Exception{
    
    if(SSStrU.isEmpty(user)){
      return null;
    }
    
    return new SSUser(SSUri.get(user));
  }

  public static SSUser get(SSUri user){
    return new SSUser(user);
  }
  
  private SSUser(SSUri user){
    
    super(user);
    
    this.uri = user;
  }
}