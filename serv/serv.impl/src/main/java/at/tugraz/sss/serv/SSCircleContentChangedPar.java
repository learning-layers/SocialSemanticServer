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
package at.tugraz.sss.serv;

import java.util.ArrayList;
import java.util.List;

public class SSCircleContentChangedPar{
  
  public List<SSUri> recursiveEntities        = new ArrayList<>();
  
  public SSUri          user                  = null;
  public SSUri          circle                = null;
  public Boolean        isCirclePublic        = false;
  public List<SSUri>    usersToAdd            = new ArrayList<>();
  public List<SSEntity> entitiesToAdd         = new ArrayList<>();
  public List<SSUri>    usersToPushEntitiesTo = new ArrayList<>();
  public List<SSUri>    circleUsers           = new ArrayList<>();
  public List<SSEntity> circleEntities        = new ArrayList<>();
  
  public SSCircleContentChangedPar(
    final List<SSUri>    recursiveEntitiesToAdd,    
    final SSUri          user,
    final SSUri          circle,
    final Boolean        isCirclePublic,
    final List<SSUri>    usersToAdd,
    final List<SSEntity> entitiesToAdd,
    final List<SSUri>    usersToPushEntitiesTo,
    final List<SSUri>    circleUsers,
    final List<SSEntity> circleEntities){
    
    SSUri.addDistinctWithoutNull            (this.recursiveEntities, recursiveEntitiesToAdd);
      
    this.user           = user;
    this.circle         = circle;
    this.isCirclePublic = isCirclePublic;
    
    SSUri.addDistinctWithoutNull            (this.usersToAdd,            usersToAdd);
    SSEntity.addEntitiesDistinctWithoutNull (this.entitiesToAdd,         entitiesToAdd);
    SSUri.addDistinctWithoutNull            (this.usersToPushEntitiesTo, usersToPushEntitiesTo);
    SSUri.addDistinctWithoutNull            (this.circleUsers,           circleUsers);
    SSEntity.addEntitiesDistinctWithoutNull (this.circleEntities,        circleEntities);
  }
}
