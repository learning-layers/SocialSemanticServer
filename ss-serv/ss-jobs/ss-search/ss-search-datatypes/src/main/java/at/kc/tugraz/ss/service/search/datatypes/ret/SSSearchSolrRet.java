/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.kc.tugraz.ss.service.search.datatypes.ret;

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.ss.serv.jsonld.util.SSJSONLDU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.serv.datatypes.SSServRetI;
import at.kc.tugraz.ss.service.search.datatypes.SSSearchResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSSearchSolrRet extends SSServRetI{

  public List<SSSearchResult> searchResults = new ArrayList<SSSearchResult>();

  public static SSSearchSolrRet get(List<SSSearchResult> searchResults, SSMethU op){
    return new SSSearchSolrRet(searchResults, op);
  }
  
  private SSSearchSolrRet(List<SSSearchResult> searchResults, SSMethU op){
    
    super(op);
    
    this.searchResults = searchResults;
  }

  @Override
  public Map<String, Object> jsonLDDesc(){
    
    Map<String, Object> ld               = new HashMap<String, Object>();
    Map<String, Object> searchResultsObj = new HashMap<String, Object>();
    
    searchResultsObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSSearchResult.class.getName());
    searchResultsObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarU.searchResults, searchResultsObj);
    
    return ld;
  }
  
  /*************** getters to allow for json enconding ********************/
  public List<SSSearchResult> isSearchResults() {
    return searchResults;
  }
}
