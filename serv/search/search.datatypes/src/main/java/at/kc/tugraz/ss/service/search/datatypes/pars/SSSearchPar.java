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

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.service.search.datatypes.SSSearchLabel;
import at.kc.tugraz.ss.service.search.datatypes.SSSearchOpE;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.JsonNode;

public class SSSearchPar extends SSServPar{
  
  public Boolean             includeTextualContent      = null;
  public List<String>        wordsToSearchFor           = new ArrayList<>();
  public Boolean             includeTags                = null;
  public List<String>        tagsToSearchFor            = new ArrayList<>();
  public Boolean             includeLabel               = null;
  public List<SSSearchLabel> labelsToSearchFor          = new ArrayList<>();
  public Boolean             includeDescription         = null;
  public List<SSSearchLabel> descriptionsToSearchFor    = new ArrayList<>();
  public List<SSEntityE>     typesToSearchOnlyFor       = new ArrayList<>();
  public Boolean             includeOnlySubEntities     = null;
  public List<SSUri>         entitiesToSearchWithin     = new ArrayList<>();
  public Boolean             extendToParents            = null;
  public Boolean             includeRecommendedResults  = null;
  public Boolean             provideEntries             = null;
  public String              pagesID                    = null;
  public Integer             pageNumber                 = null;
  public Integer             minRating                  = null;
  public Integer             maxRating                  = null;
  public SSSearchOpE         localSearchOp              = SSSearchOpE.or;
  public SSSearchOpE         globalSearchOp             = SSSearchOpE.or;
  
  public SSSearchPar(
    final SSMethU             op,
    final String              key,
    final SSUri               user,
    final Boolean             includeTextualContent      ,
    final List<String>        wordsToSearchFor           ,
    final Boolean             includeTags                ,
    final List<String>        tagsToSearchFor            ,
    final Boolean             includeLabel               ,
    final List<SSSearchLabel> labelsToSearchFor          ,
    final Boolean             includeDescription         ,
    final List<SSSearchLabel> descriptionsToSearchFor    ,
    final List<SSEntityE>     typesToSearchOnlyFor       ,
    final Boolean             includeOnlySubEntities     ,
    final List<SSUri>         entitiesToSearchWithin     ,
    final Boolean             extendToParents            ,
    final Boolean             includeRecommendedResults  ,
    final Boolean             provideEntries             ,
    final String              pagesID                    ,
    final Integer             pageNumber                 ,
    final Integer             minRating                  ,
    final Integer             maxRating                  ,
    final SSSearchOpE         localSearchOp              ,
    final SSSearchOpE         globalSearchOp             ){
    
    super(op, key, user);
    
    this.includeTextualContent      = includeTextualContent;
    
    if(wordsToSearchFor != null){
      this.wordsToSearchFor.addAll(wordsToSearchFor);
    }
    
    this.includeTags                = includeTags;
    
    if(tagsToSearchFor != null){
      this.tagsToSearchFor.addAll(tagsToSearchFor);
    }
    
    this.includeLabel               = includeLabel;
    
    if(labelsToSearchFor != null){
      this.labelsToSearchFor.addAll(labelsToSearchFor);
    }
    
    this.includeDescription         = includeDescription;
    
    if(descriptionsToSearchFor != null){
      this.descriptionsToSearchFor.addAll(descriptionsToSearchFor);
    }
    
    if(typesToSearchOnlyFor != null){
      this.typesToSearchOnlyFor.addAll(typesToSearchOnlyFor);
    }    
    
    this.includeOnlySubEntities     = includeOnlySubEntities;
    
    if(entitiesToSearchWithin != null){
      this.entitiesToSearchWithin.addAll(entitiesToSearchWithin);
    }    
    
    this.extendToParents            = extendToParents;
    this.includeRecommendedResults  = includeRecommendedResults;
    this.provideEntries             = provideEntries;
    this.pagesID                    = pagesID;
    this.pageNumber                 = pageNumber;
    this.minRating                  = minRating;
    this.maxRating                  = maxRating;
    this.localSearchOp              = localSearchOp;
    this.globalSearchOp             = globalSearchOp;
  }
  
