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
package at.kc.tugraz.ss.serv.lomextractor.datatypes;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityA;
import java.util.ArrayList;
import java.util.List;

public class SSLOMTitle extends SSEntityA{
  
  public String label = SSStrU.empty;
  public String lang  = SSStrU.empty;
  
  public SSLOMTitle(String label, String lang)throws Exception{
    
    super(label);
    
    this.label = label;
    this.lang  = lang;
  }
  
  public static List<SSLOMTitle> distinctTitles(List<SSLOMTitle> titles){
    
    List<String>     titleLabels = new ArrayList<String>();
    List<SSLOMTitle> result      = new ArrayList<SSLOMTitle>();
    
    for(SSLOMTitle title : titles){
      
      if(
        SSStrU.isNotEmpty     (title.label) &&
        !titleLabels.contains (title.label)){
        
        titleLabels.add (title.label);
        result.add      (title);
      }
    }
    
    return result;
  }

  public SSLOMTitle titleInLangWithDefault(List<SSLOMTitle> titles, String lang) throws Exception{
    
    for(SSLOMTitle title : titles){
      
      if(
        SSStrU.equals     (title.lang, lang) &&
        SSStrU.isNotEmpty (title.label)){
        return title;
      }
    }
    
    for(SSLOMTitle title : titles){
      
       if(SSStrU.isNotEmpty (title.label)){
         return title;
       }
    }
    
    return new SSLOMTitle(SSStrU.empty, SSStrU.empty);    
  }
  
  public SSLOMTitle titleInLang(List<SSLOMTitle> titles, String lang) throws Exception{
    
    for(SSLOMTitle title : titles){
      
      if(
        SSStrU.equals     (title.lang, lang) &&
        SSStrU.isNotEmpty (title.label)){
        return title;
      }
    }
    
    return new SSLOMTitle(SSStrU.empty, SSStrU.empty);    
  }
  
  @Override
  public Object jsonLDDesc(){
    return "dtheiler";
  }
}
