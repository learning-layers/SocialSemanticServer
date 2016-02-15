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

import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteInfo;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteNoteAddPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteNoteGetPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteNoteStoreGetPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteNoteTagNamesGetPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteNotebookGetPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteNotebooksSharedGetPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteResourceAddPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteResourceByHashGetPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteResourceGetPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteUSNSetPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteUserAddPar;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.impl.api.SSServServerI;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;
import com.evernote.edam.type.Resource;
import com.evernote.edam.type.SharedNotebook;
import java.util.List;

public interface SSEvernoteServerI extends SSServServerI{
  
  public SSEvernoteInfo       evernoteNoteStoreGet       (final SSEvernoteNoteStoreGetPar       par) throws SSErr;
  public Resource             evernoteResourceGet        (final SSEvernoteResourceGetPar        par) throws SSErr;
  public Note                 evernoteNoteGet            (final SSEvernoteNoteGetPar            par) throws SSErr;
  public List<String>         evernoteNoteTagNamesGet    (final SSEvernoteNoteTagNamesGetPar    par) throws SSErr;
  public Notebook             evernoteNotebookGet        (final SSEvernoteNotebookGetPar        par) throws SSErr;
  public Resource             evernoteResourceByHashGet  (final SSEvernoteResourceByHashGetPar  par) throws SSErr;
  public List<SharedNotebook> evernoteNotebooksSharedGet (final SSEvernoteNotebooksSharedGetPar par) throws SSErr;
  public boolean              evernoteUserAdd            (final SSEvernoteUserAddPar            par) throws SSErr;
  public boolean              evernoteNoteAdd            (final SSEvernoteNoteAddPar            par) throws SSErr;
  public boolean              evernoteUSNSet             (final SSEvernoteUSNSetPar             par) throws SSErr;
  public boolean              evernoteResourceAdd        (final SSEvernoteResourceAddPar        par) throws SSErr;

  //  public String               evernoteUsersAuthTokenGet  (final SSServPar parA) throws SSErr;
//  public List<Note>           evernoteNotesLinkedGet     (final SSServPar parA) throws SSErr;
//  public List<Notebook>       evernoteNotebooksGet       (final SSServPar parA) throws SSErr;
  //  public List<LinkedNotebook> evernoteNotebooksLinkedGet (final SSServPar parA) throws SSErr;
  //  public List<Note>           evernoteNotesGet           (final SSServPar parA) throws SSErr;
}
