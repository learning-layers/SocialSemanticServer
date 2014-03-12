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
package at.kc.tugraz.ss.serv.jobs.evernote.impl.helper;

import at.kc.tugraz.socialserver.utils.SSLinkU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.datatypes.datatypes.SSLabelStr;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteInfo;
import com.evernote.edam.type.LinkedNotebook;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;
import com.evernote.edam.type.Resource;
import java.util.List;

public class SSEvernoteUriHelper {
  
  public SSUri getNormalOrSharedNotebookUri(SSLabelStr userName, Notebook notebook, List<String> sharedNotebookGuids) throws Exception{
    
    try{
     
      if(
        sharedNotebookGuids.contains   (notebook.getGuid()) &&
        SSStrU.isNotEmpty              (notebook.getName())){
        return SSUri.get(createSharedNotebookUriStr(userName, notebook));
      }
      
    }catch(Exception error){}
    
    return getNotebookDefaultUri(notebook);
  }
  
  public Boolean isSharedNotebookUri(SSLabelStr userName, Notebook notebook, SSUri notebookUri){
    
    String sharedNotebookUriStr;
    
    try{
      sharedNotebookUriStr = createSharedNotebookUriStr(userName, notebook);
    }catch(Exception error){
      return false;
    }
    
    if(
      notebookUri == null || 
      SSStrU.isEmpty(notebookUri.toString())){
      return false;
    }
    
    return notebookUri.toString().equals(sharedNotebookUriStr);
  }
  
  public SSUri getNormalOrSharedNoteUri(SSEvernoteInfo evernoteInfo, Note note) throws Exception {
    return SSUri.get(evernoteInfo.shardUri + "view/notebook/" + note.getGuid());
  }
  
  public SSUri getLinkedNoteUri(LinkedNotebook linkedNotebook, Note note) throws Exception{
    return SSUri.get(linkedNotebook.getWebApiUrlPrefix() + "share/view/" + linkedNotebook.getShareKey() + "?#n=" + note.getGuid());
  }
  
  public SSUri getLinkedNotebookUri(LinkedNotebook linkedNotebook) throws Exception {
    return SSUri.get(linkedNotebook.getWebApiUrlPrefix() + "share/" + linkedNotebook.getShareKey());
  }
  
  public SSUri getResourceUri(SSEvernoteInfo evernoteInfo, Resource resource) throws Exception{
    return SSUri.get(evernoteInfo.shardUri + "res/" + resource.getGuid());
  }
  
  private String createSharedNotebookUriStr(SSLabelStr userName, Notebook notebook) throws Exception{
    
    //TODO dtheiler: check evernote environment to use here
    return SSLinkU.httpsEvernote + "pub/" + SSStrU.toString(userName) + SSStrU.slash + notebook.getPublishing().getUri(); //7SSStrU.replaceBlanksSpecialCharactersDoubleDots(notebook.getName(), SSStrU.empty)
  }
  
  private SSUri getNotebookDefaultUri(Notebook notebook) throws Exception{
    
    if(
      notebook                  == null ||
      SSStrU.isEmpty(notebook.getGuid())){
      return SSUri.get(SSLinkU.httpsEvernote);
    }
    
    return SSUri.get(SSLinkU.httpsEvernote + "Home.action#b=" + notebook.getGuid());
  }
}