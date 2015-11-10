/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package at.tugraz.sss.servs.kcprojwiki.impl;

import at.tugraz.sss.serv.SSFileU;
import at.tugraz.sss.servs.kcprojwiki.conf.SSKCProjWikiConf;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
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
  
  private final SSKCProjWikiConf conf;
  private final HttpClient       httpclient;
  private final String           token;
  private final String           sessionID;
  private final String           title;
  
  public SSKCProjWikiImportFct(final SSKCProjWikiConf conf) throws Exception{
    
    this.conf          = conf;
    this.title         = "14-Non-K-WissServer-1-Vor";
    this.httpclient    = HttpClients.createDefault();
    this.token         = loginFirstTime  ();
    this.sessionID     = loginSecondTime ();
    
    //    changeVorgangBasics   ("14-Non-K-WissServer-1-Vor");
    changeVorgangResources();
    
    logout();
  }
  
  private String changeVorgangResources() throws Exception{
    
    String         content    = getContent();
    final String   editToken  = getEditToken();
    final HttpPost httppost   = new HttpPost(conf.wikiURI + "api.php?action=edit");
    Integer        firstIndex, secondIndex;
    
    httppost.addHeader("Cookie", sessionID);
    
    while(content.contains("{{Works In Vorgang")){
    
      firstIndex = content.indexOf("{{Works In Vorgang");
    
      if(firstIndex == -1){
        continue;
      }
      
      secondIndex = content.indexOf("}}", firstIndex);
      content  = content.substring(0, firstIndex) + content.substring(secondIndex + 2);
    }
    
    final List<NameValuePair> urlParameters = new ArrayList<>();
    
    urlParameters.add(new BasicNameValuePair("title", title));
    urlParameters.add(new BasicNameValuePair("text",  content));
    urlParameters.add(new BasicNameValuePair("token", editToken));
    
    httppost.setEntity(new UrlEncodedFormEntity(urlParameters));
    
    final HttpResponse response   = httpclient.execute(httppost);
    final HttpEntity   entity     = response.getEntity();
    
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
  }
  
  private String getEditToken() throws Exception{
    
    final HttpGet httpget = 
      new HttpGet(conf.wikiURI + "api.php?action=query"
        + "&prop=info" 
        + "&intoken=edit" 
        + "&rvprp=timestamp"
        + "&indexpageids"
        + "&format=json"
        + "&titles=" + title);
    
    final HttpResponse response   = httpclient.execute(httpget);
    final HttpEntity   entity     = response.getEntity();
    
    InputStream        in         = null;

    if(entity == null){
      throw new Exception("getEditToken error 1");
    }
    
    try{
      in        = entity.getContent();
      JSONObject json = new JSONObject(SSFileU.readStreamText(in));
      JSONObject query = (JSONObject) json.get("query");
      JSONArray pageids = (JSONArray) query.get("pageids");
      String pageID = pageids.get(0).toString();
      JSONObject pages = (JSONObject) query.get("pages");
      JSONObject firstPage = (JSONObject) pages.get(pageID);
      return ((JSONObject) firstPage).get("edittoken").toString();
      
      }catch(Exception error){
      throw new Exception("getEditToken error 2");
    }finally{
      if(in != null){
        in.close();
      }
    }
  }
  
  private String changeVorgangBasics() throws Exception{
    
    final HttpGet httpget = 
      new HttpGet(conf.wikiURI + "api.php?action=sfautoedit"
        + "&form=Projekt-Vorgangsebene" 
        + "&target=" + title
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
        + "&Projekt-Vorgangsebene[Changes%20BMD]="           + "No"
      );
    
    
//            + "&Works%20In%20Vorgang[Employee]="                 + "Oliver%20Pimas"  
//        + "&Works%20In%20Vorgang[Spent%20Hours]="            + "111"
//        + "&Works%20In%20Vorgang[Total%20Hours]="            + "10"
    httpget.addHeader("Cookie", sessionID);
    
    final HttpResponse response   = httpclient.execute(httpget);
    final HttpEntity   entity     = response.getEntity();
    final JSONObject   json, query, pages, firstPage;
    final JSONArray    revisions, pageids;
    final String       content, pageID;
    
    InputStream        in         = null;

    if(entity == null){
      throw new Exception("changeVorgangBasics error 1");
    }
    
    try{
       in        = entity.getContent();
       System.out.println(SSFileU.readStreamText(in));
       
       return "";
    }catch(Exception error){
      throw new Exception("changeVorgangBasics error 2");
    }finally{
      if(in != null){
        in.close();
      }
    }
  }
  
  private List<BasicNameValuePair> getFirstLoginParams(){
    
    final List<BasicNameValuePair> params = new ArrayList<>();
    
    params.add(new BasicNameValuePair("lgname",     conf.userName));
    params.add(new BasicNameValuePair("lgpassword", conf.password));
    params.add(new BasicNameValuePair("lgdomain",   conf.domain));
    
    return params;
  }
  
  private List<BasicNameValuePair> getSecondLoginParams(){
    
    final List<BasicNameValuePair> params = new ArrayList<>();
    
    params.add(new BasicNameValuePair("lgname",     conf.userName));
    params.add(new BasicNameValuePair("lgpassword", conf.password));
    params.add(new BasicNameValuePair("lgdomain",   conf.domain));
    params.add(new BasicNameValuePair("lgtoken",    token));
    
    return params;
  }
  
  private String loginFirstTime() throws Exception{
    
    final HttpPost     post     = new HttpPost(conf.wikiURI + "api.php?action=login&format=json");
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
    
  private String loginSecondTime() throws Exception{
    
    final HttpPost     post     = new HttpPost(conf.wikiURI + "api.php?action=login&format=json");
    final HttpResponse response;
    final HttpEntity   entity;
    final JSONObject   json;
    final String       sessionID;
    InputStream        in  = null;
    
    post.setEntity(new UrlEncodedFormEntity(getSecondLoginParams(), "UTF-8"));
    
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
  
  private void logout() throws Exception{
    
    final HttpGet      httpget    = new HttpGet (conf.wikiURI + "api.php?action=logout&format=json");
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
  
  private String getContent() throws Exception{
    
    final HttpGet      httpget    = new HttpGet(conf.wikiURI + "api.php?action=query&indexpageids&prop=revisions&rvlimit=1&rvprop=content&format=json&titles=" + title);
  
    httpget.addHeader("Cookie", sessionID);
    
    final HttpResponse response   = httpclient.execute(httpget);
    final HttpEntity   entity     = response.getEntity();
    final JSONObject   json, query, pages, firstPage;
    final JSONArray    revisions, pageids;
    final String       content, pageID;
    
    InputStream        in         = null;

    if(entity == null){
      throw new Exception("getContent error 1");
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
      
       return content;
    }catch(Exception error){
      throw new Exception("getContent error 2");
    }finally{
      if(in != null){
        in.close();
      }
    }
  }
}
