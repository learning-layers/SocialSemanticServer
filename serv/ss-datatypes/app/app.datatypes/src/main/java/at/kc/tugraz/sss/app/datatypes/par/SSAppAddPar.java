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
package at.kc.tugraz.sss.app.datatypes.par;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.SSTextComment;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.JsonNode;

@XmlRootElement
@ApiModel(value = "appAdd request parameter")
public class SSAppAddPar extends SSServPar{
  
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
    this.descriptionShort = SSTextComment.get(descriptionShort);
  }
  
  @ApiModelProperty(
    required = false,
    value = "functional description")
  public SSTextComment               descriptionFunctional        = null;
  
  @XmlElement
  public void setDescriptionFunctional(final String descriptionFunctional) throws Exception{
    this.descriptionFunctional = SSTextComment.get(descriptionFunctional);
  }
  
  @ApiModelProperty(
    required = false,
    value = "technical description")
  public SSTextComment               descriptionTechnical        = null;
  
  @XmlElement
  public void setDescriptionTechnical(final String descriptionTechnical) throws Exception{
    this.descriptionTechnical = SSTextComment.get(descriptionTechnical);
  }
  
  @ApiModelProperty(
    required = false,
    value = "install description")
  public SSTextComment               descriptionInstall        = null;
  
  @XmlElement
  public void setDescriptionInstall(final String descriptionInstall) throws Exception{
    this.descriptionInstall = SSTextComment.get(descriptionInstall);
  }

  @ApiModelProperty(
    required = false,
    value = "download links")
  public List<SSUri>               downloads       = new ArrayList<>();
  
  @XmlElement
  public void setDownloads(final List<String> downloads) throws Exception{
    this.downloads = SSUri.get(downloads);
  }
  
  @ApiModelProperty(
    required = false,
    value = "download link IOS")
  public SSUri               downloadIOS        = null;
  
  @XmlElement
  public void setDownloadIOS(final String downloadIOS) throws Exception{
    this.downloadIOS = SSUri.get(downloadIOS);
  }
  
  @ApiModelProperty(
    required = false,
    value = "download link Android")
  public SSUri               downloadAndroid        = null;
  
  @XmlElement
  public void setDownloadAndroid(final String downloadAndroid) throws Exception{
    this.downloadAndroid = SSUri.get(downloadAndroid);
  }
  
  @ApiModelProperty(
    required = false,
    value = "github fork link")
  public SSUri               fork        = null;
  
  @XmlElement
  public void setFork(final String fork) throws Exception{
    this.fork = SSUri.get(fork);
  }
  
  @ApiModelProperty(
    required = false,
    value = "screenShots")
  public List<SSUri>               screenShots        = new ArrayList<>();
  
  @XmlElement
  public void setScreenShots(final List<String> screenShots) throws Exception{
    this.screenShots = SSUri.get(screenShots);
  }
  
    @ApiModelProperty(
    required = false,
    value = "videos")
  public List<SSUri>               videos        = new ArrayList<>();
  
  @XmlElement
  public void setVideos(final List<String> videos) throws Exception{
    this.videos = SSUri.get(videos);
  }
  
  public SSAppAddPar(){}
  
  public SSAppAddPar(SSServPar par) throws Exception{
    
    super(par);
    
    try{
      
      if(pars != null){
        
        label                  = (SSLabel)         pars.get(SSVarU.label);
        descriptionShort       = (SSTextComment)   pars.get(SSVarU.descriptionShort);
        descriptionFunctional  = (SSTextComment)   pars.get(SSVarU.descriptionFunctional);
        descriptionTechnical   = (SSTextComment)   pars.get(SSVarU.descriptionTechnical);
        descriptionInstall     = (SSTextComment)   pars.get(SSVarU.descriptionInstall);
        downloadIOS            = (SSUri)           pars.get(SSVarU.downloadIOS);
        downloadAndroid        = (SSUri)           pars.get(SSVarU.downloadAndroid);
        fork                   = (SSUri)           pars.get(SSVarU.fork);
        downloads              = (List<SSUri>)     pars.get(SSVarU.downloads);
        screenShots            = (List<SSUri>)     pars.get(SSVarU.screenShots);
        videos                 = (List<SSUri>)     pars.get(SSVarU.videos);

      }
      
      if(par.clientJSONObj != null){
        
        label =  SSLabel.get      (par.clientJSONObj.get(SSVarU.label).getTextValue());
        
        try{
          descriptionShort =  SSTextComment.get      (par.clientJSONObj.get(SSVarU.descriptionShort).getTextValue());
        }catch(Exception error){}
        
        try{
          descriptionFunctional =  SSTextComment.get      (par.clientJSONObj.get(SSVarU.descriptionFunctional).getTextValue());
        }catch(Exception error){}
        
        try{
          descriptionTechnical =  SSTextComment.get      (par.clientJSONObj.get(SSVarU.descriptionTechnical).getTextValue());
        }catch(Exception error){}
        
        try{
          descriptionInstall =  SSTextComment.get      (par.clientJSONObj.get(SSVarU.descriptionInstall).getTextValue());
        }catch(Exception error){}
        
        try{
          downloadIOS =  SSUri.get      (par.clientJSONObj.get(SSVarU.downloadIOS).getTextValue());
        }catch(Exception error){}
        
        try{
          downloadAndroid =  SSUri.get      (par.clientJSONObj.get(SSVarU.downloadAndroid).getTextValue());
        }catch(Exception error){}
        
        try{
          fork =  SSUri.get      (par.clientJSONObj.get(SSVarU.fork).getTextValue());
        }catch(Exception error){}
        
        try{
          for (final JsonNode objNode : par.clientJSONObj.get(SSVarU.downloads)) {
            downloads.add(SSUri.get(objNode.getTextValue()));
          }
        }catch(Exception error){}
        
        try{
          for (final JsonNode objNode : par.clientJSONObj.get(SSVarU.screenShots)) {
            screenShots.add(SSUri.get(objNode.getTextValue()));
          }
        }catch(Exception error){}
        
        try{
          for (final JsonNode objNode : par.clientJSONObj.get(SSVarU.videos)) {
            videos.add(SSUri.get(objNode.getTextValue()));
          }
        }catch(Exception error){}
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  /* json getters */
  public String getLabel(){
    return SSStrU.toStr(label);
  }

  public String getDescriptionShort(){
    return SSStrU.toStr(descriptionShort);
  }

  public String getDescriptionFunctional(){
    return SSStrU.toStr(descriptionFunctional);
  }

  public String getDescriptionTechnical(){
    return SSStrU.toStr(descriptionTechnical);
  }

  public String getDescriptionInstall(){
    return SSStrU.toStr(descriptionInstall);
  }

  public List<String> getDownloads() throws Exception{
    return SSStrU.toStr(downloads);
  }

  public String getDownloadIOS(){
    return SSStrU.toStr(downloadIOS);
  }

  public String getDownloadAndroid(){
    return SSStrU.toStr(downloadAndroid);
  }

  public String getFork(){
    return SSStrU.toStr(fork);
  }

  public List<String> getScreenShots()throws Exception{
    return SSStrU.toStr(screenShots);
  }

  public List<String> getVideos()throws Exception{
    return SSStrU.toStr(videos);
  }
}