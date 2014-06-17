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
package at.kc.tugraz.ss.serv.db.api;

import at.kc.tugraz.ss.datatypes.datatypes.entity.SSLiteral;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSSpaceE;
import at.kc.tugraz.socialserver.utils.*;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityA;
import at.kc.tugraz.ss.serv.db.datatypes.graph.SSQueryResultItem;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import java.util.*;

public class SSDBGraphFct extends SSDBFct{

  protected static final String              graphCommon   = "commonGraph";
  protected static final String              graphEvent    = "eventGraph";
  private static final String                namespaceRDF  = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
  private static final String              hasResource   = "hasResource";
  private static final String              hasDiscussion   = "hasDiscussion";
  private static final String              hasEntry   = "hasEntry";
  private static final String              hasContent   = "hasContent";
  private static final String              hasRating   = "hasRating";
  private static final String              isAuthor   = "isAuthor";
  private static final String              hasCollectionEntry   = "hasCollectionEntry";
  private static final String              belongsTo   = "belongsTo";
  private static final String              resourceBelongsTo   = "resourceBelongsTo";
  private static final String              rated   = "rated";
  private static final String              value   = "value";
  private static final String              follows   = "follows";
  private static final String              entryType   = "entryType";
  private static final String              collectionType   = "collectionType";
  private static final String              timestamp   = "timestamp";
  private static final String              hasCollectionPosition   = "hasCollectionPosition";
  private static final String              hasLabel   = "hasLabel";
  private static final String              hasLocation   = "hasLocation";
  private static final String              content   = "content";
  private static final String              actionType   = "actionType";
  private static final String              isRootColl   = "isRootColl";
  private static final String              type         = "type";
  
  
  
//  HAS_EVENT("hasEvent,
//  HAS_EVENTS("hasEvents,
   // HAS_TAG("hasTag,
  //  GIVEN_BY("givenBy,
//  TAGGED("annotated,
//  HAS_PROFILE("hasProfile,
//  HAS_HISTORY("hasHistory,
//  HAS_FAVORITE("hasFavorite,
//  HAS_FAVORITES("hasFavorites,
  
  protected static final String              bindUser      = "user";
  protected static final String              bindResource  = "resource";
  protected static final String              bindLabel     = "label";
  protected static final String              bindTagSet    = "tagSet";
  protected static final String              bindTag       = "tag";
  protected static final String              bindColl      = "coll";
  protected static final String              bindTrue      = "true";
  protected static final String              bindCollEntry = "collEntry";
  protected static final String              bindTime      = "time";
  protected static final String              bindType      = "space";
  protected static final String              bindContent   = "content";
  protected static final String              bindAuthor    = "author";
  protected static final String              bindDisc      = "disc";
  protected static final String              bindDiscEntry = "discEntry";
  protected static final String              bindTarget    = "target";
  protected static final String              bindValue     = "value";
  protected static final String              bindRating    = "rating";
  protected static final String              bindEventType = "eventType";
  protected static final String              bindEvent     = "event";
  protected        final SSUri               namedGraphUri;
  protected        final SSDBGraphI          dbGraph;
  
  protected SSDBGraphFct(final SSDBGraphI dbGraph) throws Exception{
    
    super();
    
    this.dbGraph       = dbGraph;
    this.namedGraphUri = null;// SSUri.get(SSServCaller.vocURIPrefixGet(), graphCommon);
  }
  
  protected SSLiteral objHasCollectionPosition(
    SSUri coll,
    int   pos) throws Exception {

    if(pos <= 0) {
      SSServErrReg.regErrThrow(new Exception());
    }

    return SSLiteral.get(coll + SSStrU.pipe + String.valueOf(pos));
  }
  
  protected SSLiteral objHasEntryType(
    SSUri       resource,
    SSSpaceE space) throws Exception {

    if(
      SSObjU.isNull(resource) ||
      SSObjU.isNull(space)){
      
      return null;
    }
    
    return SSLiteral.get(resource + SSStrU.pipe + space);
  }
  
  protected SSLiteral objTrue() throws Exception{
    return SSLiteral.get(Boolean.TRUE.toString());
  }
  
  protected SSLiteral objEventType(String eventType) throws Exception{
    return SSLiteral.get(eventType);
  }
  
//  protected SSUri objUserEvent() throws Exception{
//    return SSUri.get(SSServCaller.vocURIPrefixGet(), SSEntityEnum.userEvent.toString());
//  }
  //  SSLiteral.get(SSStrU.toString(actionType))

//  protected SSUri createEvent() throws Exception{
//    return SSUri.get(objUserEvent() + SSDateU.dateAsNano().toString());
//  }
 
//  protected SSUri predHasResource() throws Exception{
//    return SSUri.get(SSServCaller.vocURIPrefixGet(), hasResource.toString());
//  }
//  
//  protected SSUri predEventType() throws Exception{
//    return SSUri.get(SSServCaller.vocURIPrefixGet(), actionType.toString());
//  }
//  
//  protected SSUri predContent() throws Exception{
//    return SSUri.get(SSServCaller.vocURIPrefixGet(), content.toString());
//  }
  
