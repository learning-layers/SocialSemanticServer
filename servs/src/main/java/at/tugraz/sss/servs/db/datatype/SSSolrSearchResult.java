/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
 /**
 * @author Dieter Theiler
 */

package at.tugraz.sss.servs.db.datatype;

import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.util.SSLogU;
import java.util.*;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

public class SSSolrSearchResult {

  public final String id;
  
	public static SSSolrSearchResult get(
    final String id){
		
    return new SSSolrSearchResult(id);
	}
  
  public static synchronized List<SSSolrSearchResult> get(final SolrDocumentList solrDocList) throws SSErr {
    
    final List<SSSolrSearchResult> result = new ArrayList<>();
    
		for(SolrDocument doc : solrDocList){
			
			result.add(
        get(
          getFieldAsString (
            doc, 
            SSSolrSearchFieldE.id)));
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
    final SSSolrSearchFieldE    field) {
    
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
      SSLogU.warn("field " + field + " could not be converted to any supported class. " + x, null);
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