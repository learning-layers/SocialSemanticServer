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
package at.tugraz.sss.servs.search.datatype;

import at.tugraz.sss.serv.conf.SSConf;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.servs.search.datatype.SSSearchLabel;
import at.tugraz.sss.serv.datatype.enums.SSSearchOpE;
import io.swagger.annotations.*;
import java.util.List;

@ApiModel
public class SSSearchRESTPar{
  
  @ApiModelProperty(
    value = "keywords to be used in textual content search in documents",
    required = false)
  public List<String>        documentContentsToSearchFor           = null;
  
  @ApiModelProperty( 
    required = false, 
    value = "tags to be searched for" )
  public List<String>        tagsToSearchFor            = null;
  
  @ApiModelProperty( 
    required = false, 
    value = "authors to be searched for" )
  public List<SSUri>        authorsToSearchFor            = null;
  
  public void setAuthorsToSearchFor(final List<String> authorsToSearchFor) throws SSErr{
    this.authorsToSearchFor = SSUri.get(authorsToSearchFor, SSConf.sssUri);
  }
  
  @ApiModelProperty( 
    required = false, 
    value = "certain labels to be searched for" )
  public List<SSSearchLabel> labelsToSearchFor          = null;
  
  public void setLabelsToSearchFor(final List<String> labelsToSearchFor) throws SSErr{
    this.labelsToSearchFor = SSSearchLabel.get(labelsToSearchFor);
  }
  
  @ApiModelProperty( 
    required = false, 
    value = "certain descriptions to be searched for" )
  public List<SSSearchLabel> descriptionsToSearchFor    = null;
  
  
  public void setDescriptionsToSearchFor(final List<String> descriptionsToSearchFor) throws SSErr{
    this.descriptionsToSearchFor = SSSearchLabel.get(descriptionsToSearchFor);
  }
  
  
  @ApiModelProperty( 
    required = false, 
    value = "whether the global search op shall hold between label and description, i.e., same keywords in label and description for AND" )
  public boolean             applyGlobalSearchOpBetweenLabelAndDescription     = false;
  
  @ApiModelProperty( 
    required = false,
    value = "list of entity types to be considered for search exclusively " )
  public List<SSEntityE>     typesToSearchOnlyFor       = null;
  
  
  public void setTypesToSearchOnlyFor(final List<String> typesToSearchOnlyFor) throws SSErr{
    this.typesToSearchOnlyFor = SSEntityE.get(typesToSearchOnlyFor);
  }
  
  
  @ApiModelProperty( 
    required = false, 
    value = "whether possibly recommended entities should be included in search results" )
  public boolean             includeRecommendedResults  = false;
  
  
  @ApiModelProperty(
    required = false,
    value = "size of the resulting pages")
  public Integer              pageSize             = null;
  
  
  @ApiModelProperty(
    required = false,
    value = "unique identifier for the pages of a previous search result")
  public String              pagesID             = null;
  
  
  @ApiModelProperty(
    required = false,
    value = "number of the page to be requested from a previous search result")
  public Integer             pageNumber             = null;
  
  
  @ApiModelProperty(
    required = false,
    value = "minimum overall star rating the entity must have to be returned")
  public Integer              minRating             = null;
  
  
  @ApiModelProperty(
    required = false,
    value = "maximum overall star rating the entity must have to be returned")
  public Integer              maxRating             = null;
  
    
  @ApiModelProperty(
    required = false,
    value = "start timestamp")
  public Long            startTime      = null;
  
  
  @ApiModelProperty(
    required = false,
    value = "end timestamp")
  public Long            endTime        = null;
    
  @ApiModelProperty(
    required = false,
    value = "how results will be comined for query parameter separately (i.e. or | and; e.g. and: results have to match for all tags given in tagsToSearchFor)")
  public SSSearchOpE              localSearchOp     = SSSearchOpE.or;
  
  public void setLocalSearchOp(final String localSearchOp) throws SSErr{
    this.localSearchOp = SSSearchOpE.get(localSearchOp);
  }
  
  @ApiModelProperty(
    required = false,
    value = "how results will be comined overall (i.e. or | and; e.g. and: results have to match all given tags labels)")
  public SSSearchOpE              globalSearchOp     = SSSearchOpE.or;
  
  public void setGlobalSearchOp(final String globalSearchOp) throws SSErr{
    this.globalSearchOp = SSSearchOpE.get(globalSearchOp);
  }
  
  @ApiModelProperty(
    required = false)
  public boolean             orderByLabel  = false;
  
  @ApiModelProperty(
    required = false)
  public boolean             orderByCreationTime  = false;
  
  public SSSearchRESTPar(){/* Do nothing because of only JSON Jackson needs this */ }
}
