/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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

package at.tugraz.sss.servs.video.datatype;

import at.tugraz.sss.servs.util.SSVarNames;
import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSLabel;
import at.tugraz.sss.servs.entity.datatype.SSTextComment;
import at.tugraz.sss.servs.entity.datatype.SSServPar; 
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SSVideoAnnotationsSetPar extends SSServPar{
  
  public SSUri                     video              = null;
  public List<Long>                timePoints         = new ArrayList<>();
  public List<Float>               x                  = new ArrayList<>();
  public List<Float>               y                  = new ArrayList<>();
  public List<SSLabel>             labels             = new ArrayList<>();
  public List<SSTextComment>       descriptions       = new ArrayList<>();
  public boolean                   removeExisting     = false;

  public void setVideo(final String video) throws SSErr{
    this.video = SSUri.get(video);
  }
  
  public void setLabels(final List<String> labels) throws SSErr{
    this.labels = SSLabel.get(labels);
  }
  
  public void setDescriptions(final List<String> descriptions) throws SSErr{
    this.descriptions = SSTextComment.get(descriptions);
  }
  
  public String getVideo(){
    return SSStrU.removeTrailingSlash(video);
  }
  
  public List<String> getLabels(){
    return SSStrU.toStr(labels);
  }
  
  public List<String> getDescriptions(){
    return SSStrU.toStr(descriptions);
  }
  
  public SSVideoAnnotationsSetPar(){/* Do nothing because of only JSON Jackson needs this */ }
    
  public SSVideoAnnotationsSetPar(
    final SSServPar servPar,
    final SSUri                user,
    final SSUri                video,
    final List<Long>           timePoints,
    final List<Float>          x,
    final List<Float>          y,
    final List<SSLabel>        labels,
    final List<SSTextComment>  descriptions, 
    final boolean              removeExisting,
    final boolean              withUserRestriction, 
    final boolean              shouldCommit){
    
    super(SSVarNames.videoAnnotationsSet, null, user, servPar.sqlCon);
    
    this.video               = video;
    
    if(timePoints != null){
      this.timePoints.addAll(timePoints);
    }
    
    if(x != null){
      this.x.addAll(x);
    }
    
    if(y != null){
      this.y.addAll(y);
    }
    
    SSLabel.addDistinctNotNull           (this.labels, labels);
    SSTextComment.addDistinctWithoutNull (this.descriptions, descriptions);
    
    this.removeExisting      = removeExisting;
    this.withUserRestriction = withUserRestriction;
    this.shouldCommit        = shouldCommit;
  }
}