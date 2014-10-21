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
package at.kc.tugraz.ss.service.tag.datatypes;

import at.kc.tugraz.socialserver.utils.*;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityA;
import at.kc.tugraz.ss.service.tag.datatypes.pars.err.SSTagInvalidTagErr;
import java.util.ArrayList;
import java.util.List;

public class SSTagLabel extends SSEntityA{

  public static SSTagLabel get(
    final String string) throws Exception{
    
    return new SSTagLabel(string);
  }
  
  public static List<SSTagLabel> get(
    final List<String> strings) throws Exception{

    final List<SSTagLabel> result = new ArrayList<>();
    
    if(strings == null){
      return result;
    }
    
    for(String string : strings){
      result.add(get(string));
    }
    
    return result;
  }
  
//  public static Boolean isTagLabel(
//    final String string) throws Exception {
//    
//    try{
//      if(SSStrU.isEmpty(string)){
//        return false;
//      }
//      
//      final String tmpTagLabel = string.replaceAll("[^a-zA-Z0-9]+", SSStrU.empty);
////      final String tmpTagLabel = string.replaceAll("[/\\*\\?\\'<>]", SSStrU.empty);
//      
//      if(
//        SSStrU.isEmpty(tmpTagLabel) ||
//        !Pattern.matches("^[a-zA-Z0-9_-]*$", tmpTagLabel)){
//        return false;
//      }
//      
//      return true;
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }
  
  public static List<SSTagLabel> asListWithoutNullAndEmpty(final SSTagLabel... tagLabels){
   
    final List<SSTagLabel> result = new ArrayList<>();
    
    if(tagLabels == null){
      return result;
    }
    
    for(SSTagLabel tagLabel : tagLabels){
      
      if(SSStrU.isEmpty(tagLabel)){
        continue;
      }
      
      result.add(tagLabel);
    }
    
    return result;
  }
  
  public static List<SSTagLabel> asListWithoutNullAndEmpty(final List<SSTagLabel> tagLabels){
   
    final List<SSTagLabel> result = new ArrayList<>();
    
    if(tagLabels == null){
      return result;
    }
    
    for(SSTagLabel tagLabel : tagLabels){
      
      if(SSStrU.isEmpty(tagLabel)){
        continue;
      }
      
      result.add(tagLabel);
    }
    
    return result;
  }
  
  @Override
  public Object jsonLDDesc() {
    return SSVarU.xsd + SSStrU.colon + SSStrU.valueString;
  }
  
  protected SSTagLabel(final String label) throws Exception{
    super(getTagLabel(label));
  }
  
  private static String getTagLabel(final String label) throws Exception{
    
    try{
      
      String tmpLabel = SSStrU.replaceAll(label, SSStrU.blank, SSStrU.underline);
      
      return tmpLabel.replaceAll("[^p{L}0-9_-]+", SSStrU.empty); //return tmpLabel.replaceAll("[^a-zA-Z0-9_]+", SSStrU.empty);
      
    }catch(Exception error){
      throw new SSTagInvalidTagErr("tag: " + label + "is not valid");
    }
  }
}

//public static Collection<String> toString(
//    SSTagString[] tagStrings){
//    
//    List<String> result = new ArrayList<>();
//    
//    for (SSTagString tagString : tagStrings){
//      result.add(tagString.toString());
//    }
//    
//    return result;
//  }


//  public static SSTagLabel[] toTagStringArray(Collection<SSTagLabel> toConvert) {
//    return (SSTagLabel[]) toConvert.toArray(new SSTagLabel[toConvert.size()]);
//  } 
