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
package at.kc.tugraz.ss.serv.job.dataexport.datatypes.par;

import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServErrReg;
import java.util.ArrayList;
import java.util.List;

public class SSDataExportAddTagsCategoriesTimestampsForUserEntityPar extends SSServPar{
  
  public String       fileName   = null;
  public SSUri        forUser    = null;
  public SSUri        entity     = null;
  public List<String> tags       = new ArrayList<>();
  public List<String> categories = new ArrayList<>();
  
  public SSDataExportAddTagsCategoriesTimestampsForUserEntityPar(final SSServPar par) throws Exception{
    
    super(par);
    
    try{
      
      if(pars != null){
        fileName              = (String)                   pars.get(SSVarNames.fileName);
        forUser               = (SSUri)                    pars.get(SSVarNames.forUser);
        entity                = (SSUri)                    pars.get(SSVarNames.entity);
        tags                  = (List<String>)             pars.get(SSVarNames.tags);
        categories            = (List<String>)             pars.get(SSVarNames.categories);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
