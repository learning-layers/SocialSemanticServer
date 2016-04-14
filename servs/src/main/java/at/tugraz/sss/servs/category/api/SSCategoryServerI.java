/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
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

package at.tugraz.sss.servs.category.api;

import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.category.datatype.SSCategoryFrequ;
import at.tugraz.sss.servs.category.datatype.SSCategoriesAddPar;
import at.tugraz.sss.servs.category.datatype.SSCategoriesGetPar;
import at.tugraz.sss.servs.category.datatype.SSCategoriesPredefinedAddPar;
import at.tugraz.sss.servs.category.datatype.SSCategoriesPredefinedGetPar;
import at.tugraz.sss.servs.category.datatype.SSCategoriesRemovePar;
import at.tugraz.sss.servs.category.datatype.SSCategoryAddPar;
import at.tugraz.sss.servs.category.datatype.SSCategoryFrequsGetPar;
import at.tugraz.sss.servs.category.datatype.SSCategoryEntitiesForCategoriesGetPar;
import at.tugraz.sss.servs.entity.datatype.SSEntity;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.common.api.SSServServerI;
import java.util.List;

public interface SSCategoryServerI extends SSServServerI{
  
  public boolean                         categoriesPredefinedAdd          (final SSCategoriesPredefinedAddPar par) throws SSErr;
  public List<String>                    categoriesPredefinedGet          (final SSCategoriesPredefinedGetPar par) throws SSErr;
  
  public List<SSUri>                     categoryEntitiesForCategoriesGet (final SSCategoryEntitiesForCategoriesGetPar par) throws SSErr;
  public List<SSUri>                     categoriesAdd                    (final SSCategoriesAddPar                    par) throws SSErr;
  public SSUri                           categoryAdd                      (final SSCategoryAddPar                      par) throws SSErr;
  public boolean                         categoriesRemove                 (final SSCategoriesRemovePar                 par) throws SSErr;
  public List<SSEntity>                  categoriesGet                    (final SSCategoriesGetPar                    par) throws SSErr;
  public List<SSCategoryFrequ>           categoryFrequsGet                (final SSCategoryFrequsGetPar                par) throws SSErr;
}