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
package at.tugraz.sss.adapter.rest.v2.recomm;

import at.kc.tugraz.ss.conf.conf.SSVocConf;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@ApiModel(value = "recommResources request parameter")
public class SSRecommResourcesRESTAPIV2Par{
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "realm the user wants to query")
  public String         realm    = null;
  
  @ApiModelProperty( 
    required = false, 
    value = "user to be considered to retrieve recommendations for")
  public SSUri         forUser    = null;
  
  @XmlElement
  public void setForUser(final String forUser) throws Exception{
    this.forUser = SSUri.get(forUser, SSVocConf.sssUri);
  }
  
  @ApiModelProperty( 
    required = false, 
    value = "resource to be considered to retrieve recommendations for")
  public SSUri         entity     = null;
  
  @XmlElement
  public void setEntity(final String entity) throws Exception{
    this.entity = SSUri.get(entity, SSVocConf.sssUri);
  }
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "additional information to be taken into account")
  public List<String>  categories = null;
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "number of resources to be returned")
  public Integer       maxResources    = 10;
    
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "whether circle types (i.e. priv, group, pub) for recommended entities shall be set")
  public Boolean       setCircleTypes    = null;
  
  @ApiModelProperty( 
    required = false, 
    value = "entity types to be recommended")
  public List<SSEntityE> typesToRecommOnly = null;

  @XmlElement
  public void setTypesToRecommOnly(final List<String> typesToRecommOnly) throws Exception{
    this.typesToRecommOnly = SSEntityE.get(typesToRecommOnly);
  }
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "whether own entities should be included in the result")
  public Boolean includeOwn = true;
    
  public SSRecommResourcesRESTAPIV2Par(){}
}
