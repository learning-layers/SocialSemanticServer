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
package at.kc.tugraz.ss.serv.job.dataexport.impl.fct;

import at.kc.tugraz.ss.category.api.SSCategoryServerI;
import at.kc.tugraz.ss.category.datatypes.SSCategory;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoriesGetPar;
import at.tugraz.sss.serv.SSUri;
import at.kc.tugraz.ss.service.tag.api.SSTagServerI;
import at.kc.tugraz.ss.service.tag.datatypes.SSTag;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagsGetPar;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSSpaceE;
import java.util.List;
import java.util.Map;

public class SSDataExportFct{
  
  public static Map<String, List<String>> getTagsOfUserPerEntities(
    final SSUri          userUri,
    final SSUri          forUser,
    final List<SSUri>    entities,
    final SSUri          circle) throws Exception{
    
    return SSTag.getTagLabelsPerEntities(
      ((SSTagServerI) SSServReg.getServ(SSTagServerI.class)).tagsGet(
        new SSTagsGetPar(
          userUri,
          forUser, //forUser
          entities, //entities
          null, //labels
          null, //labelSearchOp
          SSSpaceE.asListWithoutNull(), //spaces
          SSUri.asListWithoutNullAndEmpty(circle), //circles
          null, //startTime
          false))); //withUserRestriction
  }
  
  public static Map<String, List<String>> getCategoriesPerEntities(
    final SSUri          userUri,
    final SSUri          forUser,
    final List<SSUri>    entities,
    final SSUri          circle) throws Exception{
    
    return SSCategory.getCategoryLabelsPerEntities(
      ((SSCategoryServerI) SSServReg.getServ(SSCategoryServerI.class)).categoriesGet(
        new SSCategoriesGetPar(
          userUri,
          forUser, //forUser
          entities, //entities
          null, //labels
          null, //labelSearchOp
          null, //spaces
          SSUri.asListWithoutNullAndEmpty(circle), //circles
          null, //startTime
          false))); //withUserRestriction
  }
}
