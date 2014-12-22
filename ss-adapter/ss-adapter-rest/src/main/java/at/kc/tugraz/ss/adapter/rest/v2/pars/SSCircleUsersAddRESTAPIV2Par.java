/**
 * Copyright 2014 Graz University of Technology - KTI (Knowledge Technologies Institute)
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
package at.kc.tugraz.ss.adapter.rest.v2.pars;

import at.kc.tugraz.ss.adapter.rest.SSRestMain;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@ApiModel(value = "entityUserUsersToCircleAdd request parameter")
public class SSCircleUsersAddRESTAPIV2Par{
  
  @ApiModelProperty(
    required = true,
    value = "users to add")
  public List<SSUri> users   = new ArrayList<>();
  
  @XmlElement
  public void setUsers(final List<String> users) throws Exception{
    this.users = SSUri.get(users, SSRestMain.conf.vocConf.uriPrefix);
  }
  
  public SSCircleUsersAddRESTAPIV2Par(){}
}