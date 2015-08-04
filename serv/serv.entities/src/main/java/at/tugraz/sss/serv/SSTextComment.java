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
package at.tugraz.sss.serv;

import java.util.ArrayList;
import java.util.List;

public class SSTextComment extends SSEntityA {

  @Override
  public Object jsonLDDesc() {
    return SSVarNames.xsd + SSStrU.colon + SSStrU.valueString;
  }
  
  public static SSTextComment get(final String comment) throws Exception{
    
    if(comment == null){
      return null;
    }
    
    return new SSTextComment(SSStrU.replaceAllLineFeedsWithTextualRepr(comment));
  }
  
  public static List<SSTextComment> get(final List<String> strings) throws Exception{
    
    final List<SSTextComment> result = new ArrayList<>();
    
    if(strings == null){
      return result;
    }
    
    for(String string : strings){
      result.add(get(string));
    }
    
    return result;
  }

  public static List<SSTextComment> asListWithoutNullAndEmpty(final SSTextComment... comments){
    
    final List<SSTextComment> result = new ArrayList<>();
    
    if(comments == null){
      return result;
    }
    
    for(SSTextComment comment : comments){
      
      if(SSStrU.isEmpty(comment)){
        continue;
      }
      
      result.add(comment);
    }
    
    return result;
  }
  
  public static void addDistinctWithoutNull(
    final List<SSTextComment>  comments,
    final List<SSTextComment>  toAddComments){
    
    if(SSObjU.isNull(comments, toAddComments)){
      return;
    }
    
    for(SSTextComment comment : toAddComments){
      
      if(comment == null){
        continue;
      }
      
      if(!SSStrU.contains(comments, comment)){
        comments.add(comment);
      }
    }
  }
  
  private SSTextComment(final String value) throws Exception{
    super(value);
  }
}