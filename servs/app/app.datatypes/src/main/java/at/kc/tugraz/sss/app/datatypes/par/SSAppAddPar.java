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

import at.tugraz.sss.serv.SSMethU;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarU;
import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServErrReg;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.JsonNode;

public class SSAppAddPar extends SSServPar{
  
  public SSLabel             label                  = null;
  public SSTextComment       descriptionShort       = null;
  public SSTextComment       descriptionFunctional  = null;
  public SSTextComment       descriptionTechnical   = null;
  public SSTextComment       descriptionInstall     = null;
  public List<SSUri>         downloads              = new ArrayList<>();
  public SSUri               downloadIOS            = null;
  public SSUri               downloadAndroid        = null;
  public SSUri               fork                   = null;
  public List<SSUri>         screenShots            = new ArrayList<>();
  public List<SSUri>         videos                 = new ArrayList<>();
  
  public SSAppAddPar(
    final SSMethU             op,
    final String              key,
    final SSUri               user,
    final SSLabel             label                  ,
    final SSTextComment       descriptionShort       ,
    final SSTextComment       descriptionFunctional  ,
    final SSTextComment       descriptionTechnical   ,
    final SSTextComment       descriptionInstall     ,
    final List<SSUri>         downloads              ,
    final SSUri               downloadIOS            ,
    final SSUri               downloadAndroid        ,
    final SSUri               fork                   ,
    final List<SSUri>         screenShots            ,
    final List<SSUri>         videos                 ){
    
    super(op, key, user);
    
    this.label                  = label;
    this.descriptionShort       = descriptionShort;
    this.descriptionFunctional  = descriptionFunctional;
    this.descriptionTechnical   = descriptionTechnical;
    this.descriptionInstall     = descriptionInstall;
    
    if(downloads != null){
      this.downloads.addAll(downloads);
    }
    
    this.downloadIOS            = downloadIOS;
    this.downloadAndroid        = downloadAndroid;
    this.fork                   = fork;
    
    if(screenShots != null){
      this.screenShots.addAll(screenShots);
    }
    
    if(videos != null){
      this.videos.addAll(videos);
    }
  }
  
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