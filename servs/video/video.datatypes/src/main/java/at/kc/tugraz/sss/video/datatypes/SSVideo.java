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

import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import java.util.ArrayList;
import java.util.List;

public class SSVideo extends SSEntity{
  
  public String                   genre         = null;
  public List<SSEntity>           annotations   = new ArrayList<>();
  public SSUri                    link          = null;
  public SSVideoE                 videoType     = null;
 
  public String getLink(){
    return SSStrU.removeTrailingSlash(link);
  }
  
   public String getVideoType(){
    return SSStrU.toStr(videoType);
  }
     
  public static SSVideo get(
    final SSVideo      video,
    final SSEntity     entity) throws Exception{
    
    return new SSVideo(video, entity);
  }
  
  protected SSVideo(
    final SSVideo     video,
    final SSEntity    entity) throws Exception{
    
    super(video, entity);
    
    if(video.genre != null){
      this.genre = video.genre;
    }else{
      
      if(entity instanceof SSVideo){
        this.genre = ((SSVideo) entity).genre;
      }
    }
    
    if(video.link != null){
      this.link = video.link;
    }else{
      
      if(entity instanceof SSVideo){
        this.link = ((SSVideo) entity).link;
      }
    }
    
    if(video.videoType != null){
      this.videoType = video.videoType;
    }else{
      
      if(entity instanceof SSVideo){
        this.videoType = ((SSVideo) entity).videoType;
      }
    }
   
    SSEntity.addEntitiesDistinctWithoutNull(this.annotations, video.annotations);
    
    if(entity instanceof SSVideo){
      SSEntity.addEntitiesDistinctWithoutNull(this.annotations, ((SSVideo) entity).annotations);
    }
  }
  
  public static SSVideo get(
    final SSUri                   id,
    final String                  genre, 
    final List<SSVideoAnnotation> annotations,
    final SSUri                   link, 
    final SSVideoE                videoType) throws Exception{
    
    return new SSVideo(
      id,
      genre,
      annotations,
      link, 
      videoType);
  }
  
  protected SSVideo(
    final SSUri                   id,
    final String                  genre,
    final List<SSVideoAnnotation> annotations,
    final SSUri                   link, 
    final SSVideoE                videoType) throws Exception{
    
    super(id, SSEntityE.video);
    
    this.genre               = genre;
    
    if(annotations != null){
      this.annotations.addAll(annotations);
    }
    
    this.link                = link;
    this.videoType           = videoType;
  }
}