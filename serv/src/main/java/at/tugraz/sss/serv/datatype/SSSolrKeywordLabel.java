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
package at.tugraz.sss.serv.datatype;

import at.tugraz.sss.serv.errreg.SSServErrReg;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.api.SSEntityA;
import at.tugraz.sss.serv.datatype.enums.*;
import io.swagger.annotations.*;
import java.util.ArrayList;
import java.util.List;

@ApiModel
public class SSSolrKeywordLabel extends SSEntityA{

  public static SSSolrKeywordLabel get(final String string) throws SSErr{
    return new SSSolrKeywordLabel(string);
  }
  
  public static List<SSSolrKeywordLabel> get(final List<String> strings) throws SSErr{
    
    final List<SSSolrKeywordLabel> result = new ArrayList<>();
    
    for(String string : strings){
      result.add(get(string));
    }
    
    return result;
  }
  
  public static boolean isSolrKeywordLabel(
    final String string) throws SSErr{
    
    if(string == null || string.trim().isEmpty()){
      return false;
    }
    
//    final String tmpTag = string.replaceAll("[/\\*\\?<>]", "");
    final String tmpKeyword = string.replaceAll("[^\\p{L}\\p{Zs}0-9_-]+", SSStrU.empty);
    
//    if(!Pattern.matches("^[a-zA-Z0-9_-]*$\\s", tmpTag)){return false};
      
//    if(
//      tmpKeyword == null          ||
//      tmpKeyword.trim().isEmpty()){
//      return false;
//    }
    
    return true;
  }
  
  public SSSolrKeywordLabel(){/* Do nothing because of only JSON Jackson needs this */ }
  
  private SSSolrKeywordLabel(final String label) throws SSErr{
    
    super(label);
    
    if(!isSolrKeywordLabel(label)){
      SSServErrReg.regErrThrow(SSErrE.solrKeywordInvald);
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
