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
package at.kc.tugraz.ss.service.tag.datatypes.pars;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSSpaceE;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagLabel;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@ApiModel(value = "tagUserEntitiesForTagsGet request parameter")
public class SSTagUserEntitiesForTagsGetPar extends SSServPar{

  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "user to retrieve entities via tags for (optional)")
  public SSUri             forUser   = null;
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "tag labels to consider for retrieving entities via tags (optional)")
  public List<SSTagLabel>  labels    = new ArrayList<>();
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "access restriction for tags to be considered (i.e. privateSpace, sharedSpace) (optional)")
  public SSSpaceE          space     = null;
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "timestamp to retrieve tags (optional)")
  public Long              startTime = null;
  
  public SSTagUserEntitiesForTagsGetPar(){}
  
  public SSTagUserEntitiesForTagsGetPar(SSServPar par) throws Exception{
    
    super(par);
    
    try{
     
      if(pars != null){
        forUser     = (SSUri)                       pars.get(SSVarU.forUser);
        labels      = SSTagLabel.get((List<String>) pars.get(SSVarU.labels));
        space       = (SSSpaceE)                    pars.get(SSVarU.space);
        startTime   = (Long)                        pars.get(SSVarU.startTime);
      }
      
      if(clientPars != null){
        
        try{
          labels = SSTagLabel.get (SSStrU.splitDistinctWithoutEmptyAndNull(clientPars.get(SSVarU.labels), SSStrU.comma));
        }catch(Exception error){}
        
        try{
          forUser      = SSUri.get(clientPars.get(SSVarU.forUser));
        }catch(Exception error){}
        
        try{
          space      = SSSpaceE.get  (clientPars.get(SSVarU.space));
        }catch(Exception error){}
        
        try{
          startTime      = Long.valueOf(clientPars.get(SSVarU.startTime));
        }catch(Exception error){}
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  /* json getters */
  public String getForUser(){
    return SSStrU.removeTrailingSlash(forUser);
  }

  public List<String> getLabels() throws Exception{
    return SSStrU.toStr(labels);
  }

  public String getSpace(){
    return SSStrU.toStr(space);
  }
}
