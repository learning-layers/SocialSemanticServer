/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
