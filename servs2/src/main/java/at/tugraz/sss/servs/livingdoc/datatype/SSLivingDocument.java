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
package at.tugraz.sss.servs.livingdoc.datatype;

import at.tugraz.sss.servs.entity.datatype.SSEntityE;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSEntity;
import io.swagger.annotations.*;

@ApiModel
public class SSLivingDocument extends SSEntity{

  public static SSLivingDocument get(
    final SSLivingDocument livingDocument, 
    final SSEntity         entity) throws SSErr{
    
    return new SSLivingDocument(livingDocument, entity);
  }
   
  public static SSLivingDocument get(
    final SSUri           id) throws SSErr{
    
    return new SSLivingDocument(id);
  }
  
  public SSLivingDocument(){/* Do nothing because of only JSON Jackson needs this */ }
  
  protected SSLivingDocument(
    final SSUri id) throws SSErr{
    
    super(id, SSEntityE.livingDoc);
  }
  
  protected SSLivingDocument(
    final SSLivingDocument   livingDocument,
    final SSEntity           entity) throws SSErr{
    
    super(livingDocument, entity);
  }
}
