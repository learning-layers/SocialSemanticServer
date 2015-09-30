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
package at.kc.tugraz.ss.serv.dataimport.datatypes.pars;

import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSUri;

public class SSDataImportBitsAndPiecesPar extends SSServPar{
  
  public String  authToken        = null;
  public String  authEmail        = null;
  public String  emailInUser      = null;
  public String  emailInPassword  = null;
  public String  emailInEmail     = null;
  public Boolean importEvernote   = false;
  public Boolean importEmails     = false;
  
  public SSDataImportBitsAndPiecesPar(){}
  
  public SSDataImportBitsAndPiecesPar(
    final SSUri   user,
    final String  authToken,
    final String  authEmail,
    final String  emailInUser,
    final String  emailInPassword,
    final String  emailInEmail,
    final Boolean importEvernote, 
    final Boolean importEmails,
    final Boolean withUserRestriction,
    final Boolean shouldCommit){
    
    super(SSServOpE.dataImportBitsAndPieces, null, user);
    
    this.authToken           = authToken;
    this.authEmail           = authEmail;
    this.emailInUser         = emailInUser;
    this.emailInPassword     = emailInPassword;
    this.emailInEmail        = emailInEmail;
    this.importEvernote      = importEvernote;
    this.importEmails        = importEmails;
    this.withUserRestriction = withUserRestriction;
    this.shouldCommit        = shouldCommit;
  }
}