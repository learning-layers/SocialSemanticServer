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
package at.kc.tugraz.ss.main;

import at.tugraz.sss.serv.SSFileU;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

public class SSMediaWikiWithLDAP{

  public SSMediaWikiWithLDAP() throws Exception{
    
    final HttpClient httpclient = HttpClients.createDefault();
    final String     token      = loginFirstTime  (httpclient);
    final String     sessionID  = loginSecondTime (httpclient, token);
    String           content    = queryContent    (httpclient, sessionID, "MyFirstVorgang");
    
    changeContent(httpclient, sessionID, "MyFirstVorgang");
    
    content    = queryContent    (httpclient, sessionID, "MyFirstVorgang");
    
    logout(httpclient);
  }
  
  private static String changeContent(
    final HttpClient httpclient,
    final String     cookie,
    final String     title) throws Exception{
    
    final HttpGet httpget = 
      new HttpGet("http://kcs-wmforum/knowiki/api.php?action=sfautoedit"
        + "&form=Projekt-Vorgangsebene" 
        + "&target=" + title
        + "&Projekt-Vorgangsebene[Project%20Number]="                   + "A13490adasf" 
        + "&Projekt-Vorgangsebene[Project%20Number%20Business%20Plan]=" + "000bus"
        + "&Projekt-Vorgangsebene[Project%20Name]="                     + "a%20name%20for%20the%20proj" 
        + "&Projekt-Vorgangsebene[Workpackage]="                        + "WP5" 
        + "&Projekt-Vorgangsebene[Project%20Type]="                     + "COMET" 
        + "&Projekt-Vorgangsebene[Project%20Type%20Detail]="            + "some%20details" 
        + "&Projekt-Vorgangsebene[Area]="                               + "KE" 
        + "&Projekt-Vorgangsebene[Business%20Partner]="            + "BP1,BP2" 
        + "&Projekt-Vorgangsebene[Scientific%20Partner]="          + "SP1,SP2" 
        + "&Projekt-Vorgangsebene[Responsible%20TL]="              + "User:Dtheiler" 
        + "&Projekt-Vorgangsebene[Responsible%20BM]="              + "User:Dtheiler" 
        + "&Projekt-Vorgangsebene[Responsible%20PM]="                + "User:Dtheiler" 
        + "&Projekt-Vorgangsebene[Start%20Overall%20Project]="       + "2012/02/16%2000:15:35"
        + "&Projekt-Vorgangsebene[End%20Overall%20Project]="         + "2014/02/16%2000:15:35" 
        + "&Projekt-Vorgangsebene[Export%20Date]="                   + "2014/10/16%2000:15:35"
        + "&Projekt-Vorgangsebene[KC%20Personnel%20Involved]="       + "User:dtheiler,%20User:sdennerlein,%20User:dkowald" 
        + "&Projekt-Vorgangsebene[Total%20Project%20Resources]="     + "300" 
        + "&Projekt-Vorgangsebene[Resources%20Used%20Month%20End]="    + "100" 
        + "&Projekt-Vorgangsebene[Project%20Progress]="            + "90" 
        + "&Projekt-Vorgangsebene[Reisekosten%20Projekt%20Budget]="  + "1500" 
        + "&Projekt-Vorgangsebene[Reisekosten%20Verbraucht]="      + "30" 
      );
    
    httpget.addHeader("Cookie", cookie);
    
    final HttpResponse response   = httpclient.execute(httpget);
    final HttpEntity   entity     = response.getEntity();
    final JSONObject   json, query, pages, firstPage;
    final JSONArray    revisions, pageids;
    final String       content, pageID;
    
    InputStream        in         = null;

    if(entity == null){
      throw new Exception("changeContent error 1");
    }
    
    try{
       in        = entity.getContent();
       System.out.println(SSFileU.readStreamText(in));
       
       return "";
    }catch(Exception error){
      throw new Exception("queryContent error 2");
    }finally{
      if(in != null){
        in.close();
      }
    }
    
//    
  }
  
  private static List<BasicNameValuePair> getFirstLoginParams(){
    
    final List<BasicNameValuePair> params = new ArrayList<>();
    
    params.add(new BasicNameValuePair("lgname",     "test"));
    params.add(new BasicNameValuePair("lgpassword", "test"));
    params.add(new BasicNameValuePair("lgdomain",   "know"));
    
    return params;
  }
  
