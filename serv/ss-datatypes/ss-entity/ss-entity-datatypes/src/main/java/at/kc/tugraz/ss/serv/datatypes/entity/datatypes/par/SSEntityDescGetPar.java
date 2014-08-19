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

import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;

public class SSEntityDescGetPar extends SSServPar{
  
  public SSUri    entity            = null;
  public Boolean  getTags           = false;
  public Boolean  getOverallRating  = false;
  public Boolean  getDiscs          = false;
  public Boolean  getUEs            = false;
  public Boolean  getThumb          = false;
  public Boolean  getFlags          = false;
  
  public static SSEntityDescGetPar get(
    final SSServPar      par,
    final SSUri          entity,
    final Boolean        getTags,
    final Boolean        getOverallRating,
    final Boolean        getDiscs,
    final Boolean        getUEs,
    final Boolean        getThumb,
    final Boolean        getFlags) throws Exception{
    
    return new SSEntityDescGetPar(par, entity, getTags, getOverallRating, getDiscs, getUEs, getThumb, getFlags);
  }
  
  private SSEntityDescGetPar(
    final SSServPar      par,
    final SSUri          entity,
    final Boolean        getTags,
    final Boolean        getOverallRating,
    final Boolean        getDiscs,
    final Boolean        getUEs,
    final Boolean        getThumb,
    final Boolean        getFlags) throws Exception{
    
    super(par);
    
    this.getTags = getTags;
    this.getOverallRating = getOverallRating;
    this.getDiscs = getDiscs;
    this.getUEs = getUEs;
    this.getThumb = getThumb;
    this.getFlags = getFlags;
    this.entity = entity;
  }
  
  public SSEntityDescGetPar(SSServPar par) throws Exception{
    
    super(par);
    
    try{
      
      if(pars != null){
        entity           = (SSUri)   pars.get(SSVarU.entity);
        getTags          = (Boolean) pars.get(SSVarU.getTags);
        getOverallRating = (Boolean) pars.get(SSVarU.getOverallRating);
        getDiscs         = (Boolean) pars.get(SSVarU.getDiscs);
        getUEs           = (Boolean) pars.get(SSVarU.getUEs);
        getThumb         = (Boolean) pars.get(SSVarU.getThumb);
        getFlags         = (Boolean) pars.get(SSVarU.getFlags);
      }
      
      if(clientPars != null){
        entity          = SSUri.get        (clientPars.get(SSVarU.entity));
        
        try{
          getTags            = Boolean.valueOf  (clientPars.get(SSVarU.getTags));
        }catch(Exception error){}
        
        try{
          getOverallRating   = Boolean.valueOf  (clientPars.get(SSVarU.getOverallRating));
        }catch(Exception error){}
        
        try{
          getDiscs        = Boolean.valueOf  (clientPars.get(SSVarU.getDiscs));
        }catch(Exception error){}
        
        try{
          getUEs        = Boolean.valueOf  (clientPars.get(SSVarU.getUEs));
        }catch(Exception error){}
        
        try{
          getThumb        = Boolean.valueOf  (clientPars.get(SSVarU.getThumb));
        }catch(Exception error){}
        
        try{
          getFlags        = Boolean.valueOf  (clientPars.get(SSVarU.getFlags));
        }catch(Exception error){}
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}