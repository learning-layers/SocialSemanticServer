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

package at.tugraz.sss.servs.category.datatype;

import at.tugraz.sss.servs.util.SSVarNames;
import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.category.datatype.SSCategoryLabel;

import at.tugraz.sss.servs.entity.datatype.SSSpaceE;
import at.tugraz.sss.servs.entity.datatype.SSServPar;
import java.sql.*;

public class SSCategoriesRemovePar extends SSServPar{
  
  public SSUri             forUser    = null;
  public SSUri             entity     = null;
  public SSCategoryLabel   label      = null;
  public SSSpaceE          space      = null;
  public SSUri             circle     = null;
  
  public void setForUser(final String forUser) throws SSErr{
    this.forUser = SSUri.get(forUser);
  }
  
  public void setEntity(final String entity) throws SSErr{
    this.entity = SSUri.get(entity);
  }
  
  public void setLabel(final String label) throws SSErr{
    this.label = SSCategoryLabel.get(label);
  }
  
  public void setSpace(final String space) throws SSErr{
    this.space =  SSSpaceE.get(space);
  }
  
  public void setCircle(final String circle) throws SSErr{
    this.circle = SSUri.get(circle);
  }
  
  public String getForUser(){
    return SSStrU.removeTrailingSlash(forUser);
  }
  
  public String getEntity(){
    return SSStrU.removeTrailingSlash(entity);
  }
  
  public String getLabel(){
    return SSStrU.toStr(label);
  }
  
  public String getSpace(){
    return SSStrU.toStr(space);
  }
  
  public String getCircle(){
    return SSStrU.removeTrailingSlash(circle);
  }
  
  public SSCategoriesRemovePar(){/* Do nothing because of only JSON Jackson needs this */ }
  
  public SSCategoriesRemovePar(
    final SSServPar servPar,
    final SSUri           user,
    final SSUri           forUser,
    final SSUri           entity,
    final SSCategoryLabel label,
    final SSSpaceE        space,
    final SSUri           circle,
    final boolean         withUserRestriction,
    final boolean         shouldCommit){
    
    super(SSVarNames.categoriesRemove, null, user, servPar.sqlCon);
    
    this.forUser             = forUser;
    this.entity              = entity;
    this.label               = label;
    this.space               = space;
    this.circle              = circle;
    this.withUserRestriction = withUserRestriction;
    this.shouldCommit        = shouldCommit;
  }
}