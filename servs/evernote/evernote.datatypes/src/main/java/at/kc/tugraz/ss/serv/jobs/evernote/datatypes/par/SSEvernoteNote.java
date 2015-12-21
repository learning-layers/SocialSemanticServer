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
package at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par;

import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.*;
import java.util.Map;

public class SSEvernoteNote extends SSEntity{
  
  public SSUri notebook = null;
  
  public static SSEvernoteNote get(
    final SSEvernoteNote note,
    final SSEntity       entity) throws Exception{
    
    return new SSEvernoteNote(note, entity);
  }
  
  public static SSEvernoteNote get(
    final SSUri id,
    final SSUri notebook) throws Exception{
    
    return new SSEvernoteNote(id, notebook);
  }
  
  protected SSEvernoteNote(
    final SSEvernoteNote note,
    final SSEntity       entity) throws Exception{

    super(note, entity);
    
    if(note.notebook != null){
      this.notebook     = note.notebook;
    }else{
      
      if(entity instanceof SSEvernoteNote){
        this.notebook = ((SSEvernoteNote) entity).notebook;
      }
    }
  }
  
  protected SSEvernoteNote(
    final SSUri id,
    final SSUri notebook) throws Exception{

    super(id, SSEntityE.evernoteNote);
    
    this.notebook = notebook;
  }

  public String getNotebook(){
    return SSStrU.removeTrailingSlash(notebook);
  }
}