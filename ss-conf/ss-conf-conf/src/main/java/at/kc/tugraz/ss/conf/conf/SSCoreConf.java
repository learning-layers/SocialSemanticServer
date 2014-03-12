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
package at.kc.tugraz.ss.conf.conf;

import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.kc.tugraz.ss.serv.db.conf.SSDBGraphConf;
import at.kc.tugraz.ss.serv.modeling.ue.conf.SSModelUEConf;
import at.kc.tugraz.ss.log.conf.SSLogConf;
import at.kc.tugraz.ss.serv.auth.conf.SSAuthConf;
import at.kc.tugraz.socialserver.service.broadcast.conf.SSBroadcasterConf;
import at.kc.tugraz.ss.conf.api.SSCoreConfA;
import at.kc.tugraz.ss.serv.db.conf.SSDBSQLConf;
import at.kc.tugraz.ss.serv.coll.conf.SSCollConf;
import at.kc.tugraz.ss.serv.dataimport.conf.SSDataImportConf;
import at.kc.tugraz.ss.serv.serv.datatypes.entity.conf.SSEntityConf;
import at.kc.tugraz.ss.serv.datatypes.learnep.conf.SSLearnEpConf;
import at.kc.tugraz.ss.serv.datatypes.location.conf.SSLocationConf;
import at.kc.tugraz.ss.serv.disc.conf.SSDiscConf;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.job.dataexport.conf.SSDataExportConf;
import at.kc.tugraz.ss.serv.job.file.sys.local.conf.SSFileSysLocalConf;
import at.kc.tugraz.ss.serv.job.recomm.conf.SSRecommConf;
import at.kc.tugraz.ss.serv.jobs.evernote.conf.SSEvernoteConf;
import at.kc.tugraz.ss.serv.jsonld.conf.SSJSONLDConf;
import at.kc.tugraz.ss.serv.lomextractor.conf.SSLOMExtractorConf;
import at.kc.tugraz.ss.serv.rating.conf.SSRatingConf;
import at.kc.tugraz.ss.serv.scaff.conf.SSScaffConf;
import at.kc.tugraz.ss.serv.search.conf.SSSearchConf;
import at.kc.tugraz.ss.serv.tag.conf.SSTagConf;
import at.kc.tugraz.ss.serv.ue.conf.SSUEConf;
import at.kc.tugraz.ss.serv.user.conf.SSUserConf;
import at.kc.tugraz.ss.service.filerepo.conf.SSFileRepoConf;

public class SSCoreConf extends SSCoreConfA{

  private static SSCoreConf inst;
  
  private SSConf              ssConf             = null;
  private SSJSONLDConf        jsonLDConf         = null;
  private SSDBGraphConf       dbGraphConf        = null;
  private SSDBSQLConf         dbSQLConf          = null;
  private SSModelUEConf       modelConf          = null;
  private SSLogConf           logConf            = null;
  private SSFileRepoConf      filerepoConf       = null;
  private SSFileRepoConf      solrConf           = null;
  private SSFileRepoConf      localWorkConf      = null;
  private SSVocConf           vocConf            = null;
  private SSBroadcasterConf   broadcasterConf    = null;
  private SSRecommConf        recommConf         = null;
  private SSFileSysLocalConf  fileSysLocalConf   = null;
  private SSEvernoteConf      evernoteConf       = null;
  private SSAuthConf          authConf           = null;
  private SSScaffConf         scaffConf          = null;
  private SSDataImportConf    dataImportConf     = null;
  private SSDataExportConf    dataExportConf     = null;
  private SSLOMExtractorConf  lomExtractorConf   = null;
  private SSUserConf          userConf           = null;
  private SSEntityConf        entityConf         = null;
  private SSCollConf          collConf           = null;
  private SSLocationConf      locationConf       = null;
  private SSUEConf            ueConf             = null;
  private SSRatingConf        ratingConf         = null;
  private SSTagConf           tagConf            = null;
  private SSSearchConf        searchConf         = null;
  private SSDiscConf          discConf           = null;
  private SSLearnEpConf       learnEpConf        = null;

  public static synchronized SSCoreConf instSet(final String pathToFile) throws Exception{
    
    if(inst != null){
      return inst;
    }
    
    inst = (SSCoreConf) SSCoreConfA.instSet(pathToFile, SSCoreConf.class);

    return inst;
  }
  
  public static SSCoreConf instGet() throws Exception{
    
    if(inst == null){
      SSServErrReg.regErrThrow(new Exception("conf not set"));
      return null;
    }
    
    return inst;
  }
  
  public SSFileRepoConf getFilerepoConf() {
    return filerepoConf;
  }

  public void setFilerepoConf(SSFileRepoConf filerepoConf) {
    this.filerepoConf = filerepoConf;
  }

  public SSFileRepoConf getSolrConf() {
    return solrConf;
  }

  public void setSolrConf(SSFileRepoConf solrConf) {
    this.solrConf = solrConf;
  }
  
  public SSFileRepoConf getLocalWorkConf() {
    return localWorkConf;
  }

  public void setLocalWorkConf(SSFileRepoConf localWorkConf) {
    this.localWorkConf = localWorkConf;
  }

  public SSModelUEConf getModelConf() {
    return modelConf;
  }
  
  public void setLogConf(SSLogConf logConf){
    this.logConf = logConf;
  }
    
