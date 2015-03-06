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
package at.kc.tugraz.ss.conf.conf;

import sss.serv.eval.conf.SSEvalConf;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.kc.tugraz.ss.serv.db.conf.SSDBGraphConf;
import at.kc.tugraz.ss.serv.modeling.ue.conf.SSModelUEConf;
import at.kc.tugraz.ss.serv.auth.conf.SSAuthConf;
import at.kc.tugraz.socialserver.service.broadcast.conf.SSBroadcasterConf;
import at.kc.tugraz.ss.activity.conf.SSActivityConf;
import at.kc.tugraz.ss.category.conf.SSCategoryConf;
import at.kc.tugraz.ss.circle.conf.SSCircleConf;
import at.kc.tugraz.ss.cloud.conf.SSCloudConf;
import at.kc.tugraz.ss.conf.api.SSCoreConfA;
import at.kc.tugraz.ss.like.conf.SSLikeConf;
import at.kc.tugraz.ss.message.conf.SSMessageConf;
import at.kc.tugraz.ss.recomm.conf.SSFriendConf;
import at.kc.tugraz.ss.serv.db.conf.SSDBSQLConf;
import at.kc.tugraz.ss.serv.coll.conf.SSCollConf;
import at.kc.tugraz.ss.serv.dataimport.conf.SSDataImportConf;
import at.kc.tugraz.ss.serv.serv.datatypes.entity.conf.SSEntityConf;
import at.kc.tugraz.ss.serv.datatypes.learnep.conf.SSLearnEpConf;
import at.kc.tugraz.ss.serv.disc.conf.SSDiscConf;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.job.dataexport.conf.SSDataExportConf;
import at.kc.tugraz.ss.serv.job.i5cloud.conf.SSI5CloudConf;
import at.kc.tugraz.ss.recomm.conf.SSRecommConf;
import at.kc.tugraz.ss.serv.jobs.evernote.conf.SSEvernoteConf;
import at.kc.tugraz.ss.serv.jsonld.conf.SSJSONLDConf;
import at.kc.tugraz.ss.serv.rating.conf.SSRatingConf;
import at.kc.tugraz.ss.serv.search.conf.SSSearchConf;
import at.kc.tugraz.ss.serv.tag.conf.SSTagConf;
import at.kc.tugraz.ss.serv.ue.conf.SSUEConf;
import at.kc.tugraz.ss.serv.user.conf.SSUserConf;
import at.kc.tugraz.ss.service.filerepo.conf.SSFileRepoConf;
import at.kc.tugraz.sss.app.conf.SSAppConf;
import at.kc.tugraz.sss.appstacklayout.conf.SSAppStackLayoutConf;
import at.kc.tugraz.sss.appstacklayout.conf.SSVideoConf;
import at.kc.tugraz.sss.comment.conf.SSCommentConf;
import at.kc.tugraz.sss.flag.conf.SSFlagConf;

public class SSCoreConf extends SSCoreConfA{

  private static       SSCoreConf        inst;
//  private static final List<SSConfA>     confs = new ArrayList<SSConfA>();
  
  private SSConf                ssConf             = null;
  private SSJSONLDConf          jsonLDConf         = null;
  private SSDBGraphConf         dbGraphConf        = null;
  private SSDBSQLConf           dbSQLConf          = null;
  private SSModelUEConf         modelConf          = null;
  private SSFileRepoConf        filerepoConf       = null;
  private SSFileRepoConf        solrConf           = null;
  private SSVocConf             vocConf            = null;
  private SSBroadcasterConf     broadcasterConf    = null;
  private SSRecommConf          recommConf         = null;
  private SSEvernoteConf        evernoteConf       = null;
  private SSI5CloudConf         i5CloudConf        = null;
  private SSCloudConf           cloudConf          = null;
  private SSAuthConf            authConf           = null;
  private SSDataImportConf      dataImportConf     = null;
  private SSDataExportConf      dataExportConf     = null;
  private SSUserConf            userConf           = null;
  private SSEntityConf          entityConf         = null;
  private SSCollConf            collConf           = null;
  private SSActivityConf        activityConf       = null;
  private SSUEConf              ueConf             = null;
  private SSRatingConf          ratingConf         = null;
  private SSTagConf             tagConf            = null;
  private SSSearchConf          searchConf         = null;
  private SSDiscConf            discConf           = null;
  private SSLearnEpConf         learnEpConf        = null;
  private SSCategoryConf        categoryConf       = null;
  private SSFlagConf            flagConf           = null;
  private SSCommentConf         commentConf        = null;
  private SSMessageConf         messageConf        = null;
  private SSAppConf             appConf            = null;
  private SSFriendConf          friendConf         = null;
  private SSAppStackLayoutConf  appStackLayoutConf = null;
  private SSVideoConf           videoConf          = null;
  private SSLikeConf            likeConf           = null;
  private SSCircleConf          circleConf         = null;
  private SSEvalConf            evalConf           = null;

