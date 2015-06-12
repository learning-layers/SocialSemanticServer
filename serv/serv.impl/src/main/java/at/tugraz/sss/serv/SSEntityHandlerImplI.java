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
package at.tugraz.sss.serv;

import java.util.List;

public interface SSEntityHandlerImplI{

  public void removeDirectlyAdjoinedEntitiesForUser(
    final SSUri       userUri,
    final SSEntityE   entityType,
    final SSUri       entityUri,
    final Boolean     removeUserTags,
    final Boolean     removeUserRatings,
    final Boolean     removeFromUserColls,
    final Boolean     removeUserLocations) throws Exception;

  public Boolean setEntityPublic(
    final SSUri                 userUri, 
    final SSUri                 entityUri, 
    final SSEntityE             entityType,
    final SSUri                 publicCircleUri) throws Exception;

  public void shareEntityWithUsers(
    final SSUri          user, 
    final List<SSUri>    users,
    final SSUri          entity, 
    final SSUri          circle,
    final SSEntityE      entityType,
    final Boolean        saveActivity) throws Exception;

  public void addEntityToCircle(
    final SSUri        user, 
    final SSUri        circle,
    final List<SSUri>  circleUsers,
    final SSUri        entity, 
    final SSEntityE    entityType) throws Exception;
  
  public void addUsersToCircle(
    final SSUri        user, 
    final List<SSUri>  users,
    final SSEntityCircle        circle) throws Exception;
  
  public Boolean copyEntity(
    final SSUri        user,
    final List<SSUri>  users,
    final SSUri        entity, 
    final List<SSUri>  entitiesToExclude,
    final SSEntityE    entityType) throws Exception;

  public List<SSUri> getSubEntities(
    final SSUri         user, 
    final SSUri         entity, 
    final SSEntityE     type) throws Exception;
  
  public List<SSUri> getParentEntities(
    final SSUri         user,
    final SSUri         entity,
    final SSEntityE     type) throws Exception;
}