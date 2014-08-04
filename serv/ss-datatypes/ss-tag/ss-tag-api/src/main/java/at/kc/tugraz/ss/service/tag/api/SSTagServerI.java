/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
 package at.kc.tugraz.ss.service.tag.api;

import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.service.tag.datatypes.SSTag;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagFrequ;
import java.util.List;

public interface SSTagServerI {

  public SSUri                           tagUserEdit                  (final SSServPar parA) throws Exception;
  public List<SSUri>                     tagUserEntitiesForTagsGet    (final SSServPar parA) throws Exception;
  public List<SSUri>                     tagsAdd                      (final SSServPar parA) throws Exception;
  public List<SSUri>                     tagsAddAtCreationTime        (final SSServPar parA) throws Exception;
  public SSUri                           tagAdd                       (final SSServPar parA) throws Exception;
  public SSUri                           tagAddAtCreationTime         (final SSServPar parA) throws Exception;
  public Boolean                         tagsRemove                   (final SSServPar parA) throws Exception;
  public Boolean                         tagsUserRemove               (final SSServPar parA) throws Exception;
  public List<SSTag>                     tagsUserGet                  (final SSServPar parA) throws Exception;
  public List<SSTagFrequ>                tagUserFrequsGet             (final SSServPar parA) throws Exception;
}