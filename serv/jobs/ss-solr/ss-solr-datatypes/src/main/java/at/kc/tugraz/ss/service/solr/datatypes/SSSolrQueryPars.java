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
 package at.kc.tugraz.ss.service.solr.datatypes;

import at.kc.tugraz.socialserver.utils.*;
import at.kc.tugraz.ss.serv.solr.datatypes.SSSolrKeywordLabel;
import at.kc.tugraz.ss.service.solr.datatypes.enums.*;

/**
 * class that offers setting the field and the term to query for. add 
 * further restrictions (eg date-range) here and update {@link QueryParser}
 */
public class SSSolrQueryPars {

	public final SSSolrKeywordLabel    searchTerm;
	public final SSSolrSearchFieldEnum searchField;
  public final Integer               numRows;
	
	/**
	 * sets the field to the default SSSolrSearchField DOC_TEXT
	 */
	public SSSolrQueryPars(
    final SSSolrKeywordLabel  searchTerm, 
    final Integer             numRows) {
		
    this.searchTerm  = searchTerm;
		this.searchField = SSSolrSearchFieldEnum.docText;
    this.numRows     = numRows;
	}
	
	/**
	 * use in case you want to set your own field to query for than 
	 * the default one  
	 */
	public SSSolrQueryPars(
    final SSSolrKeywordLabel    searchTerm, 
    final SSSolrSearchFieldEnum field, 
    final Integer               numRows) {
		
    this.searchField = field;
		this.searchTerm  = searchTerm;
    this.numRows     = numRows;
	}
	
	public static synchronized SSSolrQueryPars getQueryParamsAll(
    final Integer numRows) throws Exception {
		
    return new SSSolrQueryPars(
      SSSolrKeywordLabel.get(SSSolrSearchFieldEnum.all.toString()), 
      SSSolrSearchFieldEnum.all, 
      numRows);
	}
  
  public String getQuery(){
    return searchField.getSolrField() + SSStrU.colon + searchTerm;
	}

	public String getQueryAll() {
		return SSSolrSearchFieldEnum.all + SSStrU.colon + SSSolrSearchFieldEnum.all;
	}
}