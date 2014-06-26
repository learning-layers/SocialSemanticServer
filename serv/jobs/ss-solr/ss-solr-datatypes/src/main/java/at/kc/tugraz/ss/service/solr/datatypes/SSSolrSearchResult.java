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

import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.ss.service.solr.datatypes.enums.*;
import java.util.*;
import org.apache.solr.common.*;

public class SSSolrSearchResult {

  public final String id;
  
	public String title;
	public String content;
	public String language;
	public String keywords;
	public String abstracts;
  
	public static synchronized SSSolrSearchResult get(
    String id,
    String content,
    String language,
    String keywords,
    String title, 
    String abstracts){
		
    SSSolrSearchResult result = new SSSolrSearchResult(id);
    
    result.content   = content;
    result.language  = language;
    result.keywords  = keywords;
    result.title     = title;
    result.abstracts = abstracts;
    
    return result;
	}
  
  public static synchronized List<SSSolrSearchResult> get(final SolrDocumentList solrDocList) throws Exception {
    
    List<SSSolrSearchResult> result = new ArrayList<SSSolrSearchResult>();
    
		for(SolrDocument doc : solrDocList){
			
      String id = getFieldAsString(doc, SSSolrSearchFieldEnum.id);
			
      if (id.isEmpty()) {
        SSLogU.warn("document retrieved from solr does not have any ID. skipping...");
				continue;
			}
      
			result.add(get(
        id,
        getFieldAsString (doc, SSSolrSearchFieldEnum.docText),
        getFieldAsString (doc, SSSolrSearchFieldEnum.docLang),
        getFieldAsString (doc, SSSolrSearchFieldEnum.keywords),
        getFieldAsString (doc, SSSolrSearchFieldEnum.title),
        getFieldAsString (doc, SSSolrSearchFieldEnum.abstracT)));
		}
    
    return result;
	}
  
  /**
   * tries to convert a solr field into a String
   *
   * @param sdoc
   * @param field
   * @return empty string in case conversion was not successful
   */
  private static synchronized String getFieldAsString(
    final SolrDocument sdoc, 
    final SSSolrSearchFieldEnum    field) {
    
    Object tmp = sdoc.get(field.toString());
    
    if(tmp instanceof String) {
      return (String) tmp;
      
    }else if (tmp instanceof ArrayList) {
      
      @SuppressWarnings("unchecked")
      ArrayList<String> al = (ArrayList<String>) tmp;
      StringBuilder sb = new StringBuilder();
      for (String s : al) {
        sb.append(s).append(" ");
      }
      return sb.toString();
    } else {
      String x = "xx";
      if (tmp != null) {
        x = tmp.getClass() + "";
      }
      SSLogU.warn("field " + field + " could not be converted to any supported class. " + x);
      return "";
    }
  }
  
  private SSSolrSearchResult(String id){
  
    this.id = id;
  }
}


///**
//	 * @param numWords
//	 * @return the first <code>numWords</code> chars of the content
//	 */
//	public String getPseudoAbstract(int numWords) {
//		String[] words = content.split(" ");
//		String ret = "";
//		int cnt = 0;
//		for (String s : words) {
//			if (cnt >= numWords) {
//				break;
//			}
//			ret += s + " ";
//			cnt++;
//		}
//		return ret;
//	}


//	public void setAbstract(String abstr) {
//		this.abstracts = abstr;
//	}
//	
//	public String getAbstract() {
//		if (abstracts == null || abstracts.trim().isEmpty()) {
//			return getPseudoAbstract(40);
//		}
//		return abstracts;
//	}