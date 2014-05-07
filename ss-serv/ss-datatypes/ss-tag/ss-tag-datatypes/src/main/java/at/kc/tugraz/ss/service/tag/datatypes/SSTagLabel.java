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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SSTagLabel extends SSEntityA{

  public static SSTagLabel get(
    final String string) throws Exception{
    
    if(string == null){
      return null;
    }
    
    return new SSTagLabel(string);
  }
  
  public static List<SSTagLabel> get(
    final List<String> strings) throws Exception{

    final List<SSTagLabel> result = new ArrayList<SSTagLabel>();
    
    for(String string : strings){
      result.add(get(string));
    }
    
    return result;
  }
  
  public static void checkTagLabel(
    final String tagLabel) throws Exception {

    if(SSStrU.isEmpty(tagLabel)){
      throw new Exception("Invalid tag (null or empty): " + tagLabel);
    }
    
    final String tmpTagLabel = tagLabel.replaceAll("[/\\*\\?<>]", SSStrU.empty);
    
    if(
      SSStrU.isEmpty(tmpTagLabel) ||
      !Pattern.matches("^[a-zA-Z0-9_-]*$", tmpTagLabel)){
      throw new Exception("Invalid tag: " + tmpTagLabel);
    }
  }
  
  @Override
  public Object jsonLDDesc() {
    return SSVarU.xsd + SSStrU.colon + SSStrU.valueString;
  }
  
  protected SSTagLabel(final String label) throws Exception{
    
    super(label);
    
    checkTagLabel(label);
  }
}

//public static Collection<String> toString(
//    SSTagString[] tagStrings){
//    
//    List<String> result = new ArrayList<String>();
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