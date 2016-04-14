/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
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

package at.tugraz.sss.servs.evernote.datatype;

import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSLabel;
import com.evernote.clients.NoteStoreClient;
import com.evernote.clients.UserStoreClient;
import com.evernote.edam.notestore.SyncChunk;

public class SSEvernoteInfo {

  public UserStoreClient userStore          = null;
  public NoteStoreClient noteStore          = null;
  public SSUri           shardUri           = null;
  public SyncChunk       noteStoreSyncChunk = null;
  public String          authToken          = null;
  public SSLabel         userName           = null;
  
  public static SSEvernoteInfo get(
    final UserStoreClient userStore, 
    final NoteStoreClient noteStore, 
    final SSUri           shardUri,
    final SyncChunk       noteStoreSyncChunk,
    final String          authToken){
    
    return new SSEvernoteInfo(userStore, noteStore, shardUri, noteStoreSyncChunk, authToken);
  }
  
  private SSEvernoteInfo(
    final UserStoreClient userStore,
    final NoteStoreClient noteStore,
    final SSUri           shardUri,
    final SyncChunk       noteStoreSyncChunk,
    final String          authToken){
    
    this.userStore          = userStore;
    this.noteStore          = noteStore;
    this.shardUri           = shardUri;
    this.noteStoreSyncChunk = noteStoreSyncChunk;
    this.authToken          = authToken;
  }
}
