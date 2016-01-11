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
package at.tugraz.sss.adapter.rest.v2.app;

import at.tugraz.sss.serv.datatype.SSTextComment;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.conf.SSConf;
import io.swagger.annotations.*;
import java.util.List;

@ApiModel
public class SSAppAddRESTAPIV2Par{
  
  @ApiModelProperty(
    required = true)
  public SSLabel               label        = null;
  
  
  public void setLabel(final String label) throws Exception{
    this.label = SSLabel.get(label);
  }
  
  @ApiModelProperty(
    required = false)
  public SSTextComment               descriptionShort        = null;
  
  
  public void setDescriptionShort(final String descriptionShort) throws Exception{
    this.descriptionShort = SSTextComment.get(descriptionShort);
  }
  
  @ApiModelProperty(
    required = false)
  public SSTextComment               descriptionFunctional        = null;
  
  
  public void setDescriptionFunctional(final String descriptionFunctional) throws Exception{
    this.descriptionFunctional = SSTextComment.get(descriptionFunctional);
  }
  
  @ApiModelProperty(
    required = false)
  public SSTextComment               descriptionTechnical        = null;
  
  
  public void setDescriptionTechnical(final String descriptionTechnical) throws Exception{
    this.descriptionTechnical = SSTextComment.get(descriptionTechnical);
  }
  
  @ApiModelProperty(
    required = false)
  public SSTextComment               descriptionInstall        = null;
  
  
  public void setDescriptionInstall(final String descriptionInstall) throws Exception{
    this.descriptionInstall = SSTextComment.get(descriptionInstall);
  }

  @ApiModelProperty(
    required = false)
  public List<SSUri>               downloads       = null;
  
  
  public void setDownloads(final List<String> downloads) throws Exception{
    this.downloads = SSUri.get(downloads, SSConf.sssUri);
  }
  
  @ApiModelProperty(
    required = false)
  public SSUri               downloadIOS        = null;
  
  
  public void setDownloadIOS(final String downloadIOS) throws Exception{
    this.downloadIOS = SSUri.get(downloadIOS, SSConf.sssUri);
  }
  
  @ApiModelProperty(
    required = false)
  public SSUri               downloadAndroid        = null;
  
  
  public void setDownloadAndroid(final String downloadAndroid) throws Exception{
    this.downloadAndroid = SSUri.get(downloadAndroid, SSConf.sssUri);
  } 
  
  @ApiModelProperty(
    required = false)
  public SSUri               fork        = null;
  
  
  public void setFork(final String fork) throws Exception{
    this.fork = SSUri.get(fork, SSConf.sssUri);
  }
  
  @ApiModelProperty(
    required = false)
  public List<SSUri>               screenShots        = null;
  
  
  public void setScreenShots(final List<String> screenShots) throws Exception{
    this.screenShots = SSUri.get(screenShots, SSConf.sssUri);
  }
  
  @ApiModelProperty(
    required = false)
  public List<SSUri>               videos        = null;
  
  public void setVideos(final List<String> videos) throws Exception{
    this.videos = SSUri.get(videos, SSConf.sssUri);
  }
  
  public SSAppAddRESTAPIV2Par(){}
}