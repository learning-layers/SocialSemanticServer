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

import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.SSErrE;
import at.tugraz.sss.serv.util.SSEncodingU;
import at.tugraz.sss.serv.util.SSFileU;
import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.servs.kcprojwiki.conf.SSKCProjWikiConf;
import at.tugraz.sss.servs.kcprojwiki.datatype.SSKCProjWikiVorgang;
import at.tugraz.sss.servs.kcprojwiki.datatype.SSKCProjWikiVorgangEmployeeResource;
import java.io.*;
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

public class SSKCProjWikiImportCommons {
  
  private final HttpClient       httpclient;
  private String                 token;
  private String                 sessionID;
  
  public SSKCProjWikiImportCommons(){
    this.httpclient    = HttpClients.createDefault();
  }
  
  public String createVorgangTitle(
    final SSKCProjWikiVorgang   vorgang) throws SSErr{
    
    try{
      return vorgang.vorgangNumber + SSStrU.dash + vorgang.projectAcronym + SSStrU.dash + SSMediaWikiLangE.Vor;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public String createProjectTitle(
    final SSKCProjWikiVorgang vorgang) throws SSErr{
    
    try{
      return vorgang.projectNumber + SSStrU.dash + vorgang.projectAcronym + SSStrU.dash + SSMediaWikiLangE.Pro;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public String createProject(
    final SSKCProjWikiConf      conf,
    final SSKCProjWikiVorgang   vorgang) throws SSErr{
    
    try{
      
      if(!conf.createVorgaenge){
        return null;
      }
      
      final String              projectTitle          = createProjectTitle  (vorgang);
      final String              editToken             = getWikiPageEditToken(conf, projectTitle);
      final List<NameValuePair> postPars              = new ArrayList<>();
      final HttpResponse        response;
      final HttpPost            httpPost              =
        new HttpPost(
          conf.wikiURI
            + SSMediaWikiLangE.apiActionEdit
            + SSStrU.ampersand
            + SSMediaWikiLangE.formatEqualsJson);
      
      String content = SSStrU.empty;
      
      httpPost.addHeader(SSMediaWikiLangE.Cookie.toString(), sessionID);
      
      content +=
        SSStrU.doubleCurlyBracketOpen
        + SSMediaWikiLangE.Projekt
        + System.lineSeparator()
        + SSStrU.pipe
        + SSMediaWikiLangE.ProjectBlankNumber
        + SSStrU.equal
        + vorgang.projectNumber
        + System.lineSeparator()
        + SSStrU.pipe
        + SSMediaWikiLangE.ProjectBlankName
        + SSStrU.equal
        + vorgang.projectName
        + System.lineSeparator()
        + SSStrU.doubleCurlyBracketClose
        + System.lineSeparator()
        + "= '''Project Description''' =\n"
        + "<span style=\"color:DARKRED\"> (in case of COMET projects enter description from business plan)</span>\n"
        + "-enter text here- \n"
        + "= '''Project Status''' =\n"
        + "== <span style=\"color:DARKBLUE\"> '''H1 (January - June)''' ==\n"
        + "<span style=\"color:DARKRED\"> (Enter short summary of project progress here. In case of a COMET project please refer to requirements of business plan!)</span>\n"
        + "-enter text here-\n"
        + "== <span style=\"color:DARKBLUE\"> '''H2 (July - December)''' ==\n"
        + "<span style=\"color:DARKRED\"> (Enter short summary of project progress here. In case of a COMET project please refer to requirements of business plan!)</span>\n"
        + "-enter text here-\n"
        + "<headertabs />\n"
        + "__NOTOC__";
      
      postPars.add(new BasicNameValuePair(SSMediaWikiLangE.title.toString(), projectTitle));
      postPars.add(new BasicNameValuePair(SSMediaWikiLangE.text.toString(),  content.trim()));
      postPars.add(new BasicNameValuePair(SSMediaWikiLangE.token.toString(), editToken));
      
      httpPost.setEntity(new UrlEncodedFormEntity(postPars));
      
      response = httpclient.execute(httpPost);
      
      parseUpdateResponse(response, projectTitle);
      
      SSLogU.info("created project " + projectTitle + SSStrU.blank + vorgang.projectNumber);
      
      return projectTitle;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public String createVorgang(
    final SSKCProjWikiConf      conf,
    final SSKCProjWikiVorgang   vorgang) throws SSErr{
    
    try{
      
      if(!conf.createVorgaenge){
        return null;
      }
      
      final String              vorgangTitle          = createVorgangTitle  (vorgang);
      final String              editToken             = getWikiPageEditToken(conf, vorgangTitle);
      final List<NameValuePair> postPars              = new ArrayList<>();
      final HttpResponse        response;
      final HttpPost            httpPost              =
        new HttpPost(
          conf.wikiURI
            + SSMediaWikiLangE.apiActionEdit
            + SSStrU.ampersand
            + SSMediaWikiLangE.formatEqualsJson);
      
      String content = SSStrU.empty;
      
      httpPost.addHeader(SSMediaWikiLangE.Cookie.toString(), sessionID);
      
      content +=
        SSStrU.doubleCurlyBracketOpen
        + SSMediaWikiLangE.ProjektVorgangsebene
        + System.lineSeparator()
        + SSStrU.pipe
        + SSMediaWikiLangE.VorgangBlankNumber
        + SSStrU.equal
        + vorgang.vorgangNumber
        + System.lineSeparator()
        + SSStrU.pipe
        + SSMediaWikiLangE.VorgangBlankName
        + SSStrU.equal
        + vorgang.vorgangName
        + System.lineSeparator()
        + SSStrU.pipe
        + SSMediaWikiLangE.ProjectBlankNumber
        + SSStrU.equal
        + vorgang.projectNumber
        + System.lineSeparator()
        + SSStrU.doubleCurlyBracketClose
        + System.lineSeparator()
        + "= '''Management & Resources''' =\n"
        + "== <span style=\"color:NAVY\"> '''Which changes in the BMD Data are necessary?''' ==\n"
        + "-enter text here- \n"
        + "== <span style=\"color:NAVY\"> '''Is a shortage of resources (time or personnel) to be expected? If yes: what, when, for how long.''' ==\n"
        + "== <span style=\"color:ROYALBLUE\"> '''---> Any other comments concerning the resource status?''' ==\n"
        + "-enter text here-\n"
        + "== <span style=\"color:NAVY\"> '''Changes concerning participating partners?''' ==\n"
        + "-enter text here-\n"
        + "== <span style=\"color:NAVY\"> '''Changes concerning project content?'''==\n"
        + "-enter text here-\n"
        + "== <span style=\"color: ROYALBLUE\"> '''---> Any other comments concerning management issues?'''==\n"
        + "-enter text here-\n"
        + "= '''Motivation''' =\n"
        + "== <span style=\"color:NAVY\"> '''Are there issues within the KC-team (co-operation, communication, motivation) that should be addressed?''' ==\n"
        + "-enter text here-\n"
        + "== <span style=\"color:NAVY\"> '''What are the reasons and what could be done to solve the issues?''' ==\n"
        + "-enter text here-\n"
        + "== <span style=\"color:ROYALBLUE\"> ''' ---> Any other comments concerning the motivation within KC?''' ==\n"
        + "-enter text here-\n"
        + "== <span style=\"color:NAVY\"> '''Are there issues concerning the communication with the partners that should be addressed?''' ==\n"
        + "-enter text here-\n"
        + "== <span style=\"color:NAVY\"> '''Are there issues concerning the co-operation (data exchange, work done by partners, ...) with the partners that should be addressed?'''==\n"
        + "-enter text here-\n"
        + "== <span style=\"color:ROYALBLUE\"> '''---> Any other comments concerning the motivation of the project partners?''' ==\n"
        + "-enter text here-\n"
        + "= '''Project results''' =\n"
        + "== <span style=\"color:NAVY\"> '''What has happened within the project?'''==\n"
        + "-enter text here-\n"
        + "== <span style=\"color:NAVY\"> '''Have any important goals, milestones, etc. been reached?'''==\n"
        + "<span style=\"color:FORESTGREEN\"> Please add the '''month''' in which they were reached! </span>\n"
        + "-enter text here-\n"
        + "== <span style=\"color:NAVY\"> '''Did any special results and achievements take place (e.g. highlights, prototypes, products, ...)''' ==\n"
        + "<span style=\"color:FORESTGREEN\"> Please add the '''month''' in which they took place! </span>\n"
        + "-enter text here-\n"
        + "== <span style=\"color:NAVY\"> '''Were any important workshops or meetings organised?''' ==\n"
        + "<span style=\"color:FORESTGREEN\"> Please add the '''month''' in which they took place! </span>\n"
        + "-enter text here-\n"
        + "== <span style=\"color:NAVY\"> '''Check with defined risks: Is attention needed anywhere?''' ==\n"
        + "<span style=\"color:FORESTGREEN\"> Please add the relevant '''month'''! </span>\n"
        + "-enter text here-\n"
        + "== <span style=\"color:NAVY\"> '''---> Any other comments concerning project results?''' ==\n"
        + "-enter text here-\n"
        + "<headertabs />\n"
        + "__NOTOC__";
      
      postPars.add(new BasicNameValuePair(SSMediaWikiLangE.title.toString(), vorgangTitle));
      postPars.add(new BasicNameValuePair(SSMediaWikiLangE.text.toString(),  content.trim()));
      postPars.add(new BasicNameValuePair(SSMediaWikiLangE.token.toString(), editToken));
      
      httpPost.setEntity(new UrlEncodedFormEntity(postPars));
      
      response = httpclient.execute(httpPost);
      
      parseUpdateResponse(response, vorgangTitle);
      
      SSLogU.info("created vorgang " + vorgangTitle + SSStrU.blank + vorgang.vorgangNumber);
      
      return vorgangTitle;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public void updateVorgangBasics(
    final SSKCProjWikiConf    conf,
    final SSKCProjWikiVorgang vorgang,
    final String              vorgangTitle) throws SSErr{
    
    try{
      
      final HttpResponse response;
      final HttpGet      httpGet =
        new HttpGet(
          conf.wikiURI
            + SSMediaWikiLangE.apiActionSfautoedit
            + SSStrU.ampersand + SSMediaWikiLangE.formEquals                 + SSMediaWikiLangE.ProjektVorgangsebene
            + SSStrU.ampersand + SSMediaWikiLangE.targetEquals               + vorgangTitle
            + SSStrU.ampersand + SSMediaWikiLangE.ProjektVorgangsebene + SSStrU.squareBracketOpen + SSMediaWikiLangE.RealProjectStart      + SSStrU.squareBracketClose + SSStrU.equal + vorgang.vorgangStart
            + SSStrU.ampersand + SSMediaWikiLangE.ProjektVorgangsebene + SSStrU.squareBracketOpen + SSMediaWikiLangE.RealProjectkEnd       + SSStrU.squareBracketClose + SSStrU.equal + vorgang.vorgangEnd
            + SSStrU.ampersand + SSMediaWikiLangE.ProjektVorgangsebene + SSStrU.squareBracketOpen + SSMediaWikiLangE.TotalProjectResources + SSStrU.squareBracketClose + SSStrU.equal + vorgang.totalResources
            + SSStrU.ampersand + SSMediaWikiLangE.ProjektVorgangsebene + SSStrU.squareBracketOpen + SSMediaWikiLangE.ResourcesUsedMonthEnd + SSStrU.squareBracketClose + SSStrU.equal + vorgang.usedResources
            + SSStrU.ampersand + SSMediaWikiLangE.ProjektVorgangsebene + SSStrU.squareBracketOpen + SSMediaWikiLangE.ExportDate            + SSStrU.squareBracketClose + SSStrU.equal + vorgang.exportDate
            + SSStrU.ampersand + SSMediaWikiLangE.ProjektVorgangsebene + SSStrU.squareBracketOpen + SSMediaWikiLangE.ProjectProgress       + SSStrU.squareBracketClose + SSStrU.equal + vorgang.progress
            + SSStrU.ampersand + SSMediaWikiLangE.formatEqualsJson
          
          //            + SSStrU.ampersand + valueProjektVorgangsebene + SSStrU.squareBracketOpen + valueChangesBMD + SSStrU.squareBracketClose + SSStrU.equal + "No"
          
//        + "&Projekt-Vorgangsebene[Project%20Number]="                   + "A13490adasf"
//        + "&Projekt-Vorgangsebene[Project%20Number%20Business%20Plan]=" + "000bus"
//        + "&Projekt-Vorgangsebene[Project%20Name]="                     + "a%20name%20for%20the%20proj"
//        + "&Projekt-Vorgangsebene[Workpackage]="                        + "WP5"
//        + "&Projekt-Vorgangsebene[Project%20Type]="                     + "COMET"
//        + "&Projekt-Vorgangsebene[Project%20Type%20Detail]="            + "some%20details"
//        + "&Projekt-Vorgangsebene[Area]="                               + "KE"
//        + "&Projekt-Vorgangsebene[Business%20Partner]="                 + "BP1,BP2"
//        + "&Projekt-Vorgangsebene[Scientific%20Partner]="               + "SP1,SP2"
//        + "&Projekt-Vorgangsebene[Responsible%20TL]="                   + "User:Dtheiler"
//        + "&Projekt-Vorgangsebene[Responsible%20BM]="                   + "User:Dtheiler"
//        + "&Projekt-Vorgangsebene[Responsible%20PM]="                   + "User:Dtheiler"
//        + "&Projekt-Vorgangsebene[Start%20Overall%20Project]="          + "2012/02/16%2000:15:35"
//        + "&Projekt-Vorgangsebene[End%20Overall%20Project]="            + "2014/02/16%2000:15:35"
//        + "&Projekt-Vorgangsebene[Export%20Date]="                      + "2014/10/16%2000:15:35"
//        + "&Projekt-Vorgangsebene[KC%20Personnel%20Involved]="          + "User:dtheiler,%20User:sdennerlein,%20User:dkowald"
//        + "&Projekt-Vorgangsebene[Total%20Project%20Resources]="        + "300"
//        + "&Projekt-Vorgangsebene[Resources%20Used%20Month%20End]="     + "100"
//        + "&Projekt-Vorgangsebene[Project%20Progress]="                 + "90"
//        + "&Projekt-Vorgangsebene[Reisekosten%20Projekt%20Budget]="     + "1500"
//        + "&Projekt-Vorgangsebene[Reisekosten%20Verbraucht]="           + "30"
        );
      
      httpGet.addHeader(SSMediaWikiLangE.Cookie.toString(), sessionID);
      
      response = httpclient.execute(httpGet);
      
      parseUpdateResponse(response, vorgangTitle);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void updateVorgangEmployeeResources(
    final SSKCProjWikiConf    conf,
    final SSKCProjWikiVorgang vorgang,
    final String              vorgangTitle) throws SSErr{
    
    try{
      final String              editToken             = getWikiPageEditToken  (conf, vorgangTitle);
      final String              vorgangPageContent    = getWikiPageContent    (conf, vorgangTitle);
      final List<NameValuePair> postPars              = new ArrayList<>();
      final HttpResponse        response;
      final HttpPost            httpPost              =
        new HttpPost(
          conf.wikiURI
            + SSMediaWikiLangE.apiActionEdit
            + SSStrU.ampersand
            + SSMediaWikiLangE.formatEqualsJson);
      
      String content = removeEmployeeResourcesFromVorgangContent(vorgangPageContent) + System.lineSeparator();
      
      httpPost.addHeader(SSMediaWikiLangE.Cookie.toString(), sessionID);
      
      for(SSKCProjWikiVorgangEmployeeResource employeeResource : vorgang.employeeResources.values()){
        
        content +=
          SSStrU.doubleCurlyBracketOpen
          + SSMediaWikiLangE.WorksInVorgang
          + SSStrU.pipe
          + employeeResource.employee
          + SSStrU.pipe
          + employeeResource.used.toString()
          + SSStrU.pipe
          + employeeResource.total.toString()
          + SSStrU.pipe
          + vorgang.vorgangEnd
          + SSStrU.doubleCurlyBracketClose
          + System.lineSeparator();
      }
      
      postPars.add(new BasicNameValuePair(SSMediaWikiLangE.title.toString(), vorgangTitle));
      postPars.add(new BasicNameValuePair(SSMediaWikiLangE.text.toString(),  content.trim()));
      postPars.add(new BasicNameValuePair(SSMediaWikiLangE.token.toString(), editToken));
      
      httpPost.setEntity(new UrlEncodedFormEntity(postPars));
      
      response = httpclient.execute(httpPost);
      
      parseUpdateResponse(response, vorgangTitle);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public String getVorgangPageTitleByVorgangNumber(
    final SSKCProjWikiConf    conf,
    final String              vorgangNumber) throws SSErr{
    
    InputStream in = null;
    
    try{
      
      //      http://mint/projwiki_dieter/api.php?action=ask&q=[[Category:Projekt-Vorgangsebene]][[Project%20Number::20143516]]
      final JSONObject json, results, ask, item, title;
      final JSONArray items;
      
      final HttpGet httpget =
        new HttpGet(
          conf.wikiURI
            + SSMediaWikiLangE.apiActionAsk
            + SSStrU.ampersand
            + SSMediaWikiLangE.q
            + SSStrU.equal
            + SSMediaWikiLangE.CategoryProjektVorgangsebene
            + SSStrU.doubleSquareBracketOpen
            + SSMediaWikiLangE.VorgangNumber
            + SSStrU.doubleColon
            + vorgangNumber
            + SSStrU.doubleSquareBracketClose
            + SSStrU.ampersand
            + SSMediaWikiLangE.formatEqualsJson);
      
      httpget.addHeader(SSMediaWikiLangE.Cookie.toString(), sessionID);
      
      in       = httpclient.execute(httpget).getEntity().getContent();
      json     = new JSONObject(SSFileU.readStreamText(in));
      ask      = (JSONObject) json.get      (SSMediaWikiLangE.ask.toString());
      
      try{
        results  = (JSONObject) ask.get       (SSMediaWikiLangE.results.toString());
      }catch(Exception error){
        SSLogU.trace(error, false, false);
        return null;
      }
      
      items    = (JSONArray)  results.get   (SSMediaWikiLangE.items.toString());
      item     = (JSONObject) items.get(0);
      title    = (JSONObject) item.get      (SSMediaWikiLangE.title.toString());
      
      return (String) title.get(SSMediaWikiLangE.mUrlform.toString());
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      
      if(in != null){
        try {
          in.close();
        } catch (IOException ex) {
          SSLogU.err(ex);
        }
      }
    }
  }
  
  public String getVorgangPageTitleByProjectNumber(
    final SSKCProjWikiConf    conf,
    final String              projectNumber) throws SSErr{
    
    InputStream in = null;
    
    try{
      
      //      http://mint/projwiki_dieter/api.php?action=ask&q=[[Category:Projekt-Vorgangsebene]][[Project%20Number::20143516]]
      final JSONObject json, results, ask, item, title;
      final JSONArray items;
      
      final HttpGet httpget =
        new HttpGet(
          conf.wikiURI
            + SSMediaWikiLangE.apiActionAsk
            + SSStrU.ampersand
            + SSMediaWikiLangE.q
            + SSStrU.equal
            + SSMediaWikiLangE.CategoryProjektVorgangsebene
            + SSStrU.doubleSquareBracketOpen
            + SSMediaWikiLangE.ProjectNumber
            + SSStrU.doubleColon
            + projectNumber
            + SSStrU.doubleSquareBracketClose
            + SSStrU.ampersand
            + SSMediaWikiLangE.formatEqualsJson);
      
      httpget.addHeader(SSMediaWikiLangE.Cookie.toString(), sessionID);
      
      in       = httpclient.execute(httpget).getEntity().getContent();
      json     = new JSONObject(SSFileU.readStreamText(in));
      ask      = (JSONObject) json.get      (SSMediaWikiLangE.ask.toString());
      results  = (JSONObject) ask.get       (SSMediaWikiLangE.results.toString());
      items    = (JSONArray)  results.get   (SSMediaWikiLangE.items.toString());
      item     = (JSONObject) items.get(0);
      title    = (JSONObject) item.get      (SSMediaWikiLangE.title.toString());
      
      return (String) title.get(SSMediaWikiLangE.mUrlform.toString());
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      
      if(in != null){
        try {
          in.close();
        } catch (IOException ex) {
          SSLogU.err(ex);
        }
      }
    }
  }
  
  public String getProjectPageTitleByProjectNumber(
    final SSKCProjWikiConf    conf,
    final String              projectNumber) throws SSErr{
    
    InputStream in = null;
    
    try{
      
      //      http://mint/projwiki_dieter/api.php?action=ask&q=[[Category:Projekt]][[Project%20Number::20143516]]
      final JSONObject json, results, ask, item, title;
      final JSONArray items;
      
      final HttpGet httpget =
        new HttpGet(
          conf.wikiURI
            + SSMediaWikiLangE.apiActionAsk
            + SSStrU.ampersand
            + SSMediaWikiLangE.q
            + SSStrU.equal
            + SSMediaWikiLangE.CategoryProjekt
            + SSStrU.doubleSquareBracketOpen
            + SSMediaWikiLangE.ProjectNumber
            + SSStrU.doubleColon
            + projectNumber
            + SSStrU.doubleSquareBracketClose
            + SSStrU.ampersand
            + SSMediaWikiLangE.formatEqualsJson);
      
      httpget.addHeader(SSMediaWikiLangE.Cookie.toString(), sessionID);
      
      in       = httpclient.execute(httpget).getEntity().getContent();
      json     = new JSONObject(SSFileU.readStreamText(in));
      ask      = (JSONObject) json.get     (SSMediaWikiLangE.ask.toString());
      
      try{
        results  = (JSONObject) ask.get      (SSMediaWikiLangE.results.toString());
      }catch(Exception error){
        SSLogU.trace(error, false, false);
        return null;
      }
      
      items    = (JSONArray)  results.get  (SSMediaWikiLangE.items.toString());
      item     = (JSONObject) items.get(0);
      title    = (JSONObject) item.get     (SSMediaWikiLangE.title.toString());
      
      return (String) title.get(SSMediaWikiLangE.mUrlform.toString());
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      
      if(in != null){
        try {
          in.close();
        } catch (IOException ex) {
          SSLogU.err(ex);
        }
      }
    }
  }
  
  private void loginFirstTime(
    final SSKCProjWikiConf    conf) throws SSErr{
    
    InputStream in = null;
    
    try{
      
      final HttpResponse response;
      final JSONObject   json;
      final HttpPost     post =
        new HttpPost(
          conf.wikiURI
            + SSMediaWikiLangE.apiActionLogin
            + SSStrU.ampersand
            + SSMediaWikiLangE.formatEqualsJson);
      
      post.setEntity(new UrlEncodedFormEntity(getFirstLoginParams(conf), SSEncodingU.utf8.toString()));
      
      response = httpclient.execute(post);
      in       = response.getEntity().getContent();
      json     = new JSONObject(SSFileU.readStreamText(in));
      token    = ((JSONObject)json.get(SSMediaWikiLangE.login.toString())).get(SSMediaWikiLangE.token.toString()).toString();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      if(in != null){
        try {
          in.close();
        } catch (IOException ex) {
          SSLogU.err(ex);
        }
      }
    }
  }
  
  private void loginSecondTime(
    final SSKCProjWikiConf    conf) throws SSErr{
    
    InputStream in  = null;
    
    try{
      
      final HttpResponse response;
      final JSONObject   json;
      final HttpPost     post =
        new HttpPost(
          conf.wikiURI
            + SSMediaWikiLangE.apiActionLogin
            + SSStrU.ampersand
            + SSMediaWikiLangE.formatEqualsJson);
      
      post.setEntity(new UrlEncodedFormEntity(getSecondLoginParams(conf), SSEncodingU.utf8.toString()));
      
      response  = httpclient.execute(post);
      in        = response.getEntity().getContent();
      json      = new JSONObject(SSFileU.readStreamText(in));
      sessionID = ((JSONObject)json.get(SSMediaWikiLangE.login.toString())).get(SSMediaWikiLangE.sessionid.toString()).toString();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(SSErrE.mediaWikiUserLoginSecondTimeFailed, error);
    }finally{
      if(in != null){
        try {
          in.close();
        } catch (IOException ex) {
          SSLogU.err(ex);
        }
      }
    }
  }
  
  private void logout(
    final SSKCProjWikiConf conf) throws SSErr{
    
    try{
      httpclient.execute(
        new HttpGet(
          conf.wikiURI
            + SSMediaWikiLangE.apiActionLogout
            + SSStrU.ampersand
            + SSMediaWikiLangE.formatEqualsJson));
      
    }catch(Exception error){
      SSLogU.warn("logout failed", error);
    }
  }
  
  private String getWikiPageContent(
    final SSKCProjWikiConf    conf,
    final String              title) throws SSErr{
    
    InputStream in = null;
    
    try{
//            &&prop=revisions&rvlimit=1&rvprop=content&format=json&titles=" + title);

final HttpGet httpget =
  new HttpGet(
    conf.wikiURI
      + SSMediaWikiLangE.apiActionQuery
      + SSStrU.ampersand + SSMediaWikiLangE.indexpageids
      + SSStrU.ampersand + SSMediaWikiLangE.prop    + SSStrU.equal   + SSMediaWikiLangE.revisions
      + SSStrU.ampersand + SSMediaWikiLangE.rvlimit + SSStrU.equal   + "1"
      + SSStrU.ampersand + SSMediaWikiLangE.rvprop  + SSStrU.equal   + SSMediaWikiLangE.content
      + SSStrU.ampersand + SSMediaWikiLangE.titles  + SSStrU.equal   + title
      + SSStrU.ampersand + SSMediaWikiLangE.formatEqualsJson);

httpget.addHeader(SSMediaWikiLangE.Cookie.toString(), sessionID);

final HttpResponse response   = httpclient.execute(httpget);
final JSONObject   json;
final JSONObject   query;
final JSONObject   pages;
final JSONObject   firstPage;
final JSONArray    revisions;
final JSONArray    pageids;
final String       content;
final String       pageID;

in        = response.getEntity().getContent();
json      = new JSONObject(SSFileU.readStreamText(in));
query     = (JSONObject)  json.get(SSMediaWikiLangE.query.toString());
pageids   = (JSONArray)   query.get(SSMediaWikiLangE.pageids.toString());
pageID    = pageids.get(0).toString();
pages     = (JSONObject)  query.get(SSMediaWikiLangE.pages.toString());
firstPage = (JSONObject)  pages.get(pageID);
revisions = (JSONArray)   firstPage.get(SSMediaWikiLangE.revisions.toString());
content   = ((JSONObject) revisions.get(0)).get("*").toString();

return content;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      if(in != null){
        try {
          in.close();
        } catch (IOException ex) {
          SSLogU.err(ex);
        }
      }
    }
  }
  
  private String getWikiPageEditToken(
    final SSKCProjWikiConf    conf,
    final String              title) throws SSErr{
    
    InputStream in = null;
    
    try{
      
      final HttpGet httpget =
        new HttpGet(
          conf.wikiURI
            + SSMediaWikiLangE.apiActionQuery
            + SSStrU.ampersand + SSMediaWikiLangE.indexpageids
            + SSStrU.ampersand + SSMediaWikiLangE.prop    + SSStrU.equal + SSMediaWikiLangE.info
            + SSStrU.ampersand + SSMediaWikiLangE.intoken + SSStrU.equal + SSMediaWikiLangE.edit
            + SSStrU.ampersand + SSMediaWikiLangE.rvprop  + SSStrU.equal + SSMediaWikiLangE.timestamp//"&rvprp=timestamp"
            + SSStrU.ampersand + SSMediaWikiLangE.titles  + SSStrU.equal + title
            + SSStrU.ampersand + SSMediaWikiLangE.formatEqualsJson);
      
      final HttpResponse response   = httpclient.execute(httpget);
      final HttpEntity   entity     = response.getEntity();
      final JSONObject   json;
      final JSONObject   query;
      final JSONObject   pages;
      final JSONObject   firstPage;
      final JSONArray    pageids;
      final String       pageID;
      
      in        = entity.getContent();
      json      = new JSONObject(SSFileU.readStreamText(in));
      query     = (JSONObject) json.get(SSMediaWikiLangE.query.toString());
      pageids   = (JSONArray)  query.get(SSMediaWikiLangE.pageids.toString());
      pageID    = pageids.get(0).toString();
      pages     = (JSONObject) query.get(SSMediaWikiLangE.pages.toString());
      firstPage = (JSONObject) pages.get(pageID);
      
      return firstPage.get(SSMediaWikiLangE.edittoken.toString()).toString();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      
      if(in != null){
        try {
          in.close();
        } catch (IOException ex) {
          SSLogU.err(ex);
        }
      }
    }
  }
  
  public void start(final SSKCProjWikiConf    conf) throws SSErr{
    
    try{
      loginFirstTime  (conf);
      loginSecondTime (conf);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void end(final SSKCProjWikiConf    conf) throws SSErr{
    
    try{
      logout(conf);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private List<BasicNameValuePair> getFirstLoginParams(
    final SSKCProjWikiConf    conf) throws SSErr{
    
    try{
      final List<BasicNameValuePair> params = new ArrayList<>();
      
      params.add(new BasicNameValuePair(SSMediaWikiLangE.lgname.toString(),      conf.userName));
      params.add(new BasicNameValuePair(SSMediaWikiLangE.lgpassword.toString(),  conf.password));
      params.add(new BasicNameValuePair(SSMediaWikiLangE.lgdomain.toString(),    conf.domain));
      
      return params;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private List<BasicNameValuePair> getSecondLoginParams(
    final SSKCProjWikiConf    conf) throws SSErr{
    
    try{
      final List<BasicNameValuePair> params = getFirstLoginParams(conf);
      
      params.add(new BasicNameValuePair(SSMediaWikiLangE.lgtoken.toString(),    token));
      
      return params;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private String removeEmployeeResourcesFromVorgangContent(
    final String content) throws SSErr{
    
    try{
      String                    result = content;
      Integer                   firstIndex;
      Integer                   secondIndex;
      
      while(result.contains(SSStrU.doubleCurlyBracketOpen + SSMediaWikiLangE.WorksInVorgang)){
        
        firstIndex = result.indexOf(SSStrU.doubleCurlyBracketOpen + SSMediaWikiLangE.WorksInVorgang);
        
        if(firstIndex == -1){
          continue;
        }
        
        secondIndex = result.indexOf(SSStrU.doubleCurlyBracketClose, firstIndex);
        result      = result.substring(0, firstIndex) + result.substring(secondIndex + 2);
      }
      
      return result.trim();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private void parseUpdateResponse(
    final HttpResponse            response,
    final String                  pageTitle) throws SSErr{
    
    InputStream in = null;
    
    try{
      
      JSONObject  json;
      JSONObject  edit;
      String      strResult;
      
      in     = response.getEntity().getContent();
      json   = new JSONObject(SSFileU.readStreamText(in));
      
      try{
        edit = (JSONObject)             json.get          (SSMediaWikiLangE.edit.toString());
      }catch(Exception error){
        
        SSLogU.debug(error);
        
        JSONObject  jsonResult = (JSONObject)             json.get          (SSMediaWikiLangE.result.toString());
        Integer     code       = Integer.valueOf((String) jsonResult.get    (SSMediaWikiLangE.code.toString()));
        
        if(code.compareTo(200) != 0){
          SSServErrReg.regErrThrow(SSErrE.mediaWikiParseUpdateResponseFailed);
          return;
        }
        
        return;
      }
      
      strResult = (String) edit.get          (SSMediaWikiLangE.result.toString());
      
      if(!SSStrU.isEqual(strResult, SSMediaWikiLangE.Success.toString())){
        SSServErrReg.regErrThrow(SSErrE.mediaWikiParseUpdateResponseFailed);
        return;
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      if(in != null){
        try {
          in.close();
        } catch (IOException ex) {
          SSLogU.err(ex);
        }
      }
    }
  }
}