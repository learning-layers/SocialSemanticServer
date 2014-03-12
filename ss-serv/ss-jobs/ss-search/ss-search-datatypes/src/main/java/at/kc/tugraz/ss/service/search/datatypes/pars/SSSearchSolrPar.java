/**
 * Copyright 2013 Graz University of Technology - KTI (Knowledge Technologies Institute)
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
 package at.kc.tugraz.ss.service.search.datatypes.pars;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.solr.datatypes.SSSolrKeywordLabel;
import java.util.List;

public class SSSearchSolrPar  extends SSServPar{
  
  public List<SSSolrKeywordLabel>  keywords   = null;
  public String                    searchOp   = null;
    
  public SSSearchSolrPar(SSServPar par) throws Exception{
      
    super(par);
    
    try{
      if(pars != null){
        searchOp   = (String)                   pars.get(SSVarU.searchOp);
        keywords   = (List<SSSolrKeywordLabel>) pars.get(SSVarU.keywords);
      }
      
      if(clientPars != null){
        searchOp   = (String) clientPars.get(SSVarU.searchOp);
        keywords   = SSSolrKeywordLabel.getDistinct(SSStrU.split((String)clientPars.get(SSVarU.keywords), SSStrU.comma));
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}