  private static List<BasicNameValuePair> getSecondLoginParams(
    final String token){
    
    final List<BasicNameValuePair> params = new ArrayList<>();
    
    params.add(new BasicNameValuePair("lgname",     "test"));
    params.add(new BasicNameValuePair("lgpassword", "test"));
    params.add(new BasicNameValuePair("lgdomain",   "know"));
    params.add(new BasicNameValuePair("lgtoken",    token));
    
    return params;
  }
  
  private static String loginFirstTime(
    final HttpClient httpclient) throws Exception{
    
    final HttpPost     post     = new HttpPost("http://kcs-wmforum/knowiki/api.php?action=login&format=json");
    final HttpResponse response;
    final HttpEntity   entity;
    final JSONObject   json;
    final String       token;
    InputStream        in = null;
    
    post.setEntity(new UrlEncodedFormEntity(getFirstLoginParams(), "UTF-8"));
    
    response = httpclient.execute(post);
    entity   = response.getEntity();
    
    if(entity == null){
      throw new Exception("loginFirstTime error 1");
    }
    
    try{
      in    = entity.getContent();
      json  = new JSONObject(SSFileU.readStreamText(in));
      token = ((JSONObject)json.get("login")).get("token").toString();
      
      System.out.println(token);
      
      return token;
    }catch(Exception error){
      throw new Exception("loginFirstTime error 2");
    }finally{
      if(in != null){
        in.close();
      }
    }
  }
    
  private static String loginSecondTime(
    final HttpClient httpclient,
    final String     token) throws Exception{
    
    final HttpPost     post     = new HttpPost("http://kcs-wmforum/knowiki/api.php?action=login&format=json");
    final HttpResponse response;
    final HttpEntity   entity;
    final JSONObject   json;
    final String       sessionID;
    InputStream        in  = null;
    
    post.setEntity(new UrlEncodedFormEntity(getSecondLoginParams(token), "UTF-8"));
    
    response = httpclient.execute(post);
    entity   = response.getEntity();
    
    if(entity == null){
      throw new Exception("loginSecondTime error 1");
    }
    
    try{
      in        = entity.getContent();
      json      = new JSONObject(SSFileU.readStreamText(in));
      sessionID = ((JSONObject)json.get("login")).get("sessionid").toString();
      
      System.out.println(sessionID);
      
      return sessionID;
    }catch(Exception error){
      throw new Exception("loginSecondTime error 2");
    }finally{
      if(in != null){
        in.close();
      }
    }
  }
  
  private static void logout(
    final HttpClient httpclient) throws Exception{
    
    final HttpGet      httpget    = new HttpGet ("http://kcs-wmforum/knowiki/api.php?action=logout&format=json");
    final HttpResponse response   = httpclient.execute(httpget);
    final HttpEntity   entity     = response.getEntity();
    InputStream        in         = null;

    if(entity == null){
      throw new Exception("logout error 1");
    }
    
    try{
       in = entity.getContent();
        
       System.out.println("logout: " + SSFileU.readStreamText(in));
    }catch(Exception error){
      throw new Exception("logout error 2");
    }finally{
      if(in != null){
        in.close();
      }
    }
  }
  
  private static String queryContent(
    final HttpClient httpclient,
    final String     cookie,
    final String     title) throws Exception{
    
    final HttpGet      httpget    = new HttpGet("http://kcs-wmforum/knowiki/api.php?action=query&indexpageids&prop=revisions&rvlimit=1&rvprop=content&format=json&titles=" + title);
  
    httpget.addHeader("Cookie", cookie);
    
    final HttpResponse response   = httpclient.execute(httpget);
    final HttpEntity   entity     = response.getEntity();
    final JSONObject   json, query, pages, firstPage;
    final JSONArray    revisions, pageids;
    final String       content, pageID;
    
    InputStream        in         = null;

    if(entity == null){
      throw new Exception("queryContent error 1");
    }
    
    try{
       in        = entity.getContent();
       json      = new JSONObject(SSFileU.readStreamText(in));
       query     = (JSONObject) json.get("query");
       pageids   = (JSONArray) query.get("pageids");
       pageID    = pageids.get(0).toString();
       pages     = (JSONObject) query.get("pages");
       firstPage = (JSONObject) pages.get(pageID);
       revisions = (JSONArray)  firstPage.get("revisions");
       content   = ((JSONObject) revisions.get(0)).get("*").toString();
      
       System.out.println("content: " + content);
       
       return content;
    }catch(Exception error){
      throw new Exception("queryContent error 2");
    }finally{
      if(in != null){
        in.close();
      }
    }
  }
}