  protected SSLiteral objValue(
    int value) throws Exception {

    if(value <= 0){
      SSServErrReg.regErrThrow(new Exception());
    }
    
    return SSLiteral.get(String.valueOf(value));
  }
  
  protected SSLiteral objHasLabel(
    String value) throws Exception{
    
    return SSLiteral.get(SSStrU.toString(value));
  }
  
  protected SSLiteral objContent(String value) throws Exception{
    return SSLiteral.get(SSStrU.toString(value));
  }
  
  protected SSLiteral objSpace(
    SSSpaceE value) throws Exception{
    
    return SSLiteral.get(SSStrU.toString(value));
  }
  
//  protected SSUri objTagSet() throws Exception{
//    return voc.vocGraphUriForNamespace(SSVocNamespace.tagSet);
//  }
  
  protected SSUri predType() throws Exception{
    return SSUri.get(namespaceRDF + type);
  }
  
//  protected SSUri predRating() throws Exception{
//    return SSUri.get(SSServCaller.vocURIPrefixGet(), hasRating.toString());
//  }
//  
//  protected SSUri predRated() throws Exception{
//    return SSUri.get(SSServCaller.vocURIPrefixGet(), rated.toString());
//  }
//  
//  protected SSUri predValue() throws Exception{
//    return SSUri.get(SSServCaller.vocURIPrefixGet(), value.toString());
//  }
//    
//  protected SSUri predTime() throws Exception{
//    return SSUri.get(SSServCaller.vocURIPrefixGet(), timestamp.toString());
//  }
//  
//  protected SSUri predBelongsTo() throws Exception{
//    return SSUri.get(SSServCaller.vocURIPrefixGet(), belongsTo.toString());
//  }
//
//  protected SSUri predResourceBelongsTo() throws Exception{
//    return SSUri.get(SSServCaller.vocURIPrefixGet(), resourceBelongsTo.toString());
//  }
//  
//  protected SSUri predHasEntry() throws Exception{
//    return SSUri.get(SSServCaller.vocURIPrefixGet(), hasEntry.toString());
//  }
//  
//  protected SSUri predHasContent() throws Exception{
//    return SSUri.get(SSServCaller.vocURIPrefixGet(), hasContent.toString());
//  }
//  
//  protected SSUri predIsAuthor() throws Exception{
//    return SSUri.get(SSServCaller.vocURIPrefixGet(), isAuthor.toString());
//  }
//  
//  protected SSUri predIsRootColl() throws Exception{
//    return SSUri.get(SSServCaller.vocURIPrefixGet(), isRootColl.toString());
//  }
//  
//  protected SSUri predHasLabel() throws Exception{
//    return SSUri.get(SSServCaller.vocURIPrefixGet(), hasLabel.toString());
//  }
//  
//  protected SSUri predHasLocation() throws Exception{
//    return SSUri.get(SSServCaller.vocURIPrefixGet(), hasLocation.toString());
//  }
//  
//  protected SSUri predCollectionType() throws Exception{
//    return SSUri.get(SSServCaller.vocURIPrefixGet(), collectionType.toString());
//  }
//  
//  protected SSUri predHasCollectionEntry() throws Exception{
//    return SSUri.get(SSServCaller.vocURIPrefixGet(), hasCollectionEntry.toString());
//  }
//  
//  protected SSUri predFollows() throws Exception{
//    return SSUri.get(SSServCaller.vocURIPrefixGet(), follows.toString());
//  }
//  
//  protected SSUri predHasDiscussion() throws Exception{
//    return SSUri.get(SSServCaller.vocURIPrefixGet(), hasDiscussion.toString());
//  }
//  
//  protected SSUri predHasCollectionPosition() throws Exception{
//    return SSUri.get(SSServCaller.vocURIPrefixGet(), hasCollectionPosition.toString());
//  }
//  
//  protected SSUri predHasEntryType() throws Exception{
//    return SSUri.get(SSServCaller.vocURIPrefixGet(), entryType.toString());
//  }
  
  protected String selectDistinctWithParFromAndWhere(
    SSUri     context,
    String... pars) throws Exception{
    
    if(SSObjU.isNull((Object[]) pars)){
      SSServErrReg.regErrThrow(new Exception("select parameters must not be null"));
    }
    
    return selectDistinctWithParFromAndWhere(context, Arrays.asList(pars));
  }
  
