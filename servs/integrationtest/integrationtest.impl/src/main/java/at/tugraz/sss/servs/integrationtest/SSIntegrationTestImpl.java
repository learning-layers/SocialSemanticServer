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
package at.tugraz.sss.servs.integrationtest;

import at.tugraz.sss.serv.datatype.enums.SSSearchOpE;
import at.tugraz.sss.serv.datatype.par.SSDBNoSQLAddDocPar;
import at.tugraz.sss.serv.db.api.SSDBNoSQLI;
import at.tugraz.sss.serv.datatype.par.SSDBNoSQLRemoveDocPar;
import at.tugraz.sss.serv.datatype.par.SSDBNoSQLSearchPar;
import at.tugraz.sss.serv.db.api.SSDBSQLI;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.impl.api.SSServImplWithDBA;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.datatype.SSSolrKeywordLabel;
import at.tugraz.sss.serv.datatype.enums.SSSolrSearchFieldE;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSIntegrationTestImpl
extends
  SSServImplWithDBA
implements
  SSIntegrationTestClientI,
  SSIntegrationTestServerI{
  
  public SSIntegrationTestImpl(final SSIntegrationTestConf conf) throws SSErr {
    super(conf, (SSDBSQLI) SSServReg.getServ(SSDBSQLI.class), (SSDBNoSQLI) SSServReg.getServ(SSDBNoSQLI.class));
  }
  
  @Override
  public Boolean integrationTestSolrForSearch() throws Exception {
    
    try{
      
      SSLogU.info("start intgration test solr for search");
      
      dbNoSQL.addDoc(new SSDBNoSQLAddDocPar("muell.pdf"));
      
      final Map<SSSolrSearchFieldE, List<SSSolrKeywordLabel>> wheres   = new HashMap<>();
      final List<String>                                      keywords = new ArrayList<>();
      
      keywords.add("Halbjahr 1");
      keywords.add("Muell");
      
      wheres.put(SSSolrSearchFieldE.docText, SSSolrKeywordLabel.get(keywords));
        
      final List<String> result =
        dbNoSQL.search(
          new SSDBNoSQLSearchPar(
            SSSearchOpE.and.toString(),
            SSSearchOpE.or.toString(),
            wheres,
            100));
      
      System.out.println(result);
      
      dbNoSQL.removeDoc(new SSDBNoSQLRemoveDocPar("muell.pdf"));
      
      SSLogU.info("end intgration test solr for search");
      
      return true;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
    }
  }
}