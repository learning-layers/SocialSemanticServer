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
package at.tugraz.sss.adapter.rest.v3.app;

import at.tugraz.sss.serv.datatype.SSTextComment;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.conf.SSConf;
import at.tugraz.sss.serv.util.*;
import io.swagger.annotations.*;
import java.util.List;

@ApiModel
public class SSAppAddRESTPar{
  
  @ApiModelProperty(
    required = true)
  public SSLabel               label        = null;
  
  public void setLabel(final String label) throws SSErr{
    this.label = SSLabel.get(label);
  }
  
  public String getLabel(){
    return SSStrU.toStr(label);
  }
  
  @ApiModelProperty(
    required = false)
  public SSTextComment               descriptionShort        = null;
  
  public void setDescriptionShort(final String descriptionShort) throws SSErr{
    this.descriptionShort = SSTextComment.get(descriptionShort);
  }

  public String getDescriptionShort(){
    return SSStrU.toStr(descriptionShort);
  }
  
  @ApiModelProperty(
    required = false)
  public SSTextComment               descriptionFunctional        = null;
  
  public void setDescriptionFunctional(final String descriptionFunctional) throws SSErr{
    this.descriptionFunctional = SSTextComment.get(descriptionFunctional);
  }
  
  public String getDescriptionFunctional(){
    return SSStrU.toStr(descriptionFunctional);
  }
  
  @ApiModelProperty(
    required = false)
  public SSTextComment               descriptionTechnical        = null;
  
  public void setDescriptionTechnical(final String descriptionTechnical) throws SSErr{
    this.descriptionTechnical = SSTextComment.get(descriptionTechnical);
  }

  public String getDescriptionTechnical(){
    return SSStrU.toStr(descriptionTechnical);
  }
    
  @ApiModelProperty(
    required = false)
  public SSTextComment               descriptionInstall        = null;
  
  public void setDescriptionInstall(final String descriptionInstall) throws SSErr{
    this.descriptionInstall = SSTextComment.get(descriptionInstall);
  }

  public String getDescriptionInstall(){
    return SSStrU.toStr(descriptionInstall);
  }
    
  @ApiModelProperty(
    required = false)
  public List<SSUri>               downloads       = null;
  
  public void setDownloads(final List<String> downloads) throws SSErr{
    this.downloads = SSUri.get(downloads, SSConf.sssUri);
  }
  
  public List<String> getDownloads(){
    return SSStrU.removeTrailingSlash(downloads);
  }
  
  @ApiModelProperty(
    required = false)
  public SSUri               downloadIOS        = null;
  
  public void setDownloadIOS(final String downloadIOS) throws SSErr{
    this.downloadIOS = SSUri.get(downloadIOS, SSConf.sssUri);
  }
  
  public String getDownloadIOS(){
    return SSStrU.removeTrailingSlash(downloadIOS);
  }
  
  @ApiModelProperty(
    required = false)
  public SSUri               downloadAndroid        = null;
  
  public void setDownloadAndroid(final String downloadAndroid) throws SSErr{
    this.downloadAndroid = SSUri.get(downloadAndroid, SSConf.sssUri);
  } 
  
  public String getDownloadAndroid(){
    return SSStrU.removeTrailingSlash(downloadAndroid);
  }
    
  @ApiModelProperty(
    required = false)
  public SSUri               fork        = null;
  
  public void setFork(final String fork) throws SSErr{
    this.fork = SSUri.get(fork, SSConf.sssUri);
  }
  
  public String getFork(){
    return SSStrU.removeTrailingSlash(fork);
  }  
  
  @ApiModelProperty(
    required = false)
  public List<SSUri>               screenShots        = null;
  
  public void setScreenShots(final List<String> screenShots) throws SSErr{
    this.screenShots = SSUri.get(screenShots, SSConf.sssUri);
  }
  
  public List<String> getScreenShots(){
    return SSStrU.removeTrailingSlash(screenShots);
  }
  
  @ApiModelProperty(
    required = false)
  public List<SSUri>               videos        = null;
  
  public void setVideos(final List<String> videos) throws SSErr{
    this.videos = SSUri.get(videos, SSConf.sssUri);
  }
  
  public List<String> getVideos(){
    return SSStrU.removeTrailingSlash(videos);
  }
   
  public SSAppAddRESTPar(){/* Do nothing because of only JSON Jackson needs this */ }
}