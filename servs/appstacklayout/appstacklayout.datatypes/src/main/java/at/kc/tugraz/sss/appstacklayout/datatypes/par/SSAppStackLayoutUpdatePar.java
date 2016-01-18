/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
package at.kc.tugraz.sss.appstacklayout.datatypes.par;

import at.tugraz.sss.serv.datatype.SSTextComment;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.par.SSServPar; 
import at.tugraz.sss.serv.util.*;
import java.sql.*;

public class SSAppStackLayoutUpdatePar extends SSServPar{
  
  public SSUri               stack            = null;
  public SSUri               app              = null;
  public SSLabel             label            = null;
  public SSTextComment       description      = null;

  public void setStack(final String stack) throws Exception{
    this.stack = SSUri.get(stack);
  }

  public void setApp(final String app)throws Exception{
    this.app = SSUri.get(app);
  }

  public void setLabel(final String label)throws Exception{
    this.label = SSLabel.get(label);
  }

  public void setDescription(final String description)throws Exception{
    this.description = SSTextComment.get(description);
  }
  
  public String getStack(){
    return SSStrU.removeTrailingSlash(stack);
  }
  
  public String getApp(){
    return SSStrU.removeTrailingSlash(app);
  }
  
  public String getLabel(){
    return SSStrU.toStr(label);
  }

  public String getDescription(){
    return SSStrU.toStr(description);
  }
  
  public SSAppStackLayoutUpdatePar(){}
  
  public SSAppStackLayoutUpdatePar(
    final SSServPar servPar,
    final SSUri          user,
    final SSUri          stack,
    final SSUri          app,
    final SSLabel        label,
    final SSTextComment  description, 
    final boolean        withUserDescription, 
    final boolean        shouldCommit){
    
    super(SSVarNames.appStackLayoutUpdate, null, user, servPar.sqlCon);
    
    this.stack               = stack;
    this.app                 = app;
    this.label               = label;
    this.description         = description;
    this.withUserRestriction = withUserDescription;
    this.shouldCommit        = shouldCommit;
  }
}