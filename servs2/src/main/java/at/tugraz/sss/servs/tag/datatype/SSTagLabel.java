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
package at.tugraz.sss.servs.tag.datatype;

import at.tugraz.sss.servs.util.SSObjU;
import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.common.impl.SSServErrReg;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSErrE;
import at.tugraz.sss.servs.entity.datatype.SSEntityA;
import java.util.ArrayList;
import java.util.List;

public class SSTagLabel extends SSEntityA{

  public static SSTagLabel get(
    final String string) throws SSErr{
    
    if(string == null){
      return null;
    }
    
    return new SSTagLabel(string);
  }
  
  public static List<SSTagLabel> get(
    final List<String> strings) throws SSErr{

    final List<SSTagLabel> result = new ArrayList<>();
    
    if(strings == null){
      return result;
    }
    
    for(String string : strings){
      result.add(get(string));
    }
    
    return result;
  }
  
  public static void addDistinctNotEmpty(
    final List<SSTagLabel>     labels,
    final SSTagLabel           label){
    
    if(
      SSStrU.isEmpty (labels, label) ||
      labels.contains(label)){
      return;
    }
    
    labels.add(label);
  }
  
  public static void addDistinctNotEmpty(
    final List<SSTagLabel>  labels,
    final List<SSTagLabel>  toAddLabels){
    
    if(SSObjU.isNull(labels, toAddLabels)){
      return;
    }
    
    toAddLabels.stream().filter((label) -> (
      !SSStrU.isEmpty   (label) &&
        !labels.contains(label))).forEach((label) -> {
          labels.add(label);
        });
  }
  
  public static List<SSTagLabel> asListNotEmpty(final SSTagLabel... labels){
   
    final List<SSTagLabel> result = new ArrayList<>();
    
    if(labels == null){
      return result;
    }
    
    for(SSTagLabel label : labels){
      
      if(!SSStrU.isEmpty(label)){
        result.add(label);
      }
    }
    
    return result;
  }

  public static List<SSTagLabel> asListNotEmpty(final List<SSTagLabel> labels){
    return asListNotEmpty(labels.toArray(new SSTagLabel[labels.size()]));
  }
  
  protected SSTagLabel(final String label) throws SSErr{
    super(getTagLabel(label));
  }
  
  private static String getTagLabel(final String label) throws SSErr{
    
    if(label == null){
      return null;
    }
    
    try{
      
//previously replaced blanks with underlines automatically SSStrU.replaceAll(label, SSStrU.blank, SSStrU.underline);
//previously accepted only latin letters, numbers and underline return tmpLabel.replaceAll("[^a-zA-Z0-9_]+", SSStrU.empty);
      
      //accept unicode letters, blank, numbers, underline, hyphen
      final String tmpLabel = label.replaceAll("[^\\p{L}\\p{Zs}0-9_-]+", SSStrU.empty);
      
      if(SSStrU.isEmpty(tmpLabel)){
        throw SSErr.get(SSErrE.tagLabelInvalid);
      }
      
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