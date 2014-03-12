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
package at.kc.tugraz.ss.serv.lomextractor.datatypes;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityA;
import java.util.ArrayList;
import java.util.List;

public class SSLOMUser extends SSEntityA{
  
  public String                                  fullName  = null;
  public String                                  tempRole  = null;
//  //role: resourceUri, resource
//  public Map<String, Map<String, SSLOMResource>> resources = new HashMap<String, Map<String, SSLOMResource>>();
  
  
  public SSLOMUser(
    String fullName,
    String tempRole){
    
    super(fullName);
    
    this.fullName = fullName;
    this.tempRole = tempRole;
  }

  public static List<SSLOMUser> distinctUsers(List<SSLOMUser> users){
    
    List<String>    userNames = new ArrayList<String>();
    List<SSLOMUser> result    = new ArrayList<SSLOMUser>();
    
    for(SSLOMUser user : users){
      
      if(
        SSStrU.isNotEmpty  (user.fullName) &&
        !userNames.contains (user.fullName)){
        
        userNames.add (user.fullName);
        result.add    (user);
      }
    }
    
    return result;
  }
  
  @Override
  public Object jsonLDDesc(){
    return "dtheiler";
  }
}