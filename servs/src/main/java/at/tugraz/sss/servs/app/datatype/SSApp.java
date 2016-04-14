/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
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

/**
 * @author Dieter Theiler
 */

package at.tugraz.sss.servs.app.datatype;

import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSEntityE;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSEntity;
import at.tugraz.sss.servs.entity.datatype.SSTextComment;
import io.swagger.annotations.*;
import java.util.ArrayList;
import java.util.List;

@ApiModel
public class SSApp extends SSEntity{

  @ApiModelProperty
  public SSTextComment   descriptionShort      = null;
  
  public void setDescriptionShort(final String descriptionShort) throws SSErr{
    this.descriptionShort = SSTextComment.get(descriptionShort);
  }
  
  public String getDescriptionShort(){
    return SSStrU.toStr(descriptionShort);
  }
  
  @ApiModelProperty
  public SSTextComment   descriptionFunctional = null;
  
  public void setDescriptionFunctional(final String descriptionFunctional) throws SSErr{
    this.descriptionFunctional = SSTextComment.get(descriptionFunctional);
  }
  
  public String getDescriptionFunctional(){
    return SSStrU.toStr(descriptionFunctional);
  }
   
  @ApiModelProperty
  public SSTextComment   descriptionTechnical  = null;
  
  public void setDescriptionTechnical(final String descriptionTechnical) throws SSErr{
    this.descriptionTechnical = SSTextComment.get(descriptionTechnical);
  }
  
  public String getDescriptionTechnical(){
    return SSStrU.toStr(descriptionTechnical);
  }
  
  @ApiModelProperty
  public SSTextComment   descriptionInstall    = null;
  
  public void setDescriptionInstall(final String descriptionInstall) throws SSErr{
    this.descriptionInstall = SSTextComment.get(descriptionInstall);
  }
  
  public String getDescriptionInstall(){
    return SSStrU.toStr(descriptionInstall);
  }
  
  @ApiModelProperty
  public List<SSUri>     downloads             = new ArrayList<>();
  
  public void setDownloads(final List<String> downloads) throws SSErr{
    this.downloads = SSUri.get(downloads);
  }
    
  public List<String> getDownloads(){
    return SSStrU.removeTrailingSlash(downloads);
  }
  
  @ApiModelProperty
  public SSUri           downloadIOS           = null;
  
  public void setDownloadIOS(final String downloadIOS) throws SSErr{
    this.downloadIOS = SSUri.get(downloadIOS);
  }
  
  public String getDownloadIOS(){
    return SSStrU.removeTrailingSlash(downloadIOS);
  }
  
  @ApiModelProperty
  public SSUri           downloadAndroid       = null;
  
  public void setDownloadAndroid(final String downloadAndroid) throws SSErr{
    this.downloadAndroid = SSUri.get(downloadAndroid);
  }
  
  public String getDownloadAndroid(){
    return SSStrU.removeTrailingSlash(downloadAndroid);
  }
  
  @ApiModelProperty
  public SSUri           fork                  = null;
  
  public void setFork(final String fork) throws SSErr{
    this.fork = SSUri.get(fork);
  }

  public String getFork(){
    return  SSStrU.removeTrailingSlash(fork);
  }

  public static SSApp get(
    final SSApp     app,
    final SSEntity  entity) throws SSErr{
    
    return new SSApp(app, entity);
  }
  
  public static SSApp get(
    final SSUri           id,
    final SSTextComment   descriptionShort,
    final SSTextComment   descriptionFunctional,
    final SSTextComment   descriptionTechnical,
    final SSTextComment   descriptionInstall,
    final SSUri           downloadIOS,
    final SSUri           downloadAndroid,
    final SSUri           fork) throws SSErr{
    
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
  
  public SSApp(){/* Do nothing because of only JSON Jackson needs this */ }
  
  protected SSApp(
    final SSApp     app,
    final SSEntity  entity) throws SSErr{
    
    super(app, entity);
    
    this.descriptionShort               = app.descriptionShort;
    this.descriptionFunctional          = app.descriptionFunctional;
    this.descriptionTechnical           = app.descriptionTechnical;
    this.descriptionInstall             = app.descriptionInstall;
    this.downloadIOS                    = app.downloadIOS;
    this.downloadAndroid                = app.downloadAndroid;
    this.fork                           = app.fork;
    this.downloads                      = app.downloads;
  }
  
  protected SSApp(
    final SSUri           id,
    final SSTextComment   descriptionShort,
    final SSTextComment   descriptionFunctional,
    final SSTextComment   descriptionTechnical,
    final SSTextComment   descriptionInstall,
    final SSUri           downloadIOS,
    final SSUri           downloadAndroid,
    final SSUri           fork) throws SSErr{
    
    super(id, SSEntityE.app);
    
    this.descriptionShort               = descriptionShort;
    this.descriptionFunctional          = descriptionFunctional;
    this.descriptionTechnical           = descriptionTechnical;
    this.descriptionInstall             = descriptionInstall;
    this.downloadIOS                    = downloadIOS;
    this.downloadAndroid                = downloadAndroid;
    this.fork                           = fork;
  }
}