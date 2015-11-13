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
package at.tugraz.sss.servs.kcprojwiki.impl;

import at.tugraz.sss.serv.SSEncodingU;
import at.tugraz.sss.serv.SSFileU;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.servs.kcprojwiki.conf.SSKCProjWikiConf;
import at.tugraz.sss.servs.kcprojwiki.datatype.SSKCProjWikiVorgang;
import at.tugraz.sss.servs.kcprojwiki.datatype.SSKCProjWikiVorgangEmployeeResource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

public class SSKCProjWikiImportFct {

  private static final String valueProjektVorgangsebene             = "Projekt-Vorgangsebene";
  private static final String valueWorksInVorgang                   = "{{Works In Vorgang";
  private static final String propertyCategoryProjektVorgangsebene  = "[[Category:Projekt-Vorgangsebene]]";
  private static final String propertyCategoryProjekt               = "[[Category:Projekt]]";
  private static final String propertyStartProjectNumber            = "[[Project%20Number::";
  private static final String propertyStartVorgangNumber            = "[[Vorgang%20Number::";
  private static final String propertyTotalProjectResources         = "Total%20Project%20Resources";
  private static final String propertyResourcesUsedMonthEnd         = "Resources%20Used%20Month%20End";
  private static final String valueCookie                           = "Cookie";
  private static final String pathActionQuery                       = "api.php?action=query";
  private static final String pathActionLogin                       = "api.php?action=login";
  private static final String pathActionLogout                      = "api.php?action=logout";
  private static final String pathActionEdit                        = "api.php?action=edit";
  private static final String pathActionSFAutoEdit                  = "api.php?action=sfautoedit";
  private static final String pathActionAsk                         = "api.php?action=ask";
  private static final String valueForm                             = "form=";
  private static final String valueTarget                           = "target=";
  private static final String valueChangesBMD                       = "Changes%20BMD";
  private static final String valueTitle                            = "title";
  private static final String valueMUrlform                         = "mUrlform";
  private static final String valueText                             = "text";
  private static final String valueLogin                            = "login";
  private static final String valueSessionId                        = "sessionid";
  private static final String valueToken                            = "token";
  private static final String valueLgname                           = "lgname";
  private static final String valueLgpassword                       = "lgpassword";
  private static final String valueLgdomain                         = "lgdomain";
  private static final String valueLgtoken                          = "lgtoken";
  private static final String valueFormatJson                       = "format=json";
  private static final String valueIndexPageIds                     = "indexpageids";
  private static final String valueProp                             = "prop";
  private static final String valueRevisions                        = "revisions";
  private static final String valueRvlimit                          = "rvlimit";
  private static final String valueRvprop                           = "rvprop";
  private static final String valueContent                          = "content";
  private static final String valueTitles                           = "titles";
  private static final String valueQuery                            = "query";
  private static final String valuePageIds                          = "pageids";
  private static final String valueResults                          = "results";
  private static final String valueAsk                              = "ask";
  private static final String valueItems                            = "items";
  private static final String valuePages                            = "pages";
  private static final String valueEditToken                        = "edittoken";
  private static final String valueTimestamp                        = "timestamp";
  private static final String valueIntoken                          = "intoken";
  private static final String valueEdit                             = "edit";
  private static final String valueInfo                             = "info";
  private static final String valueQ                                = "q";
  
  private final SSKCProjWikiConf conf;
  private final HttpClient       httpclient;
  private String                 token;
  private String                 sessionID;
  
  public SSKCProjWikiImportFct(final SSKCProjWikiConf conf) throws Exception{
    
    this.conf          = conf;
    this.httpclient    = HttpClients.createDefault();
  }
  
