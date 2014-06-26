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

public class SSLOMDesc extends SSEntityA{
  
  public String label = SSStrU.empty;
  public String lang  = SSStrU.empty;
  
  public SSLOMDesc(
    String label,
    String lang) throws Exception{
    
    super(label);
    
    this.label = label;
    this.lang  = lang;
  }
  
  public List<SSLOMDesc> distinctDescs(List<SSLOMDesc> descs){
    
    List<String>    descLabels = new ArrayList<>();
    List<SSLOMDesc> result     = new ArrayList<SSLOMDesc>();
    
    for(SSLOMDesc desc : descs){
      
      if(
        !SSStrU.isEmpty      (desc.label) &&
        !descLabels.contains (desc.label)){
        
        descLabels.add  (desc.label);
        result.add      (desc);
      }
    }
    
    return result;
  }
  
  public SSLOMDesc descInLangWithDefault(List<SSLOMDesc> descs, String lang)throws Exception{
    
    for(SSLOMDesc desc : descs){
      
      if(SSStrU.equals(desc.lang, lang)){
        return desc;
      }
    }
    
    if(descs.size() >= 1){
      return descs.get(0); 
    }
    
    return new SSLOMDesc(SSStrU.empty, SSStrU.empty);
  }
  
  public SSLOMDesc descInLang(List<SSLOMDesc> descs, String lang)throws Exception{
    
    for(SSLOMDesc desc : descs){
      
      if(SSStrU.equals(desc.lang, lang)){
        return desc;
      }
    }
    
    return new SSLOMDesc(SSStrU.empty, SSStrU.empty);
  }
  
  @Override
  public Object jsonLDDesc(){
    return "dtheiler";
  }
}
