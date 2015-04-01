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
package at.kc.tugraz.sss.app.datatypes;

import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SSApp extends SSEntity{
  
  public SSTextComment   descriptionShort      = null;
  public SSTextComment   descriptionFunctional = null;
  public SSTextComment   descriptionTechnical  = null;
  public SSTextComment   descriptionInstall    = null;
  public List<SSUri>     downloads             = new ArrayList<>();
  public SSUri           downloadIOS           = null;
  public SSUri           downloadAndroid       = null;
  public SSUri           fork                  = null;
  
  public static SSApp get(
    final SSApp     app,
    final SSEntity  entity) throws Exception{
    
    return new SSApp(app, entity);
  }
  
  protected SSApp(
    final SSApp     app,
    final SSEntity  entity) throws Exception{
    
    super(entity);
    
    this.descriptionShort               = app.descriptionShort;
    this.descriptionFunctional          = app.descriptionFunctional;
    this.descriptionTechnical           = app.descriptionTechnical;
    this.descriptionInstall             = app.descriptionInstall;
    this.downloadIOS                    = app.downloadIOS;
    this.downloadAndroid                = app.downloadAndroid;
    this.fork                           = app.fork;
    this.downloads                      = app.downloads;
  }
  
  public static SSApp get(
    final SSUri           id,
    final SSTextComment   descriptionShort,
    final SSTextComment   descriptionFunctional,
    final SSTextComment   descriptionTechnical,
    final SSTextComment   descriptionInstall,
    final SSUri           downloadIOS,
    final SSUri           downloadAndroid,
    final SSUri           fork) throws Exception{
    
    return new SSApp(
      id,
      descriptionShort,
      descriptionFunctional,
      descriptionTechnical,
      descriptionInstall,
      downloadIOS,
      downloadAndroid,
      fork);
  }
  
  protected SSApp(
    final SSUri           id,
    final SSTextComment   descriptionShort,
    final SSTextComment   descriptionFunctional,
    final SSTextComment   descriptionTechnical,
    final SSTextComment   descriptionInstall,
    final SSUri           downloadIOS,
    final SSUri           downloadAndroid,
    final SSUri           fork) throws Exception{
    
    super(id, SSEntityE.app);
    
    this.descriptionShort               = descriptionShort;
    this.descriptionFunctional          = descriptionFunctional;
    this.descriptionTechnical           = descriptionTechnical;
    this.descriptionInstall             = descriptionInstall;
    this.downloadIOS                    = downloadIOS;
    this.downloadAndroid                = downloadAndroid;
    this.fork                           = fork;
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
  
  /* json getters */
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
    return SSStrU.removeTrailingSlash(downloads);
  }

  public String getDownloadIOS() throws Exception{
    return SSStrU.removeTrailingSlash(downloadIOS);
  }

  public String getDownloadAndroid() throws Exception{
    return  SSStrU.removeTrailingSlash(downloadAndroid);
  }

  public String getFork() throws Exception{
    return  SSStrU.removeTrailingSlash(fork);
  }  
}