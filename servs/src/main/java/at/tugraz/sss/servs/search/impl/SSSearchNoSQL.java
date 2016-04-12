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
package at.tugraz.sss.servs.search.impl;

import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.conf.SSConf;
import at.tugraz.sss.servs.db.api.SSDBNoSQLFctA;
import at.tugraz.sss.servs.db.api.SSDBNoSQLI;
import at.tugraz.sss.servs.db.datatype.SSDBNoSQLSearchPar;
import at.tugraz.sss.servs.search.datatype.SSSearchOpE;
import at.tugraz.sss.servs.common.impl.SSServErrReg;
import at.tugraz.sss.servs.db.datatype.SSSolrKeywordLabel;
import at.tugraz.sss.servs.db.datatype.SSSolrSearchFieldE;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSSearchNoSQL extends SSDBNoSQLFctA{

  public SSSearchNoSQL(final SSDBNoSQLI dbNoSQL){
    super(dbNoSQL);
  }
  
  public List<SSUri> search(
    final SSSearchOpE  globalSearchOp,
    final SSSearchOpE  localSearchOp,
    final List<String> keywords) throws SSErr {
    
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
        SSConf.sssUri);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}
