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
package at.kc.tugraz.ss.service.userevent.datatypes;

import at.kc.tugraz.ss.datatypes.datatypes.SSEntityEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSLabelStr;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityDescA;

public class SSUEDesc extends SSEntityDescA{
  
  public SSUEDesc(SSUri entityUri, SSLabelStr entityLabel, Long creationTime, final SSUri           author){
    super(entityUri, entityLabel, creationTime, SSEntityEnum.userEvent, SSEntityEnum.userEventDesc, author);
  }
  
  public static SSUEDesc get(SSUri entityUri, SSLabelStr entityLabel, Long entityCreationTime, final SSUri           author){
    return new SSUEDesc(entityUri, entityLabel, entityCreationTime, author);
  }
  
  @Override
  public Object jsonLDDesc(){
    return super.jsonLDDesc();
  }
}

