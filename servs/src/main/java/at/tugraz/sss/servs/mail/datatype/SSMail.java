/**
 * Code contributed to the Learning Layers project
 * http://www.learning-layers.eu
 * Development is partly funded by the FP7 Programme of the European Commission under
 * Grant Agreement FP7-ICT-318209.
 * Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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

/**
 * @author Dieter Theiler
 */

package at.tugraz.sss.servs.mail.datatype;

import at.tugraz.sss.servs.entity.datatype.SSEntityE;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSEntity;
import io.swagger.annotations.*;
import java.util.ArrayList;
import java.util.List;

@ApiModel
public class SSMail extends SSEntity{

  @ApiModelProperty
  public String         subject           = null;
  
  @ApiModelProperty
  public String         content           = null;
  
  @ApiModelProperty
  public List<SSEntity> contentMultimedia = new ArrayList<>();
  
  @ApiModelProperty
  public List<SSEntity> attachments       = new ArrayList<>();

  public static SSMail get(
    final SSMail     mail, 
    final SSEntity   entity) throws SSErr{
    
    return new SSMail(mail, entity);
  }
   
  public static SSMail get(
    final SSUri  id,
    final String subject,
    final Long   creationTime) throws SSErr{
    
    return new SSMail(id, subject, creationTime);
  }
  
  public SSMail(){/* Do nothing because of only JSON Jackson needs this */ }
  
  protected SSMail(
    final SSMail   mail,
    final SSEntity entity) throws SSErr{
    
    super(mail, entity);
  }
  
  protected SSMail(
    final SSUri          id,
    final String         subject,
    final Long           creationTime) throws SSErr{
    
    super(id, SSEntityE.mail);
    
    this.subject      = subject;
    this.creationTime = creationTime;
  }
}
