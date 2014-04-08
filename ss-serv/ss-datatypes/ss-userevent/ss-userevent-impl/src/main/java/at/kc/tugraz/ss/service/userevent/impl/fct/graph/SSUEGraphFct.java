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
package at.kc.tugraz.ss.service.userevent.impl.fct.graph;

import at.kc.tugraz.ss.serv.db.api.SSDBGraphFct;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;

public class SSUEGraphFct extends SSDBGraphFct{
  
  private SSUri  eventGraphUri = null;
  
  public SSUEGraphFct(final SSServImplWithDBA serv) throws Exception{
    
    super(serv.dbGraph);
    
    eventGraphUri = SSUri.get(SSServCaller.vocURIPrefixGet(), graphEvent);
  }
  
//  public boolean addUserEvent(
//    boolean          shouldCommit, 
//    SSUri            user,
//    SSUEEnum         eventType,
//    SSUri            resource,
//    String           content) throws Exception {
//
//    boolean   changed;
//    
//    try {
//
//      if (SSObjU.isNull(resource)) {
//        resource = voc.vocGraphUri();
//      }
//
//      SSUri currentEvent  = createEvent();
//
//      db.add(currentEvent, predHasResource(), resource,                                  eventGraphUri);
//      db.add(currentEvent, predBelongsTo(),   user,                                      eventGraphUri);
//      db.add(currentEvent, predType(),        objUE(),                                  eventGraphUri);
//      db.add(currentEvent, predTime(),        objTime(),                                 eventGraphUri);
//      db.add(currentEvent, predEventType(),   objEventType(SSStrU.toString(eventType)),  eventGraphUri); 
//
//      if (content.length() > 0) {
//        db.add(currentEvent, predContent(), objContent(content),        eventGraphUri);
//      } else {
//        db.add(currentEvent, predContent(), objContent(SSStrU.valueNA), eventGraphUri);
//      }
//      
//      changed = true;
//
//      db.commit(changed, shouldCommit);
//
//    } catch (Exception error) {
//      db.rollBackAndThrow(shouldCommit, error);
//    }
//
//    return true;
//  }
//
//  public List<SSUE> getEventObjects(
//    SSUri           user, 
//    SSUEEnum        eventType, 
//    SSUri           resource, 
//    Long            startTimestamp,
//    Long            endTimestamp) throws Exception{
//
//    List<String>       pars        = new ArrayList<String>();
//    List<SSUE>         eventList   = new ArrayList<SSUE>();
//    String             queryString;
//    SSUri              resultResource;
//    SSUEEnum           resultEventType;
//    SSUri              resultUser;
//    
////    db.deleteAll(eventGraphUri);
//    
//    pars.add(bindEvent);
//    pars.add(bindTime);
//    pars.add(bindContent);
//    
//    if(SSObjU.isNull(resource)){
//      pars.add(bindResource);
//    }
//    
//    if(SSObjU.isNull(eventType)){
//      pars.add(bindEventType);
//    }
//    
//    if(SSObjU.isNull(user)){
//      pars.add(bindUser);
//    }
//    
//    queryString = 
//      selectDistinctWithParFromAndWhere(eventGraphUri, pars) +
//      and(bindEvent, predType(),    objUE())                 +
//      and(bindEvent, predTime(),    bindTime)                +
//      and(bindEvent, predContent(), bindContent);             
//      
//    if(SSObjU.isNull(resource)){
//      queryString += and(bindEvent, predHasResource(), bindResource);
//    }else{
//      queryString += and(bindEvent, predHasResource(), resource);
//    }
//
//    if(SSObjU.isNull(resource)){
//      queryString += and(bindEvent, predEventType(), bindEventType);
//    } else {
//      queryString += and(bindEvent, predEventType(), objEventType(SSStrU.toString(eventType)));
//    }
//
//    if(SSObjU.isNull(user)) {
//      queryString += and(bindEvent, predBelongsTo(), bindUser);
//    } else {
//      queryString += and(bindEvent, predBelongsTo(), user);
//    }
//    
//    if(SSObjU.isNotNull(startTimestamp)){
//      queryString += filter(bindTime, SSStrU.greaterThan, String.valueOf(startTimestamp.longValue()));
//    }
//    
//    if(SSObjU.isNotNull(endTimestamp)){
//      queryString += filter(bindTime, SSStrU.lessThan, String.valueOf(endTimestamp.longValue()));
//    }
//    
//    queryString +=
//      whereEnd()   + 
//      orderByAsc(bindTime);
//    
////    SELECT DISTINCT ?event ?time ?content ?resource ?eventType ?user FROM <http://dt.ll/eventGraph/> WHERE{?event<http://www.w3.org/1999/02/22-rdf-syntax-ns#type/><http://dt.ll/event/>.?event<http://dt.ll/timestamp/>?time.?event<http://dt.ll/content/>?content.?event<http://dt.ll/hasResource/>?resource.?event<http://dt.ll/actionType/>?eventType.?event<http://dt.ll/belongsTo/>?user. FILTER (?time> "1364919641555")} ORDER BY ASC (?time)
//      
//    for (SSQueryResultItem item : db.query(queryString)) {
//
//      if(SSObjU.isNull(resource)){
//        resultResource = SSUri.get(binding(item, bindResource));
//      }else{
//        resultResource = resource;
//      }
//      
//      if(SSObjU.isNull(eventType)){
//        resultEventType = SSUEEnum.get(binding(item, bindEventType));
//      } else {
//        resultEventType = eventType;
//      }
//
//      if(SSObjU.isNull(user)) {
//        resultUser = SSUri.get(binding(item, bindUser));
//      }else{
//       resultUser = user; 
//      }
//
//      eventList.add(SSUE.get(
//        SSUri.get(binding(item, bindEvent)),
//        resultUser,
//        resultEventType,
//        resultResource,
//        binding(item, bindContent),
//        getLongFromGraphDoubleString(binding(item, bindTime))));
//    }
//    
//    return eventList;
//  }
}