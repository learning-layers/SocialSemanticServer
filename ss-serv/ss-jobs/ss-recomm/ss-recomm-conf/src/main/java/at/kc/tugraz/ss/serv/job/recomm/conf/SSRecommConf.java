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
package at.kc.tugraz.ss.serv.job.recomm.conf;

import at.kc.tugraz.ss.serv.serv.api.SSServConfA;

public class SSRecommConf extends SSServConfA{

  public Boolean         initAtStartUp                                                                                 = null;
  public String          fileNameForOpRecommTagsBaseLevelLearningWithContextBasedOnUserEntityTagTimestamp              = null;
  public String          fileNameForOpRecommTagsLanguageModelBasedOnUserEntityTag                                      = null;
  public String          fileNameForOpRecommTagsThreeLayersBasedOnUserEntityTagCategory                                = null;
  public String          fileNameForOpRecommTagsThreeLayersBasedOnUserEntityTagCategoryTimestamp                       = null;
  
  public static SSRecommConf copy(final SSRecommConf orig){
    
    final SSRecommConf copy = (SSRecommConf) SSServConfA.copy(orig, new SSRecommConf());
    
    copy.initAtStartUp = 
      orig.initAtStartUp;
    
    copy.fileNameForOpRecommTagsBaseLevelLearningWithContextBasedOnUserEntityTagTimestamp = 
      orig.fileNameForOpRecommTagsBaseLevelLearningWithContextBasedOnUserEntityTagTimestamp;
    
    copy.fileNameForOpRecommTagsLanguageModelBasedOnUserEntityTag = 
      orig.fileNameForOpRecommTagsLanguageModelBasedOnUserEntityTag;
    
    copy.fileNameForOpRecommTagsThreeLayersBasedOnUserEntityTagCategory = 
      orig.fileNameForOpRecommTagsThreeLayersBasedOnUserEntityTagCategory;
    
    copy.fileNameForOpRecommTagsThreeLayersBasedOnUserEntityTagCategoryTimestamp = 
      orig.fileNameForOpRecommTagsThreeLayersBasedOnUserEntityTagCategoryTimestamp;
    
    return copy;
  }
    
//  public String          rPath                                                                                         = null;
//  public String          prefix                                                                                        = null;
//  public String          posfix                                                                                        = null;
//  public Integer         userLevel                                                                                     = null;
//  public Integer         resourceLevel                                                                                 = null;
//  public Integer         tagLevel                                                                                      = null;
//  public Boolean         predictTags                                                                                   = null;
//  public Integer         topics                                                                                        = null;
//  public Integer         sampleCount                                                                                   = null;
//  public Integer         neighbors                                                                                     = null;
//  public Integer         betaUpperBound                                                                                = null;
//  public Integer         modelBeta                                                                                     = null;
//  public Integer         dUpperBound                                                                                   = null;
//  public Integer         k                                                                                             = null;
//  public String          tableName                                                                                     = null;
//  public String          dataSetShortName                                                                              = null;
//  public String          dataSet                                                                                       = null;
//  public String          sampleDir                                                                                     = null;
//  public String          sampleName                                                                                    = null;
//  public String          fileNameForOpRecommTagsTemporalUsagePatterns                     = null;
//  public String          fileNameForOpRecommTagsLDA                                       = null;
//  public String          fileNameForOpRecommTagsFolkRank                                  = null;
//  public String          fileNameForOpRecommTagsAdaptedPageRank                           = null;
//  public String          fileNameForOpRecommTagsCollaborativeFilteringOnUserSimilarity    = null;
//  public String          fileNameForOpRecommTagsCollaborativeFilteringOnEntitySimilarity  = null;

//  public void setDataSetShortName(String dataSetShortName) throws Exception {
//    
//    this.dataSetShortName = dataSetShortName;
//    
//    switch(SSRecommDataSetE.get(dataSetShortName)){
//      
//      case bib: 
//        dataSet    = "bib_wiki";
//        sampleName = "bib_core/bib_sample";
//        sampleDir  = "bib_core";
//      break;
//        
//      case cul:
//        dataSet    = "citeulike_wiki";
//        sampleName = "cul_core/cul_sample";
//        sampleDir  = "cul_core";
//        break;
//        
//      case culdb:
//        dataSet    = "cul_db";
//        sampleName = "cul_db/db_sample";
//        sampleDir  = "cul_db";
//      break;
//        
//      case del: 
//        dataSet    = "del_core/del_sample_core_u40_r40_t40_c18";
//        sampleName = "del_core/del_sample";
//        sampleDir  = "del_core";
//      break;
//        
//      case flickr: 
//        dataSet    = "flickr_wiki";
//        sampleName = "flickr_core/flickr_sample";
//        sampleDir  = "flickr_core";
//      break;
//        
//      case lastfm: 
//        dataSet    = "lastfm_wiki";
//        sampleName = "lastfm_core/lastfm_sample";
//        sampleDir  = "lastfm_core";
//      break;
//        
//      case nytimes: 
//        dataSet    = "nytimes_sample_core_user_12_c12";
//        sampleName = "nytimes_core/nytimes_sample";
//        sampleDir  = "nytimes_core";
//      break;
//        
//      case wiki: 
//        dataSet    = "wiki_tags_all_with_top_cats_selected";
//        sampleName = "wiki_core/wiki_sample";
//        sampleDir  = "wiki_core";
//      break;
//        
//      case youtube: 
//        dataSet    = "youtube_wiki";
//        sampleName = "youtube_core/youtube_sample";
//        sampleDir  = "youtube_core";
//      break;
//        
//      default:
//        SSServErrReg.regErrThrow(new Exception("data set not available"));
//    }
//  }
}
//#/usr/bin/, C:\\Program Files\\R\\R-2.15.3\\bin\\