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
package at.kc.tugraz.ss.serv.jobs.evernote.api;

import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteInfo;
import com.evernote.edam.type.LinkedNotebook;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;
import com.evernote.edam.type.SharedNotebook;
import java.util.List;

public interface SSEvernoteServerI {
  public SSEvernoteInfo       evernoteNoteStoreGet       (SSServPar parA) throws Exception;
  public List<Notebook>       evernoteNotebooksGet       (SSServPar parA) throws Exception;
  public List<SharedNotebook> evernoteNotebooksSharedGet (SSServPar parA) throws Exception;
  public List<LinkedNotebook> evernoteNotebooksLinkedGet (SSServPar parA) throws Exception;
  public List<Note>           evernoteNotesGet           (SSServPar parA) throws Exception;
  public List<Note>           evernoteNotesLinkedGet     (SSServPar parA) throws Exception;
}
