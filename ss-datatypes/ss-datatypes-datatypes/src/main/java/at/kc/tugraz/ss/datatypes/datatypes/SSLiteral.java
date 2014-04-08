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
 package at.kc.tugraz.ss.datatypes.datatypes;

import at.kc.tugraz.socialserver.utils.SSLinkU;
import at.kc.tugraz.socialserver.utils.SSObjU;
import at.kc.tugraz.socialserver.utils.SSStrU;

public class SSLiteral extends SSEntityA{

  public boolean isnumeric = false;
  
  public static SSLiteral get(final String value) throws Exception{
    
    if(SSStrU.isEmpty(value)){
      return null;
    }
    
    return new SSLiteral(value);
  }
  
  public static SSLiteral get(final Long value) throws Exception{
    
    if(SSObjU.isNull(value)){
      return null;
    }
    
    return new SSLiteral(value);
  }
  
  private SSLiteral(final String value) throws Exception{
    
    super(value);
    
    isnumeric = false;
  }
  
  private SSLiteral(final Long value) throws Exception{
    
    super(String.valueOf(value.longValue()));
    
    isnumeric = true;
  }
  
  @Override
  public Object jsonLDDesc(){
    return "literal";
  }
}