  public static synchronized SSCoreConf instSet(final String pathToFile) throws Exception{
    
    if(inst != null){
      return inst;
    }
    
    inst = (SSCoreConf) SSCoreConfA.instSet(pathToFile, SSCoreConf.class);
    
//    confs.add(inst.ssConf);
//    confs.add(inst.jsonLDConf);
//    confs.add(inst.dbGraphConf);
//    confs.add(inst.dbSQLConf);
//    confs.add(inst.modelConf);
//    confs.add(inst.filerepoConf);
//    confs.add(inst.solrConf);
//    confs.add(inst.vocConf);
//    confs.add(inst.broadcasterConf);
//    confs.add(inst.recommConf);
//    confs.add(inst.evernoteConf);
//    confs.add(inst.i5CloudConf);
//    confs.add(inst.cloudConf);
//    confs.add(inst.authConf);
//    confs.add(inst.dataImportConf);
//    confs.add(inst.dataExportConf);
//    confs.add(inst.userConf);
//    confs.add(inst.entityConf);
//    confs.add(inst.collConf);
//    confs.add(inst.locationConf);
//    confs.add(inst.activityConf);
//    confs.add(inst.ueConf);
//    confs.add(inst.ratingConf);
//    confs.add(inst.tagConf);
//    confs.add(inst.searchConf);
//    confs.add(inst.discConf);
//    confs.add(inst.learnEpConf);
//    confs.add(inst.categoryConf);

    return inst;
  }
  
