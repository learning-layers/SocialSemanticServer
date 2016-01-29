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

import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.*;
import io.swagger.annotations.*;
import java.util.ArrayList;
import java.util.List;

@ApiModel
public class SSVideo extends SSEntity{

  @ApiModelProperty
  public String                   genre         = null;
  
  @ApiModelProperty
  public List<SSEntity>           annotations   = new ArrayList<>();
  
  @ApiModelProperty
  public SSUri                    link          = null;
  
  public void setLink(final String link) throws SSErr{
    this.link = SSUri.get(link);
  }
  
  public String getLink(){
    return SSStrU.removeTrailingSlash(link);
  }
  
  @ApiModelProperty
  public SSVideoE                 videoType     = null;
 
  public void setVideoType(final String videoType) throws SSErr{
    this.videoType = SSVideoE.get(videoType);
  }
  
  public String getVideoType(){
    return SSStrU.toStr(videoType);
  }
  
  public static SSVideo get(
    final SSVideo      video,
    final SSEntity     entity) throws SSErr{
    
    return new SSVideo(video, entity);
  }

  public static SSVideo get(
    final SSUri                   id,
    final String                  genre, 
    final List<SSVideoAnnotation> annotations,
    final SSUri                   link, 
    final SSVideoE                videoType) throws SSErr{
    
    return new SSVideo(
      id,
      genre,
      annotations,
      link, 
      videoType);
  }
  
  public SSVideo(){}
  
  protected SSVideo(
    final SSVideo     video,
    final SSEntity    entity) throws SSErr{
    
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
  
  protected SSVideo(
    final SSUri                   id,
    final String                  genre,
    final List<SSVideoAnnotation> annotations,
    final SSUri                   link, 
    final SSVideoE                videoType) throws SSErr{
    
    super(id, SSEntityE.video);
    
    this.genre               = genre;
    
    if(annotations != null){
      this.annotations.addAll(annotations);
    }
    
    this.link                = link;
    this.videoType           = videoType;
  }
}