  public void changeVorgangBasics(final SSKCProjWikiVorgang vorgang) throws Exception{
    
    InputStream in = null;
    
    try{
      
      final HttpGet httpget =
        new HttpGet(
          conf.wikiURI
            + pathActionSFAutoEdit
            + SSStrU.ampersand + valueForm                 + valueProjektVorgangsebene
            + SSStrU.ampersand + valueTarget               + vorgang.title
            + SSStrU.ampersand + valueProjektVorgangsebene + SSStrU.squareBracketOpen + propertyTotalProjectResources + SSStrU.squareBracketClose + SSStrU.equal + vorgang.totalResources
            + SSStrU.ampersand + valueProjektVorgangsebene + SSStrU.squareBracketOpen + propertyResourcesUsedMonthEnd + SSStrU.squareBracketClose + SSStrU.equal + vorgang.usedResources
          
          //            + SSStrU.ampersand + valueProjektVorgangsebene + SSStrU.squareBracketOpen + valueChangesBMD + SSStrU.squareBracketClose + SSStrU.equal + "No"
          
//        + "&Projekt-Vorgangsebene[Project%20Number]="                   + "A13490adasf"
//        + "&Projekt-Vorgangsebene[Project%20Number%20Business%20Plan]=" + "000bus"
//        + "&Projekt-Vorgangsebene[Project%20Name]="                     + "a%20name%20for%20the%20proj"
//        + "&Projekt-Vorgangsebene[Workpackage]="                        + "WP5"
//        + "&Projekt-Vorgangsebene[Project%20Type]="                     + "COMET"
//        + "&Projekt-Vorgangsebene[Project%20Type%20Detail]="            + "some%20details"
//        + "&Projekt-Vorgangsebene[Area]="                               + "KE"
//        + "&Projekt-Vorgangsebene[Business%20Partner]="            + "BP1,BP2"
//        + "&Projekt-Vorgangsebene[Scientific%20Partner]="          + "SP1,SP2"
//        + "&Projekt-Vorgangsebene[Responsible%20TL]="              + "User:Dtheiler"
//        + "&Projekt-Vorgangsebene[Responsible%20BM]="              + "User:Dtheiler"
//        + "&Projekt-Vorgangsebene[Responsible%20PM]="                + "User:Dtheiler"
//        + "&Projekt-Vorgangsebene[Start%20Overall%20Project]="       + "2012/02/16%2000:15:35"
//        + "&Projekt-Vorgangsebene[End%20Overall%20Project]="         + "2014/02/16%2000:15:35"
//        + "&Projekt-Vorgangsebene[Export%20Date]="                   + "2014/10/16%2000:15:35"
//        + "&Projekt-Vorgangsebene[KC%20Personnel%20Involved]="       + "User:dtheiler,%20User:sdennerlein,%20User:dkowald"
//        + "&Projekt-Vorgangsebene[Total%20Project%20Resources]="     + "300"
//        + "&Projekt-Vorgangsebene[Resources%20Used%20Month%20End]="    + "100"
//        + "&Projekt-Vorgangsebene[Project%20Progress]="            + "90"
//        + "&Projekt-Vorgangsebene[Reisekosten%20Projekt%20Budget]="  + "1500"
//        + "&Projekt-Vorgangsebene[Reisekosten%20Verbraucht]="      + "30"
        );
      
//            + "&Works%20In%20Vorgang[Employee]="                 + "Oliver%20Pimas"
//        + "&Works%20In%20Vorgang[Spent%20Hours]="            + "111"
//        + "&Works%20In%20Vorgang[Total%20Hours]="            + "10"
      
      httpget.addHeader(valueCookie, sessionID);
      
      in = httpclient.execute(httpget).getEntity().getContent();
      
      System.out.println(SSFileU.readStreamText(in));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(new Exception("changing vorgang basics failed"));
    }finally{
      if(in != null){
        in.close();
      }
    }
  }
  
