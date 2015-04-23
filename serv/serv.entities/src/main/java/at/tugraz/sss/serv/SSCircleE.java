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
package at.tugraz.sss.serv;

import at.tugraz.sss.serv.SSJSONLDPropI;
import at.tugraz.sss.serv.SSJSONLDPropI;
import at.tugraz.sss.serv.SSJSONLDPropI;
import at.tugraz.sss.serv.SSObjU;
import at.tugraz.sss.serv.SSObjU;
import at.tugraz.sss.serv.SSObjU;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSStrU;
import java.util.ArrayList;
import java.util.List;

public enum SSCircleE implements SSJSONLDPropI{
  
  priv,
  group,
  proj,
  org,
  clust,
  pub;
  
  public static List<SSCircleE> get(final List<String> values) throws Exception{
    
    final List<SSCircleE> result = new ArrayList<>();
    
    for(String value : values){
      result.add(get(value));
    }
    
    return result;
  }
  
  public static SSCircleE get(final String value) throws Exception{
    return SSCircleE.valueOf(value);
  }
  
  public static String toStr(final SSCircleE circleType){
    return SSStrU.toStr(circleType);
  }
  
  public static Boolean equals(
    final SSCircleE circleType1, 
    final SSCircleE circleType2){

    if(SSObjU.isNull(circleType1, circleType2)){
      return false;
    }
    
    return circleType1.toString().equals(circleType2.toString());
  }
  
  public static Boolean isGroupCircle(
    final SSCircleE circleType){

    return SSCircleE.equals(circleType, SSCircleE.group);
  }
  
  public static Boolean contains(
    final List<SSCircleE> circleTypes, 
    final SSCircleE       toContainCircleType) throws Exception{

    if(circleTypes == null){
      throw new Exception("pars null");
    }
    
    for(SSCircleE circleType : circleTypes){
      if(SSStrU.equals(toStr(circleType), toStr(toContainCircleType))){
        return true;
      }
    }
    
    return false;
  }
  
  private SSCircleE(){}
  
  @Override
  public Object jsonLDDesc(){
    return SSStrU.valueString;
  }
}
