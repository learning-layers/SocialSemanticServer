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
package at.kc.tugraz.ss.service.search.datatypes.pars;

import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSServPar;
import at.kc.tugraz.ss.service.search.datatypes.SSSearchLabel;
import at.kc.tugraz.ss.service.search.datatypes.SSSearchOpE;
import java.util.ArrayList;
import java.util.List;

public class SSSearchPar extends SSServPar{
  
  public List<String>        wordsToSearchFor           = new ArrayList<>();
  public List<String>        tagsToSearchFor            = new ArrayList<>();
  public List<SSUri>         authorsToSearchFor         = new ArrayList<>();
  public List<SSSearchLabel> labelsToSearchFor          = new ArrayList<>();
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
  public Boolean             invokeEntityHandlers       = false;

  public void setAuthorsToSearchFor(final List<String> authorsToSearchFor) throws Exception{
    this.authorsToSearchFor = SSUri.get(authorsToSearchFor);
  }
  
  public void setLabelsToSearchFor(final List<String> labelsToSearchFor){
    try{ this.labelsToSearchFor = SSSearchLabel.get(labelsToSearchFor); }catch(Exception error){}
  }

  public void setDescriptionsToSearchFor(final List<String> descriptionsToSearchFor){
    try{ this.descriptionsToSearchFor = SSSearchLabel.get(descriptionsToSearchFor); }catch(Exception error){}
  }

  public void setTypesToSearchOnlyFor(final List<String> typesToSearchOnlyFor){
    try{ this.typesToSearchOnlyFor = SSEntityE.get(typesToSearchOnlyFor); }catch(Exception error){}
  }

  public void setEntitiesToSearchWithin(final List<String> entitiesToSearchWithin){
    try{ this.entitiesToSearchWithin = SSUri.get(entitiesToSearchWithin); }catch(Exception error){}
  }

  public void setLocalSearchOp(final String localSearchOp){
    try{ this.localSearchOp = SSSearchOpE.get(localSearchOp); }catch(Exception error){}
  }

  public void setGlobalSearchOp(final String globalSearchOp){
    try{ this.globalSearchOp = SSSearchOpE.get(globalSearchOp); }catch(Exception error){}
  }
  
  public List<String> getAuthorsToSearchFor(){
    return SSStrU.removeTrailingSlash(authorsToSearchFor);
  }
  
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
  
  public SSSearchPar(){}
  
  public SSSearchPar(
    final SSServOpE           op,
    final String              key,
    final SSUri               user,
    final List<String>        wordsToSearchFor           ,
    final List<String>        tagsToSearchFor            ,
    final List<SSUri>         authorsToSearchFor         , 
    final List<SSSearchLabel> labelsToSearchFor          ,
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
    final SSSearchOpE         globalSearchOp             , 
    final Boolean             withUserRestriction, 
    final Boolean             invokeEntityHandlers){
    
    super(op, key, user);
    
    if(wordsToSearchFor != null){
      this.wordsToSearchFor.addAll(wordsToSearchFor);
    }
    
    if(tagsToSearchFor != null){
      this.tagsToSearchFor.addAll(tagsToSearchFor);
    }
    
    if(authorsToSearchFor != null){
      this.authorsToSearchFor.addAll(authorsToSearchFor);
    }
    
    if(labelsToSearchFor != null){
      this.labelsToSearchFor.addAll(labelsToSearchFor);
    }
    
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
    this.withUserRestriction        = withUserRestriction;
    this.invokeEntityHandlers       = invokeEntityHandlers;
  }
}