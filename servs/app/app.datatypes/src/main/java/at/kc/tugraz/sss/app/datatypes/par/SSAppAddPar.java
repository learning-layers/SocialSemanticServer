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


import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.SSTextComment;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.par.SSServPar; import at.tugraz.sss.serv.util.*;
import java.util.ArrayList;
import java.util.List;

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

  public void setLabel(final String label) throws Exception{
    this.label = SSLabel.get(label);
  }

  public void setDescriptionShort(final String descriptionShort) throws Exception{
    this.descriptionShort = SSTextComment.get(descriptionShort);
  }

  public void setDescriptionFunctional(final String descriptionFunctional)throws Exception{
    this.descriptionFunctional = SSTextComment.get(descriptionFunctional);
  }

  public void setDescriptionTechnical(final String descriptionTechnical)throws Exception{
    this.descriptionTechnical = SSTextComment.get(descriptionTechnical);
  }

  public void setDescriptionInstall(final String descriptionInstall)throws Exception{
    this.descriptionInstall = SSTextComment.get(descriptionInstall);
  }

  public void setDownloads(final List<String> downloads)throws Exception{
    this.downloads = SSUri.get(downloads);
  }

  public void setDownloadIOS(final String downloadIOS)throws Exception{
    this.downloadIOS =  SSUri.get(downloadIOS);
  }

  public void setDownloadAndroid(final String downloadAndroid)throws Exception{
    this.downloadAndroid =  SSUri.get(downloadAndroid);
  }

  public void setFork(final String fork)throws Exception{
    this.fork =  SSUri.get(fork);
  }

  public void setScreenShots(final List<String> screenShots)throws Exception{
    this.screenShots =  SSUri.get(screenShots);
  }

  public void setVideos(final List<String> videos) throws Exception{
    this.videos =  SSUri.get(videos);
  }
  
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
  
  public SSAppAddPar(){}
    
  public SSAppAddPar(
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
    final List<SSUri>         videos                 , 
    final Boolean             withUserRestriction, 
    final Boolean             shouldCommmit){
    
    super(SSVarNames.appAdd, null, user);
    
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
    
    this.withUserRestriction = withUserRestriction;
    this.shouldCommit        = shouldCommmit;
  }
}