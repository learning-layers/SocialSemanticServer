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
package at.tugraz.sss.servs.mail.conf;

import at.tugraz.sss.serv.conf.SSCoreServConfA;
import at.tugraz.sss.servs.mail.datatype.SSMailReceiveE;
import at.tugraz.sss.servs.mail.datatype.SSMailSendE;

public class SSMailConf extends SSCoreServConfA{

  public SSMailSendE    sendProvider    = null;
  public SSMailReceiveE receiveProvider = null;
  public String         sendUserName    = "xxxx@xx.at";
  public String         sendPassword    = "xxxx";
  
  public static SSMailConf copy(final SSMailConf orig){

    final SSMailConf copy = (SSMailConf) SSCoreServConfA.copy(orig, new SSMailConf());
    
    copy.sendProvider      = orig.sendProvider;
    copy.sendUserName      = orig.sendUserName;
    copy.sendPassword      = orig.sendPassword;
    copy.receiveProvider   = orig.receiveProvider;
    
    return copy;
  }
}