  public void changeVorgangEmployeeResources(final SSKCProjWikiVorgang vorgang) throws Exception{
    
    InputStream in = null;
    
    try{
      final String              editToken  = getEditToken      (vorgang.title);
      final HttpPost            httppost   = new HttpPost      (conf.wikiURI + pathActionEdit);
      final List<NameValuePair> postPars   = new ArrayList<>();
      String         content               = getWikiPageContent(vorgang.title);
      Integer        firstIndex, secondIndex;
      
      httppost.addHeader(valueCookie, sessionID);

      while(content.contains(valueWorksInVorgang)){
        
        firstIndex = content.indexOf(valueWorksInVorgang);
        
        if(firstIndex == -1){
          continue;
        }
        
        secondIndex = content.indexOf(SSStrU.curlyBracketClose + SSStrU.curlyBracketClose, firstIndex);
        content     = content.substring(0, firstIndex) + content.substring(secondIndex + 2);
      }
      
      content  = content.trim();
      content += System.lineSeparator();
        
      for(SSKCProjWikiVorgangEmployeeResource employeeResource : vorgang.employeeResources.values()){
       
        content += 
          valueWorksInVorgang 
          + SSStrU.pipe
          + employeeResource.employee 
          + SSStrU.pipe 
          + employeeResource.used 
          + SSStrU.pipe 
          + employeeResource.total
          + SSStrU.curlyBracketClose + SSStrU.curlyBracketClose 
          + System.lineSeparator();
      }
      
      content = content.trim();
      
      postPars.add(new BasicNameValuePair(valueTitle, vorgang.title));
      postPars.add(new BasicNameValuePair(valueText,  content));
      postPars.add(new BasicNameValuePair(valueToken, editToken));
      
      httppost.setEntity(new UrlEncodedFormEntity(postPars));
      
      in = httpclient.execute(httppost).getEntity().getContent();
      
      System.out.println(SSFileU.readStreamText(in));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(new Exception("changing vorgang resources failed"));
    }finally{
      if(in != null){
        in.close();
      }
    }
  }
  
  public String getVorgangPageTitleByVorgangNumber(final String vorgangNumber) throws Exception{
    
    InputStream in = null;
    
    try{
      
      //      http://mint/projwiki_dieter/api.php?action=ask&q=[[Category:Projekt-Vorgangsebene]][[Project%20Number::20143516]]
      final JSONObject json, results, ask, item, title;
      final JSONArray items;
        
      final HttpGet httpget =
        new HttpGet(
          conf.wikiURI
            + pathActionAsk
            + SSStrU.ampersand + valueQ + SSStrU.equal
            + propertyCategoryProjektVorgangsebene
            + propertyStartVorgangNumber + vorgangNumber + SSStrU.squareBracketClose + SSStrU.squareBracketClose 
            + SSStrU.ampersand + valueFormatJson);
      
      httpget.addHeader(valueCookie, sessionID);
      
      in       = httpclient.execute(httpget).getEntity().getContent();
      json     = new JSONObject(SSFileU.readStreamText(in));
      ask      = (JSONObject) json.get      (valueAsk);
      results  = (JSONObject) ask.get       (valueResults);
      items    = (JSONArray)  results.get   (valueItems);
      item     = (JSONObject) items.get(0);
      title    = (JSONObject) item.get      (valueTitle);
      
      return (String) title.get(valueMUrlform);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(new Exception("retrieving vorgang page title from vorgang number failed"));
      return null;
    }finally{
      
      if(in != null){
        in.close();
      }
    }
  }
  
  public String getVorgangPageTitleByProjectNumber(final String projectNumber) throws Exception{
    
    InputStream in = null;
    
    try{
      
      //      http://mint/projwiki_dieter/api.php?action=ask&q=[[Category:Projekt-Vorgangsebene]][[Project%20Number::20143516]]
      final JSONObject json, results, ask, item, title;
      final JSONArray items;
        
      final HttpGet httpget =
        new HttpGet(
          conf.wikiURI
            + pathActionAsk
            + SSStrU.ampersand + valueQ + SSStrU.equal
            + propertyCategoryProjektVorgangsebene
            + propertyStartProjectNumber + projectNumber + SSStrU.squareBracketClose + SSStrU.squareBracketClose 
            + SSStrU.ampersand + valueFormatJson);
      
      httpget.addHeader(valueCookie, sessionID);
      
      in       = httpclient.execute(httpget).getEntity().getContent();
      json     = new JSONObject(SSFileU.readStreamText(in));
      ask      = (JSONObject) json.get      (valueAsk);
      results  = (JSONObject) ask.get       (valueResults);
      items    = (JSONArray)  results.get   (valueItems);
      item     = (JSONObject) items.get(0);
      title    = (JSONObject) item.get      (valueTitle);
      
      return (String) title.get(valueMUrlform);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(new Exception("retrieving vorgang title from project number failed"));
      return null;
    }finally{
      
      if(in != null){
        in.close();
      }
    }
  }
  
