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
package at.kc.tugraz.ss.service.userevent.datatypes;

import at.kc.tugraz.socialserver.utils.*;
import at.kc.tugraz.ss.serv.jsonld.datatypes.api.SSJSONLDPropI;
import java.util.*;

public enum SSUEE implements SSJSONLDPropI{
  
  /* user events stored client-side by bits and pieces tool */
  timelineChangeTimelineRange,
  timelineViewEntityDetails,
  
  learnEpViewEntityDetails,
  learnEpOpenEpisodesDialog,
  learnEpSwitchEpisode,
  learnEpSwitchVersion,
  learnEpRenameEpisode,
  learnEpCreateNewEpisodeFromScratch,
  learnEpCreateNewEpisodeFromVersion,
  learnEpCreateNewVersion,
  learnEpDropOrganizeEntity,
  learnEpMoveOrganizeEntity,
  learnEpDeleteOrganizeEntity,
  learnEpCreateOrganizeCircle,
  learnEpChangeOrganizeCircle,
  learnEpRenameOrganizeCircle,
  learnEpDeleteOrganizeCircle,
  
  /* user events stored server-side to be used in bits and pieces tool */
  
  evernoteNotebookCreate,
  evernoteNotebookUpdate,
  evernoteNotebookFollow,
  evernoteNoteCreate,
  evernoteNoteUpdate,
  evernoteNoteDelete,
  evernoteNoteShare,
  evernoteReminderDone,
  evernoteReminderCreate,
  evernoteResourceAdd,
  
  /* user events maybe to delete as they are not stored on client-side anymore */
  selectedFromOthers,
  viewEntity,
  exportCollectionItem,
  useTag,
  renamePrivateCollection,
  renameSharedCollection,
  renamePrivateCollectionItem,
  renameSharedCollectionItem,
  structurePrivateCollection, //structurePrivateCollectionContent
  structureSharedCollection, //structureSharedCollectionContent
  shareCollection;
  
  public static SSUEE get(
    final String eventType){
    
    return SSUEE.valueOf(eventType);
  }
  
  public static List<SSUEE> get(
    final List<String> eventTypes){
    
    final List<SSUEE> result = new ArrayList<>();
    
    for (String eventType : eventTypes){
      result.add(get(eventType));
    }
    
    return result;
  }

  public static List<SSUEE> asListWithoutEmptyAndNull(final SSUEE... eventTypes){
    
    final List<SSUEE> result = new ArrayList<>();
    
    if(eventTypes == null){
      return result;
    }
    
    for(SSUEE eventType : eventTypes){
      
      if(SSStrU.isEmpty(eventType)){
        continue;
      }
      
      result.add(eventType);
    }
    
    return result;
  }
  
  @Override
  public Object jsonLDDesc(){
    return SSVarU.xsd + SSStrU.colon + SSStrU.valueString;
  }
  
  
}