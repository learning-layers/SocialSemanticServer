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

package at.tugraz.sss.servs.entity.datatype;
import at.tugraz.sss.servs.util.SSVarNames;
import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSEntityE;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSLabel;
import at.tugraz.sss.servs.entity.datatype.SSTextComment;
import at.tugraz.sss.servs.entity.datatype.SSUri;

public class SSEntityUpdatePar extends SSServPar{

  public SSUri               entity                      = null;
  public SSEntityE           type                        = null;
  public SSLabel             label                       = null;
  public SSTextComment       description                 = null;
  public Long                creationTime                = null;
  public Boolean             read                        = null;
  public boolean             setPublic                   = false;
  public boolean             createIfNotExists           = false;
  public boolean             addUserToAdditionalAuthors  = false;

  public String getEntity(){
    return SSStrU.removeTrailingSlash(entity);
  }

  public void setEntity(final String entity) throws SSErr{
    this.entity = SSUri.get(entity);
  }
  
  public String getType(){
    return SSStrU.toStr(type);
  }

  public void setType(final String type) throws SSErr{
    this.type = SSEntityE.get(type);
  }
  
  public String getLabel(){
    return SSStrU.toStr(label);
  }

  public void setLabel(final String label) throws SSErr{
    this.label = SSLabel.get(label);
  }

  public String getDescription(){
    return SSStrU.toStr(description);
  }

  public void setDescription(final String description) throws SSErr{
    this.description = SSTextComment.get(description);
  }

  public SSEntityUpdatePar(){/* Do nothing because of only JSON Jackson needs this */ }
  
  public SSEntityUpdatePar(
    final SSServPar           servPar,
    final SSUri               user,
    final SSUri               entity,
    final SSEntityE           type, 
    final SSLabel             label,
    final SSTextComment       description,
    final Long                creationTime, 
    final Boolean             read,
    final boolean             setPublic,
    final boolean             createIfNotExists, 
    final boolean             withUserRestriction, 
    final boolean             shouldCommit){

    super(SSVarNames.entityUpdate, null, user, servPar.sqlCon);
  
    this.entity              = entity;
    this.type                = type;
    this.label               = label;
    this.description         = description;
    this.creationTime        = creationTime;
    this.read                = read;
    this.setPublic           = setPublic;
    this.createIfNotExists   = createIfNotExists;
    this.withUserRestriction = withUserRestriction;
    this.shouldCommit        = shouldCommit;
  }
}