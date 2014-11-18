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
package at.kc.tugraz.ss.serv.jobs.evernote.impl.helper;

import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteInfo;
import at.kc.tugraz.ss.serv.jobs.evernote.impl.fct.sql.SSEvernoteSQLFct;
import com.evernote.edam.type.Notebook;
import com.evernote.edam.type.SharedNotebook;
import java.util.ArrayList;
import java.util.List;

public class SSEvernoteHelper {
  
  public final SSEvernoteUriHelper   uriHelper;
  public final SSEvernoteSQLFct      sqlFct;
  
  public SSEvernoteHelper(final SSDBSQLI dbSQL) throws Exception{
    
    this.uriHelper   = new SSEvernoteUriHelper   ();
    this.sqlFct      = new SSEvernoteSQLFct      (dbSQL);
  }
  
  public Boolean isSharedNootebook(SSUri notebookUri, SSLabel userName, Notebook notebook) {
    return uriHelper.isSharedNotebookUri(userName, notebook, notebookUri);
  }

  public List<String> getSharedNotebookGuids(List<SharedNotebook> sharedNotebooks) {
    
    List<String> sharedNotebookGuids = new ArrayList<>();
    
    if(sharedNotebooks == null){
      return sharedNotebookGuids;
    }
    
    for(SharedNotebook sharedNotebook : sharedNotebooks){
      sharedNotebookGuids.add(sharedNotebook.getNotebookGuid());
    }
    
    return sharedNotebookGuids;
  }

  public SSLabel getUserName(final SSEvernoteInfo evernoteInfo) throws Exception{
    return SSLabel.get(evernoteInfo.userStore.getUser().getUsername());
  }
  
  public String getUserEmail(final SSEvernoteInfo evernoteInfo) throws Exception{
    return evernoteInfo.userStore.getUser().getEmail();
  }
}