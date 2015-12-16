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
package at.tugraz.sss.servs.image.datatype.ret;

import at.tugraz.sss.serv.SSEntity;

import at.tugraz.sss.serv.SSServRetI; import at.tugraz.sss.serv.SSVarNames;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SSImagesGetRet extends SSServRetI{

  public List<SSEntity> images = new ArrayList<>();

  @Override
  public Map<String, Object> jsonLDDesc(){
    throw new UnsupportedOperationException();
  }
  
  public static SSImagesGetRet get(
    final List<SSEntity> images){
    
    return new SSImagesGetRet(images);
  }
  
  private SSImagesGetRet(
    final List<SSEntity> images) {

    super(SSVarNames.imagesGet);
    
    SSEntity.addEntitiesDistinctWithoutNull(this.images, images);
  }
}