  public String getProjectPageTitleByProjectNumber(final Integer projectNumber) throws Exception{
    
    InputStream in = null;
    
    try{
      
      //      http://mint/projwiki_dieter/api.php?action=ask&q=[[Category:Projekt]][[Project%20Number::20143516]]
      final JSONObject json, results, ask, item, title;
      final JSONArray items;
        
      final HttpGet httpget =
        new HttpGet(
          conf.wikiURI
            + pathActionAsk
            + SSStrU.ampersand + valueQ + SSStrU.equal
            + propertyCategoryProjekt
            + propertyStartProjectNumber + projectNumber + SSStrU.squareBracketClose + SSStrU.squareBracketClose 
            + SSStrU.ampersand + valueFormatJson);
      
      httpget.addHeader(valueCookie, sessionID);
      
      in       = httpclient.execute(httpget).getEntity().getContent();
      json     = new JSONObject(SSFileU.readStreamText(in));
      ask      = (JSONObject) json.get     (valueAsk);
      results  = (JSONObject) ask.get      (valueResults);
      items    = (JSONArray)  results.get  (valueItems);
      item     = (JSONObject) items.get(0);
      title    = (JSONObject) item.get     (valueTitle);
      
      return (String) title.get(valueMUrlform);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(new Exception("retrieving project page title from project number failed"));
      return null;
    }finally{
      
      if(in != null){
        in.close();
      }
    }
  }
  
  private void loginFirstTime() throws Exception{
    
    InputStream in = null;
    
    try{
      
      final HttpResponse response;
      final JSONObject   json;
      final HttpPost     post = 
        new HttpPost(
          conf.wikiURI 
            + pathActionLogin 
            + SSStrU.ampersand + valueFormatJson);
      
      post.setEntity(new UrlEncodedFormEntity(getFirstLoginParams(), SSEncodingU.utf8.toString()));
      
      response = httpclient.execute(post);
      in       = response.getEntity().getContent();
      json     = new JSONObject(SSFileU.readStreamText(in));
      token    = ((JSONObject)json.get(valueLogin)).get(valueToken).toString();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(new Exception("first login failed"));
    }finally{
      if(in != null){
        in.close();
      }
    }
  }
    
  private void loginSecondTime() throws Exception{
    
    InputStream in  = null;
    
    try{
      
      final HttpResponse response;
      final JSONObject   json;
      final HttpPost     post =
        new HttpPost(
          conf.wikiURI
            + pathActionLogin
            + SSStrU.ampersand + valueFormatJson);
      
      post.setEntity(new UrlEncodedFormEntity(getSecondLoginParams(), SSEncodingU.utf8.toString()));
      
      response  = httpclient.execute(post);
      in        = response.getEntity().getContent();
      json      = new JSONObject(SSFileU.readStreamText(in));
      sessionID = ((JSONObject)json.get(valueLogin)).get(valueSessionId).toString();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(new Exception("second login failed"));
    }finally{
      if(in != null){
        in.close();
      }
    }
  }
  
  private void logout() throws Exception{

    try{
      httpclient.execute(
        new HttpGet(
          conf.wikiURI
          + pathActionLogout 
          + SSStrU.ampersand + valueFormatJson));
      
    }catch(Exception error){
      SSLogU.warn("logout failed");
    }
  }
  
