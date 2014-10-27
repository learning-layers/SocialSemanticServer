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
package at.kc.tugraz.ss.service.search.datatypes.pars;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.service.search.datatypes.SSSearchLabel;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.JsonNode;

@XmlRootElement(name = "SSSearchPar")
@ApiModel(value = "search request parameter")
public class SSSearchPar extends SSServPar{
  
  @XmlElement
  @ApiModelProperty(
    required = false, 
    value = "general keywords to be searched for; get interpreted as, e.g. tags, words if respective flags set (e.g. includeTags)" )
  public List<String>        keywordsToSearchFor        = new ArrayList<>();
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "whether the text content (if available) of entities should be scanned" )
  public Boolean             includeTextualContent      = null;
  
  @XmlElement
  @ApiModelProperty(
    value = "keywords to be used in textual content search",
    required = false)
  public List<String>        wordsToSearchFor           = new ArrayList<>();
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "whether tags of entities should be looked to find entities" )
  public Boolean             includeTags                = null;
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "tags to be searched for" )
  public List<String>        tagsToSearchFor            = new ArrayList<>();
  
  @XmlElement
  @ApiModelProperty(
    required = false, 
    value = "whether maturing indicators should be included in search")
  public Boolean             includeMIs                 = null;
    
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "maturing indicators of entities to be matched" )
  public List<String>        misToSearchFor             = new ArrayList<>();
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "whether labels of entities should be scanned" )
  public Boolean             includeLabel               = null;
  
  @ApiModelProperty( 
    required = false, 
    value = "certain labels to be search for" )
  public List<SSSearchLabel> labelsToSearchFor          = new ArrayList<>();
  
  @XmlElement
  public void setLabelsToSearchFor(final List<String> labelsToSearchFor) throws Exception{
    this.labelsToSearchFor = SSSearchLabel.get(labelsToSearchFor);
  }
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "whether descriptions of entities should be scanned" )
  public Boolean             includeDescription         = null;
  
  @ApiModelProperty( 
    required = false, 
    value = "certain descriptions to be searched for" )
  public List<SSSearchLabel> descriptionsToSearchFor    = new ArrayList<>();
  
  @XmlElement
  public void setDescriptionsToSearchFor(final List<String> descriptionsToSearchFor) throws Exception{
    this.descriptionsToSearchFor = SSSearchLabel.get(descriptionsToSearchFor);
  }
  
  @ApiModelProperty( 
    required = false,
    value = "list of entity types to be considered for search exclusively " )
  public List<SSEntityE>     typesToSearchOnlyFor       = new ArrayList<>();
  
  @XmlElement
  public void setTypesToSearchOnlyFor(final List<String> typesToSearchOnlyFor) throws Exception{
    this.typesToSearchOnlyFor = SSEntityE.get(typesToSearchOnlyFor);
  }
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "whether only sub-entities (e.g. collection entries) of entitiesToSearchWithin should be considered" )
  public Boolean             includeOnlySubEntities     = null;
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "entities for whom only sub entities get search for")
  public List<SSUri>         entitiesToSearchWithin     = new ArrayList<>();
  
  @XmlElement
  public void setEntitiesToSearchWithin(final List<String> entitiesToSearchWithin) throws Exception{
    this.entitiesToSearchWithin = SSUri.get(entitiesToSearchWithin);
  }
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "whether search results shall contain the parents of found entities as search result" )
  public Boolean             extendToParents            = null;
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "whether possibly recommended entities should be included in search results" )
  public Boolean             includeRecommendedResults  = null;
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "whether entries (if available) of search results (e.g. the entries of a found collection) should be returned as well" )
  public Boolean             provideEntries             = null;
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "pagesID")
  public String              pagesID             = null;
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "pageNumber")
  public Integer             pageNumber             = null;
  
  public SSSearchPar(){}
  
  public SSSearchPar(final SSServPar par) throws Exception{
    
    super(par);
    
    try{
      
      if(pars != null){
        keywordsToSearchFor        = (List<String>)           pars.get(SSVarU.keywordsToSearchFor);
        includeTextualContent      = (Boolean)                pars.get(SSVarU.includeTextualContent);
        wordsToSearchFor           = (List<String>)           pars.get(SSVarU.wordsToSearchFor);
        includeTags                = (Boolean)                pars.get(SSVarU.includeTags);
        tagsToSearchFor            = (List<String>)           pars.get(SSVarU.tagsToSearchFor);
        includeMIs                 = (Boolean)                pars.get(SSVarU.includeMIs);
        misToSearchFor             = (List<String>)           pars.get(SSVarU.misToSearchFor);
        includeLabel               = (Boolean)                pars.get(SSVarU.includeLabel);
        labelsToSearchFor          = SSSearchLabel.get((List<String>)pars.get(SSVarU.labelsToSearchFor));
        includeDescription         = (Boolean)                pars.get(SSVarU.includeDescription);
        descriptionsToSearchFor    = SSSearchLabel.get((List<String>)pars.get(SSVarU.descriptionsToSearchFor));
        typesToSearchOnlyFor       = (List<SSEntityE>)        pars.get(SSVarU.typesToSearchOnlyFor);
        includeOnlySubEntities     = (Boolean)                pars.get(SSVarU.includeOnlySubEntities);
        entitiesToSearchWithin     = (List<SSUri>)            pars.get(SSVarU.entitiesToSearchWithin);
        extendToParents            = (Boolean)                pars.get(SSVarU.extendToParents);
        includeRecommendedResults  = (Boolean)                pars.get(SSVarU.includeRecommendedResults);
        provideEntries             = (Boolean)                pars.get(SSVarU.provideEntries);
        pagesID                    = (String)                 pars.get(SSVarU.pagesID);
        pageNumber                 = (Integer)                pars.get(SSVarU.pageNumber);
      }
      
      if(par.clientJSONObj != null){
        
        try{ 
          for (final JsonNode objNode : par.clientJSONObj.get(SSVarU.keywordsToSearchFor)) {
            keywordsToSearchFor.add(objNode.getTextValue());
          }
        }catch(Exception error){}
        
        try{ includeTextualContent        = par.clientJSONObj.get(SSVarU.includeTextualContent).getBooleanValue();                                                       }catch(Exception error){}
        
        try{ 
          for (final JsonNode objNode : par.clientJSONObj.get(SSVarU.wordsToSearchFor)) {
            wordsToSearchFor.add(objNode.getTextValue());
          }
        }catch(Exception error){}
        
        try{ includeTags                  = par.clientJSONObj.get(SSVarU.includeTags).getBooleanValue();                                                                       }catch(Exception error){}
        
        try{ 
          for (final JsonNode objNode : par.clientJSONObj.get(SSVarU.tagsToSearchFor)) {
            tagsToSearchFor.add(objNode.getTextValue());
          }
        }catch(Exception error){}
        
        try{ includeMIs                   = par.clientJSONObj.get(SSVarU.includeMIs).getBooleanValue();                                                                       }catch(Exception error){}
        
        try{
          for (final JsonNode objNode : par.clientJSONObj.get(SSVarU.misToSearchFor)) {
            misToSearchFor.add(objNode.getTextValue());
          }
        }catch(Exception error){}
         
        try{ includeLabel                 = par.clientJSONObj.get(SSVarU.includeLabel).getBooleanValue();                                                                       }catch(Exception error){}
        
        try{
          for (final JsonNode objNode : par.clientJSONObj.get(SSVarU.labelsToSearchFor)) {
            labelsToSearchFor.add(SSSearchLabel.get(objNode.getTextValue()));
          }
        }catch(Exception error){}
        
        try{ includeDescription           = par.clientJSONObj.get(SSVarU.includeDescription).getBooleanValue();                                                                  }catch(Exception error){}
        
        try{ 
          for (final JsonNode objNode : par.clientJSONObj.get(SSVarU.descriptionsToSearchFor)) {
            descriptionsToSearchFor.add(SSSearchLabel.get(objNode.getTextValue()));
          }
        }catch(Exception error){}
        
        try{ 
          for (final JsonNode objNode : par.clientJSONObj.get(SSVarU.typesToSearchOnlyFor)) {
            typesToSearchOnlyFor.add(SSEntityE.get(objNode.getTextValue()));
          }
        }catch(Exception error){}
        
        try{ includeOnlySubEntities       = par.clientJSONObj.get(SSVarU.includeOnlySubEntities).getBooleanValue();                                                                }catch(Exception error){}
        
        try{
          for (final JsonNode objNode : par.clientJSONObj.get(SSVarU.entitiesToSearchWithin)) {
            entitiesToSearchWithin.add(SSUri.get(objNode.getTextValue()));
          }
        }catch(Exception error){}
        
        try{ extendToParents              = par.clientJSONObj.get(SSVarU.extendToParents).getBooleanValue();                                                                       }catch(Exception error){}
        try{ includeRecommendedResults    = par.clientJSONObj.get(SSVarU.includeRecommendedResults).getBooleanValue();                                                             }catch(Exception error){}
        try{ provideEntries               = par.clientJSONObj.get(SSVarU.provideEntries).getBooleanValue();                                                                        }catch(Exception error){}
        try{ pagesID                      = par.clientJSONObj.get(SSVarU.pagesID).getTextValue();                                                                                  }catch(Exception error){}
        try{ pageNumber                   = par.clientJSONObj.get(SSVarU.pageNumber).getIntValue();                                                                                }catch(Exception error){}
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  /* json getters */
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
}
