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
package at.tugraz.sss.serv;

import org.apache.commons.lang3.StringEscapeUtils;

public class SSMediaWikiU {

  public static final String revStart = "<rev xml:space=\"preserve\">";
	public static final String revEnd   = "</rev>";
  
  public static final String userID        = "UserID";
  public static final String timestamp     = "Timestamp";
  public static final String wikiID        = "WikiID";
  public static final String wikiText      = "WikiText";
  public static final String htmlText      = "HtmlText";
  
  public static String wikiContent(String xmlString){
		
    int startIndex = xmlString.indexOf(revStart);
		int endIndex   = xmlString.indexOf(revEnd);
		
    if(
      startIndex != -1 && 
      endIndex   != -1) {
			return StringEscapeUtils.unescapeHtml4(xmlString.substring(startIndex + revEnd.length(), endIndex));
		}
    
		return null;
	}
}