  public static SSCoreConf instGet() throws Exception{
    
    try{
      
      if(inst == null){
        throw new Exception("inst not set");
      }
      
      return inst;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public static SSCoreConf copy() throws Exception{
    
    try{
      
      if(inst == null){
        throw new Exception("inst not set");
      }
      
      final SSCoreConf copy = new SSCoreConf();
      
      copy.ssConf             = SSConf.copy(inst.ssConf);
      copy.jsonLDConf         = SSJSONLDConf.copy(inst.jsonLDConf);
      copy.dbGraphConf        = SSDBGraphConf.copy(inst.dbGraphConf);
      copy.dbSQLConf          = SSDBSQLConf.copy(inst.dbSQLConf);
      copy.modelConf          = SSModelUEConf.copy(inst.modelConf);
      copy.filerepoConf       = SSFileRepoConf.copy(inst.filerepoConf);
      copy.solrConf           = SSFileRepoConf.copy(inst.solrConf);
      copy.vocConf            = SSVocConf.copy(inst.vocConf);
      copy.broadcasterConf    = SSBroadcasterConf.copy(inst.broadcasterConf);
      copy.recommConf         = SSRecommConf.copy(inst.recommConf);
      copy.evernoteConf       = SSEvernoteConf.copy(inst.evernoteConf);
      copy.i5CloudConf        = SSI5CloudConf.copy(inst.i5CloudConf);
      copy.cloudConf          = SSCloudConf.copy(inst.cloudConf);
      copy.authConf           = SSAuthConf.copy(inst.authConf);
      copy.dataImportConf     = SSDataImportConf.copy(inst.dataImportConf);
      copy.dataExportConf     = SSDataExportConf.copy(inst.dataExportConf);
      copy.userConf           = SSUserConf.copy(inst.userConf);
      copy.entityConf         = SSEntityConf.copy(inst.entityConf);
      copy.collConf           = SSCollConf.copy(inst.collConf);
      copy.activityConf       = SSActivityConf.copy(inst.activityConf);
      copy.ueConf             = SSUEConf.copy(inst.ueConf);
      copy.ratingConf         = SSRatingConf.copy(inst.ratingConf);
      copy.tagConf            = SSTagConf.copy(inst.tagConf);
      copy.searchConf         = SSSearchConf.copy(inst.searchConf);
      copy.discConf           = SSDiscConf.copy(inst.discConf);
      copy.learnEpConf        = SSLearnEpConf.copy(inst.learnEpConf);
      copy.categoryConf       = SSCategoryConf.copy(inst.categoryConf);
      copy.flagConf           = SSFlagConf.copy(inst.flagConf);
      copy.commentConf        = SSCommentConf.copy(inst.commentConf);
      copy.messageConf        = SSMessageConf.copy(inst.messageConf);
      copy.appConf            = SSAppConf.copy(inst.appConf);
      copy.friendConf         = SSFriendConf.copy(inst.friendConf);
      copy.appStackLayoutConf = SSAppStackLayoutConf.copy(inst.appStackLayoutConf);
      copy.videoConf          = SSVideoConf.copy(inst.videoConf);
      copy.likeConf           = SSLikeConf.copy(inst.likeConf);
      copy.circleConf         = SSCircleConf.copy(inst.circleConf);
      copy.evalConf           = SSEvalConf.copy(inst.evalConf);
      
      return copy;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public SSEvalConf getEvalConf() {
    return evalConf;
  }

  public void setEvalConf(SSEvalConf evalConf) {
    this.evalConf = evalConf;
  }
  
  public SSFlagConf getFlagConf() {
    return flagConf;
  }

  public void setFlagConf(SSFlagConf flagConf) {
    this.flagConf = flagConf;
  }
  
  public SSCommentConf getCommentConf() {
    return commentConf;
  }

  public void setCommentConf(SSCommentConf commentConf) {
    this.commentConf = commentConf;
  }
  
  public SSFriendConf getFriendConf() {
    return friendConf;
  }

  public void setFriendConf(SSFriendConf friendConf) {
    this.friendConf = friendConf;
  }
  
  public SSLikeConf getLikeConf() {
    return likeConf;
  }

  public void setLikeConf(SSLikeConf likeConf) {
    this.likeConf = likeConf;
  }
  
  public SSCircleConf getCircleConf() {
    return circleConf;
  }

  public void setCircleConf(SSCircleConf circleConf) {
    this.circleConf = circleConf;
  }
  
  public SSVideoConf getVideoConf() {
    return videoConf;
  }

  public void setVideoConf(SSVideoConf videoConf) {
    this.videoConf = videoConf;
  }
  
  public SSAppStackLayoutConf getAppStackLayoutConf() {
    return appStackLayoutConf;
  }

  public void setAppStackLayoutConf(SSAppStackLayoutConf appStackLayoutConf) {
    this.appStackLayoutConf = appStackLayoutConf;
  }
  
  public SSAppConf getAppConf() {
    return appConf;
  }

  public void setAppConf(SSAppConf appConf) {
    this.appConf = appConf;
  }
    
  public SSMessageConf getMessageConf() {
    return messageConf;
  }

  public void setMessageConf(SSMessageConf messageConf) {
    this.messageConf = messageConf;
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
  
  public SSModelUEConf getModelConf() {
    return modelConf;
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
  
  public void setEvernoteConf(SSEvernoteConf evernoteConf){
    this.evernoteConf = evernoteConf;
  }
  
  public SSEvernoteConf getEvernoteConf(){
    return this.evernoteConf;
  }
  
  public void setI5CloudConf(final SSI5CloudConf i5CloudConf){
    this.i5CloudConf = i5CloudConf;
  }
  
  public SSI5CloudConf getI5CloudConf(){
    return this.i5CloudConf;
  }
  
  public void setCloudConf(final SSCloudConf cloudConf){
    this.cloudConf = cloudConf;
  }
  
  public SSCloudConf getCloudConf(){
    return this.cloudConf;
  }
  
  public SSAuthConf getAuthConf(){
    return authConf;
  }
  
  public void setAuthConf(SSAuthConf authConf){
    this.authConf = authConf;
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
  
  public SSActivityConf getActivityConf() {
    return activityConf;
  }

  public void setActivityConf(SSActivityConf activityConf) {
    this.activityConf = activityConf;
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
  
  public SSCategoryConf getCategoryConf(){
    return categoryConf;
  }
  
  public void setCategoryConf(SSCategoryConf categoryConf){
    this.categoryConf = categoryConf;
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
