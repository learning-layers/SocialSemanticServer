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
package at.kc.tugraz.ss.serv.lomextractor.impl;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.serv.lomextractor.datatypes.SSLOMConceptRelation;
import at.kc.tugraz.ss.serv.lomextractor.datatypes.SSLOMCoverage;
import at.kc.tugraz.ss.serv.lomextractor.datatypes.SSLOMDesc;
import at.kc.tugraz.ss.serv.lomextractor.datatypes.SSLOMKeyword;
import at.kc.tugraz.ss.serv.lomextractor.datatypes.SSLOMResource;
import at.kc.tugraz.ss.serv.lomextractor.datatypes.SSLOMTitle;
import at.kc.tugraz.ss.serv.lomextractor.datatypes.SSLOMUser;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSLOMExtractorStats {
  
  public int                        invalidXMLFileCount          = 0;
  public int                        totalLOMFileCount            = 0;
  public int                        duplicateResourceCount       = 0;
  
  //general
  public int                        noGeneralCount               = 0;
  public int                        noUriCount                   = 0;
  public int                        noTitleCount                 = 0;
  public int                        noDescCount                  = 0;
  public int                        noLangCount                  = 0;
  public int                        noCoverageCount              = 0;
  public int                        noKeywordCount               = 0;
  
  //metaMetadata
  public int                        noMetaMetadataCount          = 0;
  public int                        noUserCount                  = 0;
  public int                        noStructuredNameCount        = 0;
  
  //technical
  public int                        noTechnicalCount             = 0;
  public int                        noFormatCount                = 0;
  
  //educational
  public int                        noEducationalCount           = 0;
  public int                        noLearningResourceTypeCount  = 0;
  public int                        noIntendedEndUserRoleCount   = 0;
  public int                        noContextCount               = 0;
  
  //classification
  public int                        noClassificationCount        = 0;
  public int                        noConceptRelationCount       = 0;
  
  public int                        totalUsedResourcesCount      = 0;
  
  public Map<String, Integer>       resourcesKeywordCount        = new HashMap<>();
  public Map<String, Integer>       resourcesConceptCount        = new HashMap<>();
  
  public List<String>               conceptsDistinct             = new ArrayList<>();
  public List<String>               relationsDistinct            = new ArrayList<>();
  
  public List<String>               usersDistinct                = new ArrayList<>();
  public Map<String, Integer>       usersResourceCount           = new HashMap<>();
  public Map<String, Integer>       usersKeywordCount            = new HashMap<>();
  
  public List<String>               keywordsDistinct             = new ArrayList<>();
  
  public List<String>               langDistinct                 = new ArrayList<>();
  public Map<String, Integer>       langTitleCount               = new HashMap<>();
  public Map<String, Integer>       langDescCount                = new HashMap<>();
  
  public void calc(Map<String, SSLOMResource> resources) {
    
    for(SSLOMResource resource : resources.values()){
      
      resource.distinctValues();
      
      if(SSLOMExtractorOutHandler.notValidToBeWritten(resource)){
        continue;
      }
      
      resource(resource);
      lang    (resource);
      
      for(SSLOMUser user : resource.users){
        
        user(user, resource);
        
        for(SSLOMKeyword keyword : resource.keywords){
          keyword(keyword);
        }
      }
    }
  }
  
  private void resource(SSLOMResource resource){
    
    resourcesKeywordCount.put(resource.id, resource.keywords.size());
    resourcesConceptCount.put(resource.id, resource.conceptRelatios.size());
    
    for(SSLOMConceptRelation conceptRelation : resource.conceptRelatios){
      
      if(!conceptsDistinct.contains(conceptRelation.concept)){
        conceptsDistinct.add(conceptRelation.concept);
      }
      
      if(!relationsDistinct.contains(conceptRelation.relation)){
        relationsDistinct.add(conceptRelation.relation);
      }
    }
    
    totalUsedResourcesCount++;
  }
  
  private void lang(SSLOMResource resource){
    
    List<String> langTitleResourceDistinct = new ArrayList<>();
    List<String> langDescResourceDistinct  = new ArrayList<>();
    
    if(!langDistinct.contains(resource.lang)){
      langDistinct.add(resource.lang);
    }
    
    for(SSLOMTitle title : resource.titles){
      
      if(!langDistinct.contains(title.lang)){
        langDistinct.add(title.lang);
      }
      
      if(langTitleResourceDistinct.contains(title.lang)){
        continue;
      }
      
      if(langTitleCount.containsKey(title.lang)){
        langTitleCount.put(title.lang, langTitleCount.get(title.lang) + 1);
      }else{
        langTitleCount.put(title.lang, 1);
        
        langTitleResourceDistinct.add(title.lang);
      }
    }
    
    for(SSLOMDesc desc : resource.descs){
      
      if(!langDistinct.contains(desc.lang)){
        langDistinct.add(desc.lang);
      }
      
      if(langDescResourceDistinct.contains(desc.lang)){
        continue;
      }
      
      if(langDescCount.containsKey(desc.lang)){
        langDescCount.put(desc.lang, langDescCount.get(desc.lang) + 1);
      }else{
        langDescCount.put(desc.lang, 1);
        
        langDescResourceDistinct.add(desc.lang);
      }
    }
    
    for(SSLOMConceptRelation conceptRelation : resource.conceptRelatios){
      
      if(!langDistinct.contains(conceptRelation.lang)){
        langDistinct.add(conceptRelation.lang);
      }
    }
    
    for(SSLOMCoverage coverage : resource.coverages){
      
      if(!langDistinct.contains(coverage.lang)){
        langDistinct.add(coverage.lang);
      }
    }
    
    for(SSLOMKeyword keyword : resource.keywords){
      
      if(!langDistinct.contains(keyword.lang)){
        langDistinct.add(keyword.lang);
      }
    }
  }

  private void user(SSLOMUser user, SSLOMResource resource){
    
    if(usersResourceCount.containsKey(user.fullName)){
      usersResourceCount.put (user.fullName,  usersResourceCount.get(user.fullName) + 1);
      usersKeywordCount.put  (user.fullName,  usersKeywordCount.get (user.fullName) + resource.keywords.size());
    }else{
      usersResourceCount.put (user.fullName, 1);
      usersKeywordCount.put  (user.fullName, resource.keywords.size());
    }
    
    if(!usersDistinct.contains(user.fullName)){
      usersDistinct.add(user.fullName);
    }
  }
  
  private void keyword(SSLOMKeyword keyword) {
   
    if(!keywordsDistinct.contains(keyword.label)){
      keywordsDistinct.add(keyword.label);
    }
  }

  public void stat(Date start){
    
    System.out.println();
    System.out.println();
    System.out.println();
    System.out.println("start: " + start);
    System.out.println("end  : " + new Date());
    System.out.println();
    
    //total lom files
    System.out.println("totalLOMFileCount:           "      + totalLOMFileCount);
    System.out.println("invalidXMLFileCount:         "      + invalidXMLFileCount);
    System.out.println();
    
    System.out.println("duplicateResourceCount:      "      + duplicateResourceCount);
    
    System.out.println();
    
    //general
    System.out.println("noGeneralCount:              "       + noGeneralCount);
    System.out.println("noUriCount:                  "       + noUriCount);
    System.out.println("noTitleCount:                "       + noTitleCount);
    System.out.println("noDescCount:                 "       + noDescCount);
    System.out.println("noLangCount:                 "       + noLangCount);
    System.out.println("noCoverageCount:             "       + noCoverageCount);
    System.out.println("noKeywordCount:              "       + noKeywordCount);
    System.out.println();
    
    //metaMetadata
    System.out.println("noMetaMetadataCount:         "       + noMetaMetadataCount);
    System.out.println("noUserCount:                 "       + noUserCount);
    System.out.println("noStructuredNameCount:       "       + noStructuredNameCount);
    System.out.println();
    
    //technical   
    System.out.println("noTechnicalCount:            "       + noTechnicalCount);
    System.out.println("noFormatCount:               "       + noFormatCount);
    System.out.println();
    
    //educational
    System.out.println("noEducationalCount:          "       + noEducationalCount);
    System.out.println("noLearningResourceTypeCount: "       + noLearningResourceTypeCount);
    System.out.println("noIntendedEndUserRoleCount:  "       + noIntendedEndUserRoleCount);
    System.out.println("noContextCount:              "       + noContextCount);
    System.out.println();
    
    //classification
    System.out.println("noClassificationCount:       "       + noClassificationCount);
    System.out.println("noConceptRelationCount:      "       + noConceptRelationCount);
    System.out.println();
    
    System.out.println("total resources used which have users and keywords: " + totalUsedResourcesCount);
    System.out.println("distinct users:             "         + usersDistinct.size());
    System.out.println("distinct keywords:          "         + keywordsDistinct.size());
    System.out.println("distinct concepts:          "         + conceptsDistinct.size());
    System.out.println("distinct relations:         "         + relationsDistinct.size());
    System.out.println("distinct languages:         "         + langDistinct.size());
    System.out.println();
    
    System.out.println("users:");
    
    for(String user : usersDistinct){
      System.out.println(SSStrU.doubleQuote + user + SSStrU.doubleQuote);
    }
    
    System.out.println("keywords:");
    
    for(String keyword : keywordsDistinct){
      System.out.println(SSStrU.doubleQuote + keyword + SSStrU.doubleQuote);
    }
    
    System.out.println("concepts:");
    
    for(String concept : conceptsDistinct){
      System.out.println(SSStrU.doubleQuote + concept + SSStrU.doubleQuote);
    }
    
    System.out.println("languages:");
    
    for(String lang : langDistinct){
      System.out.println(SSStrU.doubleQuote + lang + SSStrU.doubleQuote);
    }
    
    System.out.println();
    
    int totalUsersResourceCount = 0;
    
    for(Integer count : usersResourceCount.values()){
      totalUsersResourceCount += count;
    }
    
    System.out.println("average users' (distinct) resources :            "         + totalUsersResourceCount/usersResourceCount.size());
    
    int totalResourcesKeywordCount = 0;
    
    for(Integer count : resourcesKeywordCount.values()){
      totalResourcesKeywordCount += count;
    }
    
    System.out.println("average resources' (distinct) keywords :         "         + totalResourcesKeywordCount/resourcesKeywordCount.size());
    
    int totalResourcesConceptCount = 0;
    
    for(Integer count : resourcesConceptCount.values()){
      totalResourcesConceptCount += count;
    }
    
    System.out.println("average resources' (distinct) concepts :         "         + totalResourcesConceptCount/resourcesConceptCount.size());
    
    int totalUsersKeywordCount = 0;
    
    for(Integer count : usersKeywordCount.values()){
      totalUsersKeywordCount += count;
    }
    
    System.out.println("average users' (not distinct) keywords :         "         + totalUsersKeywordCount/usersKeywordCount.size());
    System.out.println();
    
    System.out.println("languages of titles");
    
    for(Map.Entry<String, Integer> entry : langTitleCount.entrySet()){
      System.out.println(entry.getValue() + " titles in " + entry.getKey());
    }
    
    System.out.println();
    System.out.println("languages of descriptions");
    
    for(Map.Entry<String, Integer> entry : langDescCount.entrySet()){
      System.out.println(entry.getValue() + " descs in " + entry.getKey());
    }
    
    System.out.println();
  }
}
