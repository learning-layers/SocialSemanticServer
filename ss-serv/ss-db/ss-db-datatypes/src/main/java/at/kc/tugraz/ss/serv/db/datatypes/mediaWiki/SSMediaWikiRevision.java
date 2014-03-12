/**
 * Copyright 2013 Graz University of Technology - KTI (Knowledge Technologies Institute)
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
package at.kc.tugraz.ss.serv.db.datatypes.mediaWiki;

public class SSMediaWikiRevision {
  
  public String userID      = null;
  public String wikiID      = null;
  public String timestamp   = null;
  public String wikiText    = null;
  public String htmlText    = null;
  
  public SSMediaWikiRevision(String userID, String wikiID, String timestamp, String wikiText, String htmlText) {
    this.userID     = userID;
    this.wikiID     = wikiID;
    this.timestamp  = timestamp;
    this.wikiText   = wikiText;
    this.htmlText   = htmlText;
  }
}
