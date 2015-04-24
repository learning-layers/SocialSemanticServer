/**
 * Code contributed to the Learning Layers project
 * http://www.learning-layers.eu
 * Development is partly funded by the FP7 Programme of the European Commission under
 * Grant Agreement FP7-ICT-318209.
 * Copyright (coffee) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
package at.kc.tugraz.ss.activity.datatypes.par;

import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarNames;
import at.kc.tugraz.ss.activity.datatypes.enums.SSActivityE;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSServPar;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.JsonNode;
import at.tugraz.sss.serv.SSServErrReg;

@XmlRootElement
@ApiModel(value = "activitiesUserGet request parameter")
public class SSActivitiesUserGetPar extends SSServPar{
  
  @ApiModelProperty(
    required = false,
    value = "types of activities to be queried (optional)")
  public List<SSActivityE>      types            = new ArrayList<>();
  
  @XmlElement
  public void setTypes(final List<String> types) throws Exception{
    this.types = SSActivityE.get(types);
  }
  
  @ApiModelProperty(
    required = false,
    value = "users which have been involved in activities (optional)")
  public List<SSUri>            users            = new ArrayList<>();
  
  @XmlElement
  public void setUsers(final List<String> users) throws Exception{
    this.users = SSUri.get(users);
  }
  
  @ApiModelProperty(
    required = false,
    value = "entities which have been involved in activities as targets (e.g. the target for a discussion) (optional)" )
  public List<SSUri>            entities         = new ArrayList<>();
  
  @XmlElement
  public void setEntities(final List<String> entities) throws Exception{
    this.entities = SSUri.get(entities);
  }
  
  @ApiModelProperty(
    required = false,
    value = "groups for which activities shall be retrieved (optional)" )
  public List<SSUri>            circles          = new ArrayList<>();
  
  @XmlElement
  public void setCircles(final List<String> circles) throws Exception{
    this.circles = SSUri.get(circles);
  }
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "time frame start (optional)" )
  public Long                   startTime        = null;
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "time frame end (optional)" )
  public Long                   endTime          = null;
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "whether only the last activities shall be returned (based on the combination of user, entity and activity type)")
  public Boolean includeOnlyLastActivities = null;
  
  public SSActivitiesUserGetPar(){}
  
  public SSActivitiesUserGetPar(final SSServPar par) throws Exception{
    
    super(par);
    
    try{
      
      if(pars != null){
        
        types                      = (List<SSActivityE>)   pars.get(SSVarNames.types);
        users                      = (List<SSUri>)         pars.get(SSVarNames.users);
        entities                   = (List<SSUri>)         pars.get(SSVarNames.entities);
        circles                    = (List<SSUri>)         pars.get(SSVarNames.circles);
        startTime                   = (Long)               pars.get(SSVarNames.startTime);
        endTime                     = (Long)               pars.get(SSVarNames.endTime);
        includeOnlyLastActivities   = (Boolean)            pars.get(SSVarNames.includeOnlyLastActivities);
      }
      
      if(par.clientJSONObj != null){
        
        try{
          for (final JsonNode objNode : par.clientJSONObj.get(SSVarNames.types)) {
            types.add(SSActivityE.get(objNode.getTextValue()));
          }
        }catch(Exception error){}
        
        try{
          for (final JsonNode objNode : par.clientJSONObj.get(SSVarNames.users)) {
            users.add(SSUri.get(objNode.getTextValue()));
          }
        }catch(Exception error){}
        
        try{
          for (final JsonNode objNode : par.clientJSONObj.get(SSVarNames.entities)) {
            entities.add(SSUri.get(objNode.getTextValue()));
          }
        }catch(Exception error){}
        
        try{
          for (final JsonNode objNode : par.clientJSONObj.get(SSVarNames.circles)) {
            circles.add(SSUri.get(objNode.getTextValue()));
          }
        }catch(Exception error){}
        
        try{
          startTime  = par.clientJSONObj.get(SSVarNames.startTime).getLongValue();
        }catch(Exception error){}
        
        try{
          endTime    = par.clientJSONObj.get(SSVarNames.endTime).getLongValue();
        }catch(Exception error){}
        
        try{
          includeOnlyLastActivities    = par.clientJSONObj.get(SSVarNames.includeOnlyLastActivities).getBooleanValue();
        }catch(Exception error){}
      }
      
//       if(clientPars != null){
//
//         try{ types      = SSActivityE.get (SSStrU.splitDistinctWithoutEmptyAndNull(clientPars.get(SSVarU.types),    SSStrU.comma));    }catch(Exception error){}
//         try{ users      = SSUri.get       (SSStrU.splitDistinctWithoutEmptyAndNull(clientPars.get(SSVarU.users),    SSStrU.comma));    }catch(Exception error){}
//         try{ entities   = SSUri.get       (SSStrU.splitDistinctWithoutEmptyAndNull(clientPars.get(SSVarU.entities), SSStrU.comma));    }catch(Exception error){}
//         try{ circles    = SSUri.get       (SSStrU.splitDistinctWithoutEmptyAndNull(clientPars.get(SSVarU.circles), SSStrU.comma));     }catch(Exception error){}
//         try{ startTime  = Long.valueOf    (clientPars.get(SSVarU.startTime));                                                          }catch(Exception error){}
//         try{ endTime    = Long.valueOf    (clientPars.get(SSVarU.endTime));                                                            }catch(Exception error){}
//       }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  /* json getters */
  public List<String> getCircles() throws Exception{
    return SSStrU.removeTrailingSlash(circles);
  }
  
  public List<String> getUsers() throws Exception{
    return SSStrU.removeTrailingSlash(users);
  }
  
  public List<String> getEntities() throws Exception{
    return SSStrU.removeTrailingSlash(entities);
  }
  
  public List<String> getTypes() throws Exception{
    return SSStrU.toStr(types);
  }
}