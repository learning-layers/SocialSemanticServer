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
package at.kc.tugraz.sss.appstacklayout.datatypes;

import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.SSErr;
import io.swagger.annotations.*;

@ApiModel
public class SSAppStackLayout extends SSEntity{
  
  @ApiModelProperty
  public SSUri            app         = null;
  
  public void setApp(final String app) throws SSErr{
    this.app = SSUri.get(app);
  }
  
  public String getApp(){
    return SSStrU.removeTrailingSlash(app);
  }
  
  public static SSAppStackLayout get(
    final SSUri           id,
    final SSUri           app) throws SSErr {
    
    return new SSAppStackLayout(
      id,
      app);
  }
  
  public static SSAppStackLayout get(
    final SSAppStackLayout     appStackLayout,
    final SSEntity             entity) throws SSErr {
    
    return new SSAppStackLayout(appStackLayout, entity);
  }
  
  public SSAppStackLayout(){/* Do nothing because of only JSON Jackson needs this */ }
  
  protected SSAppStackLayout(
    final SSAppStackLayout     appStackLayout,
    final SSEntity             entity) throws SSErr{
    
    super(appStackLayout, entity);
    
    this.app               = appStackLayout.app;
  }
  
  protected SSAppStackLayout(
    final SSUri           id,
    final SSUri           app) throws SSErr {
    
    super(id, SSEntityE.appStackLayout);
    
    this.app               = app;
  }
}