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


import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.par.SSServPar; 
import at.kc.tugraz.ss.service.search.datatypes.SSSearchLabel;
import at.tugraz.sss.serv.datatype.enums.SSSearchOpE;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SSSearchPar extends SSServPar{
  
  public List<String>        documentContentsToSearchFor                   = new ArrayList<>();
  public List<String>        tagsToSearchFor                               = new ArrayList<>();
  public List<SSUri>         authorsToSearchFor                            = new ArrayList<>();
  public List<SSSearchLabel> labelsToSearchFor                             = new ArrayList<>();
  public List<SSSearchLabel> descriptionsToSearchFor                       = new ArrayList<>();
  public boolean             applyGlobalSearchOpBetweenLabelAndDescription = false;
  public List<SSEntityE>     typesToSearchOnlyFor                          = new ArrayList<>();
  public boolean             includeRecommendedResults                     = false;
  public String              pagesID                                       = null;
  public Integer             pageNumber                                    = null;
  public Integer             pageSize                                      = 10;
  public Integer             minRating                                     = null;
  public Integer             maxRating                                     = null;
  public Long                startTime                                     = null;
  public Long                endTime                                       = null;
  public SSSearchOpE         localSearchOp                                 = SSSearchOpE.or;
  public SSSearchOpE         globalSearchOp                                = SSSearchOpE.or;
  public boolean             orderByLabel                                  = false;
  public boolean             orderByCreationTime                           = false;

  public void setAuthorsToSearchFor(final List<String> authorsToSearchFor) throws SSErr{
    this.authorsToSearchFor = SSUri.get(authorsToSearchFor);
  }
  
  public void setLabelsToSearchFor(final List<String> labelsToSearchFor) throws SSErr{
    this.labelsToSearchFor = SSSearchLabel.get(labelsToSearchFor); 
  }

  public void setDescriptionsToSearchFor(final List<String> descriptionsToSearchFor) throws SSErr{
    this.descriptionsToSearchFor = SSSearchLabel.get(descriptionsToSearchFor);
  }

  public void setTypesToSearchOnlyFor(final List<String> typesToSearchOnlyFor) throws SSErr{
    this.typesToSearchOnlyFor = SSEntityE.get(typesToSearchOnlyFor);
  }

  public void setLocalSearchOp(final String localSearchOp) throws SSErr{
    this.localSearchOp = SSSearchOpE.get(localSearchOp);
  }

  public void setGlobalSearchOp(final String globalSearchOp) throws SSErr{
    this.globalSearchOp = SSSearchOpE.get(globalSearchOp);
  }
  
  public List<String> getAuthorsToSearchFor(){
    return SSStrU.removeTrailingSlash(authorsToSearchFor);
  }
  
  public List<String> getLabelsToSearchFor() throws SSErr{
    return SSStrU.toStr(labelsToSearchFor);
  }
  
  public List<String> getDescriptionsToSearchFor() throws SSErr{
    return SSStrU.toStr(descriptionsToSearchFor);
  }
  
  public List<String> getTypesToSearchOnlyFor() throws SSErr{
    return SSStrU.toStr(typesToSearchOnlyFor);
  }
  
  public String getLocalSearchOp() throws SSErr{
    return SSStrU.toStr(localSearchOp);
  }
  
  public String getGlobalSearchOp() throws SSErr{
    return SSStrU.toStr(globalSearchOp);
  }
  
  public SSSearchPar(){}
  
  public SSSearchPar(
    final SSServPar servPar,
    final SSUri               user,
    final List<String>        documentContentsToSearchFor,
    final List<String>        tagsToSearchFor            ,
    final List<SSUri>         authorsToSearchFor         , 
    final List<SSSearchLabel> labelsToSearchFor          ,
    final List<SSSearchLabel> descriptionsToSearchFor    ,
    final boolean             applyGlobalSearchOpBetweenLabelAndDescription,
    final List<SSEntityE>     typesToSearchOnlyFor       ,
    final boolean             includeRecommendedResults  ,
    final Integer             pageSize,  
    final String              pagesID                    ,
    final Integer             pageNumber                 ,
    final Integer             minRating                  ,
    final Integer             maxRating                  ,
    final Long                startTime,
    final Long                endTime,
    final SSSearchOpE         localSearchOp              ,
    final SSSearchOpE         globalSearchOp             , 
    final boolean             orderByLabel       , 
    final boolean             orderByCreationTime       , 
    final boolean             withUserRestriction, 
    final boolean             invokeEntityHandlers){
    
    super(SSVarNames.search, null, user, servPar.sqlCon);
    
    if(documentContentsToSearchFor != null){
      this.documentContentsToSearchFor.addAll(documentContentsToSearchFor);
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
    
    this.applyGlobalSearchOpBetweenLabelAndDescription = applyGlobalSearchOpBetweenLabelAndDescription;
    
    if(typesToSearchOnlyFor != null){
      this.typesToSearchOnlyFor.addAll(typesToSearchOnlyFor);
    }    
    
    this.includeRecommendedResults  = includeRecommendedResults;
    
    if(pageSize != null){
      this.pageSize                   = pageSize;
    }
    
    this.pagesID                    = pagesID;
    this.pageNumber                 = pageNumber;
    this.minRating                  = minRating;
    this.maxRating                  = maxRating;
    this.startTime                  = startTime;
    this.endTime                    = endTime;
    this.localSearchOp              = localSearchOp;
    this.globalSearchOp             = globalSearchOp;
    this.orderByLabel               = orderByLabel;
    this.orderByCreationTime        = orderByCreationTime;
    this.withUserRestriction        = withUserRestriction;
    this.invokeEntityHandlers       = invokeEntityHandlers;
  }
}