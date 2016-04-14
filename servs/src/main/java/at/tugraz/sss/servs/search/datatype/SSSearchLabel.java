/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
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

/**
 * @author Dieter Theiler
 */

package at.tugraz.sss.servs.search.datatype;

import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.common.impl.SSServErrReg;
import at.tugraz.sss.servs.entity.datatype.SSEntityA;
import java.util.ArrayList;
import java.util.List;

public class SSSearchLabel extends SSEntityA{

  public static SSSearchLabel get(
    final String value) throws SSErr{
    
    if(value == null){
      return null;
    }
    
    return new SSSearchLabel(value);
  }
  
  public static List<SSSearchLabel> get(
    final List<String> strings) throws SSErr{

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
//    final String string) throws SSErr {
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
  
  protected SSSearchLabel(final String label) throws SSErr{
    super(getSearchLabel(label));
  }
  
  private static String getSearchLabel(final String label) throws SSErr{
    
    try{
      
      String tmpLabel = label;
      
      tmpLabel = tmpLabel.replace(SSStrU.singleQuote, SSStrU.empty);
      tmpLabel = tmpLabel.replace(SSStrU.doubleQuote, SSStrU.empty);
      
      return tmpLabel;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
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
