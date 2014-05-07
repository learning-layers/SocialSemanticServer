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
package at.kc.tugraz.ss.serv.lomextractor.datatypes;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityA;
import java.util.ArrayList;
import java.util.List;

public class SSLOMResource extends SSEntityA{
  
  public String                     id                    = null;
  public String                     uri                   = null;
  public List<SSLOMUser>            users                 = new ArrayList<SSLOMUser>();
  public String                     lang                  = null;
  public List<SSLOMTitle>           titles                = new ArrayList<SSLOMTitle>();
  public List<SSLOMKeyword>         keywords              = new ArrayList<SSLOMKeyword>();
  public List<SSLOMDesc>            descs                 = new ArrayList<SSLOMDesc>();
  public List<String>               formats               = new ArrayList<String>();
  public List<String>               learningResourceTypes = new ArrayList<String>();
  public List<String>               intendedEndUserRoles  = new ArrayList<String>();
  public List<String>               contexts              = new ArrayList<String>();
  public List<SSLOMConceptRelation> conceptRelatios       = new ArrayList<SSLOMConceptRelation>();
  public List<SSLOMCoverage>        coverages             = new ArrayList<SSLOMCoverage>();
  
  public SSLOMResource(
    String                     id,
    String                     uri,
    String                     lang,
    List<String>               formats,
    List<SSLOMTitle>           titles,
    List<SSLOMKeyword>         keywords,
    List<SSLOMDesc>            descs,
    List<String>               learningResourceTypes,
    List<String>               intendedEndUserRoles, 
    List<String>               contexts,
    List<SSLOMConceptRelation> conceptRelatios,
    List<SSLOMUser>            users,
    List<SSLOMCoverage>        coverages)throws Exception{
    
    super(id);
    
    this.id                   = id;
    this.uri                  = uri;
    this.lang                 = lang;
    
    this.formats.addAll               (formats);
    this.titles.addAll                (titles);
    this.keywords.addAll              (keywords);
    this.descs.addAll                 (descs);
    this.learningResourceTypes.addAll (learningResourceTypes);
    this.intendedEndUserRoles.addAll  (intendedEndUserRoles);
    this.contexts.addAll              (contexts);
    this.conceptRelatios.addAll       (conceptRelatios);
    this.users.addAll                 (users);
    this.coverages.addAll             (coverages);
  }
  
  public void distinctValues(){
    
    if(users.size() > 0){
      users = SSLOMUser.distinctUsers(users);
    }
    
    if(titles.size() > 0){
      titles = SSLOMTitle.distinctTitles(titles);
    }
    
    if(keywords.size() > 0){
      keywords = SSLOMKeyword.distinctKeywords(keywords);
    }
    
    if(descs.size() > 0){
      descs = descs.get(0).distinctDescs(descs);
    }
    
    if(formats.size() > 0){
      formats = SSStrU.distinctWithoutEmptyAndNull(formats);
    }
    
    if(learningResourceTypes.size() > 0){
      learningResourceTypes = SSStrU.distinctWithoutEmptyAndNull(learningResourceTypes);
    }
    
    if(intendedEndUserRoles.size() > 0){
      intendedEndUserRoles = SSStrU.distinctWithoutEmptyAndNull(intendedEndUserRoles);
    }
    
    if(contexts.size() > 0){
      contexts = SSStrU.distinctWithoutEmptyAndNull(contexts);
    }
    
    if(conceptRelatios.size() > 0){
      conceptRelatios = SSLOMConceptRelation.distinctRelations(conceptRelatios);
    }
    
    if(coverages.size() > 0){
      coverages = SSLOMCoverage.distinctCoverages(coverages);
    }
  }
  
  @Override
  public Object jsonLDDesc(){
    return "dtheiler";
  }
}