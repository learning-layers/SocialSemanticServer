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
package at.kc.tugraz.ss.serv.job.dataexport.datatypes.par;

import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.par.SSServPar; 
import at.tugraz.sss.serv.util.*;
import java.util.ArrayList;
import java.util.List;

public class SSDataExportUserEntityTagsCategoriesTimestampsLinePar extends SSServPar{
  
  public SSUri        forUser    = null;
  public SSUri        entity     = null;
  public List<String> tags       = new ArrayList<>();
  public List<String> categories = new ArrayList<>();
  public String       fileName   = null;
  
  public SSDataExportUserEntityTagsCategoriesTimestampsLinePar(
    final SSServPar    servPar, 
    final SSUri        user,
    final SSUri        forUser    ,
    final SSUri        entity     ,
    final List<String> tags       ,
    final List<String> categories,
    final String       fileName){
    
    super(SSVarNames.dataExportUserEntityTagsCategoriesTimestampsLine, null, user, servPar.sqlCon);
    
    this.forUser               = forUser;
    this.entity                = entity;
    
    SSStrU.addDistinctNotNull(this.tags,       tags);
    SSStrU.addDistinctNotNull(this.categories, categories);
        
    this.fileName              = fileName;
  }
}
