 /**
  * Copyright 2013 Graz University of Technology - KTI (Knowledge Technologies Institute)
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

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.serv.serv.api.SSServConfA;
import at.kc.tugraz.ss.serv.scaff.api.SSScaffClientI;
import at.kc.tugraz.ss.serv.scaff.api.SSScaffServerI;
import at.kc.tugraz.ss.serv.scaff.datatypes.par.SSScaffRecommTagsPar;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.job.recomm.datatypes.SSRecommAlgoE;
import at.kc.tugraz.ss.serv.scaff.datatypes.ret.SSScaffRecommTagsRet;
import at.kc.tugraz.ss.serv.serv.api.SSServImplMiscA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.tag.datatypes.SSTag;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SSScaffImpl extends SSServImplMiscA implements SSScaffClientI, SSScaffServerI{
  
  public SSScaffImpl(final SSServConfA conf) throws Exception{
    super(conf);
  }
  
  /* SSServRegisterableImplI */
  
  @Override
  public List<SSMethU> publishClientOps() throws Exception{
    
    List<SSMethU> clientOps = new ArrayList<SSMethU>();
    
    Method[] methods = SSScaffClientI.class.getMethods();
    
    for(Method method : methods){
      clientOps.add(SSMethU.get(method.getName()));
    }
    
    return clientOps;
  }
  
  @Override
  public List<SSMethU> publishServerOps() throws Exception{
    
    List<SSMethU> serverOps = new ArrayList<SSMethU>();
    
    Method[] methods = SSScaffServerI.class.getMethods();
    
    for(Method method : methods){
      serverOps.add(SSMethU.get(method.getName()));
    }
    
    return serverOps;
  }
  
  @Override
  public void handleClientOp(SSSocketCon sSCon, SSServPar par) throws Exception{
    SSScaffClientI.class.getMethod(SSMethU.toStr(par.op), SSSocketCon.class, SSServPar.class).invoke(this, sSCon, par);
  }
  
  @Override
  public Object handleServerOp(SSServPar par) throws Exception{
    return SSScaffServerI.class.getMethod(SSMethU.toStr(par.op), SSServPar.class).invoke(this, par);
  }
  
  /* SSScaffClientI */
  
  @Override
  public void scaffRecommTags(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSScaffRecommTagsRet.get(scaffRecommTags(parA), parA.op));
  }
  
  /* SSScaffServerI */
  
  @Override
  public List<String> scaffRecommTags(final SSServPar parA) throws Exception{
    
    final SSScaffRecommTagsPar par = new SSScaffRecommTagsPar(parA);
    
    try{
      
      if(par.algo == null){
        par.algo = SSRecommAlgoE.languageModelForTags;
      }
      
      switch(par.algo){
        
        case languageModelForTags:
          return SSTag.toDistinctStringArray(
            SSServCaller.recommTagsLanguageModel(
              par.user,
              par.forUser,
              par.resource,
              par.maxTags));
          
        case baseLevelLearningWithContextForTags:
          return SSTag.toDistinctStringArray(
            SSServCaller.recommTagsBaseLevelLearningWithContext(
              par.user,
              par.forUser,
              par.resource,
              par.maxTags));
          
        case threeLayersForTags:
          return SSTag.toDistinctStringArray(
            SSServCaller.recommTagsThreeLayers(
              par.user,
              par.forUser,
              par.resource,
              par.categories,
              par.maxTags));
          
        case threeLayersWithTimeForTags:
          return SSTag.toDistinctStringArray(
            SSServCaller.recommTagsThreeLayersWithTime(
              par.user,
              par.forUser,
              par.resource,
              par.categories,
              par.maxTags));
          
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
          
        default:
          throw new Exception("recommendation of tags for " + par.algo + " algorithm not available");
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}