  protected String selectDistinctWithParFromAndWhere(
    SSUri        context,
    List<String> pars) throws Exception{
    
    if(
      SSObjU.isNull  (context) ||
      SSObjU.isNull  (pars)
      /* TODO dtheiler: check ||
      SSStrU.isEmpty (pars.toArray(new String[pars.size()]))*/){
      
      SSServErrReg.regErrThrow(new Exception("from context and/or select parameters must not be null"));
    }
    
    String select = "SELECT DISTINCT";
    String from   = SSStrU.blank + "FROM"  + SSStrU.blank + SSStrU.lessThan + context + SSStrU.greaterThan;
    String where  = SSStrU.blank + "WHERE" + SSStrU.curlyBracketOpen;
    
    for(String par : pars){
      select += SSStrU.blank + SSStrU.questionMark + par;
    }
    
    return select + from + where;
  }
  
  protected String selectWithParFromAndWhere(
    String... pars) throws Exception{
    
    if(SSObjU.isNull((Object[]) pars)){
      SSServErrReg.regErrThrow(new Exception("select parameters must not be null"));
    }
    
    return selectWithParFromAndWhere(namedGraphUri, Arrays.asList(pars));
  }
    
  protected String selectWithParFromAndWhere(
    List<String> pars) throws Exception{
    
    if(SSObjU.isNull(pars)){
      SSServErrReg.regErrThrow(new Exception("select parameters must not be null"));
    }
    
    return selectWithParFromAndWhere(namedGraphUri, pars);
  }
  
  protected String selectWithParFromAndWhere(
    SSUri        context,
    String...    pars) throws Exception{
    
    if(SSObjU.isNull((Object[]) pars)){
      SSServErrReg.regErrThrow(new Exception("select parameters must not be null"));
    }
        
    return selectWithParFromAndWhere(context, Arrays.asList(pars));
  }
  
  protected String selectWithParFromAndWhere(
    SSUri        context,
    List<String> pars) throws Exception{
   
    if(
      SSObjU.isNull(context) ||
      SSObjU.isNull(pars)
      /*TODO dtheiler: check ||
      SSStrU.isEmpty(pars.toArray(new String[pars.size()]))*/){
      
      SSServErrReg.regErrThrow(new Exception("from context and/or select parameters must not be null"));
    }
    
    String select = "SELECT";
    String from   = SSStrU.blank + "FROM"  + SSStrU.blank + SSStrU.lessThan + context + SSStrU.greaterThan;
    String where  = SSStrU.blank + "WHERE" + SSStrU.curlyBracketOpen;
    
    for(String par : pars){
      select += SSStrU.blank + SSStrU.questionMark + par;
    }
    
    return select + from + where;
  }
  
//  protected String selectWithParFromAndWhere(
//    List<String> pars) throws Exception{
//    
//    if(SSObjectUtils.isNotNull(pars)){
//      SSLogU.logAndThrow(new Exception("select parameters must not be empty or null"));
//    }
//    
//    
//    
//    
//  }
  
  protected String and(
    String    subject,
    SSUri     predicate,
    String    object){
    
    return 
      SSStrU.questionMark + subject   +
      SSStrU.lessThan     + predicate + SSStrU.greaterThan +
      SSStrU.questionMark + object    + SSStrU.dot;
  }
  
  protected String and(
    SSUri  subject,
    SSUri  predicate,
    String object){
    
    return 
      SSStrU.lessThan     + subject   + SSStrU.greaterThan +
      SSStrU.lessThan     + predicate + SSStrU.greaterThan + 
      SSStrU.questionMark + object    + SSStrU.dot;
  }
  
  protected String and(
    SSUri       subject,
    SSUri       predicate,
    SSEntityA   object){
    
    if(object instanceof SSUri){
      return
        SSStrU.lessThan     + subject   + SSStrU.greaterThan +
        SSStrU.lessThan     + predicate + SSStrU.greaterThan +
        SSStrU.lessThan     + object    + SSStrU.greaterThan + SSStrU.dot;
    }else{
      return
        SSStrU.lessThan     + subject   + SSStrU.greaterThan +
        SSStrU.lessThan     + predicate + SSStrU.greaterThan +
        SSStrU.surroundWithDoubleQuotes(object.toString()) + SSStrU.dot;
    }
  }
  
