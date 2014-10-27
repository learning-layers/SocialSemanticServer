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
package at.kc.tugraz.ss.test.search;

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntity;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.serv.search.conf.SSSearchConf;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.serv.test.api.SSServOpTestCaseA;
import at.kc.tugraz.ss.serv.voc.serv.SSVoc;
import at.kc.tugraz.ss.service.search.datatypes.ret.SSSearchRet;
import java.util.ArrayList;
import java.util.List;

public class SSSearchTest extends SSServOpTestCaseA{
  
  public SSSearchTest(final SSSearchConf searchConf){
    super(searchConf, SSMethU.search);
  }
  
  @Override
  protected void test() throws Exception {
    
    System.out.println (op + " test start");
    
    final List<String>    keywordsToSearchFor   = new ArrayList<>();
    final List<SSEntityE> typesToSearchOnlyFor  = new ArrayList<>();
    
    keywordsToSearchFor.add("test");
    
    typesToSearchOnlyFor.add(SSEntityE.entity);
    typesToSearchOnlyFor.add(SSEntityE.file);
    typesToSearchOnlyFor.add(SSEntityE.evernoteResource);
    typesToSearchOnlyFor.add(SSEntityE.evernoteNote);
    typesToSearchOnlyFor.add(SSEntityE.evernoteNotebook);
    typesToSearchOnlyFor.add(SSEntityE.coll);
    
    keywordsToSearchFor.add("test");
    
    SSSearchRet result =
      SSServCaller.search(
        SSVoc.systemUserUri,
        keywordsToSearchFor,
        false,
        new ArrayList<>(),
        true,
        new ArrayList<>(),
        false,
        new ArrayList<>(),
        true,
        new ArrayList<>(),
        false,
        new ArrayList<>(),
        typesToSearchOnlyFor,
        false,
        new ArrayList<>(),
        false,
        false,
        false,
        null,
        null);
    
    if(result.pageNumbers > 1){
      
      result =
        SSServCaller.search(
          SSVoc.systemUserUri,
          keywordsToSearchFor,
          false,
          new ArrayList<>(),
          true,
          new ArrayList<>(),
          false,
          new ArrayList<>(),
          true,
          new ArrayList<>(),
          false,
          new ArrayList<>(),
          typesToSearchOnlyFor,
          false,
          new ArrayList<>(),
          false,
          false,
          false,
          result.pagesID,
          result.pageNumber + 1);
    }
    
    System.out.println (op + " test end");
  }
  
  @Override
  protected void testFromClient() throws Exception{
    
  }
  
  @Override
  protected void setUp() throws Exception {
  }
}