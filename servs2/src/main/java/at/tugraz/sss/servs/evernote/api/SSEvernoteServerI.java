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
package at.tugraz.sss.servs.evernote.api;

import at.tugraz.sss.servs.evernote.datatype.SSEvernoteInfo;
import at.tugraz.sss.servs.evernote.datatype.SSEvernoteNoteAddPar;
import at.tugraz.sss.servs.evernote.datatype.SSEvernoteNoteGetPar;
import at.tugraz.sss.servs.evernote.datatype.SSEvernoteNoteStoreGetPar;
import at.tugraz.sss.servs.evernote.datatype.SSEvernoteNoteTagNamesGetPar;
import at.tugraz.sss.servs.evernote.datatype.SSEvernoteNotebookGetPar;
import at.tugraz.sss.servs.evernote.datatype.SSEvernoteResourceAddPar;
import at.tugraz.sss.servs.evernote.datatype.SSEvernoteResourceByHashGetPar;
import at.tugraz.sss.servs.evernote.datatype.SSEvernoteResourceGetPar;
import at.tugraz.sss.servs.evernote.datatype.SSEvernoteUSNSetPar;
import at.tugraz.sss.servs.evernote.datatype.SSEvernoteUserAddPar;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.impl.api.SSServServerI;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;
import com.evernote.edam.type.Resource;
import java.util.List;

public interface SSEvernoteServerI extends SSServServerI{
  
  public SSEvernoteInfo       evernoteNoteStoreGet       (final SSEvernoteNoteStoreGetPar       par) throws SSErr;
  public Resource             evernoteResourceGet        (final SSEvernoteResourceGetPar        par) throws SSErr;
  public Note                 evernoteNoteGet            (final SSEvernoteNoteGetPar            par) throws SSErr;
  public List<String>         evernoteNoteTagNamesGet    (final SSEvernoteNoteTagNamesGetPar    par) throws SSErr;
  public Notebook             evernoteNotebookGet        (final SSEvernoteNotebookGetPar        par) throws SSErr;
  public Resource             evernoteResourceByHashGet  (final SSEvernoteResourceByHashGetPar  par) throws SSErr;
  public boolean              evernoteUserAdd            (final SSEvernoteUserAddPar            par) throws SSErr;
  public boolean              evernoteNoteAdd            (final SSEvernoteNoteAddPar            par) throws SSErr;
  public boolean              evernoteUSNSet             (final SSEvernoteUSNSetPar             par) throws SSErr;
  public boolean              evernoteResourceAdd        (final SSEvernoteResourceAddPar        par) throws SSErr;

//  public List<SharedNotebook> evernoteNotebooksSharedGet (final SSEvernoteNotebooksSharedGetPar par) throws SSErr;
  //  public String               evernoteUsersAuthTokenGet  (final SSServPar parA) throws SSErr;
//  public List<Note>           evernoteNotesLinkedGet     (final SSServPar parA) throws SSErr;
//  public List<Notebook>       evernoteNotebooksGet       (final SSServPar parA) throws SSErr;
  //  public List<LinkedNotebook> evernoteNotebooksLinkedGet (final SSServPar parA) throws SSErr;
  //  public List<Note>           evernoteNotesGet           (final SSServPar parA) throws SSErr;
}
