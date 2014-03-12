/**
 * Copyright 2013 Graz University of Technology - KTI (Knowledge Technologies Institute)
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
package at.kc.tugraz.ss.serv.datatypes.learnep.datatypes;

import at.kc.tugraz.ss.datatypes.datatypes.SSEntityEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSLabelStr;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityDescA;

public class SSLearnEpDesc extends SSEntityDescA{
  
  public SSLearnEpDesc(SSUri entityUri, SSLabelStr entityLabel, Long creationTime, final SSUri           author){
    super(entityUri, entityLabel, creationTime, SSEntityEnum.learnEp, SSEntityEnum.learnEpDesc,author);
  }
  
  public static SSLearnEpDesc get(SSUri entityUri, SSLabelStr entityLabel, Long entityCreationTime, final SSUri           author){
    return new SSLearnEpDesc(entityUri, entityLabel, entityCreationTime, author);
  }
  
  @Override
  public Object jsonLDDesc(){
    return super.jsonLDDesc();
  }
}
