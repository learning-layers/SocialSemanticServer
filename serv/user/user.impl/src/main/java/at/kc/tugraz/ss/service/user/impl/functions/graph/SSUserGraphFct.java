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
package at.kc.tugraz.ss.service.user.impl.functions.graph;

import at.kc.tugraz.ss.serv.db.api.SSDBGraphFct;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;

public class SSUserGraphFct extends SSDBGraphFct{

  public SSUserGraphFct(final SSServImplWithDBA serv) throws Exception{
    super(serv.dbGraph);
  }
  
//  public List<SSUri> userAll() throws Exception {
//    
//    List<SSUri> userlist  = new ArrayList<>();
//
//    for(SSStatement statem : db.get(null, predType(), objUser(), namedGraphUri)) {
//      userlist.add(statem.subject);
//    }
//    
//    return userlist;
//  }
//
//  public boolean userLogin(SSUri user) throws Exception{
//    
//    if(
//      db.add(user, predType(), objUser(), namedGraphUri) &&
//      db.add(user, predType(), objUser(), user)){
//      return true;
//    }
//    
//    return false;
//  }
//
//  public SSUri userSystem() throws Exception{
//    return SSUri.get(objUser() + SSStrU.valueSystem);
//  }
//  
//  public SSUri createUserUri(SSLabelStr userLabel) throws Exception{
//    return SSUri.get(objUser().toString() + userLabel);
//  }
}


//public String[] getUserHistory(String user, int count) {
//    List<String> outList = new ArrayList<>();
//    try {
//
//      String from = new SSUri(Vocabulary.getInstance().getGraphUri() + VocNamespace.EVENTGRAPH); //SemanticVocabulary.MATURENS + "_eventgraph");
//      String queryString = "PREFIX matns: <http://tug.mature-ip.eu/> SELECT DISTINCT ?o ?t FROM <"
//              + from
//              + "> WHERE{ "
//              + "?s matns:hasEvent ?o."
//              + "?o <"
//              //              + SemanticVocabulary.belongsTo
//              + ResourceManager.getBelongsToUri()
//              + "> <"
//              + new SSUri(user)
//              + ">. "
//              + "?o matns:timestamp ?t.}ORDER BY DESC (?t) LIMIT "
//              + count;
//
//      SSQueryResult result = serv.prepareSparqlQuery(queryString);
//
//      for (SSQueryResultItem item : result) {
//        String binding = item.getBinding("o");
//        String binding2 = item.getBinding("t");
//        outList.add(binding);
//      }
//
//    } catch (Exception e) {
//      log.error(e.getMessage(), e);
//    }
//    String[] outString = new String[outList.size()];
//    for (int i = 0; i < outList.size(); i++) {
//      outString[i] = outList.get(i);
//    }
//    return outString;
//  }