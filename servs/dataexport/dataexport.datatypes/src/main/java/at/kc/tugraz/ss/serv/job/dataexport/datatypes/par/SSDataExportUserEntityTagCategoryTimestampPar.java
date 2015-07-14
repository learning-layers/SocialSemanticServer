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
package at.kc.tugraz.ss.serv.job.dataexport.datatypes.par;

import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSUri;
import java.util.ArrayList;
import java.util.List;

public class SSDataExportUserEntityTagCategoryTimestampPar extends SSServPar{
  
  public String                      fileName               = null;
  public Boolean                     exportTags             = false;
  public Boolean                     usePrivateTagsToo      = false;
  public Boolean                     exportCategories       = false;
  public List<SSUri>                 users                  = new ArrayList<>();
  
  public SSDataExportUserEntityTagCategoryTimestampPar(final SSServPar par) throws Exception{
    
    super(par);
    
    try{
      
      if(pars != null){
        fileName              = (String)                     pars.get(SSVarNames.fileName);
        exportTags            = (Boolean)                    pars.get(SSVarNames.exportTags);
        usePrivateTagsToo     = (Boolean)                    pars.get(SSVarNames.usePrivateTagsToo);
        exportCategories      = (Boolean)                    pars.get(SSVarNames.exportCategories);
        users                 = (List<SSUri>)                pars.get(SSVarNames.users);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
