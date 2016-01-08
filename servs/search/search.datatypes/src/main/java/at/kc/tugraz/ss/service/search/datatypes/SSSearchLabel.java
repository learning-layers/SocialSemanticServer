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
package at.kc.tugraz.ss.service.search.datatypes;

import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.*;
import at.tugraz.sss.serv.datatype.api.SSEntityA;
import at.kc.tugraz.ss.service.search.datatypes.ret.SSSearchInvalidSearchLabelErr;
import java.util.ArrayList;
import java.util.List;

public class SSSearchLabel extends SSEntityA{

  public static SSSearchLabel get(
    final String string) throws Exception{
    
    return new SSSearchLabel(string);
  }
  
  public static List<SSSearchLabel> get(
    final List<String> strings) throws Exception{

    final List<SSSearchLabel> result = new ArrayList<>();
    
    if(strings == null){
      return result;
    }
    
    for(String string : strings){
      result.add(get(string));
    }
    
    return result;
  }
  
//  public static boolean isTagLabel(
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
  
  protected SSSearchLabel(final String label) throws Exception{
    super(getSearchLabel(label));
  }
  
  private static String getSearchLabel(final String label) throws Exception{
    
    try{
      
      String tmpLabel = label;
      
      tmpLabel = tmpLabel.replace(SSStrU.singleQuote, SSStrU.empty);
      tmpLabel = tmpLabel.replace(SSStrU.doubleQuote, SSStrU.empty);
      
      return tmpLabel;
    }catch(Exception error){
      throw new SSSearchInvalidSearchLabelErr("search label: " + label + "is not valid");
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
