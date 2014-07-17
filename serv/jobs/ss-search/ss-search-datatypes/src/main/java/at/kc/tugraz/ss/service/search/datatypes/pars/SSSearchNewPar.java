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
 package at.kc.tugraz.ss.service.search.datatypes.pars;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.SSTextComment;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import java.util.ArrayList;
import java.util.List;

public class SSSearchNewPar extends SSServPar{
  
  public List<String>        keywordsToSearchFor        = new ArrayList<>();
  public Boolean             includeTextualContent      = null;
  public List<String>        wordsToSearchFor           = new ArrayList<>();
  public Boolean             includeTags                = null;
  public List<String>        tagsToSearchFor            = new ArrayList<>();
  public Boolean             includeMIs                 = null;
  public List<String>        misToSearchFor             = new ArrayList<>();
  public Boolean             includeLabel               = null;
  public List<SSLabel>       labelsToSearchFor          = new ArrayList<>();
  public Boolean             includeDescription         = null;
  public List<SSTextComment> descriptionsToSearchFor    = new ArrayList<>();
  public List<SSEntityE>     typesToSearchOnlyFor       = new ArrayList<>();
  public Boolean             includeOnlySubEntities     = null;
  public List<SSUri>         entitiesToSearchWithin     = new ArrayList<>();
  public Boolean             includeRecommendedResults  = null;
  
  public SSSearchNewPar(SSServPar par) throws Exception{
    
    super(par);
    
    try{
      
      if(pars != null){
        keywordsToSearchFor        = (List<String>)           pars.get(SSVarU.keywordsToSearchFor);
        includeTextualContent      = (Boolean)                pars.get(SSVarU.includeTextualContent);
        wordsToSearchFor           = (List<String>)           pars.get(SSVarU.wordsToSearchFor);
        includeTags                = (Boolean)                pars.get(SSVarU.includeTags);
        tagsToSearchFor            = (List<String>)           pars.get(SSVarU.tagsToSearchFor);
        includeMIs                 = (Boolean)                pars.get(SSVarU.includeMIs);
        misToSearchFor             = (List<String>)           pars.get(SSVarU.misToSearchFor);
        includeLabel               = (Boolean)                pars.get(SSVarU.includeLabel);
        labelsToSearchFor          = (List<SSLabel>)          pars.get(SSVarU.labelsToSearchFor);
        includeDescription         = (Boolean)                pars.get(SSVarU.includeDescription);
        descriptionsToSearchFor    = (List<SSTextComment>)    pars.get(SSVarU.descriptionsToSearchFor);
        typesToSearchOnlyFor       = (List<SSEntityE>)        pars.get(SSVarU.typesToSearchOnlyFor);
        includeOnlySubEntities     = (Boolean)                pars.get(SSVarU.includeOnlySubEntities);
        entitiesToSearchWithin     = (List<SSUri>)            pars.get(SSVarU.entitiesToSearchWithin);
        includeRecommendedResults  = (Boolean)                pars.get(SSVarU.includeRecommendedResults);
      }
      
      if(clientPars != null){
        
        try{
          keywordsToSearchFor          = SSStrU.splitDistinctWithoutEmptyAndNull(clientPars.get(SSVarU.keywordsToSearchFor), SSStrU.comma);
        }catch(Exception error){}
        
        try{
          includeTextualContent        = Boolean.valueOf(clientPars.get(SSVarU.includeTextualContent));
        }catch(Exception error){}
        
        try{
          wordsToSearchFor             = SSStrU.splitDistinctWithoutEmptyAndNull(clientPars.get(SSVarU.wordsToSearchFor), SSStrU.comma);
        }catch(Exception error){}
        
        try{
          includeTags                  = Boolean.valueOf(clientPars.get(SSVarU.includeTags));
        }catch(Exception error){}
        
        try{
          tagsToSearchFor              = SSStrU.splitDistinctWithoutEmptyAndNull(clientPars.get(SSVarU.tagsToSearchFor), SSStrU.comma);
        }catch(Exception error){}
        
        try{
          includeMIs                   = Boolean.valueOf(clientPars.get(SSVarU.includeMIs));
        }catch(Exception error){}
        
        try{
          misToSearchFor               = SSStrU.splitDistinctWithoutEmptyAndNull(clientPars.get(SSVarU.misToSearchFor), SSStrU.comma);
        }catch(Exception error){}
        
        try{
          includeLabel                 = Boolean.valueOf(clientPars.get(SSVarU.includeLabel));
        }catch(Exception error){}
        
        try{
          labelsToSearchFor            = SSLabel.get(SSStrU.splitDistinctWithoutEmptyAndNull(clientPars.get(SSVarU.labelsToSearchFor), SSStrU.comma));
        }catch(Exception error){}
        
        try{
          includeDescription           = Boolean.valueOf(clientPars.get(SSVarU.includeDescription));
        }catch(Exception error){}
        
        try{
          descriptionsToSearchFor      = SSTextComment.get(SSStrU.splitDistinctWithoutEmptyAndNull(clientPars.get(SSVarU.descriptionsToSearchFor), SSStrU.comma));
        }catch(Exception error){}
        
        try{
          typesToSearchOnlyFor         = SSEntityE.get(SSStrU.splitDistinctWithoutEmptyAndNull(clientPars.get(SSVarU.typesToSearchOnlyFor), SSStrU.comma));
        }catch(Exception error){}
        
        try{
          includeOnlySubEntities       = Boolean.valueOf(clientPars.get(SSVarU.includeOnlySubEntities));
        }catch(Exception error){}
        
        try{
          entitiesToSearchWithin       = SSUri.get(SSStrU.splitDistinctWithoutEmptyAndNull(clientPars.get(SSVarU.entitiesToSearchWithin), SSStrU.comma));
        }catch(Exception error){}
        
        try{
          includeRecommendedResults    = Boolean.valueOf(clientPars.get(SSVarU.includeRecommendedResults));
        }catch(Exception error){}
        
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
