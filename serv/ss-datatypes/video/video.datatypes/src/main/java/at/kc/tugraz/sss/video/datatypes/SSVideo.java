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
package at.kc.tugraz.sss.video.datatypes;

import at.kc.tugraz.ss.datatypes.datatypes.SSEntity;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SSVideo extends SSEntity{
  
  public String                   genre         = null;
  public List<SSVideoAnnotation>  annotations   = new ArrayList<>();
  
  
  public static List<SSVideo> get(final List<SSUri> uris) throws Exception{
    
    final List<SSVideo> videos = new ArrayList<>();
    
    for(SSUri uri : uris){
      videos.add(SSVideo.get(uri));
    }
    
    return videos;
  }
  
  public static SSVideo get(
    final SSUri           id) throws Exception{
    
    return new SSVideo(id, null, null);
  }
  
  public static SSVideo get(
    final SSVideo      video,
    final SSEntity     entity) throws Exception{
    
    return new SSVideo(video, entity);
  }
  
  protected SSVideo(
    final SSVideo     video,
    final SSEntity    entity) throws Exception{
    
    super(entity);
    
    this.genre               = video.genre;
   
    if(video.annotations != null){
      this.annotations.addAll(video.annotations);
    }
  }
  
  public static SSVideo get(
    final SSUri                   id,
    final String                  genre, 
    final List<SSVideoAnnotation> annotations) throws Exception{
    
    return new SSVideo(
      id,
      genre,
      annotations);
  }
  
  protected SSVideo(
    final SSUri                   id,
    final String                  genre,
    final List<SSVideoAnnotation> annotations) throws Exception{
    
    super(id, SSEntityE.video);
    
    this.genre               = genre;
    
    if(annotations != null){
      this.annotations.addAll(annotations);
    }
  }

  @Override
  public Object jsonLDDesc(){
  
    final Map<String, Object> ld = (Map<String, Object>) super.jsonLDDesc();
    
//    ld.put(SSVarU.user,         SSVarU.sss + SSStrU.colon + SSUri.class.getName());
//    ld.put(SSVarU.entity,       SSVarU.sss + SSStrU.colon + SSUri.class.getName());
//    ld.put(SSVarU.appType,      SSVarU.sss + SSStrU.colon + SSAppE.class.getName());
//    ld.put(SSVarU.endTime,      SSVarU.xsd + SSStrU.colon + SSStrU.valueLong);
//    ld.put(SSVarU.value,        SSVarU.xsd + SSStrU.colon + SSStrU.valueInteger);
    
    return ld;
  }
}