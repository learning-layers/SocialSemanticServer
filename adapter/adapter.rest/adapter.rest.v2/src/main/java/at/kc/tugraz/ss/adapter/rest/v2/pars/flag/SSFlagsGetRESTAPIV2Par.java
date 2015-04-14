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
package at.kc.tugraz.ss.adapter.rest.v2.pars.flag;

import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.tugraz.sss.serv.SSUri;
import at.kc.tugraz.sss.flag.datatypes.SSFlagE;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@ApiModel(value = "flagsGet request parameter")
public class SSFlagsGetRESTAPIV2Par{
  
  @ApiModelProperty( 
    value = "entities", 
    required = false )
  public List<SSUri>   entities       = null;
  
  @XmlElement
  public void setEntities(final List<String> entities) throws Exception{
    this.entities = SSUri.get(entities, SSVocConf.sssUri);
  }
  
  @ApiModelProperty(
    value = "types", 
    required = false )
  public List<SSFlagE> types          = null;
  
  @XmlElement
  public void setTypes(final List<String> types) throws Exception{
    this.types = SSFlagE.get(types);
  }
  
  @XmlElement
  @ApiModelProperty( 
    value = "startTime", 
    required = false )
  public Long          startTime      = null;
  
  @XmlElement
  @ApiModelProperty( 
    value = "endTime", 
    required = false )
  public Long          endTime        = null;
  
  public SSFlagsGetRESTAPIV2Par(){}
}