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
package at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par;

import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSServPar;
import java.util.ArrayList;
import java.util.List;
import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSStrU;

public class SSEntityUpdatePar extends SSServPar{

  public SSEntity            givenEntity      = null;

  public SSUri               entity           = null;
  public SSUri               uriAlternative   = null;
  public SSEntityE           type             = null;
  public SSLabel             label            = null;
  public SSTextComment       description      = null;
  public List<SSTextComment> comments         = new ArrayList<>();
  public List<SSUri>         downloads        = new ArrayList<>();
  public List<SSUri>         screenShots      = new ArrayList<>();
  public List<SSUri>         images           = new ArrayList<>();
  public List<SSUri>         videos           = new ArrayList<>();
  public List<SSUri>         entitiesToAttach = new ArrayList<>();
  public Long                creationTime     = null;
  public Boolean             read             = null;
  public Boolean             setPublic        = null;

  public String getEntity(){
    return SSStrU.removeTrailingSlash(entity);
  }

  public void setEntity(final String entity) throws Exception{
    this.entity = SSUri.get(entity);
  }
  
  public void setUriAlternative(final String uriAlternative) throws Exception{
    this.uriAlternative = SSUri.get(uriAlternative);
  }
  
  public String getUriAlternative() throws Exception{
    return SSStrU.removeTrailingSlash(uriAlternative);
  }
  
  public String getType(){
    return SSStrU.toStr(type);
  }

  public void setType(final String type) throws Exception{
    this.type = SSEntityE.get(type);
  }
  
  public String getLabel(){
    return SSStrU.toStr(label);
  }

  public void setLabel(final String label) throws Exception{
    this.label = SSLabel.get(label);
  }

  public String getDescription(){
    return SSStrU.toStr(description);
  }

  public void setDescription(final String description) throws Exception{
    this.description = SSTextComment.get(description);
  }

  public List<String> getComments(){
    return SSStrU.toStr(comments);
  }

  public void setComments(final List<String> comments) throws Exception{
    this.comments = SSTextComment.get(comments);
  }

  public List<String> getDownloads(){
    return SSStrU.removeTrailingSlash(downloads);
  }

  public void setDownloads(final List<String> downloads) throws Exception{
    this.downloads = SSUri.get(downloads);
  }

  public List<String> getScreenShots(){
    return SSStrU.removeTrailingSlash(screenShots);
  }

  public void setScreenShots(final List<String> screenShots) throws Exception{
    this.screenShots = SSUri.get(screenShots);
  }

  public List<String> getImages(){
    return SSStrU.removeTrailingSlash(images);
  }

  public void setImages(final List<String> images) throws Exception{
    this.images = SSUri.get(images);
  }

  public List<String> getVideos(){
    return SSStrU.removeTrailingSlash(videos);
  }

  public void setVideos(final List<String> videos) throws Exception{
    this.videos = SSUri.get(videos);
  }

  public List<String> getEntitiesToAttach(){
    return SSStrU.removeTrailingSlash(entitiesToAttach);
  }

  public void setEntitiesToAttach(final List<String> entitiesToAttach) throws Exception{
    this.entitiesToAttach = SSUri.get(entitiesToAttach);
  }
  
  public SSEntityUpdatePar(){}
  
  public SSEntityUpdatePar(
    final SSServOpE           op,
    final String              key,
    final SSUri               user,
    final SSUri               entity,
    final SSUri               uriAlternative,
    final SSEntityE           type, 
    final SSLabel             label,
    final SSTextComment       description,
    final List<SSTextComment> comments,
    final List<SSUri>         downloads,
    final List<SSUri>         screenShots,
    final List<SSUri>         images,
    final List<SSUri>         videos,
    final List<SSUri>         entitiesToAttach,
    final Long                creationTime, 
    final Boolean             read,
    final Boolean             setPublic,
    final Boolean             withUserRestriction, 
    final Boolean             shouldCommit){

    super(op, key, user);
  
    this.entity         = entity;
    this.uriAlternative = uriAlternative;
    this.type           = type;
    this.label          = label;
    this.description    = description;
    
    if(comments != null){
      this.comments.addAll(comments);
    }
    
    SSUri.addDistinctWithoutNull(this.downloads,         downloads);
    SSUri.addDistinctWithoutNull(this.screenShots,       screenShots);
    SSUri.addDistinctWithoutNull(this.images,            images);
    SSUri.addDistinctWithoutNull(this.videos,            videos);
    SSUri.addDistinctWithoutNull(this.entitiesToAttach,  entitiesToAttach);
    
    this.creationTime        = creationTime;
    this.read                = read;
    this.setPublic           = setPublic;
    this.withUserRestriction = withUserRestriction;
    this.shouldCommit        = shouldCommit;
  }
}