/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
package at.kc.tugraz.ss.service.search.impl;

import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.tugraz.sss.serv.SSDBNoSQLFct;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBNoSQLSearchPar;
import at.tugraz.sss.serv.SSSearchOpE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSSolrKeywordLabel;
import at.tugraz.sss.serv.SSSolrSearchFieldE;
import at.tugraz.sss.serv.SSUri;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSSearchNoSQLFct extends SSDBNoSQLFct{

  public SSSearchNoSQLFct(final SSDBNoSQLI dbNoSQL) throws Exception{
    super(dbNoSQL);
  }
  
  public List<SSUri> search(
    final SSSearchOpE  globalSearchOp,
    final SSSearchOpE  localSearchOp,
    final List<String> keywords) throws Exception {
    
    try{
      final Map<SSSolrSearchFieldE, List<SSSolrKeywordLabel>> wheres = new HashMap<>();
      
      wheres.put(SSSolrSearchFieldE.docText, SSSolrKeywordLabel.get(keywords));
      
      return SSUri.get(
        dbNoSQL.search(
          new SSDBNoSQLSearchPar(
            globalSearchOp.toString(),
            localSearchOp.toString(),
            wheres,
            100)),
        SSVocConf.sssUri);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}