  private String getWikiPageContent(final String title) throws Exception{

    InputStream in = null;
    
    try{
//            &&prop=revisions&rvlimit=1&rvprop=content&format=json&titles=" + title);      
      
      final HttpGet httpget = 
        new HttpGet(
          conf.wikiURI 
            + pathActionQuery
            + SSStrU.ampersand + valueIndexPageIds
            + SSStrU.ampersand + valueProp    + SSStrU.equal   + valueRevisions
            + SSStrU.ampersand + valueRvlimit + SSStrU.equal   + "1"
            + SSStrU.ampersand + valueRvprop  + SSStrU.equal   + valueContent
            + SSStrU.ampersand + valueTitles  + SSStrU.equal   + title
            + SSStrU.ampersand + valueFormatJson);
      
      httpget.addHeader(valueCookie, sessionID);
      
      final HttpResponse response   = httpclient.execute(httpget);
      final JSONObject   json, query, pages, firstPage;
      final JSONArray    revisions, pageids;
      final String       content, pageID;
      
      in        = response.getEntity().getContent();
      json      = new JSONObject(SSFileU.readStreamText(in));
      query     = (JSONObject)  json.get(valueQuery);
      pageids   = (JSONArray)   query.get(valuePageIds);
      pageID    = pageids.get(0).toString();
      pages     = (JSONObject)  query.get(valuePages);
      firstPage = (JSONObject)  pages.get(pageID);
      revisions = (JSONArray)   firstPage.get(valueRevisions);
      content   = ((JSONObject) revisions.get(0)).get("*").toString();
      
      return content;
    }catch(Exception error){
      SSServErrReg.regErrThrow(new Exception("content retrieval failed"));
      return null;
    }finally{
      if(in != null){
        in.close();
      }
    }
  }
  
  private String getEditToken(final String title) throws Exception{
    
    InputStream in = null;
    
    try{
      
      final HttpGet httpget =
        new HttpGet(
          conf.wikiURI 
            + pathActionQuery
            + SSStrU.ampersand + valueIndexPageIds
            + SSStrU.ampersand + valueProp    + SSStrU.equal + valueInfo
            + SSStrU.ampersand + valueIntoken + SSStrU.equal + valueEdit
            + SSStrU.ampersand + valueRvprop  + SSStrU.equal + valueTimestamp//"&rvprp=timestamp"
            + SSStrU.ampersand + valueTitles  + SSStrU.equal + title
            + SSStrU.ampersand + valueFormatJson);
      
      final HttpResponse response   = httpclient.execute(httpget);
      final HttpEntity   entity     = response.getEntity();
      JSONObject json, query, pages, firstPage;
      JSONArray pageids;
      String pageID;
        
      in        = entity.getContent();
      json      = new JSONObject(SSFileU.readStreamText(in));
      query     = (JSONObject) json.get(valueQuery);
      pageids   = (JSONArray)  query.get(valuePageIds);
      pageID    = pageids.get(0).toString();
      pages     = (JSONObject) query.get(valuePages);
      firstPage = (JSONObject) pages.get(pageID);
      
      return firstPage.get(valueEditToken).toString();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(new Exception("edit token retrieval failed"));
      return null;
    }finally{
      
      if(in != null){
        in.close();
      }
    }
  }

  public void start() throws Exception{
    
    try{
      loginFirstTime  ();
      loginSecondTime ();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void end() throws Exception{
    
    try{
      logout();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private List<BasicNameValuePair> getFirstLoginParams() throws Exception{
    
    try{
      final List<BasicNameValuePair> params = new ArrayList<>();
      
      params.add(new BasicNameValuePair(valueLgname,     conf.userName));
      params.add(new BasicNameValuePair(valueLgpassword, conf.password));
      params.add(new BasicNameValuePair(valueLgdomain,   conf.domain));
      
      return params;
    }catch(Exception error){
      SSServErrReg.regErrThrow(new Exception("first login failed"));
      return null;
    }
  }
  
  private List<BasicNameValuePair> getSecondLoginParams() throws Exception{
    
    try{
      final List<BasicNameValuePair> params = getFirstLoginParams();
      
      params.add(new BasicNameValuePair(valueLgtoken,    token));
      
      return params;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(new Exception("second login failed"));
      return null;
    }
  }
}