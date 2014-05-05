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
package at.kc.tugraz.ss.test.serv.evernote;

import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.serv.jobs.evernote.conf.SSEvernoteConf;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteInfo;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.serv.test.api.SSServOpTestCaseA;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.NoteAttributes;
import com.evernote.edam.type.Notebook;
import com.evernote.edam.type.Resource;
import com.evernote.edam.type.ResourceAttributes;
import java.util.List;

public class SSEvernoteNoteStoreGetTest extends SSServOpTestCaseA{
  
  public SSEvernoteNoteStoreGetTest(SSEvernoteConf conf) throws Exception {
    super(conf, SSMethU.evernoteNoteStoreGet);
  }
  
  @Override
  protected void test() throws Exception {
    
    SSLogU.info(op + " Test start");
    
    List<Notebook>       noteBooks;
    SSEvernoteInfo       evernoteInfo;
    NoteAttributes       noteAttr;
    ResourceAttributes   resourceAttr;
    
    evernoteInfo = SSServCaller.getEvernoteInfo(userUri, ((SSEvernoteConf)conf).authTokens.get(0));
    
    //      opPars = new HashMap<String, Object>();
    //      opPars.put(SSVarU.shouldCommit,  false);
    //      opPars.put(SSVarU.noteStore,     evernoteInfo.noteStore);
    
    //      sharedNoteBooks = (List<SharedNotebook>)   SSServReg.callServServer(new SSServPar(SSMethU.evernoteNotebooksSharedGet, opPars));
    //
    //      System.out.println("shared notebooks");
    //
    //      for(SharedNotebook sharedNotebook : sharedNoteBooks){
    //
    //        System.out.println(sharedNotebook.getNotebookGuid());
    //        System.out.println(sharedNotebook.getServiceCreated());
    //        System.out.println(sharedNotebook.getUsername());
    //      }
    //
    //      System.out.println();
    
    //      linkedNotebooks = (List<LinkedNotebook>)   SSServReg.callServServer(new SSServPar(SSMethU.evernoteNotebooksLinkedGet, opPars));
    //
    //      System.out.println("linked notebooks");
    //
    //      for(LinkedNotebook linkedNotebook : linkedNotebooks){
    //
    //        System.out.println(linkedNotebook.getGuid());
    //        System.out.println(linkedNotebook.getShareName());
    //        System.out.println(linkedNotebook.getUsername());
    //      }
    //
    //      System.out.println();

    for(Notebook notebook : SSServCaller.getEvernoteNotebooks(evernoteInfo.noteStore)){
      
      System.out.println("notebook");
      System.out.println(notebook.getName());
      System.out.println(notebook.getUpdateSequenceNum());
      System.out.println(notebook.isDefaultNotebook());
      System.out.println(notebook.getServiceCreated());
      System.out.println(notebook.getServiceUpdated());
      System.out.println(notebook.getPublishing());
      System.out.println(notebook.isPublished());
      System.out.println(notebook.getSharedNotebooks());
      System.out.println(notebook.getContact());
      System.out.println();
      
      for(Note note : SSServCaller.getEvernoteNotes(evernoteInfo.noteStore, notebook.getGuid())){
        
        System.out.println("note");
        System.out.println(note.getTitle());
        System.out.println(note.getCreated());
        System.out.println(note.getUpdated());
        System.out.println(note.getDeleted());
        System.out.println(note.isActive());
        System.out.println(note.getUpdateSequenceNum());
        System.out.println(note.getResources());
        System.out.println(note.getAttributes());
        System.out.println(note.getTagNames());
        System.out.println();
        
        if(note.getTagNames() != null){
          
          System.out.println("tags");
          
          for(String tagName : note.getTagNames()){
            System.out.println(tagName);
          }
          
          System.out.println();
        }
        
        noteAttr = note.getAttributes();
        
        if(noteAttr != null){
          System.out.println("note attributes");
          System.out.println(noteAttr.getSubjectDate());
          System.out.println(noteAttr.getLatitude());
          System.out.println(noteAttr.getLongitude());
          System.out.println(noteAttr.getAltitude());
          System.out.println(noteAttr.getAuthor());
          System.out.println(noteAttr.getSource());
          System.out.println(noteAttr.getSourceURL());
          System.out.println(noteAttr.getSourceApplication());
          System.out.println(noteAttr.getShareDate());
          System.out.println(noteAttr.getReminderDoneTime());
          System.out.println(noteAttr.getReminderTime());
          System.out.println(noteAttr.getPlaceName());
          System.out.println(noteAttr.getReminderTime());
          System.out.println(noteAttr.getContentClass());
          System.out.println(noteAttr.getLastEditedBy());
          System.out.println(noteAttr.getClassifications());
          System.out.println(noteAttr.getApplicationData());
          System.out.println(noteAttr.getCreatorId());
          System.out.println(noteAttr.getLastEditorId());
          System.out.println();
        }
        
        if(note.getResources() != null){
          
          for(Resource resource : note.getResources()){
            
            System.out.println("resource");
            System.out.println(resource.getGuid());
            System.out.println(resource.getMime());
            System.out.println(resource.getWidth());
            System.out.println(resource.getHeight());
            System.out.println(resource.getDuration());
            System.out.println(resource.isActive());
            System.out.println(resource.getAttributes());
            System.out.println(resource.getUpdateSequenceNum());
            System.out.println();
            
            resourceAttr = resource.getAttributes();
            
            System.out.println("resource attributes");
            System.out.println(resourceAttr.getSourceURL());
            System.out.println(resourceAttr.getTimestamp());
            System.out.println(resourceAttr.getLatitude());
            System.out.println(resourceAttr.getLongitude());
            System.out.println(resourceAttr.getAltitude());
            System.out.println(resourceAttr.getFileName());
            System.out.println();
          }
        }
      }
    }
    
    SSLogU.info(op + " Test end");
  }
  
  @Override
  protected void testFromClient() throws Exception{
  }
  
  @Override
  protected void setUp() throws Exception {
    userUri = SSServCaller.userLogin(SSLabel.get("dt"), true);
  }
}


//		String developerToken = "S=s1:U=82ad3:E=148da4934b7:C=141829808ba:P=1cd:A=en-devtoken:V=2:H=93ad77330074d1787c405f4db7a8055c";
//		// Set up the NoteStore client
//		EvernoteAuth evernoteAuth = new EvernoteAuth(EvernoteService.SANDBOX, developerToken);
//		ClientFactory factory = new ClientFactory(evernoteAuth);
//		NoteStoreClient noteStore = factory.createNoteStoreClient();
//
//		// Make API calls, passing the developer token as the authenticationToken param
//		List<Notebook> notebooks = noteStore.listNotebooks();
//		for (Notebook n : notebooks)
//			System.out.println(n);
//
//		System.out.println(noteStore.listTags());