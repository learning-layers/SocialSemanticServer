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
package at.kc.tugraz.ss.serv.lomextractor.datatypes;

import at.kc.tugraz.socialserver.utils.SSLinkU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityA;
import java.util.ArrayList;
import java.util.List;

public class SSLOMConceptRelation extends SSEntityA{
  
  public  String concept   = null;
  public  String relation  = null;
  public  String lang      = null;
  
  public SSLOMConceptRelation(
    String concept, 
    String relation,
    String lang){
    
    super(relation + relation);
    
    this.concept   = concept;
    this.relation  = relation;
    this.lang      = lang;
  }
  
  public static List<SSLOMConceptRelation> distinctRelations(List<SSLOMConceptRelation> conceptRelations){
    
    List<String>               conceptRelationCombis = new ArrayList<String>();
    List<SSLOMConceptRelation> result                = new ArrayList<SSLOMConceptRelation>();
    
    for(SSLOMConceptRelation conceptRelation : conceptRelations){
      
      conceptRelation.relation = SSStrU.replace(conceptRelation.relation, SSLinkU.ccUahEsIeOntOeOaae, SSStrU.empty);
      conceptRelation.concept  = SSStrU.replace(conceptRelation.concept,  SSLinkU.ccUahEsIeOntOeOaae, SSStrU.empty);
      
      if(
        SSStrU.isNotEmpty              (conceptRelation.relation + conceptRelation.concept) &&
        !conceptRelationCombis.contains (conceptRelation.relation + conceptRelation.concept)){
        
        conceptRelationCombis.add  (conceptRelation.relation + conceptRelation.concept);
        result.add                 (conceptRelation);
      }
    }
    
    return result;
  }
  
  public static List<String> concepts(List<SSLOMConceptRelation> conceptRelations){
   
    List<String> concepts = new ArrayList<String>();
    
    for(SSLOMConceptRelation conceptRelation : conceptRelations){
      concepts.add(conceptRelation.concept);
    }
    
    return concepts;
  }

  @Override
  public Object jsonLDDesc(){
    return "dtheiler";
  }
}