  protected String and(
    String      subject,
    SSUri       predicate,
    SSEntityA   object){
    
    if(object instanceof SSUri){
      return
        SSStrU.questionMark + subject   +
        SSStrU.lessThan     + predicate + SSStrU.greaterThan +
        SSStrU.lessThan     + object    + SSStrU.greaterThan + SSStrU.dot;
    }else{
      return
        SSStrU.questionMark + subject   +
        SSStrU.lessThan     + predicate + SSStrU.greaterThan +
        SSStrU.surroundWithDoubleQuotes(object.toString()) + SSStrU.dot;
    }
  }
  
  protected String filter(
    String filterName, 
    String operator,
    String par){
    
    return 
      SSStrU.blank        + 
      "FILTER"            + 
      SSStrU.blank        + 
      SSStrU.bracketOpen  + 
      SSStrU.questionMark + 
      filterName          +
      operator            +
      SSStrU.blank        + 
      par                 + 
      SSStrU.bracketClose;
  }
  
  protected String whereEnd(){
    return SSStrU.curlyBracketClose;
  }
  
  protected String orderByAsc(
    String par) throws Exception{
    
    String orderByAsc = " ORDER BY ASC " + SSStrU.bracketOpen; 
    
    if(SSStrU.isEmpty(par)){
      SSServErrReg.regErrThrow(new Exception("order by parameters must not be empty or null"));
    }
    
    orderByAsc += SSStrU.questionMark + par + SSStrU.bracketClose;

    return orderByAsc;
  }
  
  protected String orderByDesc(
    String par) throws Exception{
    
    String orderByDesc = " ORDER BY DESC " + SSStrU.bracketOpen; 
    
    if(SSStrU.isEmpty(par)){
      SSServErrReg.regErrThrow(new Exception("order by parameters must not be empty or null"));
    }
    
    orderByDesc += SSStrU.questionMark + par + SSStrU.bracketClose;

    return orderByDesc;
  }
  
  protected String limit(
    int value) throws Exception{
    
    if(value <= 0){
      SSServErrReg.regErrThrow(new Exception("query limit must not be less than one"));
    }
    
    return " LIMIT " + value;
  }
  
  protected String binding(
    SSQueryResultItem item,
    String            value) throws Exception{
    
    if(SSObjU.isNull(item)){
      SSServErrReg.regErrThrow(new Exception("query result item must not be null"));
    }
    
    if(SSStrU.isEmpty(value)){
      SSServErrReg.regErrThrow(new Exception("query result item binding value must not be empty or null"));
    }
    
    return SSStrU.removeDoubleQuotes(item.getBinding(value));
  }
  
  
//  protected String whereStart(){
//    return "WHERE" + strU.curlyBracketOpen;
//  }
//  
//  protected String whereEnd(){
//    return strU.dot + strU.curlyBracketClose;
//  }
//  
//  protected String objectPar(
//    String parameterId) {
//
//    return strU.blank + strU.questionMark + parameterId;
//  }
//
//  protected String from(
//    SSUri from) {
//    
//    return "FROM" + strU.blank + strU.lessThan + from + strU.greaterThan + strU.blank;
//  }
//  
//  protected String selectPar(
//    String parameterId) {
//
//    return strU.blank + strU.questionMark + parameterId + strU.blank;
//  }
//  
//  protected String subjectPar(
//    SSUri subject) {
//
//    return strU.blank + strU.lessThan + subject + strU.greaterThan + strU.blank;
//  }
//  
//  protected String predicatePar(
//    SSUri predicate) {
//
//    return strU.blank + strU.lessThan + predicate + strU.greaterThan + strU.blank;
//  }
//  
//  protected String objectPar(
//    SSUri object) {
//
//    return strU.blank + strU.lessThan + object + strU.greaterThan;
//  }
//  protected String select(){
//    return "SELECT";
//  }
  
//  protected SSDbalConnA getCon() throws Exception{
//    return SSDbalConnectorFactory.getInstance().getConnector();
//  }
  
//  protected List<String> splitObject(Object object) throws Exception{
//    return SSStrU.split(SSStrU.removeDoubleQuotes(SSStrU.toString(object)), SSStrU.pipe);
//  }
  
  protected long getLongFromGraphDoubleString(String string){
    
    String tmp;
    
    if(SSStrU.isEmpty(string)){
      return -1;
    }
    
    tmp = SSStrU.removeTrailingString(string, "^^http://www.w3.org/2001/XMLSchema#double");
    
    return Double.valueOf(tmp).longValue();
  }
}



//private static Map<String, String> contentMap = new HashMap<String, String>();
//
//  public void setContent(String user, String content) {
//    String plain = content.replaceAll("\\<[a-zA-Z0-9 ]+\\>", "").replaceAll("\\</[a-zA-Z0-9 ]+\\>", "");
//    contentMap.put(user, plain);
//  }
//
//  public String getContent(String user) {
//    return contentMap.get(user);
//  }