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
package at.kc.tugraz.ss.serv.jobs.evernote.api;

import at.tugraz.sss.serv.SSServPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteInfo;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;
import com.evernote.edam.type.Resource;
import com.evernote.edam.type.SharedNotebook;
import java.util.List;

public interface SSEvernoteServerI {
  public Resource             evernoteResourceGet        (final SSServPar parA) throws Exception;
  public Note                 evernoteNoteGet            (final SSServPar parA) throws Exception;
  public List<String>         evernoteNoteTagNamesGet    (final SSServPar parA) throws Exception;
  public SSEvernoteInfo       evernoteNoteStoreGet       (final SSServPar parA) throws Exception;
  public Notebook             evernoteNotebookGet        (final SSServPar parA) throws Exception;
  public Resource             evernoteResourceByHashGet  (final SSServPar parA) throws Exception;
  public List<SharedNotebook> evernoteNotebooksSharedGet (final SSServPar parA) throws Exception;
  public Boolean              evernoteUserAdd            (final SSServPar parA) throws Exception;
  public String               evernoteUsersAuthTokenGet  (final SSServPar parA) throws Exception;
  public Boolean              evernoteNoteAdd            (final SSServPar parA) throws Exception;
  public Boolean              evernoteUSNSet             (final SSServPar parA) throws Exception;
  public Boolean              evernoteResourceAdd        (final SSServPar parA) throws Exception;
  
//  public List<Note>           evernoteNotesLinkedGet     (final SSServPar parA) throws Exception;
//  public List<Notebook>       evernoteNotebooksGet       (final SSServPar parA) throws Exception;
  //  public List<LinkedNotebook> evernoteNotebooksLinkedGet (final SSServPar parA) throws Exception;
  //  public List<Note>           evernoteNotesGet           (final SSServPar parA) throws Exception;
}
