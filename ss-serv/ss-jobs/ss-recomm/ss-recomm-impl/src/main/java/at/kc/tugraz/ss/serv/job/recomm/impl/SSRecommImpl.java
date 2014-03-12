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
package at.kc.tugraz.ss.serv.job.recomm.impl;

import at.kc.tugraz.socialserver.utils.SSFileExtU;
import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.serv.serv.api.SSServConfA;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.job.recomm.api.SSRecommClientI;
import at.kc.tugraz.ss.serv.job.recomm.api.SSRecommServerI;
import at.kc.tugraz.ss.serv.job.recomm.conf.SSRecommConf;
import at.kc.tugraz.ss.serv.job.recomm.datatypes.par.SSRecommBaseLevelLearningWithContextUpdatePar;
import at.kc.tugraz.ss.serv.job.recomm.datatypes.par.SSRecommLanguageModelUpdatePar;
import at.kc.tugraz.ss.serv.job.recomm.datatypes.par.SSRecommTagsAdaptedPageRankPar;
import at.kc.tugraz.ss.serv.job.recomm.datatypes.par.SSRecommTagsBaseLevelLearningWithContextPar;
import at.kc.tugraz.ss.serv.job.recomm.datatypes.par.SSRecommTagsCollaborativeFilteringOnEntitySimilarityPar;
import at.kc.tugraz.ss.serv.job.recomm.datatypes.par.SSRecommTagsCollaborativeFilteringOnUserSimilarityPar;
import at.kc.tugraz.ss.serv.job.recomm.datatypes.par.SSRecommTagsFolkRankPar;
import at.kc.tugraz.ss.serv.job.recomm.datatypes.par.SSRecommTagsLDAPar;
import at.kc.tugraz.ss.serv.job.recomm.datatypes.par.SSRecommTagsLanguageModelPar;
import at.kc.tugraz.ss.serv.job.recomm.datatypes.par.SSRecommTagsTemporalUsagePatternsPar;
import at.kc.tugraz.ss.serv.job.recomm.datatypes.par.SSRecommTagsThreeLayersPar;
import at.kc.tugraz.ss.serv.job.recomm.datatypes.par.SSRecommTagsThreeLayersWithTimePar;
import at.kc.tugraz.ss.serv.job.recomm.datatypes.par.SSRecommThreeLayersUpdatePar;
import at.kc.tugraz.ss.serv.job.recomm.impl.engine.BaseLevelLearningEngine;
import at.kc.tugraz.ss.serv.job.recomm.impl.engine.LanguageModelEngine;
import at.kc.tugraz.ss.serv.job.recomm.impl.engine.ThreeLayersEngine;
import at.kc.tugraz.ss.serv.job.recomm.impl.fct.misc.SSRecommFct;
import at.kc.tugraz.ss.serv.serv.api.SSServImplMiscA;
import at.kc.tugraz.ss.service.tag.datatypes.SSTag;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SSRecommImpl extends SSServImplMiscA implements SSRecommClientI, SSRecommServerI{
  
  private static final BaseLevelLearningEngine baseLevelLearningEngine = new BaseLevelLearningEngine();
  private static final LanguageModelEngine     languageModelEngine     = new LanguageModelEngine();
  private static final ThreeLayersEngine       threeLayersEngine       = new ThreeLayersEngine();
  
  public SSRecommImpl(final SSServConfA conf) throws Exception{
    super(conf);
  }
  
  /* SSServRegisterableImplI */
  
  @Override
  public List<SSMethU> publishClientOps() throws Exception{
    
    List<SSMethU> clientOps = new ArrayList<SSMethU>();
    
    Method[] methods = SSRecommClientI.class.getMethods();
    
    for(Method method : methods){
      clientOps.add(SSMethU.get(method.getName()));
    }
    
    return clientOps;
  }
  
  @Override
  public List<SSMethU> publishServerOps() throws Exception{
    
    List<SSMethU> serverOps = new ArrayList<SSMethU>();
    
    Method[] methods = SSRecommServerI.class.getMethods();
    
    for(Method method : methods){
      serverOps.add(SSMethU.get(method.getName()));
    }
    
    return serverOps;
  }
  
  @Override
  public void handleClientOp(SSSocketCon sSCon, SSServPar par) throws Exception{
    SSRecommClientI.class.getMethod(SSMethU.toStr(par.op), SSSocketCon.class, SSServPar.class).invoke(this, sSCon, par);
  }
  
  @Override
  public Object handleServerOp(SSServPar par) throws Exception{
    return SSRecommServerI.class.getMethod(SSMethU.toStr(par.op), SSServPar.class).invoke(this, par);
  }
  
  /* SSRecommServerI */
  @Override
  public void recommBaseLevelLearningWithContextUpdate(final SSServPar parA) throws Exception{
    
    final SSRecommBaseLevelLearningWithContextUpdatePar par        = new SSRecommBaseLevelLearningWithContextUpdatePar(parA);
    final SSRecommConf                                  recommConf = (SSRecommConf) conf;
    
    try{
      
      if(!SSRecommFct.exportEntityTagTimestampCategoryCombinationsForAllUsers(recommConf.fileNameForOpRecommTagsBaseLevelLearningWithContext)){
        return;
      }

      try{
        
        baseLevelLearningEngine.loadFile(
          SSStrU.removeTrailingString(
            recommConf.fileNameForOpRecommTagsBaseLevelLearningWithContext,
            SSStrU.dot + SSFileExtU.txt));
        
      }catch(FileNotFoundException errFileNotFound){
        SSLogU.warn("file not found for recommBaseLevelLearningWithContextUpdate");
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public void recommLanguageModelUpdate(final SSServPar parA) throws Exception{
    
    final SSRecommLanguageModelUpdatePar     par        = new SSRecommLanguageModelUpdatePar(parA);
    final SSRecommConf                       recommConf = (SSRecommConf) conf;
    
    try{
      
      if(!SSRecommFct.exportEntityTagTimestampCategoryCombinationsForAllUsers(recommConf.fileNameForOpRecommTagsLanguageModel)){
        return;
      }

      try{
        
        languageModelEngine.loadFile(
          SSStrU.removeTrailingString(
            recommConf.fileNameForOpRecommTagsLanguageModel,
            SSStrU.dot + SSFileExtU.txt));
        
      }catch(FileNotFoundException errFileNotFound){
        SSLogU.warn("file not found for recommLanguageModelUpdate");
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public void recommThreeLayersUpdate(final SSServPar parA) throws Exception{
    
    final SSRecommThreeLayersUpdatePar     par        = new SSRecommThreeLayersUpdatePar(parA);
    final SSRecommConf                     recommConf = (SSRecommConf) conf;
    
    try{
      
      if(!SSRecommFct.exportEntityTagTimestampCategoryCombinationsForAllUsers(recommConf.fileNameForOpRecommTagsThreeLayers)){
        return;
      }

      try{
        
        threeLayersEngine.loadFile(
          SSStrU.removeTrailingString(
            recommConf.fileNameForOpRecommTagsThreeLayers,
            SSStrU.dot + SSFileExtU.txt));
        
      }catch(FileNotFoundException errFileNotFound){
        SSLogU.warn("file not found for recommThreeLayersUpdate");
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public List<SSTag> recommTagsBaseLevelLearningWithContext(final SSServPar parA) throws Exception{
    
    final SSRecommTagsBaseLevelLearningWithContextPar par = new SSRecommTagsBaseLevelLearningWithContextPar(parA);
    
    try{
      
      final List<SSTag> tags = new ArrayList<SSTag>();
      
      tags.addAll(
        SSTag.getDistinct(
          baseLevelLearningEngine.getTags(
            SSUri.toStr(par.forUser),
            SSUri.toStr(par.entityUri),
            par.maxTags)));
      
      SSLogU.info("base level learning with context recommends tags: " + tags.toString());
      
      return tags;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSTag> recommTagsLanguageModel(final SSServPar parA) throws Exception{
    
    final SSRecommTagsLanguageModelPar par = new SSRecommTagsLanguageModelPar(parA);
    
    try{
      final List<SSTag> tags = new ArrayList<SSTag>();
      
      tags.addAll(
        SSTag.getDistinct(
          languageModelEngine.getTags(
            SSUri.toStr (par.forUser),
            SSUri.toStr (par.entityUri),
            par.maxTags)));
      
      SSLogU.info("language model recommends tags: " + tags.toString());
      
      return tags;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSTag> recommTagsThreeLayers(final SSServPar parA) throws Exception{
    
    final SSRecommTagsThreeLayersPar par = new SSRecommTagsThreeLayersPar(parA);
    
    try{
      final List<SSTag> tags = new ArrayList<SSTag>();
      
      tags.addAll(
        SSTag.getDistinct(
          threeLayersEngine.getTags(
            SSUri.toStr (par.forUser),
            SSUri.toStr (par.entityUri),
            par.categories,
            par.maxTags,
            false)));
      
      SSLogU.info("three layers recommends tags: " + tags.toString());
      
      return tags;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSTag> recommTagsThreeLayersWithTime(final SSServPar parA) throws Exception{
    
    final SSRecommTagsThreeLayersWithTimePar par = new SSRecommTagsThreeLayersWithTimePar(parA);
    
    try{
      final List<SSTag> tags = new ArrayList<SSTag>();
      
      tags.addAll(
        SSTag.getDistinct(
          threeLayersEngine.getTags(
            SSUri.toStr (par.forUser),
            SSUri.toStr (par.entityUri),
            par.categories,
            par.maxTags,
            true)));
      
      SSLogU.info("three layers with time recommends tags: " + tags.toString());
      
      return tags;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSTag> recommTagsCollaborativeFilteringOnEntitySimilarity(final SSServPar parA) throws Exception{
    
    final SSRecommTagsCollaborativeFilteringOnEntitySimilarityPar par = new SSRecommTagsCollaborativeFilteringOnEntitySimilarityPar(parA);
    
    throw new UnsupportedOperationException();
  }
  
  @Override 
  public List<SSTag> recommTagsCollaborativeFilteringOnUserSimilarity(final SSServPar parA) throws Exception{
    
    final SSRecommTagsCollaborativeFilteringOnUserSimilarityPar par = new SSRecommTagsCollaborativeFilteringOnUserSimilarityPar(parA);
    
    throw new UnsupportedOperationException();
  }
   
  @Override 
  public List<SSTag> recommTagsAdaptedPageRank(final SSServPar parA) throws Exception{
    
    final SSRecommTagsAdaptedPageRankPar par = new SSRecommTagsAdaptedPageRankPar(parA);
    
    throw new UnsupportedOperationException();
  }
    
  @Override 
  public List<SSTag> recommTagsFolkRank(final SSServPar parA) throws Exception{
    
    final SSRecommTagsFolkRankPar par = new SSRecommTagsFolkRankPar(parA);
    
    throw new UnsupportedOperationException();
  }
  
  @Override 
  public List<SSTag> recommTagsLDA(final SSServPar parA) throws Exception{
    
    final SSRecommTagsLDAPar par = new SSRecommTagsLDAPar(parA);
    
    throw new UnsupportedOperationException();
  }
  
  @Override 
  public List<SSTag> recommTagsTemporalUsagePatterns(final SSServPar parA) throws Exception{
    
    final SSRecommTagsTemporalUsagePatternsPar par = new SSRecommTagsTemporalUsagePatternsPar(parA);
    
    throw new UnsupportedOperationException();
  }
  
  
  
  //  @Override
  //  public void recommWriteMetricsMulan(SSServPar parA) throws Exception{
  //
  //    SSRecommWriteMetricsMulanPar par = new SSRecommWriteMetricsMulanPar(parA);
  //
  //		for (int counter = 1; counter <= par.k; counter++) {
  //
  //      for (int innerCounter = 1; innerCounter <= par.sampleCount; innerCounter++) {
  //				MetricsCalculator.calculateMetrics(par.sampleName + SSStrU.underline + innerCounter + "_res_switch", counter, null, false);
  //			}
  //
  //      MetricsCalculator.writeAverageMetrics(par.sampleDir + "/mulan_metrics", counter, (double)par.sampleCount);
  //		}
  //
  //		for (int counter = 1; counter <= par.sampleCount; counter++) {
  //			for (int innerCounter = 1; innerCounter <= par.k; innerCounter++) {
  //
  //        MetricsCalculator.calculateMetrics(par.sampleName + SSStrU.underline + counter + "_res_switch", innerCounter, par.sampleDir + "/mulan_metrics", innerCounter == par.k);
  //			}
  //		}
  //
  //		MetricsCalculator.resetMetrics();
  //	}
  
  
  //  //dkowald: CHECK LOOP-ORDER !!!
  //  @Override
  //	public void recommCalcAct(SSServPar parA) throws Exception{
  //
  //    SSRecommCalcActPar par = new SSRecommCalcActPar(parA);
  //    List<Integer>      dValues;
  //    List<Integer>      betaValues;
  //
  //    opPars = new HashMap<String, Object>();
  //    opPars.put(SSVarU.sample, par.sampleName + SSStrU.underline + 1);
  //
  //    recommTrainTestSize(new SSServPar(SSMethU.recommTrainTestSize, opPars));
  //
  //    opPars = new HashMap<String, Object>();
  //    opPars.put(SSVarU.betaUpperBound, par.dUpperBound);
  //
  //    dValues   = recommBetaValues(new SSServPar(SSMethU.recommBetaValues, opPars));
  //
  //    opPars = new HashMap<String, Object>();
  //    opPars.put(SSVarU.betaUpperBound, par.betaUpperBound);
  //
  //    betaValues = recommBetaValues(new SSServPar(SSMethU.recommBetaValues, opPars));
  //
  //    opPars = new HashMap<String, Object>();
  //    opPars.put(SSVarU.sampleDir,    par.sampleDir);
  //    opPars.put(SSVarU.sampleName,   par.sampleName);
  //    opPars.put(SSVarU.sampleCount,  par.sampleCount);
  //    opPars.put(SSVarU.k,            10);
  //    opPars.put(SSVarU.posfix,       null);
  //
  //    for (int couner = 1; couner <= par.sampleCount; couner++){
  //      for (int dVal : dValues) {
  //        for (int betaVal : betaValues) {
  //
  //          ActCalculator.predictSample(par.sampleName + "_" + couner, trainSize, testSize, true, true, dVal, betaVal);
  //
  //          opPars.put(SSVarU.prefix, "act_" + betaVal + SSStrU.underline + dVal);
  //
  //          recommWriteMetrics(new SSServPar(SSMethU.recommWriteMetrics, opPars));
  //        }
  //
  //        ActCalculator.predictSample(par.sampleName + SSStrU.underline + couner, trainSize, testSize, true, false, dVal, 5);
  //
  //        opPars.put(SSVarU.prefix, "user_act_" + 5 + SSStrU.underline + dVal);
  //
  //        recommWriteMetrics(new SSServPar(SSMethU.recommWriteMetrics, opPars));
  //
  //        ActCalculator.predictSample(par.sampleName + SSStrU.underline + couner, trainSize, testSize, false, true, dVal, 5);
  //
  //        opPars.put(SSVarU.prefix, "res_act_" + 5 + SSStrU.underline + dVal);
  //
  //        recommWriteMetrics(new SSServPar(SSMethU.recommWriteMetrics, opPars));
  //      }
  //    }
  //  }
  
  //  @Override
  //  public void recommCalcLanguageModel(SSServPar parA) throws Exception{
  //
  //    SSRecommCalcLanguageModelPar par = new SSRecommCalcLanguageModelPar(parA);
  //    List<Integer>                betaValues;
  //
  //    opPars = new HashMap<String, Object>();
  //    opPars.put(SSVarU.sample, par.sampleName + SSStrU.underline + 1);
  //
  //    recommTrainTestSize(new SSServPar(SSMethU.recommTrainTestSize, opPars));
  //
  //    opPars = new HashMap<String, Object>();
  //    opPars.put(SSVarU.betaUpperBound, par.betaUpperBound);
  //
  //    betaValues = recommBetaValues(new SSServPar(SSMethU.recommBetaValues, opPars));
  //
  //    opPars = new HashMap<String, Object>();
  //    opPars.put(SSVarU.sampleDir,    par.sampleDir);
  //    opPars.put(SSVarU.sampleName,   par.sampleName);
  //    opPars.put(SSVarU.sampleCount,  par.sampleCount);
  //    opPars.put(SSVarU.k,            10);
  //    opPars.put(SSVarU.posfix,       null);
  //
  //		for (int beta : betaValues) {
  //
  //      for (int i = 1; i <= par.sampleCount; i++) {
  //        LanguageModelCalculator.predictSample(par.sampleName + SSStrU.underline + i, trainSize, testSize, true, true, beta);
  //			}
  //
  //      opPars.put(SSVarU.prefix, "model_" + beta);
  //
  //      recommWriteMetrics(new SSServPar(SSMethU.recommWriteMetrics, opPars));
  //		}
  //
  //		for (int i = 1; i <= par.sampleCount; i++) {
  //			LanguageModelCalculator.predictSample(par.sampleName + SSStrU.underline + i, trainSize, testSize, true, false, 5);
  //			LanguageModelCalculator.predictSample(par.sampleName + SSStrU.underline + i, trainSize, testSize, false, true, 5);
  //		}
  //
  //    opPars.put(SSVarU.prefix, "user_model_" + 5);
  //
  //    recommWriteMetrics(new SSServPar(SSMethU.recommWriteMetrics, opPars));
  //
  //    opPars.put(SSVarU.prefix, "res_model_" + 5);
  //
  //    recommWriteMetrics(new SSServPar(SSMethU.recommWriteMetrics, opPars));
  //	}
  
  //  @Override
  //  public void recommCalcCFTag(SSServPar parA) throws Exception{
  //
  //    SSRecommCalcCFTagPar par = new SSRecommCalcCFTagPar(parA);
  //    List<Integer>        betaValues;
  //
  //    opPars = new HashMap<String, Object>();
  //    opPars.put(SSVarU.sample, par.sampleName + SSStrU.underline + 1);
  //
  //    recommTrainTestSize(new SSServPar(SSMethU.recommTrainTestSize, opPars));
  //
  //    opPars = new HashMap<String, Object>();
  //    opPars.put(SSVarU.betaUpperBound, par.betaUpperBound);
  //
  //    betaValues = recommBetaValues(new SSServPar(SSMethU.recommBetaValues, opPars));
  //
  //    opPars = new HashMap<String, Object>();
  //    opPars.put(SSVarU.sampleDir,    par.sampleDir);
  //    opPars.put(SSVarU.sampleName,   par.sampleName);
  //    opPars.put(SSVarU.sampleCount,  par.sampleCount);
  //    opPars.put(SSVarU.k,            10);
  //    opPars.put(SSVarU.posfix,       null);
  //
  //    for (int beta : betaValues) {
  //
  //      for (int counter = 1; counter <= par.sampleCount; counter++) {
  //        BM25Calculator.predictTags(par.sampleName + SSStrU.underline + counter, trainSize, testSize, par.neighbors, true, true, beta);
  //      }
  //
  //      opPars.put(SSVarU.prefix, "cf_" + beta);
  //
  //      recommWriteMetrics(new SSServPar(SSMethU.recommWriteMetrics, opPars));
  //    }
  //
  //    for (int i = 1; i <= par.sampleCount; i++) {
  //      BM25Calculator.predictTags(par.sampleName + SSStrU.underline + i, trainSize, testSize, par.neighbors, true, false, 5);
  //      BM25Calculator.predictTags(par.sampleName + SSStrU.underline + i, trainSize, testSize, par.neighbors, false, true, 5);
  //    }
  //
  //    opPars.put(SSVarU.prefix, "usercf_" + 5);
  //
  //    recommWriteMetrics(new SSServPar(SSMethU.recommWriteMetrics, opPars));
  //
  //    opPars.put(SSVarU.prefix, "rescf_" + 5);
  //
  //    recommWriteMetrics(new SSServPar(SSMethU.recommWriteMetrics, opPars));
  //  }
  
  //  @Override
  //  public void recommCalcFolkRank(SSServPar parA) throws Exception{
  //
  //    SSRecommCalcFolkRankPar par = new SSRecommCalcFolkRankPar(parA);
  //
  //    opPars = new HashMap<String, Object>();
  //    opPars.put(SSVarU.sample, par.sampleName + SSStrU.underline + 1);
  //
  //    recommTrainTestSize(new SSServPar(SSMethU.recommTrainTestSize, opPars));
  //
  //    for (int counter = 1; counter <= par.sampleCount; counter++) {
  //      FolkRankCalculator.predictSample(par.sampleName + SSStrU.underline + counter, trainSize, testSize, true);
  //    }
  //
  //    opPars = new HashMap<String, Object>();
  //    opPars.put(SSVarU.sampleDir,    par.sampleDir);
  //    opPars.put(SSVarU.sampleName,   par.sampleName);
  //    opPars.put(SSVarU.sampleCount,  par.sampleCount);
  //    opPars.put(SSVarU.k,            10);
  //    opPars.put(SSVarU.posfix,       null);
  //
  //    opPars.put(SSVarU.prefix, "fr");
  //
  //    recommWriteMetrics(new SSServPar(SSMethU.recommWriteMetrics, opPars));
  //
  //    opPars.put(SSVarU.prefix, "pr");
  //
  //    recommWriteMetrics(new SSServPar(SSMethU.recommWriteMetrics, opPars));
  //	}
  
  //  @Override
  //  public void recommCalcBaseline(SSServPar parA) throws Exception{
  //
  //    SSRecommCalcBaselinePar par = new SSRecommCalcBaselinePar(parA);
  //
  //    opPars = new HashMap<String, Object>();
  //    opPars.put(SSVarU.sample, par.sampleName + SSStrU.underline + 1);
  //
  //    recommTrainTestSize(new SSServPar(SSMethU.recommTrainTestSize, opPars));
  //
  //    for (int counter = 1; counter <= par.size; counter++) {
  //      BaselineCalculator.predictPopularTags(par.sampleName + SSStrU.underline + counter, testSize);
  //    }
  //
  //    opPars = new HashMap<String, Object>();
  //    opPars.put(SSVarU.sampleDir,    par.sampleDir);
  //    opPars.put(SSVarU.sampleName,   par.sampleName);
  //    opPars.put(SSVarU.sampleCount,  par.size);
  //    opPars.put(SSVarU.k,            10);
  //    opPars.put(SSVarU.posfix,       null);
  //
  //    opPars.put(SSVarU.prefix, "pop");
  //
  //    recommWriteMetrics(new SSServPar(SSMethU.recommWriteMetrics, opPars));
  //  }
  
  //  @Override
  //  public void recommCalcBllCF(SSServPar parA) throws Exception{
  //
  //    SSRecommCalcBLLCFPar par = new SSRecommCalcBLLCFPar(parA);
  //
  //		opPars = new HashMap<String, Object>();
  //    opPars.put(SSVarU.sample, par.sampleName + SSStrU.underline + 1);
  //
  //    recommTrainTestSize(new SSServPar(SSMethU.recommTrainTestSize, opPars));
  //
  //		for (int counter = 1; counter <= par.sampleCount; counter++) {
  //			   BllCfCalculator.predictSample(par.sampleName + SSStrU.underline + counter, trainSize, testSize, par.neighbors, par.dVal);
  //		}
  //
  //    opPars = new HashMap<String, Object>();
  //    opPars.put(SSVarU.sampleDir,    par.sampleDir);
  //    opPars.put(SSVarU.sampleName,   par.sampleName);
  //    opPars.put(SSVarU.sampleCount,  par.sampleCount);
  //    opPars.put(SSVarU.k,            10);
  //    opPars.put(SSVarU.posfix,       null);
  //
  //    opPars.put(SSVarU.prefix, "bllcf");
  //
  //    recommWriteMetrics(new SSServPar(SSMethU.recommWriteMetrics, opPars));
  //	}
  
  //  @Override
  //  public void recommCalcLDAModel(SSServPar parA) throws Exception{
  //
  //    SSRecommCalcLDAModelPar par = new SSRecommCalcLDAModelPar(parA);
  //    List<Integer>           betaValues;
  //
  //		opPars = new HashMap<String, Object>();
  //    opPars.put(SSVarU.sample, par.sampleName + SSStrU.underline + 1);
  //
  //    recommTrainTestSize(new SSServPar(SSMethU.recommTrainTestSize, opPars));
  //
  //    opPars = new HashMap<String, Object>();
  //    opPars.put(SSVarU.betaUpperBound, par.betaUpperBound);
  //
  //    betaValues   = recommBetaValues(new SSServPar(SSMethU.recommBetaValues, opPars));
  //
  //    for (int beta : betaValues) {
  //
  //      for (int counter = 1; counter <= par.sampleCount; counter++) {
  //        LanguageModelCalculator.predictModelLDASample(par.sampleName + SSStrU.underline + counter, trainSize, testSize, (short)par.topics.intValue(), beta, par.modelBeta);
  //      }
  //
  //      opPars = new HashMap<String, Object>();
  //      opPars.put(SSVarU.sampleDir,    par.sampleDir);
  //      opPars.put(SSVarU.sampleName,   par.sampleName);
  //      opPars.put(SSVarU.sampleCount,  par.sampleCount);
  //      opPars.put(SSVarU.k,            10);
  //      opPars.put(SSVarU.posfix,       null);
  //
  //      opPars.put(SSVarU.prefix, "model_lda_" + par.topics + SSStrU.underline + beta);
  //
  //      recommWriteMetrics(new SSServPar(SSMethU.recommWriteMetrics, opPars));
  //    }
  //  }
  
  //  @Override
  //  public void recommCalcMulan(SSServPar parA) throws Exception{
  //
  //    SSRecommCalcMulanPar par = new SSRecommCalcMulanPar(parA);
  //
  //    for (int counter = 1; counter <= par.sampleCount; counter++) {
  //      ArffTextWriter.writeArffFiles(par.sampleName + SSStrU.underline + counter, trainSize, testSize, true, false, false);
  //    }
  //
  //    for (int counter = 1; counter <= par.sampleCount; counter++) {
  //      MulanCalculator.predictSample(par.sampleName + SSStrU.underline + counter, testSize);
  //    }
  //  }
  
  //  @Override
  //  public void recommCreateLanguageModelSamples(SSServPar parA) throws Exception{
  //
  //    SSRecommCreateLanguageModelSamplesPar par = new SSRecommCreateLanguageModelSamplesPar(parA);
  //
  //    opPars = new HashMap<String, Object>();
  //    opPars.put(SSVarU.sample, par.sampleName + SSStrU.underline + 1);
  //
  //    recommTrainTestSize(new SSServPar(SSMethU.recommTrainTestSize, opPars));
  //
  //    for (int counter = 1; counter <= par.sampleCount; counter++) {
  //      LanguageModelCalculator.createSample(par.sampleName + SSStrU.underline + counter, testSize, true, true);
  //    }
  //  }
  
  //  @Override
//  public void recommSplitSample(SSServPar parA) throws Exception{
//    
//    SSRecommSplitSamplePar par = new SSRecommSplitSamplePar(parA);
//    
//    WikipediaSplitter.splitSample(par.dataSet, par.sampleName, par.sampleCount, par.userLevel, par.resourceLevel, par.tagLevel);
//  }
//  
//  @Override
//  public List<Integer> recommBetaValues(SSServPar parA) throws Exception{
//    
//    SSRecommBetaValuesPar par        = new SSRecommBetaValuesPar(parA);
//    List<Integer>         betaValues = new ArrayList<Integer>();
//    
//    if (par.betaUpperBound < 0) {
//      betaValues.add(par.betaUpperBound * (-1));
//    } else {
//      for (int betaVal = 1; betaVal <= par.betaUpperBound; betaVal++) {
//        betaValues.add(betaVal);
//      }
//    }
//    
//    return betaValues;
//  }
//  
//  @Override
//  public void recommTrainTestSize(SSServPar parA) throws Exception{
//    
//    SSRecommTrainTestSizePar par         = new SSRecommTrainTestSizePar(parA);
//    WikipediaReader          trainReader = new WikipediaReader(-1, false);
//    WikipediaReader          testReader  = new WikipediaReader(-1, false);
//    
//    trainReader.readFile(par.sample + "_res_train");
//    trainSize = trainReader.getUserLines().size();
//    SSLogU.logInfo("Train-size: " + trainSize);
//    
//    testReader.readFile(par.sample + "_res_test");
//    testSize = testReader.getUserLines().size();
//    SSLogU.logInfo("Test-size: " + testSize);
//  }
//  
//  @Override
//  public void recommWriteMetrics(SSServPar parA) throws Exception{
//    
//    SSRecommWriteMetricsPar par         = new SSRecommWriteMetricsPar(parA);
//    String                  topicString = (par.posfix == null ? SSStrU.empty : SSStrU.underline + par.posfix);
//    
//    for (int counter = 1; counter <= par.k; counter++) {
//      for (int innerCounter = 1; innerCounter <= par.sampleCount; innerCounter++) {
//        //MetricsCalculator.calculateMetrics(sampleName + "_" + j + topicString + "_res_" + prefix, i, null, false);
//        MetricsCalculator.calculateMetrics(par.sampleName + "_" + innerCounter + topicString + "_res_" + par.prefix, counter, par.sampleDir + SSStrU.slash + par.prefix + topicString + "_metrics", false);
//      }
//      MetricsCalculator.writeAverageMetrics(par.sampleDir + "/" + par.prefix + topicString + "_metrics", counter, (double)par.sampleCount);
//    }
//    
//    /*
//     * for (int i = 1; i <= sampleCount; i++) {
//     * for (int j = 1; j <= k; j++) {
//     * MetricsCalculator.calculateMetrics(sampleName + "_" + i + topicString + "_res_" + prefix, j,
//     * sampleDir + "/" + prefix + topicString + "_metrics", j == k);
//     * }
//     * }
//     */
//    
//    MetricsCalculator.resetMetrics();
//  }
//  
//  @Override
//  public void recommCalcThreeLayers(SSServPar parA) throws Exception{
//    
//    SSRecommCalcThreeLayersPar par         = new SSRecommCalcThreeLayersPar(parA);
//    String                     topicString = (par.prefix.equals(SSStrU.zero) ? SSStrU.empty : SSStrU.underline + par.prefix);
//    
//    for (int counter = 1; counter <= par.sampleCount; counter++) {
//      ThreeLayersCalculator.predictSample(par.sampleName + SSStrU.underline + counter + topicString, ((SSRecommConf)conf).rPath, par.predictTags);
//    }
//  }
//  
//  @Override
//  public void recommCalcLDA(SSServPar parA) throws Exception{
//    
//    SSRecommCalcLDAPar  par = new SSRecommCalcLDAPar(parA);
//    Map<String, Object> opPars;
//    
//    opPars = new HashMap<String, Object>();
//    opPars.put(SSVarU.sample, par.sampleName + SSStrU.underline + 1);
//    
//    recommTrainTestSize(new SSServPar(SSMethU.recommTrainTestSize, opPars));
//    
//    for (int counter = 1; counter <= par.sampleCount; counter++) {
//      LdaMergedCalculator.predictSample(par.sampleName + SSStrU.underline + counter, trainSize, testSize, (short)par.topics.intValue());
//    }
//    
//    opPars = new HashMap<String, Object>();
//    opPars.put(SSVarU.sampleDir,    par.sampleDir);
//    opPars.put(SSVarU.sampleName,   par.sampleName);
//    opPars.put(SSVarU.sampleCount,  par.sampleCount);
//    opPars.put(SSVarU.k,            10);
//    opPars.put(SSVarU.posfix,       null);
//    
//    opPars.put(SSVarU.prefix, "lda_" + par.topics);
//    
//    recommWriteMetrics(new SSServPar(SSMethU.recommWriteMetrics, opPars));
//  }  
//  @Override
//  public void recommCreateLDASamples(SSServPar parA) throws Exception{
//    
//    SSRecommCreateLDASamplesPar par = new SSRecommCreateLDASamplesPar(parA);
//    Map<String, Object>         opPars;
//    
//    opPars = new HashMap<String, Object>();
//    opPars.put(SSVarU.sample, par.sampleName + SSStrU.underline + 1);
//    
//    recommTrainTestSize(new SSServPar(SSMethU.recommTrainTestSize, opPars));
//    
//    for (int counter = 1; counter <= par.sampleCount; counter++) {
//      LdaMergedCalculator.createSample(par.sampleName + SSStrU.underline + counter, testSize, (short)par.topics.intValue());
//    }
//  }
  
//  @Override
//  public void recommStatistics(SSServPar parA) throws Exception{
//    
//    SSRecommStatisticsPar par   = new SSRecommStatisticsPar (parA);
//    WikipediaReader      reader = new WikipediaReader       (0, false);
//    int bookmarks;
//    int users;
//    int resources;
//    int tags;
//    int tagAssignments;
//    
//    reader.readFile(par.dataset);
//    
//    bookmarks = reader.getUserLines().size();
//    SSLogU.logInfo("Bookmarks: " + bookmarks);
//    
//    users = reader.getUsers().size();
//    SSLogU.logInfo("Users: " + users);
//    
//    resources = reader.getResources().size();
//    SSLogU.logInfo("Resources: " + resources);
//    
//    tags = reader.getTags().size();
//    SSLogU.logInfo("Tags: " + tags);
//    
//    tagAssignments = reader.getTagAssignmentsCount();
//    SSLogU.logInfo("Tag-Assignments: " + tagAssignments);
//    SSLogU.logInfo("Max P@10: " + tagAssignments / users / 10);
//  }
}
