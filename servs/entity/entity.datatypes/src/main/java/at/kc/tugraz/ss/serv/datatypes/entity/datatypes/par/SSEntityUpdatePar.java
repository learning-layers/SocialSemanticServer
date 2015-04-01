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

import at.tugraz.sss.serv.SSVarU;
import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServErrReg;
import java.util.ArrayList;
import java.util.List;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;

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
        entity         = (SSUri)               pars.get(SSVarU.entity);
        label          = (SSLabel)             pars.get(SSVarU.label);
        description    = (SSTextComment)       pars.get(SSVarU.description);
        comments       = (List<SSTextComment>) pars.get(SSVarU.comments);
        downloads      = (List<SSUri>) pars.get(SSVarU.downloads);
        screenShots    = (List<SSUri>) pars.get(SSVarU.screenShots);
        images         = (List<SSUri>) pars.get(SSVarU.images);
        videos         = (List<SSUri>) pars.get(SSVarU.videos);
      }
    }catch(Exception error){
      throw new SSErr(SSErrE.servParCreationFailed);
    }
  }
}