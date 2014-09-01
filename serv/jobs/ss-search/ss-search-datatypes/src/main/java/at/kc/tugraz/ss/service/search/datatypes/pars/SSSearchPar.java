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
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "certain labels to be search for" )
  public List<SSSearchLabel> labelsToSearchFor          = new ArrayList<>();
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "whether descriptions of entities should be scanned" )
  public Boolean             includeDescription         = null;
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "certain descriptions to be searched for" )
  public List<SSSearchLabel> descriptionsToSearchFor    = new ArrayList<>();
  
  @XmlElement
  @ApiModelProperty( 
    required = false,
    value = "list of entity types to be considered for search exclusively " )
  public List<SSEntityE>     typesToSearchOnlyFor       = new ArrayList<>();
  
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
        labelsToSearchFor          = (List<SSSearchLabel>)    pars.get(SSVarU.labelsToSearchFor);
        includeDescription         = (Boolean)                pars.get(SSVarU.includeDescription);
        descriptionsToSearchFor    = (List<SSSearchLabel>)    pars.get(SSVarU.descriptionsToSearchFor);
        typesToSearchOnlyFor       = (List<SSEntityE>)        pars.get(SSVarU.typesToSearchOnlyFor);
        includeOnlySubEntities     = (Boolean)                pars.get(SSVarU.includeOnlySubEntities);
        entitiesToSearchWithin     = (List<SSUri>)            pars.get(SSVarU.entitiesToSearchWithin);
        extendToParents            = (Boolean)                pars.get(SSVarU.extendToParents);
        includeRecommendedResults  = (Boolean)                pars.get(SSVarU.includeRecommendedResults);
        provideEntries             = (Boolean)                pars.get(SSVarU.provideEntries);
      }
      
      if(clientPars != null){
        
        try{ keywordsToSearchFor          = SSStrU.splitDistinctWithoutEmptyAndNull(clientPars.get(SSVarU.keywordsToSearchFor), SSStrU.comma);                              }catch(Exception error){}
        try{ includeTextualContent        = Boolean.valueOf(clientPars.get(SSVarU.includeTextualContent));                                                                  }catch(Exception error){}
        try{ wordsToSearchFor             = SSStrU.splitDistinctWithoutEmptyAndNull(clientPars.get(SSVarU.wordsToSearchFor), SSStrU.comma);                                 }catch(Exception error){}
        try{ includeTags                  = Boolean.valueOf(clientPars.get(SSVarU.includeTags));                                                                            }catch(Exception error){}
        try{ tagsToSearchFor              = SSStrU.splitDistinctWithoutEmptyAndNull(clientPars.get(SSVarU.tagsToSearchFor), SSStrU.comma);                                  }catch(Exception error){}
        try{ includeMIs                   = Boolean.valueOf(clientPars.get(SSVarU.includeMIs));                                                                             }catch(Exception error){}
        try{ misToSearchFor               = SSStrU.splitDistinctWithoutEmptyAndNull(clientPars.get(SSVarU.misToSearchFor), SSStrU.comma);                                   }catch(Exception error){}
        try{ includeLabel                 = Boolean.valueOf(clientPars.get(SSVarU.includeLabel));                                                                           }catch(Exception error){}
        try{ labelsToSearchFor            = SSSearchLabel.get(SSStrU.splitDistinctWithoutEmptyAndNull(clientPars.get(SSVarU.labelsToSearchFor), SSStrU.comma));             }catch(Exception error){} 
        try{ includeDescription           = Boolean.valueOf(clientPars.get(SSVarU.includeDescription));                                                                     }catch(Exception error){}
        try{ descriptionsToSearchFor      = SSSearchLabel.get(SSStrU.splitDistinctWithoutEmptyAndNull(clientPars.get(SSVarU.descriptionsToSearchFor), SSStrU.comma));       }catch(Exception error){}
        try{ typesToSearchOnlyFor         = SSEntityE.get(SSStrU.splitDistinctWithoutEmptyAndNull(clientPars.get(SSVarU.typesToSearchOnlyFor), SSStrU.comma));              }catch(Exception error){}
        try{ includeOnlySubEntities       = Boolean.valueOf(clientPars.get(SSVarU.includeOnlySubEntities));                                                                 }catch(Exception error){}
        try{ entitiesToSearchWithin       = SSUri.get(SSStrU.splitDistinctWithoutEmptyAndNull(clientPars.get(SSVarU.entitiesToSearchWithin), SSStrU.comma));                }catch(Exception error){}
        try{ extendToParents              = Boolean.valueOf(clientPars.get(SSVarU.extendToParents));                                                                        }catch(Exception error){}
        try{ includeRecommendedResults    = Boolean.valueOf(clientPars.get(SSVarU.includeRecommendedResults));                                                              }catch(Exception error){}
        try{ provideEntries               = Boolean.valueOf(clientPars.get(SSVarU.provideEntries));                                                                         }catch(Exception error){}
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
