/**
 * Code contributed to the Learning Layers project
 * http://www.learning-layers.eu
 * Development is partly funded by the FP7 Programme of the European Commission under
 * Grant Agreement FP7-ICT-318209.
 * Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
package at.tugraz.sss.adapter.rest.v2.search;

import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import at.kc.tugraz.ss.service.search.datatypes.SSSearchLabel;
import at.kc.tugraz.ss.service.search.datatypes.SSSearchOpE;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "SSSearchPar")
@ApiModel(value = "search request parameter")
public class SSSearchRESTAPIV2Par{
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "whether the text content (if available) of entities should be scanned" )
  public Boolean             includeTextualContent      = null;
  
  @XmlElement
  @ApiModelProperty(
    value = "keywords to be used in textual content search",
    required = false)
  public List<String>        wordsToSearchFor           = null;
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "whether tags of entities should be looked to find entities" )
  public Boolean             includeTags                = null;
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "tags to be searched for" )
  public List<String>        tagsToSearchFor            = null;
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "whether authors of entities should be looked to find entities" )
  public Boolean             includeAuthors                = null;
  
  @ApiModelProperty( 
    required = false, 
    value = "authors to be searched for" )
  public List<SSUri>        authorsToSearchFor            = null;
  
  @XmlElement
  public void setAuthorsToSearchFor(final List<String> authorsToSearchFor) throws Exception{
    this.authorsToSearchFor = SSUri.get(authorsToSearchFor);
  }
  
  @XmlElement
  @ApiModelProperty(
    required = false, 
    value = "whether maturing indicators should be included in search")
  public Boolean             includeMIs                 = null;
    
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "maturing indicators of entities to be matched" )
  public List<String>        misToSearchFor             = null;
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "whether labels of entities should be scanned" )
  public Boolean             includeLabel               = null;
  
  @ApiModelProperty( 
    required = false, 
    value = "certain labels to be searched for" )
  public List<SSSearchLabel> labelsToSearchFor          = null;
  
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
  public List<SSSearchLabel> descriptionsToSearchFor    = null;
  
  @XmlElement
  public void setDescriptionsToSearchFor(final List<String> descriptionsToSearchFor) throws Exception{
    this.descriptionsToSearchFor = SSSearchLabel.get(descriptionsToSearchFor);
  }
  
  @ApiModelProperty( 
    required = false,
    value = "list of entity types to be considered for search exclusively " )
  public List<SSEntityE>     typesToSearchOnlyFor       = null;
  
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
  public List<SSUri>         entitiesToSearchWithin     = null;
  
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
    value = "unique identifier for the pages of a previous search result")
  public String              pagesID             = null;
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "number of the page to be requested from a previous search result")
  public Integer             pageNumber             = null;
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "minimum overall star rating the entity must have to be returned")
  public Integer              minRating             = null;
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "maximum overall star rating the entity must have to be returned")
  public Integer              maxRating             = null;
    
  @ApiModelProperty(
    required = false,
    value = "how results will be comined for query parameter separately (i.e. or | and; e.g. and: results have to match for all tags given in tagsToSearchFor)")
  public SSSearchOpE              localSearchOp     = SSSearchOpE.or;
  
  @XmlElement
  public void setLocalSearchOp(final String localSearchOp) throws Exception{
    this.localSearchOp = SSSearchOpE.get(localSearchOp);
  }
  
  @ApiModelProperty(
    required = false,
    value = "how results will be comined overall (i.e. or | and; e.g. and: results have to match all given tags labels)")
  public SSSearchOpE              globalSearchOp     = SSSearchOpE.or;
  
  @XmlElement
  public void setGlobalSearchOp(final String globalSearchOp) throws Exception{
    this.globalSearchOp = SSSearchOpE.get(globalSearchOp);
  }
    
  public SSSearchRESTAPIV2Par(){}
}
