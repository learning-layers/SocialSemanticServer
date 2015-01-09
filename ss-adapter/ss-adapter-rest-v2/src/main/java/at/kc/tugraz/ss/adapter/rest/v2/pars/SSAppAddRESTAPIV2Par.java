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
package at.kc.tugraz.ss.adapter.rest.v2.pars;

import at.kc.tugraz.ss.datatypes.datatypes.SSTextComment;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@ApiModel(value = "appAdd request parameter")
public class SSAppAddRESTAPIV2Par{
  
  @ApiModelProperty(
    required = true,
    value = "name")
  public SSLabel               label        = null;
  
  @XmlElement
  public void setLabel(final String label) throws Exception{
    this.label = SSLabel.get(label);
  }
  
  @ApiModelProperty(
    required = false,
    value = "short description")
  public SSTextComment               descriptionShort        = null;
  
  @XmlElement
  public void setDescriptionShort(final String descriptionShort) throws Exception{
    try{ this.descriptionShort = SSTextComment.get(descriptionShort); }catch(Exception error){}
  }
  
  @ApiModelProperty(
    required = false,
    value = "functional description")
  public SSTextComment               descriptionFunctional        = null;
  
  @XmlElement
  public void setDescriptionFunctional(final String descriptionFunctional) throws Exception{
    try{ this.descriptionFunctional = SSTextComment.get(descriptionFunctional); }catch(Exception error){}
  }
  
  @ApiModelProperty(
    required = false,
    value = "technical description")
  public SSTextComment               descriptionTechnical        = null;
  
  @XmlElement
  public void setDescriptionTechnical(final String descriptionTechnical) throws Exception{
    try{ this.descriptionTechnical = SSTextComment.get(descriptionTechnical); }catch(Exception error){}
  }
  
  @ApiModelProperty(
    required = false,
    value = "install description")
  public SSTextComment               descriptionInstall        = null;
  
  @XmlElement
  public void setDescriptionInstall(final String descriptionInstall) throws Exception{
    try{ this.descriptionInstall = SSTextComment.get(descriptionInstall); }catch(Exception error){}
  }

  @ApiModelProperty(
    required = false,
    value = "download links")
  public List<SSUri>               downloads       = new ArrayList<>();
  
  @XmlElement
  public void setDownloads(final List<String> downloads) throws Exception{
    try{ this.downloads = SSUri.get(downloads, SSVocConf.sssUri); }catch(Exception error){}
  }
  
  @ApiModelProperty(
    required = false,
    value = "download link IOS")
  public SSUri               downloadIOS        = null;
  
  @XmlElement
  public void setDownloadIOS(final String downloadIOS) throws Exception{
    try{ this.downloadIOS = SSUri.get(downloadIOS, SSVocConf.sssUri); }catch(Exception error){}
  }
  
  @ApiModelProperty(
    required = false,
    value = "download link Android")
  public SSUri               downloadAndroid        = null;
  
  @XmlElement
  public void setDownloadAndroid(final String downloadAndroid) throws Exception{
    try{ this.downloadAndroid = SSUri.get(downloadAndroid, SSVocConf.sssUri); }catch(Exception error){}
  } 
  
  @ApiModelProperty(
    required = false,
    value = "github fork link")
  public SSUri               fork        = null;
  
  @XmlElement
  public void setFork(final String fork) throws Exception{
    try{ this.fork = SSUri.get(fork, SSVocConf.sssUri); }catch(Exception error){}
  }
  
  @ApiModelProperty(
    required = false,
    value = "screenShots")
  public List<SSUri>               screenShots        = new ArrayList<>();
  
  @XmlElement
  public void setScreenShots(final List<String> screenShots) throws Exception{
    try{ this.screenShots = SSUri.get(screenShots, SSVocConf.sssUri); }catch(Exception error){}
  }
  
    @ApiModelProperty(
    required = false,
    value = "videos")
  public List<SSUri>               videos        = new ArrayList<>();
  
  @XmlElement
  public void setVideos(final List<String> videos) throws Exception{
    try{ this.videos = SSUri.get(videos, SSVocConf.sssUri); }catch(Exception error){}
  }
  
  public SSAppAddRESTAPIV2Par(){}
}