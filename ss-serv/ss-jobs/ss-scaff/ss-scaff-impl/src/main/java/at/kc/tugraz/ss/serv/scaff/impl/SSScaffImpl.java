/**
 * Copyright 2014 Graz University of Technology - KTI (Knowledge Technologies Institute)
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
package at.kc.tugraz.ss.serv.scaff.impl;

import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.serv.serv.api.SSServConfA;
import at.kc.tugraz.ss.serv.scaff.api.SSScaffClientI;
import at.kc.tugraz.ss.serv.scaff.api.SSScaffServerI;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.scaff.datatypes.par.SSScaffRecommTagsBasedOnUserEntityTagCategoryPar;
import at.kc.tugraz.ss.serv.scaff.datatypes.par.SSScaffRecommTagsBasedOnUserEntityTagCategoryTimePar;
import at.kc.tugraz.ss.serv.scaff.datatypes.par.SSScaffRecommTagsBasedOnUserEntityTagPar;
import at.kc.tugraz.ss.serv.scaff.datatypes.par.SSScaffRecommTagsBasedOnUserEntityTagTimePar;
import at.kc.tugraz.ss.serv.scaff.datatypes.ret.SSScaffRecommTagsRet;
import at.kc.tugraz.ss.serv.serv.api.SSServImplMiscA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import java.util.List;

public class SSScaffImpl extends SSServImplMiscA implements SSScaffClientI, SSScaffServerI{
  
  public SSScaffImpl(final SSServConfA conf) throws Exception{
    super(conf);
  }
  
  /* SSScaffClientI */
  
  @Override
  public void scaffRecommTagsBasedOnUserEntityTag(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSScaffRecommTagsRet.get(scaffRecommTagsBasedOnUserEntityTag(parA), parA.op));
  }
  
  @Override
  public void scaffRecommTagsBasedOnUserEntityTagTime(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSScaffRecommTagsRet.get(scaffRecommTagsBasedOnUserEntityTagTime(parA), parA.op));
  }
  
  @Override
  public void scaffRecommTagsBasedOnUserEntityTagCategory(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSScaffRecommTagsRet.get(scaffRecommTagsBasedOnUserEntityTagCategory(parA), parA.op));
  }
  
  @Override
  public void scaffRecommTagsBasedOnUserEntityTagCategoryTime(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSScaffRecommTagsRet.get(scaffRecommTagsBasedOnUserEntityTagCategoryTime(parA), parA.op));
  }
  
  /* SSScaffServerI */
  
  @Override
  public List<String> scaffRecommTagsBasedOnUserEntityTag(final SSServPar parA) throws Exception{
    
    final SSScaffRecommTagsBasedOnUserEntityTagPar par = new SSScaffRecommTagsBasedOnUserEntityTagPar(parA);
    
    return SSServCaller.recommTagsLanguageModelBasedOnUserEntityTag(
      par.user,
      par.forUserUri,
      par.entityUri,
      par.maxTags);
  }
  
  @Override
  public List<String> scaffRecommTagsBasedOnUserEntityTagTime(final SSServPar parA) throws Exception{
    
    final SSScaffRecommTagsBasedOnUserEntityTagTimePar par = new SSScaffRecommTagsBasedOnUserEntityTagTimePar(parA);
    
    return SSServCaller.recommTagsBaseLevelLearningWithContextBasedOnUserEntityTagTimestamp(
      par.user,
      par.forUserUri,
      par.entityUri,
      par.maxTags);
  }
  
  @Override
  public List<String> scaffRecommTagsBasedOnUserEntityTagCategory(final SSServPar parA) throws Exception{
    
    final SSScaffRecommTagsBasedOnUserEntityTagCategoryPar par = new SSScaffRecommTagsBasedOnUserEntityTagCategoryPar(parA);
    
    return SSServCaller.recommTagsThreeLayersBasedOnUserEntityTagCategory(
      par.user,
      par.forUserUri,
      par.entityUri,
      par.categories,
      par.maxTags);
  }
  
  @Override
  public List<String> scaffRecommTagsBasedOnUserEntityTagCategoryTime(final SSServPar parA) throws Exception{
    
    final SSScaffRecommTagsBasedOnUserEntityTagCategoryTimePar par = new SSScaffRecommTagsBasedOnUserEntityTagCategoryTimePar(parA);
    
    return SSServCaller.recommTagsThreeLayersBasedOnUserEntityTagCategoryTimestamp(
      par.user,
      par.forUserUri,
      par.entityUri,
      par.categories,
      par.maxTags);
  }
}


//        case collaborativeFilteringOnEntitySimilarityForTags:
//          return SSTag.toDistinctStringArray(
//            SSServCaller.recommTagsCollaborativeFilteringOnEntitySimilarity(
//              par.user,
//              par.forUser,
//              par.resource,
//              par.maxTags));
//
//        case collaborativeFilteringOnUserSimilarityForTags:
//          return SSTag.toDistinctStringArray(
//            SSServCaller.recommTagsCollaborativeFilteringOnUserSimilarity(
//              par.user,
//              par.forUser,
//              par.resource,
//              par.maxTags));
//
//        case adaptedPageRankForTags:
//          return SSTag.toDistinctStringArray(
//            SSServCaller.recommTagsAdaptedPageRank(
//              par.user,
//              par.forUser,
//              par.resource,
//              par.maxTags));
//
//        case folkRankForTags:
//          return SSTag.toDistinctStringArray(
//            SSServCaller.recommTagsFolkRank(
//              par.user,
//              par.forUser,
//              par.resource,
//              par.maxTags));
//
//        case ldaForTags:
//          return SSTag.toDistinctStringArray(
//            SSServCaller.recommTagsLDA(
//              par.user,
//              par.forUser,
//              par.resource,
//              par.maxTags));
//
//        case temporalUsagePatternsForTags:
//          return SSTag.toDistinctStringArray(
//            SSServCaller.recommTagsTemporalUsagePatterns(
//              par.user,
//              par.forUser,
//              par.resource,
//              par.maxTags));