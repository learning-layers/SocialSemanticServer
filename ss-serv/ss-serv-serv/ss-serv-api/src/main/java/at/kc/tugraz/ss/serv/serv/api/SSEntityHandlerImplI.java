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
package at.kc.tugraz.ss.serv.serv.api;

import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityA;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityDescA;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import java.util.List;

public interface SSEntityHandlerImplI{

  public SSEntityDescA getDescForEntity(
    final SSEntityE       entityType, 
    final SSUri           userUri, 
    final SSUri           entityUri, 
    final SSLabel         label,
    final Long            creationTime,
    final List<String>    tags, 
    final SSEntityA       overallRating,
    final List<SSUri>     discUris,
    final SSUri           author) throws Exception;
  
  public void removeDirectlyAdjoinedEntitiesForUser(
    final SSUri       userUri,
    final SSEntityE   entityType,
    final SSUri       entityUri,
    final Boolean     removeUserTags,
    final Boolean     removeUserRatings,
    final Boolean     removeFromUserColls,
    final Boolean     removeUserLocations) throws Exception;

  public Boolean setUserEntityPublic(
    final SSUri                    userUri, 
    final SSUri                    entityUri, 
    final SSEntityE             entityType,
    final SSUri                    publicCircleUri) throws Exception;

  public Boolean shareUserEntity(
    final SSUri          user, 
    final List<SSUri>    usersToShareWith,
    final SSUri          entity, 
    final SSUri          circle,
    final SSEntityE      entityType) throws Exception;
  
  public SSUri copyUserEntity(
    final SSUri        user,
    final SSUri        forUser,
    final SSUri        entity, 
    final SSEntityE    entityType) throws Exception;
    
  public Boolean addEntityToCircle(
    final SSUri        userUri, 
    final SSUri        circleUri, 
    final SSUri        entityUri, 
    final SSEntityE    entityType) throws Exception;

  public List<SSUri> getSubEntities(
    final SSUri         user, 
    final SSUri         entity, 
    final SSEntityE     type) throws Exception;
}