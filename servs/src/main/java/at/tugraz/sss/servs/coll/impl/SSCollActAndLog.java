 /**
  * Code contributed to the Learning Layers project
  * http://www.learning-layers.eu
  * Development is partly funded by the FP7 Programme of the European Commission under
  * Grant Agreement FP7-ICT-318209.
  * Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
  
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

package at.tugraz.sss.servs.coll.impl;

import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.activity.api.SSActivityServerI;
import at.tugraz.sss.servs.util.SSLogU;
import at.tugraz.sss.servs.activity.datatype.SSActivityE;
import at.tugraz.sss.servs.activity.datatype.SSActivityAddPar;
import at.tugraz.sss.servs.entity.datatype.SSTextComment;
import at.tugraz.sss.servs.coll.datatype.SSCollUserEntriesAddPar;
import at.tugraz.sss.servs.coll.datatype.SSCollUserEntriesDeletePar;
import at.tugraz.sss.servs.coll.datatype.SSCollUserEntryAddPar;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.common.impl.SSServErrReg;
import at.tugraz.sss.servs.activity.impl.*;

public class SSCollActAndLog{
  
  public void removeCollEntries(
    final SSCollUserEntriesDeletePar par) throws SSErr{
    
    try{
      
      final SSActivityServerI activityServ = new SSActivityImpl();
      
      activityServ.activityAdd(
        new SSActivityAddPar(
          par,
          par.user,
          SSActivityE.removeCollEntry,
          par.coll,
          SSUri.asListNotNull(),
          par.entries,
          SSTextComment.asListWithoutNullAndEmpty(),
          null,
          par.shouldCommit));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addCollEntry(
    final SSCollUserEntryAddPar par) throws SSErr{
    
    try{
      
      final SSActivityServerI activityServ = new SSActivityImpl();
      
      activityServ.activityAdd(
        new SSActivityAddPar(
          par,
          par.user,
          SSActivityE.addCollEntry,
          par.coll,
          SSUri.asListNotNull(),
          SSUri.asListNotNull(par.entry),
          SSTextComment.asListWithoutNullAndEmpty(),
          null,
          par.shouldCommit));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addCollEntries(
    final SSCollUserEntriesAddPar par) throws SSErr{
    
    try{
      
      final SSActivityServerI activityServ = new SSActivityImpl();
      
      activityServ.activityAdd(
        new SSActivityAddPar(
          par,
          par.user,
          SSActivityE.addCollEntry,
          par.coll,
          SSUri.asListNotNull(),
          par.entries,
          SSTextComment.asListWithoutNullAndEmpty(),
          null,
          par.shouldCommit));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }  
}
