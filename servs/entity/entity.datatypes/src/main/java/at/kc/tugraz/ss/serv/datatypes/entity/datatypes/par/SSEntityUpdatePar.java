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
package at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par;

import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSServPar;
import java.util.ArrayList;
import java.util.List;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;

public class SSEntityUpdatePar extends SSServPar{
  
  public SSUri               entity        = null;
  public SSLabel             label         = null;
  public SSTextComment       description   = null;
  public List<SSTextComment> comments      = new ArrayList<>();
  public List<SSUri>         downloads     = new ArrayList<>();
  public List<SSUri>         screenShots   = new ArrayList<>();
  public List<SSUri>         images        = new ArrayList<>();
  public List<SSUri>         videos        = new ArrayList<>();

  public SSEntityUpdatePar(SSServPar par) throws Exception{
      
    super(par);
    
    try{
      
      if(pars != null){
        entity         = (SSUri)               pars.get(SSVarNames.entity);
        label          = (SSLabel)             pars.get(SSVarNames.label);
        description    = (SSTextComment)       pars.get(SSVarNames.description);
        comments       = (List<SSTextComment>) pars.get(SSVarNames.comments);
        downloads      = (List<SSUri>) pars.get(SSVarNames.downloads);
        screenShots    = (List<SSUri>) pars.get(SSVarNames.screenShots);
        images         = (List<SSUri>) pars.get(SSVarNames.images);
        videos         = (List<SSUri>) pars.get(SSVarNames.videos);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(new SSErr(SSErrE.servParCreationFailed));
    }
  }
}