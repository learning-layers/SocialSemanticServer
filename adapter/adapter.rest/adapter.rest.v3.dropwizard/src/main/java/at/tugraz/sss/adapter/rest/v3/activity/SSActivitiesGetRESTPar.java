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
package at.tugraz.sss.adapter.rest.v3.activity;

import at.kc.tugraz.ss.activity.datatypes.enums.SSActivityE;
import at.tugraz.sss.serv.conf.SSConf;
import at.tugraz.sss.serv.datatype.*;
import io.swagger.annotations.*;
import java.util.List;

@ApiModel
public class SSActivitiesGetRESTPar{
  
  @ApiModelProperty(
    required = false)
  public List<SSActivityE>      types                     = null;
  
  
  public void setTypes(final List<String> types) throws SSErr{
    this.types = SSActivityE.get(types);
  }
  
  @ApiModelProperty(
    required = false)
  public List<SSUri>            users                     = null;
  
  
  public void setUsers(final List<String> users) throws SSErr{
    this.users = SSUri.get(users, SSConf.sssUri);
  }
  
  @ApiModelProperty(
    required = false)
  public List<SSUri>            entities                  = null;
  
  
  public void setEntities(final List<String> entities) throws SSErr{
    this.entities = SSUri.get(entities, SSConf.sssUri);
  }
  
  @ApiModelProperty(
    required = false)
  public List<SSUri>            circles                   = null;
  
  
  public void setCircles(final List<String> circles) throws SSErr{
    this.circles = SSUri.get(circles, SSConf.sssUri);
  }
  
  
  @ApiModelProperty(
    required = false)
  public Long                   startTime                 = null;
  
  
  @ApiModelProperty(
    required = false)
  public Long                   endTime                   = null;
  
  
  @ApiModelProperty(
    required = false)
  public boolean                includeOnlyLastActivities = false;
  
  public SSActivitiesGetRESTPar(){/* Do nothing because of only JSON Jackson needs this */ }
}
