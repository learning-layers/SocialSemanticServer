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
package at.kc.tugraz.ss.datatypes.datatypes.entity;

import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.jsonld.datatypes.api.SSJSONLDPropI;
import java.util.ArrayList;
import java.util.List;

public abstract class SSEntityA implements SSJSONLDPropI{
  
  protected String val = null;

  @Override 
  public String toString(){
    return val;
  }
  
  protected SSEntityA(final String value) throws Exception{
    
    if(value == null){
      throw new Exception("entity cannot be created from null");
    }
    
    this.val = value;
  }
  
  protected SSEntityA(final SSEntityA entity) throws Exception{
    
    if(entity == null){
      throw new Exception("entity cannot be created from null");
    }
    
    this.val = entity.toString();
  }
  
  protected SSEntityA(final Integer value) throws Exception{
    
    if(value == null){
      throw new Exception("entity cannot be created from null");
    }
    
    this.val = value.toString();
  }
  
  protected SSEntityA(final Double value) throws Exception{
    
    if(value == null){
      throw new Exception("entity cannot be created from null");
    }
    
    this.val = value.toString();
  }
}

//  public static SSEntityA[] toArray(Collection<? extends SSEntityA> entities) {
//    return (SSEntityA[]) entities.toArray(new SSEntityA[entities.size()]);
//  }