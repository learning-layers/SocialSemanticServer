/**
 * Copyright 2014 Graz University of Technology - KTI (Knowledge Technologies Institute)
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
package at.kc.tugraz.ss.serv.datatypes.entity.datatypes;

import at.kc.tugraz.socialserver.utils.SSObjU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.jsonld.datatypes.api.SSJSONLDPropI;
import java.util.List;

public enum SSEntityCircleTypeE implements SSJSONLDPropI{
  
  priv,
  group,
  proj,
  org,
  clust,
  pub;
  
  public static SSEntityCircleTypeE get(final String value) throws Exception{
    return SSEntityCircleTypeE.valueOf(value);
  }
  
  public static String toStr(final SSEntityCircleTypeE circleType){
    return SSStrU.toString(circleType);
  }
  
  public static Boolean equals(
    final SSEntityCircleTypeE circleType1, 
    final SSEntityCircleTypeE circleType2){

    if(SSObjU.isNull(circleType1, circleType2)){
      return false;
    }
    
    return circleType1.toString().equals(circleType2.toString());
  }
  
  public static Boolean contains(
    final List<SSEntityCircleTypeE> circleTypes, 
    final SSEntityCircleTypeE       toContainCircleType) throws Exception{

    if(circleTypes == null){
      SSServErrReg.regErrThrow(new Exception("pars null"));
      return null;
    }
    
    for(SSEntityCircleTypeE circleType : circleTypes){
      if(SSStrU.equals(toStr(circleType), toStr(toContainCircleType))){
        return true;
      }
    }
    
    return false;
  }
  
  private SSEntityCircleTypeE(){}
  
  @Override
  public Object jsonLDDesc(){
    return SSStrU.valueString;
  }
}
