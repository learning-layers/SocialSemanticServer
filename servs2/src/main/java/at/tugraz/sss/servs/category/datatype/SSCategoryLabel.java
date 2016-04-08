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
package at.tugraz.sss.servs.category.datatype;

import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.datatype.enums.SSErrE;
import at.tugraz.sss.serv.datatype.api.SSEntityA;
import java.util.ArrayList;
import java.util.List;

public class SSCategoryLabel extends SSEntityA{

  public static SSCategoryLabel get(
    final String string) throws SSErr{
    
    if(string == null){
      return null;
    }
    
    return new SSCategoryLabel(string);
  }
  
  public static List<SSCategoryLabel> get(
    final List<String> strings) throws SSErr{

    final List<SSCategoryLabel> result = new ArrayList<>();
    
    if(strings == null){
      return result;
    }
    
    for(String string : strings){
      result.add(get(string));
    }
    
    return result;
  }
  
  public static void addDistinctNotEmpty(
    final List<SSCategoryLabel>     labels,
    final SSCategoryLabel           label){
    
    if(
      SSStrU.isEmpty (labels, label) ||
      labels.contains(label)){
      return;
    }

    labels.add(label);
  }
  
  public static void addDistinctNotEmpty(
    final List<SSCategoryLabel>  labels,
    final List<SSCategoryLabel>  toAddLabels){
    
    if(SSObjU.isNull(labels, toAddLabels)){
      return;
    }
    
    toAddLabels.stream().filter((label) -> (
      !SSStrU.isEmpty (label) &&
        !labels.contains(label))).forEach((label) -> {
          labels.add(label);
        });
  }
  
  public static List<SSCategoryLabel> asListNotEmpty(final List<SSCategoryLabel> labels){
    return asListNotEmpty(labels.toArray(new SSCategoryLabel[labels.size()]));
  }
  
  public static List<SSCategoryLabel> asListNotEmpty(final SSCategoryLabel... labels){
   
    final List<SSCategoryLabel> result = new ArrayList<>();
    
    if(labels == null){
      return result;
    }
    
    for(SSCategoryLabel label : labels){
      
      if(!SSStrU.isEmpty(label)){
        result.add(label);
      }
    }
    
    return result;
  }
  
  protected SSCategoryLabel(final String label) throws SSErr{
    super(getCategoryLabel(label));
  }
  
  private static String getCategoryLabel(final String label) throws SSErr{
    
    try{
      if(label == null){
        return null;
      }
      
//previously replaced blanks with underlines automatically SSStrU.replaceAll(label, SSStrU.blank, SSStrU.underline);
//previously accepted only latin letters, numbers and underline return tmpLabel.replaceAll("[^a-zA-Z0-9_]+", SSStrU.empty);
      
      //accept unicode letters, blank, numbers, underline, hyphen
      
      final String tmpLabel = label.replaceAll("[^\\p{L}\\p{Zs}0-9_-]+", SSStrU.empty);
      
      if(SSStrU.isEmpty(tmpLabel)){
        throw SSErr.get(SSErrE.categoryLabelInvalid);
      }
      
      return tmpLabel;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}