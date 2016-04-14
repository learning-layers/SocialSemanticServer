/**
 * Code contributed to the Learning Layers project
 * http://www.learning-layers.eu
 * Development is partly funded by the FP7 Programme of the European Commission under
 * Grant Agreement FP7-ICT-318209.
 * Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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

/**
 * @author Dieter Theiler
 */

package at.tugraz.sss.servs.mail.datatype;

import at.tugraz.sss.servs.util.SSVarNames;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSServPar; 
import java.sql.*;

public class SSMailSendPar extends SSServPar{
  
  public String fromEmail = null;
  public String toEmail   = null;
  public String subject   = null;
  public String content   = null;
  
  public SSMailSendPar(){/* Do nothing because of only JSON Jackson needs this */ }
  
  public SSMailSendPar(
    final SSServPar servPar,
    final SSUri     user,
    final String    fromEmail,
    final String    toEmail,
    final String    subject,
    final String    content,
    final boolean   withUserRestriction,
    final boolean   shouldCommit){
    
    super(SSVarNames.mailSend, null, user, servPar.sqlCon);
    
    this.fromEmail            = fromEmail;
    this.toEmail              = toEmail;
    this.subject              = subject;
    this.content              = content;
    this.withUserRestriction  = withUserRestriction;
    this.shouldCommit         = shouldCommit;
  }
}