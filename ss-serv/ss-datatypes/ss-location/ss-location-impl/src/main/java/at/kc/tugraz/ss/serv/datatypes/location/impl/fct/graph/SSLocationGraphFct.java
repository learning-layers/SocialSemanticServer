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
package at.kc.tugraz.ss.serv.datatypes.location.impl.fct.graph;

import at.kc.tugraz.ss.serv.db.api.SSDBGraphFct;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;

public class SSLocationGraphFct extends SSDBGraphFct{
  
  public SSLocationGraphFct(final SSServImplWithDBA serv) throws Exception{
    super(serv.dbGraph);
  }
  
//  public String getLocation(SSUri resource) throws Exception{
//    
//    for (SSStatement statem : serv.dbSQL.get(resource, predHasLocation(), null, namedGraphUri)) {
//      return SSStrU.removeDoubleQuotes(statem.object.toString());
//		}
//		
//		return SSStrU.empty;
//  }
//
//  public void setLocation(SSUri resource, String location) throws Exception{
//    
////    for (SSStatement state : serv.dbSQL.get(resource, predHasLabel(), null, namedGraphUri)) {
////      serv.dbSQL.remove(state, namedGraphUri);
////    }
//    
//    serv.dbSQL.remove (resource, predHasLocation(), null,                    namedGraphUri);
//    serv.dbSQL.add    (resource, predHasLocation(), SSLiteral.get(location), namedGraphUri);
//  }
}
