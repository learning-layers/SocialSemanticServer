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
package at.kc.tugraz.ss.serv.scaff.datatypes.par;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import java.util.ArrayList;
import java.util.List;

public class SSScaffRecommTagsBasedOnUserEntityTagCategoryPar extends SSServPar{
  
  public SSUri            forUser       = null;
  public SSUri            entity        = null;
  public List<String>     categories    = new ArrayList<String>();
  public Integer          maxTags       = 10;
  
  public SSScaffRecommTagsBasedOnUserEntityTagCategoryPar(SSServPar par) throws Exception{
    
    super(par);
    
    try{
      if(pars != null){
        forUser     = (SSUri)         pars.get(SSVarU.forUser);
        entity      = (SSUri)         pars.get(SSVarU.entity);
        categories  = (List<String>)  pars.get(SSVarU.categories);
        maxTags     = (Integer)       pars.get(SSVarU.maxTags);
      }
      
      if(clientPars != null){
        
        try{
          forUser = SSUri.get          (clientPars.get(SSVarU.forUser));
        }catch(Exception error){}
        
        try{
          entity = SSUri.get         (clientPars.get(SSVarU.entity));
        }catch(Exception error){}
        
        try{
          categories = SSStrU.splitDistinctWithoutEmptyAndNull(clientPars.get(SSVarU.categories), SSStrU.comma);
        }catch(Exception error){}
        
        try{
          maxTags  = Integer.valueOf   (clientPars.get(SSVarU.maxTags));
        }catch(Exception error){}
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
