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
 package at.kc.tugraz.ss.service.search.datatypes.pars;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.service.search.datatypes.SSSearchOpE;
import java.util.ArrayList;
import java.util.List;

public class SSSearchSolrPar extends SSServPar{
  
  public List<String>  keywords   = new ArrayList<>();
  public SSSearchOpE   searchOp   = null;
    
  public SSSearchSolrPar(SSServPar par) throws Exception{
      
    super(par);
    
    try{
      if(pars != null){
        searchOp   = (SSSearchOpE)  pars.get(SSVarU.searchOp);
        keywords   = (List<String>) pars.get(SSVarU.keywords);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static SSSearchSolrPar get(
    final SSUri        user,
    final List<String> keywords,
    final SSSearchOpE  searchOp) throws Exception{
    
    return new SSSearchSolrPar(user, keywords, searchOp);
  }
  
  private SSSearchSolrPar(
    final SSUri        user,
    final List<String> keywords,
    final SSSearchOpE  searchOp) throws Exception{
    
    super();
    
    this.user     = user;
    this.searchOp = searchOp;
    
    this.keywords.addAll(SSStrU.distinctWithoutEmptyAndNull(keywords));
  }
}