  public SSSearchPar(final SSServPar par) throws Exception{
    
    super(par);
      
    try{
      
      if(pars != null){
        includeTextualContent      = (Boolean)                pars.get(SSVarU.includeTextualContent);
        wordsToSearchFor           = (List<String>)           pars.get(SSVarU.wordsToSearchFor);
        includeTags                = (Boolean)                pars.get(SSVarU.includeTags);
        tagsToSearchFor            = (List<String>)           pars.get(SSVarU.tagsToSearchFor);
        includeLabel               = (Boolean)                pars.get(SSVarU.includeLabel);
        labelsToSearchFor          = SSSearchLabel.get((List<String>)pars.get(SSVarU.labelsToSearchFor));
        includeDescription         = (Boolean)                pars.get(SSVarU.includeDescription);
        descriptionsToSearchFor    = SSSearchLabel.get((List<String>)pars.get(SSVarU.descriptionsToSearchFor));
        typesToSearchOnlyFor       = (List<SSEntityE>)        pars.get(SSVarU.typesToSearchOnlyFor);
        includeOnlySubEntities     = (Boolean)                pars.get(SSVarU.includeOnlySubEntities);
        entitiesToSearchWithin     = (List<SSUri>)            pars.get(SSVarU.entitiesToSearchWithin);
        extendToParents            = (Boolean)                pars.get(SSVarU.extendToParents);
        includeRecommendedResults  = (Boolean)                pars.get(SSVarU.includeRecommendedResults);
        provideEntries             = (Boolean)                pars.get(SSVarU.provideEntries);
        pagesID                    = (String)                 pars.get(SSVarU.pagesID);
        pageNumber                 = (Integer)                pars.get(SSVarU.pageNumber);
        minRating                  = (Integer)                pars.get(SSVarU.minRating);
        maxRating                  = (Integer)                pars.get(SSVarU.maxRating);
        localSearchOp              = (SSSearchOpE)            pars.get(SSVarU.localSearchOp);
      }
      
      if(par.clientJSONObj != null){
        
        try{ includeTextualContent        = par.clientJSONObj.get(SSVarU.includeTextualContent).getBooleanValue();                                                       }catch(Exception error){}
        
        try{ 
          for (final JsonNode objNode : par.clientJSONObj.get(SSVarU.wordsToSearchFor)) {
            wordsToSearchFor.add(objNode.getTextValue());
          }
        }catch(Exception error){}
        
        try{ includeTags                  = par.clientJSONObj.get(SSVarU.includeTags).getBooleanValue();                                                                       }catch(Exception error){}
        
        try{ 
          for (final JsonNode objNode : par.clientJSONObj.get(SSVarU.tagsToSearchFor)) {
            tagsToSearchFor.add(objNode.getTextValue());
          }
        }catch(Exception error){}
        
        try{ includeLabel                 = par.clientJSONObj.get(SSVarU.includeLabel).getBooleanValue();                                                                       }catch(Exception error){}
        
        try{
          for (final JsonNode objNode : par.clientJSONObj.get(SSVarU.labelsToSearchFor)) {
            labelsToSearchFor.add(SSSearchLabel.get(objNode.getTextValue()));
          }
        }catch(Exception error){}
        
        try{ includeDescription           = par.clientJSONObj.get(SSVarU.includeDescription).getBooleanValue();                                                                  }catch(Exception error){}
        
        try{ 
          for (final JsonNode objNode : par.clientJSONObj.get(SSVarU.descriptionsToSearchFor)) {
            descriptionsToSearchFor.add(SSSearchLabel.get(objNode.getTextValue()));
          }
        }catch(Exception error){}
        
        try{ 
          for (final JsonNode objNode : par.clientJSONObj.get(SSVarU.typesToSearchOnlyFor)) {
            typesToSearchOnlyFor.add(SSEntityE.get(objNode.getTextValue()));
          }
        }catch(Exception error){}
        
        try{ includeOnlySubEntities       = par.clientJSONObj.get(SSVarU.includeOnlySubEntities).getBooleanValue();                                                                }catch(Exception error){}
        
        try{
          for (final JsonNode objNode : par.clientJSONObj.get(SSVarU.entitiesToSearchWithin)) {
            entitiesToSearchWithin.add(SSUri.get(objNode.getTextValue()));
          }
        }catch(Exception error){}
        
        try{ extendToParents              = par.clientJSONObj.get(SSVarU.extendToParents).getBooleanValue();                                                                       }catch(Exception error){}
        try{ includeRecommendedResults    = par.clientJSONObj.get(SSVarU.includeRecommendedResults).getBooleanValue();                                                             }catch(Exception error){}
        try{ provideEntries               = par.clientJSONObj.get(SSVarU.provideEntries).getBooleanValue();                                                                        }catch(Exception error){}
        try{ pagesID                      = par.clientJSONObj.get(SSVarU.pagesID).getTextValue();                                                                                  }catch(Exception error){}
        try{ pageNumber                   = par.clientJSONObj.get(SSVarU.pageNumber).getIntValue();                                                                                }catch(Exception error){}
        try{ minRating                    = par.clientJSONObj.get(SSVarU.minRating).getIntValue();                                                                                 }catch(Exception error){}
        try{ maxRating                    = par.clientJSONObj.get(SSVarU.maxRating).getIntValue();                                                                                 }catch(Exception error){}
        try{ localSearchOp                = SSSearchOpE.valueOf(par.clientJSONObj.get(SSVarU.localSearchOp).getTextValue());                                                       }catch(Exception error){}
        try{ globalSearchOp               = SSSearchOpE.valueOf(par.clientJSONObj.get(SSVarU.globalSearchOp).getTextValue());                                                       }catch(Exception error){}
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  /* json getters */
  public List<String> getLabelsToSearchFor() throws Exception{
    return SSStrU.toStr(labelsToSearchFor);
  }
  
  public List<String> getDescriptionsToSearchFor() throws Exception{
    return SSStrU.toStr(descriptionsToSearchFor);
  }
  
  public List<String> getTypesToSearchOnlyFor() throws Exception{
    return SSStrU.toStr(typesToSearchOnlyFor);
  }
  
  public List<String> getEntitiesToSearchWithin() throws Exception{
    return SSStrU.removeTrailingSlash(entitiesToSearchWithin);
  }
  
  public String getLocalSearchOp() throws Exception{
    return SSStrU.toStr(localSearchOp);
  }
  
  public String getGlobalSearchOp() throws Exception{
    return SSStrU.toStr(globalSearchOp);
  }
}
