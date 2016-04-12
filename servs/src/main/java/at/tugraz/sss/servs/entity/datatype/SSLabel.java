/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
package at.tugraz.sss.servs.entity.datatype;

import at.tugraz.sss.servs.util.SSObjU;
import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSEntityA;
import io.swagger.annotations.*;
import java.util.ArrayList;
import java.util.List;

@ApiModel
public class SSLabel extends SSEntityA{

  public static SSLabel get(final String label) throws SSErr {
    
    if(label == null){
      return null;
    }
    
    return new SSLabel(label);
  }
  
  public static SSLabel get(final SSEntityA entity) throws SSErr {
    
    if(entity == null){
      return null;
    }
    
    return new SSLabel(SSStrU.toStr(entity));
  }
  
  public static List<SSLabel> get(final List<String> strings) throws SSErr {
    
    final List<SSLabel> result = new ArrayList<>();
    
    if(strings == null){
      return result;
    }
    
    for(String string : strings){
      result.add(get(string));
    }
    
    return result;
  }
  
  public SSLabel(){/* Do nothing because of only JSON Jackson needs this */ }
  
  protected SSLabel(final String string) throws SSErr {
    super(string);
  }
  
  public static void addDistinctNotNull(
    final List<SSLabel>  labels,
    final List<SSLabel>  toAddLabels){

    if(SSObjU.isNull(labels, toAddLabels)){
      return;
    }
    
    addDistinctNotNull(labels, toAddLabels.toArray(new SSLabel[toAddLabels.size()]));
  }
  
  public static void addDistinctNotNull(
    final List<SSLabel>     labels,
    final SSLabel...        toAddLabels){
    
    if(SSObjU.isNull(labels, toAddLabels)){
      return;
    }
    
    for(SSLabel label : toAddLabels){
      
      if(
        label != null &&
        !SSStrU.contains(labels, label)){
        
        labels.add(label);
      }
    }
  }
  
  public static List<SSLabel> asListNotNull(final SSLabel... labels){
   
    final List<SSLabel> result = new ArrayList<>();
    
    if(labels == null){
      return result;
    }
    
    for(SSLabel label : labels){
      
      if(label != null){
        result.add(label);
      }
    }
    
    return result;
  }
}