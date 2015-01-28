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
 package at.kc.tugraz.ss.serv.auth.conf;

import at.kc.tugraz.ss.serv.serv.api.SSCoreServConfA;
import at.kc.tugraz.ss.serv.ss.auth.datatypes.enums.SSAuthEnum;

public class SSAuthConf extends SSCoreServConfA{
  
  public static final String   noAuthKey              = "1234";
  public SSAuthEnum            authType               = null;
  public String                fileName               = null;
  public String                systemUserPassword     = null;
  
  public static SSAuthConf copy(final SSAuthConf orig){
    
    final SSAuthConf copy = (SSAuthConf) SSCoreServConfA.copy(orig, new SSAuthConf());
    
    copy.authType               = SSAuthEnum.get(SSAuthEnum.toStr(orig.authType));
    copy.fileName               = orig.fileName;
    copy.systemUserPassword     = orig.systemUserPassword;
    
    return copy;
  }
}