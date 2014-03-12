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
package at.kc.tugraz.ss.serv.job.recomm.api;

import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.service.tag.datatypes.SSTag;
import java.util.List;

public interface SSRecommServerI{

  public List<SSTag> recommTagsBaseLevelLearningWithContext             (final SSServPar parA) throws Exception;
  public List<SSTag> recommTagsLanguageModel                            (final SSServPar parA) throws Exception;
  
  public void        recommBaseLevelLearningWithContextUpdate           (final SSServPar parA) throws Exception;
  public void        recommLanguageModelUpdate                          (final SSServPar parA) throws Exception;
  public void        recommThreeLayersUpdate                            (final SSServPar parA) throws Exception;
  
  public List<SSTag> recommTagsCollaborativeFilteringOnEntitySimilarity (final SSServPar parA) throws Exception;
  public List<SSTag> recommTagsCollaborativeFilteringOnUserSimilarity   (final SSServPar parA) throws Exception;
  public List<SSTag> recommTagsAdaptedPageRank                          (final SSServPar parA) throws Exception;
  public List<SSTag> recommTagsFolkRank                                 (final SSServPar parA) throws Exception;
  public List<SSTag> recommTagsLDA                                      (final SSServPar parA) throws Exception;
  public List<SSTag> recommTagsThreeLayers                              (final SSServPar parA) throws Exception;
  public List<SSTag> recommTagsThreeLayersWithTime                      (final SSServPar parA) throws Exception;
  public List<SSTag> recommTagsTemporalUsagePatterns                    (final SSServPar parA) throws Exception;
  
//  public void          recommSplitSample                (SSServPar parA)     throws Exception;
//  public List<Integer> recommBetaValues                 (SSServPar parI)     throws Exception;
//  public void          recommTrainTestSize              (SSServPar parA)     throws Exception;
//  public void          recommWriteMetrics               (SSServPar parA)     throws Exception;
//  public void          recommCalcThreeLayers            (SSServPar parA)     throws Exception;
//  public void          recommCalcLDA                    (SSServPar parA)     throws Exception;
//  public void          recommCreateLDASamples           (SSServPar parA)     throws Exception;
//  public void          recommStatistics                 (SSServPar parA)     throws Exception;
  //  public void          recommWriteMetricsMulan          (SSServPar parA)     throws Exception;
  //  public void          recommCalcAct                    (SSServPar parI)     throws Exception;
  //  public void          recommCalcLanguageModel          (SSServPar parA)     throws Exception;
  //  public void          recommCalcCFTag                  (SSServPar parA)     throws Exception;
  //  public void          recommCalcFolkRank               (SSServPar parA)     throws Exception;
  //  public void          recommCalcBaseline               (SSServPar parA)     throws Exception;
  //  public void          recommCalcBllCF                  (SSServPar parA)     throws Exception;
  //  public void          recommCalcLDAModel               (SSServPar parA)     throws Exception;
  //  public void          recommCalcMulan                  (SSServPar parA)     throws Exception;
  //  public void          recommCreateLanguageModelSamples (SSServPar parA)     throws Exception;
//  public List<String>  recommLanguageModel              (SSServPar parA)     throws Exception;
}
