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
package at.kc.tugraz.ss.test.serv.coll;

import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSUri;
import at.kc.tugraz.ss.serv.coll.conf.SSCollConf;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.serv.SSServOpTestCaseA;
import at.kc.tugraz.ss.serv.voc.serv.SSVoc;
import at.kc.tugraz.ss.service.coll.datatypes.SSColl;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntriesAddPar;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserEntriesAddRet;

import java.util.ArrayList;
import java.util.List;

public class SSCollUserEntriesAddTest extends SSServOpTestCaseA{
  
  public SSCollUserEntriesAddTest(SSCollConf collConf) throws Exception {
    super(collConf, null, SSServOpE.collUserEntriesAdd);
  }
  
  @Override
  protected void test() throws Exception {
    
    final List<SSUri>       entries      = new ArrayList<>(); 
    final List<SSLabel>     entryLabels  = new ArrayList<>(); 
    final SSColl            rootColl     = SSServCaller.collUserRootGet      (SSVoc.systemUserUri);
    final SSColl            rootCollAfterAddingEntries;
    
    entries.add(SSUri.get("http://test.uri/hugo.html"));
    entries.add(SSUri.get("http://test.uri/hugo.pdf"));
    entries.add(SSUri.get("http://test.uri/hugo.csv"));
    
    entryLabels.add(SSLabel.get("first entry"));
    entryLabels.add(SSLabel.get("second entry"));
    entryLabels.add(SSLabel.get("third entry"));
        
    SSServCaller.collUserEntriesAdd(SSVoc.systemUserUri, rootColl.id, entries, entryLabels, false, true);
    
    rootCollAfterAddingEntries = SSServCaller.collUserRootGet      (SSVoc.systemUserUri);
    
    System.out.println (op + " test end");
  }
  
  @Override
  protected void testFromClient() throws Exception{
    
//    jsonRequ            = 
//      "{\"op\":\"collUserEntriesAdd\",\"user\":\"http://eval.bp/user/domi\",\"coll\":\"http://eval.bp/coll/156429620336965611\",\"entries\":\"http://eval.bp/file/15643192983658651.pdf,http://eval.bp/file/15643192983936052.pdf,http://eval.bp/file/15643234366971803.pdf,http://eval.bp/file/15643238295416004.pdf,http://eval.bp/file/15643262129184815.pdf,http://eval.bp/file/15643269770083966.pdf,http://eval.bp/file/15643282063142977.pdf,http://eval.bp/file/15643289814828958.pdf,http://eval.bp/file/15643296164379459.pdf\",\"entryLabels\":\"Aufgabenblatt-08.pdf,Aufgabenblatt-05.pdf,Aufgabenblatt-01.pdf,Aufgabenblatt-04.pdf,Aufgaben-Regeln.pdf,Aufgabenblatt-07.pdf,Aufgabenblatt-06.pdf,Aufgabenblatt-02.pdf,Aufgabenblatt-03.pdf\",\"circleUris\":\"null,null,null,null,null,null,null,null,null\",\"key\":\"681V454J1P3H4W3B367BB79615U184N22356I3E\"}";    
//    clientServPar                                   = new SSServPar          (jsonRequ);
//    final SSCollUserEntriesAddPar  serverServPar    = new SSCollUserEntriesAddPar (clientServPar);
//    final Boolean                  serverServResult =
//      SSServCaller.collUserEntriesAdd(
//        serverServPar.user,
//        serverServPar.coll,
//        serverServPar.entries,
//        serverServPar.labels,
//        true,
//        true);
//    
//    createJSONClientRetStr(SSCollUserEntriesAddRet.get(serverServResult, op));
//      
//    System.out.println (op + " test from client end");
  }
  
  @Override
  protected void setUp() throws Exception {
  }
}