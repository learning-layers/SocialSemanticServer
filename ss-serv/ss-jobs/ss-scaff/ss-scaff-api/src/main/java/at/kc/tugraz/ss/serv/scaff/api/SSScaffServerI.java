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
package at.kc.tugraz.ss.serv.scaff.api;

import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import java.util.List;

public interface SSScaffServerI {
  public List<String> scaffRecommTagsBasedOnUserEntityTag                 (final SSServPar parA) throws Exception;
  public List<String> scaffRecommTagsBasedOnUserEntityTagTime             (final SSServPar parA) throws Exception;
  public List<String> scaffRecommTagsBasedOnUserEntityTagCategory         (final SSServPar parA) throws Exception;
  public List<String> scaffRecommTagsBasedOnUserEntityTagCategoryTime     (final SSServPar parA) throws Exception;
}