  public SSLogConf getLogConf(){
    return logConf;
  }

  public void setModelConf(SSModelUEConf modelConf) {
    this.modelConf = modelConf;
  }
  
  public SSVocConf getVocConf() {
    return vocConf;
  }

  public void setVocConf(SSVocConf vocConf) {
    this.vocConf = vocConf;
  }
  
  public void setBroadcasterConf(SSBroadcasterConf broadcasterConf){
    this.broadcasterConf = broadcasterConf;
  }
  
  public SSBroadcasterConf getBroadcasterConf(){
    return broadcasterConf;
  }
  
  public SSDBGraphConf getDbGraphConf() {
    return dbGraphConf;
  }

  public void setDbGraphConf(SSDBGraphConf dbGraphConf) {
    this.dbGraphConf = dbGraphConf;
  }
  
  public SSDBSQLConf getDbSQLConf() {
    return dbSQLConf;
  }

  public void setDbSQLConf(SSDBSQLConf dbSQLConf) {
    this.dbSQLConf = dbSQLConf;
  }
  
  public void setRecommConf(SSRecommConf recommConf){
    this.recommConf = recommConf;
  }
  
  public SSRecommConf getRecommConf(){
    return recommConf;
  }
  
  public void setFileSysLocalConf(SSFileSysLocalConf fileSysLocalConf){
    this.fileSysLocalConf = fileSysLocalConf;
  }
  
  public SSFileSysLocalConf getFileSysLocalConf(){
    return fileSysLocalConf;
  }
  
  public void setEvernoteConf(SSEvernoteConf evernoteConf){
    this.evernoteConf = evernoteConf;
  }
  
  public SSEvernoteConf getEvernoteConf(){
    return this.evernoteConf;
  }
  
  public SSAuthConf getAuthConf(){
    return authConf;
  }
  
  public void setAuthConf(SSAuthConf authConf){
    this.authConf = authConf;
  }
  
  public SSScaffConf getScaffConf(){
    return scaffConf;
  }
  
  public void setScaffConf(SSScaffConf scaffConf){
    this.scaffConf = scaffConf;
  }
  
  public SSDataImportConf getDataImportConf(){
    return dataImportConf;
  }
  
  public void setDataImportConf(SSDataImportConf dataImportConf){
    this.dataImportConf = dataImportConf;
  }
  
  public SSDataExportConf getDataExportConf() {
    return dataExportConf;
  }

  public void setDataExportConf(SSDataExportConf dataExportConf) {
    this.dataExportConf = dataExportConf;
  }
  
  public SSLOMExtractorConf getLomExtractorConf(){
    return lomExtractorConf;
  }
  
  public void setLomExtractorConf(SSLOMExtractorConf lomExtractorConf){
    this.lomExtractorConf = lomExtractorConf;
  }
  
  public SSUserConf getUserConf(){
    return userConf;
  }
  
  public void setUserConf(SSUserConf userConf){
    this.userConf = userConf;
  }
  
  public SSEntityConf getEntityConf(){
    return entityConf;
  }
  
  public void setEntityConf(SSEntityConf entityConf){
    this.entityConf = entityConf;
  }
  
  public SSCollConf getCollConf(){
    return collConf;
  }
  
  public void setCollConf(SSCollConf collConf){
    this.collConf = collConf;
  }
  
  public SSLocationConf getLocationConf() {
    return locationConf;
  }

  public void setLocationConf(SSLocationConf locationConf) {
    this.locationConf = locationConf;
  }
  
  public SSUEConf getUeConf(){
    return ueConf;
  }
  
  public void setUeConf(SSUEConf ueConf){
    this.ueConf = ueConf;
  }
  
  public SSRatingConf getRatingConf(){
    return ratingConf;
  }
  
  public void setRatingConf(SSRatingConf ratingConf){
    this.ratingConf = ratingConf;
  }
  
  public SSTagConf getTagConf(){
    return tagConf;
  }
  
  public void setTagConf(SSTagConf tagConf){
    this.tagConf = tagConf;
  }
  
  public SSSearchConf getSearchConf(){
    return searchConf;
  }
  
  public void setSearchConf(SSSearchConf searchConf){
    this.searchConf = searchConf;
  }
  
  public SSDiscConf getDiscConf(){
    return discConf;
  }
  
  public void setLearnEpConf(SSLearnEpConf learnEpConf){
    this.learnEpConf = learnEpConf;
  }
  
  public SSLearnEpConf getLearnEpConf(){
    return learnEpConf;
  }
  
  public void setDiscConf(SSDiscConf discConf){
    this.discConf = discConf;
  }
  
  public SSConf getSsConf() {
    return ssConf;
  }

  public void setSsConf(SSConf ssConf) {
    this.ssConf = ssConf;
  }

  public SSJSONLDConf getJsonLDConf() {
    return jsonLDConf;
  }

  public void setJsonLDConf(SSJSONLDConf jsonLDConf) {
    this.jsonLDConf = jsonLDConf;
  }
}

//  private ExternalsrcConf   extsrcConf      = new ExternalsrcConf   ();


//  public ExternalsrcConf getExtsrcConf() {
//    return extsrcConf;
//  }
//
//  public void setExtsrcConf(ExternalsrcConf extsrcConf) {
//    this.extsrcConf = extsrcConf;
//  }
