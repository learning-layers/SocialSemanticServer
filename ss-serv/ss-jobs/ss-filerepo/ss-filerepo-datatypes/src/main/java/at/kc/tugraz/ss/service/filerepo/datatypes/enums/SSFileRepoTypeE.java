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
 package at.kc.tugraz.ss.service.filerepo.datatypes.enums;

import at.kc.tugraz.socialserver.utils.*;

public enum SSFileRepoTypeE {
  
  localWork,
  fileSys,
  webdav, 
  solr,
  i5Cloud;
  
  public static SSFileRepoTypeE get(
    String value){
    
    if(SSStrU.isEmpty(value)){
      return null;
    }
    
    return SSFileRepoTypeE.valueOf(value);
  }
  
  public static Boolean isSame(SSFileRepoTypeE type1, SSFileRepoTypeE type2){
    
    if(
      type1 == null ||
      type2 == null){
      return false;
    }
    
    return type1.toString().equals(type2.toString());
  }
}