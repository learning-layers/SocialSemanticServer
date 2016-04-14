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

package at.tugraz.sss.servs.tag.api;

import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.tag.datatype.SSTagFrequ;
import at.tugraz.sss.servs.tag.datatype.SSTagAddPar;
import at.tugraz.sss.servs.tag.datatype.SSTagEntitiesForTagsGetPar;
import at.tugraz.sss.servs.tag.datatype.SSTagFrequsGetPar;
import at.tugraz.sss.servs.tag.datatype.SSTagsAddPar;
import at.tugraz.sss.servs.tag.datatype.SSTagsGetPar;
import at.tugraz.sss.servs.tag.datatype.SSTagsRemovePar;
import at.tugraz.sss.servs.entity.datatype.SSEntity;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.common.api.SSServServerI;
import java.util.List;

public interface SSTagServerI extends SSServServerI{
  
  public List<SSUri>                     tagEntitiesForTagsGet        (final SSTagEntitiesForTagsGetPar par) throws SSErr;
  public List<SSUri>                     tagsAdd                      (final SSTagsAddPar               par) throws SSErr;
  public SSUri                           tagAdd                       (final SSTagAddPar                par) throws SSErr;
  public boolean                         tagsRemove                   (final SSTagsRemovePar            par) throws SSErr;
  public List<SSEntity>                  tagsGet                      (final SSTagsGetPar               par) throws SSErr;
  public List<SSTagFrequ>                tagFrequsGet                 (final SSTagFrequsGetPar          par) throws SSErr;
}