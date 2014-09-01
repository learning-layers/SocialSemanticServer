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
package at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.SSTextComment;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@ApiModel(value = "entityUserCopy request parameter")
public class SSEntityUserCopyPar extends SSServPar{
  
  @XmlElement
  @ApiModelProperty(
    required = true,
    value = "entity to copy")
  public SSUri         entity             = null;
  
  @XmlElement
  @ApiModelProperty(
    required = true,
    value = "users to copy the entity for")
  public List<SSUri>   users              = null;
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "if set contains sub entities to exclude from copying")
  public List<SSUri>   entitiesToExclude  = new ArrayList<>();
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "optional describing text")
  public SSTextComment comment            = null;
  
  public SSEntityUserCopyPar(){}
    
  public SSEntityUserCopyPar(final SSServPar par) throws Exception{
    
    super(par);
    
    try{
      
      if(pars != null){
        entity            = (SSUri)         pars.get(SSVarU.entity);
        users             = (List<SSUri>)   pars.get(SSVarU.users);
        entitiesToExclude = (List<SSUri>)   pars.get(SSVarU.entitiesToExclude);
        comment           = (SSTextComment) pars.get(SSVarU.comment);
      }
      
      if(clientPars != null){
        entity       = SSUri.get (clientPars.get(SSVarU.entity));
        users        = SSUri.get (SSStrU.splitDistinctWithoutEmptyAndNull(clientPars.get(SSVarU.users), SSStrU.comma));
        
        try{
          entitiesToExclude = SSUri.get(SSStrU.splitDistinctWithoutEmptyAndNull(clientPars.get(SSVarU.entitiesToExclude), SSStrU.comma));
        }catch(Exception error){}
        
        try{
          comment = SSTextComment.get(clientPars.get(SSVarU.comment));
        }catch(Exception error){}
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  /* json getters */
  public String getEntity(){
    return SSStrU.removeTrailingSlash(entity);
  }
  
  public List<String> getUsers() throws Exception{
    return SSStrU.removeTrailingSlash(users);
  }
  
  public List<String> getEntitiesToExclude() throws Exception{
    return SSStrU.removeTrailingSlash(entitiesToExclude);
  }
  
  public String getComment(){
    return SSStrU.toStr(comment);
  }
}