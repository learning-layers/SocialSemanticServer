/**
 * Copyright 2013 Graz University of Technology - KTI (Knowledge Technologies Institute)
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
package at.kc.tugraz.ss.serv.datatypes.entity.api;

import at.kc.tugraz.ss.datatypes.datatypes.SSEntityEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSLabelStr;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityDescA;

public interface SSEntityServerI {
  
  public SSEntityEnum  entityTypeGet                            (final SSServPar parA) throws Exception;
  public SSEntityDescA entityDescGet                            (final SSServPar parA) throws Exception;
  public SSUri         entityAdd                                (final SSServPar parA) throws Exception;
  public SSUri         entityAddAtCreationTime                  (final SSServPar parA) throws Exception;
  public Long          entityCreationTimeGet                    (final SSServPar parA) throws Exception;
  public SSUri         entityUserDirectlyAdjoinedEntitiesRemove (final SSServPar parA) throws Exception;
  public SSUri         entityRemove                             (final SSServPar parA) throws Exception;
  public void          entityRemoveAll                          (final SSServPar parA) throws Exception;
  public SSLabelStr    entityLabelGet                           (final SSServPar parA) throws Exception;
  public SSUri         entityAuthorGet                          (final SSServPar parA) throws Exception;
  public SSUri         entityLabelSet                           (final SSServPar parA) throws Exception;
}