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
package at.tugraz.sss.servs.mail.datatype.par;

import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSUri;

public class SSMailsReceivePar extends SSServPar{
  
  //TODO change name to fromUser
  public String fromEmail    = null;
  public String fromPassword = null;
  
  public SSMailsReceivePar(){}
  
  public SSMailsReceivePar(
    final SSUri     user,
    final String    fromEmail,
    final String    fromPassword,
    final Boolean   withUserRestriction,
    final Boolean   shouldCommit){
    
    super(SSServOpE.mailsReceive, null, user);
    
    this.fromEmail            = fromEmail;
    this.fromPassword         = fromPassword;
    this.withUserRestriction  = withUserRestriction;
    this.shouldCommit         = shouldCommit;
  }
}