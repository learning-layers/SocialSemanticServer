 /**
  * Code contributed to the Learning Layers project http://www.learning-layers.eu
  * Development is partly funded by the FP7 Programme of the European Commission
  * under Grant Agreement FP7-ICT-318209. Copyright (c) 2014, Graz University of
  * Technology - KTI (Knowledge Technologies Institute). For a list of
  * contributors see the AUTHORS file at the top-level directory of this
  * distribution.
  *
  * Licensed under the Apache License, Version 2.0 (the "License"); you may not
  * use this file except in compliance with the License. You may obtain a copy of
  * the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
  * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
  * License for the specific language governing permissions and limitations under
  * the License.
  */
package at.kc.tugraz.ss.conf.conf;

import sss.serv.eval.conf.SSEvalConf;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.kc.tugraz.ss.serv.modeling.ue.conf.SSModelUEConf;
import at.kc.tugraz.ss.serv.auth.conf.SSAuthConf;
import at.kc.tugraz.socialserver.service.broadcast.conf.SSBroadcasterConf;
import at.kc.tugraz.ss.activity.conf.SSActivityConf;
import at.kc.tugraz.ss.category.conf.SSCategoryConf;
import at.kc.tugraz.ss.circle.conf.SSCircleConf;
import at.kc.tugraz.ss.cloud.conf.SSCloudConf;
import at.tugraz.sss.serv.SSCoreConfA;
import at.kc.tugraz.ss.like.conf.SSLikeConf;
import at.kc.tugraz.ss.message.conf.SSMessageConf;
import at.kc.tugraz.ss.recomm.conf.SSFriendConf;
import at.tugraz.sss.serv.SSDBSQLConf;
import at.kc.tugraz.ss.serv.coll.conf.SSCollConf;
import at.kc.tugraz.ss.serv.dataimport.conf.SSDataImportConf;
import at.kc.tugraz.ss.serv.serv.datatypes.entity.conf.SSEntityConf;
import at.kc.tugraz.ss.serv.disc.conf.SSDiscConf;
import at.kc.tugraz.ss.serv.job.dataexport.conf.SSDataExportConf;
import at.kc.tugraz.ss.serv.job.i5cloud.conf.SSI5CloudConf;
import at.kc.tugraz.ss.recomm.conf.SSRecommConf;
import at.kc.tugraz.ss.serv.datatypes.learnep.conf.SSLearnEpConf;
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
import at.kc.tugraz.sss.comment.conf.SSCommentConf;
import at.kc.tugraz.sss.flag.conf.SSFlagConf;
import at.kc.tugraz.sss.video.conf.SSVideoConf;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSDBNoSQLConf;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.servs.location.conf.SSLocationConf;
import at.tugraz.sss.servs.ocd.conf.SSOCDConf;
import at.tugraz.sss.servs.image.conf.SSImageConf;
import at.tugraz.sss.servs.integrationtest.SSIntegrationTestConf;
import at.tugraz.sss.servs.livingdocument.conf.SSLivingDocConf;
import at.tugraz.sss.servs.mail.conf.SSMailConf;

public class SSCoreConf extends SSCoreConfA {
  
  private static SSCoreConf inst;
//  private static final List<SSConfA>     confs = new ArrayList<SSConfA>();
  
  private SSConf sss = null;
  private SSJSONLDConf jsonLD = null;
  private SSDBSQLConf dbSQL = null;
  private SSDBNoSQLConf dbNoSQL = null;
  private SSModelUEConf model = null;
  private SSFileRepoConf filerepo = null;
  private SSVocConf voc = null;
  private SSBroadcasterConf broadcaster = null;
  private SSRecommConf recomm = null;
  private SSEvernoteConf evernote = null;
  private SSI5CloudConf i5Cloud = null;
  private SSCloudConf cloud = null;
  private SSAuthConf auth = null;
  private SSDataImportConf dataImport = null;
  private SSDataExportConf dataExport = null;
  private SSUserConf user = null;
  private SSEntityConf entity = null;
  private SSCollConf coll = null;
  private SSActivityConf activity = null;
  private SSUEConf ue = null;
  private SSRatingConf rating = null;
  private SSTagConf tag = null;
  private SSSearchConf search = null;
  private SSDiscConf disc = null;
  private SSLearnEpConf learnEp = null;
  private SSCategoryConf category = null;
  private SSFlagConf flag = null;
  private SSCommentConf comment = null;
  private SSMessageConf message = null;
  private SSAppConf app = null;
  private SSFriendConf friend = null;
  private SSAppStackLayoutConf appStackLayout = null;
  private SSVideoConf video = null;
  private SSLikeConf like = null;
  private SSCircleConf circle = null;
  private SSEvalConf eval = null;
  private SSOCDConf ocd = null;
  private SSImageConf image = null;
  private SSLocationConf location = null;
  private SSIntegrationTestConf integrationTest = null;
  private SSLivingDocConf  livingDocument = null;
  private SSMailConf  mail = null;
  
  public static synchronized SSCoreConf instSet(final String pathToFile) throws Exception {
    
    if(inst != null){
      return inst;
    }
    
    inst = (SSCoreConf) SSCoreConfA.instSet(pathToFile, SSCoreConf.class);
    
    return inst;
  }
  
  public static SSCoreConf instGet() throws Exception {
    
    try {
      
      if (inst == null) {
        throw new Exception("inst not set");
      }
      
      return inst;
    } catch (Exception error) {
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public static SSCoreConf copy() throws Exception {
    
    try {
      
      if (inst == null) {
        throw new Exception("inst not set");
      }
      
      final SSCoreConf copy = new SSCoreConf();
      
      copy.sss = SSConf.copy(inst.sss);
      copy.jsonLD = SSJSONLDConf.copy(inst.jsonLD);
      copy.dbSQL = SSDBSQLConf.copy(inst.dbSQL);
      copy.model = SSModelUEConf.copy(inst.model);
      copy.filerepo = SSFileRepoConf.copy(inst.filerepo);
      copy.voc = SSVocConf.copy(inst.voc);
      copy.broadcaster = SSBroadcasterConf.copy(inst.broadcaster);
      copy.recomm = SSRecommConf.copy(inst.recomm);
      copy.evernote = SSEvernoteConf.copy(inst.evernote);
      copy.i5Cloud = SSI5CloudConf.copy(inst.i5Cloud);
      copy.cloud = SSCloudConf.copy(inst.cloud);
      copy.auth = SSAuthConf.copy(inst.auth);
      copy.dataImport = SSDataImportConf.copy(inst.dataImport);
      copy.dataExport = SSDataExportConf.copy(inst.dataExport);
      copy.user = SSUserConf.copy(inst.user);
      copy.entity = SSEntityConf.copy(inst.entity);
      copy.coll = SSCollConf.copy(inst.coll);
      copy.activity = SSActivityConf.copy(inst.activity);
      copy.ue = SSUEConf.copy(inst.ue);
      copy.rating = SSRatingConf.copy(inst.rating);
      copy.tag = SSTagConf.copy(inst.tag);
      copy.search = SSSearchConf.copy(inst.search);
      copy.disc = SSDiscConf.copy(inst.disc);
      copy.learnEp = SSLearnEpConf.copy(inst.learnEp);
      copy.category = SSCategoryConf.copy(inst.category);
      copy.flag = SSFlagConf.copy(inst.flag);
      copy.comment = SSCommentConf.copy(inst.comment);
      copy.message = SSMessageConf.copy(inst.message);
      copy.app = SSAppConf.copy(inst.app);
      copy.friend = SSFriendConf.copy(inst.friend);
      copy.appStackLayout = SSAppStackLayoutConf.copy(inst.appStackLayout);
      copy.video = SSVideoConf.copy(inst.video);
      copy.like = SSLikeConf.copy(inst.like);
      copy.circle = SSCircleConf.copy(inst.circle);
      copy.eval = SSEvalConf.copy(inst.eval);
      copy.ocd = SSOCDConf.copy(inst.ocd);
      copy.image = SSImageConf.copy(inst.image);
      copy.location = SSLocationConf.copy(inst.location);
      copy.integrationTest = SSIntegrationTestConf.copy(inst.integrationTest);
      copy.livingDocument = SSLivingDocConf.copy(inst.livingDocument);
      copy.mail = SSMailConf.copy(inst.mail);
      
      return copy;
    } catch (Exception error) {
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public SSConf getSss() {
    return sss;
  }
  
  public void setSss(SSConf sss) {
    this.sss = sss;
  }
  
  public SSJSONLDConf getJsonLD() {
    return jsonLD;
  }
  
  public void setJsonLD(SSJSONLDConf jsonLD) {
    this.jsonLD = jsonLD;
  }
  
  public SSDBSQLConf getDbSQL() {
    return dbSQL;
  }
  
  public void setDbSQL(SSDBSQLConf dbSQL) {
    this.dbSQL = dbSQL;
  }
  
  public SSDBNoSQLConf getDbNoSQL() {
    return dbNoSQL;
  }
  
  public void setDbNoSQL(SSDBNoSQLConf dbNoSQL) {
    this.dbNoSQL = dbNoSQL;
  }
  
  public SSModelUEConf getModel() {
    return model;
  }
  
  public void setModel(SSModelUEConf model) {
    this.model = model;
  }
  
  public SSFileRepoConf getFilerepo() {
    return filerepo;
  }
  
  public void setFilerepo(SSFileRepoConf filerepo) {
    this.filerepo = filerepo;
  }
  
  public SSVocConf getVoc() {
    return voc;
  }
  
  public void setVoc(SSVocConf voc) {
    this.voc = voc;
  }
  
  public SSBroadcasterConf getBroadcaster() {
    return broadcaster;
  }
  
  public void setBroadcaster(SSBroadcasterConf broadcaster) {
    this.broadcaster = broadcaster;
  }
  
  public SSRecommConf getRecomm() {
    return recomm;
  }
  
  public void setRecomm(SSRecommConf recomm) {
    this.recomm = recomm;
  }
  
  public SSEvernoteConf getEvernote() {
    return evernote;
  }
  
  public void setEvernote(SSEvernoteConf evernote) {
    this.evernote = evernote;
  }
  
  public SSI5CloudConf getI5Cloud() {
    return i5Cloud;
  }
  
  public void setI5Cloud(SSI5CloudConf i5Cloud) {
    this.i5Cloud = i5Cloud;
  }
  
  public SSCloudConf getCloud() {
    return cloud;
  }
  
  public void setCloud(SSCloudConf cloud) {
    this.cloud = cloud;
  }
  
  public SSAuthConf getAuth() {
    return auth;
  }
  
  public void setAuth(SSAuthConf auth) {
    this.auth = auth;
  }
  
  public SSDataImportConf getDataImport() {
    return dataImport;
  }
  
  public void setDataImport(SSDataImportConf dataImport) {
    this.dataImport = dataImport;
  }
  
  public SSDataExportConf getDataExport() {
    return dataExport;
  }
  
  public void setDataExport(SSDataExportConf dataExport) {
    this.dataExport = dataExport;
  }
  
  public SSUserConf getUser() {
    return user;
  }
  
  public void setUser(SSUserConf user) {
    this.user = user;
  }
  
  public SSEntityConf getEntity() {
    return entity;
  }
  
  public void setEntity(SSEntityConf entity) {
    this.entity = entity;
  }
  
  public SSCollConf getColl() {
    return coll;
  }
  
  public void setColl(SSCollConf coll) {
    this.coll = coll;
  }
  
  public SSActivityConf getActivity() {
    return activity;
  }
  
  public void setActivity(SSActivityConf activity) {
    this.activity = activity;
  }
  
  public SSUEConf getUe() {
    return ue;
  }
  
  public void setUe(SSUEConf ue) {
    this.ue = ue;
  }
  
  public SSRatingConf getRating() {
    return rating;
  }
  
  public void setRating(SSRatingConf rating) {
    this.rating = rating;
  }
  
  public SSTagConf getTag() {
    return tag;
  }
  
  public void setTag(SSTagConf tag) {
    this.tag = tag;
  }
  
  public SSSearchConf getSearch() {
    return search;
  }
  
  public void setSearch(SSSearchConf search) {
    this.search = search;
  }
  
  public SSDiscConf getDisc() {
    return disc;
  }
  
  public void setDisc(SSDiscConf disc) {
    this.disc = disc;
  }
  
  public SSLearnEpConf getLearnEp() {
    return learnEp;
  }
  
  public void setLearnEp(SSLearnEpConf learnEp) {
    this.learnEp = learnEp;
  }
  
  public SSCategoryConf getCategory() {
    return category;
  }
  
  public void setCategory(SSCategoryConf category) {
    this.category = category;
  }
  
  public SSFlagConf getFlag() {
    return flag;
  }
  
  public void setFlag(SSFlagConf flag) {
    this.flag = flag;
  }
  
  public SSCommentConf getComment() {
    return comment;
  }
  
  public void setComment(SSCommentConf comment) {
    this.comment = comment;
  }
  
  public SSMessageConf getMessage() {
    return message;
  }
  
  public void setMessage(SSMessageConf message) {
    this.message = message;
  }
  
  public SSAppConf getApp() {
    return app;
  }
  
  public void setApp(SSAppConf app) {
    this.app = app;
  }
  
  public SSFriendConf getFriend() {
    return friend;
  }
  
  public void setFriend(SSFriendConf friend) {
    this.friend = friend;
  }
  
  public SSAppStackLayoutConf getAppStackLayout() {
    return appStackLayout;
  }
  
  public void setAppStackLayout(SSAppStackLayoutConf appStackLayout) {
    this.appStackLayout = appStackLayout;
  }
  
  public SSVideoConf getVideo() {
    return video;
  }
  
  public void setVideo(SSVideoConf video) {
    this.video = video;
  }
  
  public SSLikeConf getLike() {
    return like;
  }
  
  public void setLike(SSLikeConf like) {
    this.like = like;
  }
  
  public SSCircleConf getCircle() {
    return circle;
  }
  
  public void setCircle(SSCircleConf circle) {
    this.circle = circle;
  }
  
  public SSEvalConf getEval() {
    return eval;
  }
  
  public void setEval(SSEvalConf eval) {
    this.eval = eval;
  }
  
  public SSOCDConf getOcd() {
    return ocd;
  }
  
  public void setOcd(SSOCDConf ocd) {
    this.ocd = ocd;
  }
  
  public SSImageConf getImage() {
    return image;
  }
  
  public void setImage(SSImageConf image) {
    this.image = image;
  }
  
  public SSLocationConf getLocation() {
    return location;
  }
  
  public void setLocation(SSLocationConf location) {
    this.location = location;
  }
  
  public SSIntegrationTestConf getIntegrationTest() {
    return integrationTest;
  }
  
  public void setIntegrationTest(SSIntegrationTestConf integrationTest) {
    this.integrationTest = integrationTest;
  }
  
  public SSLivingDocConf getLivingDocument() {
    return livingDocument;
  }
  
  public void setLivingDocument(SSLivingDocConf livingDocument) {
    this.livingDocument = livingDocument;
  }
  
  public SSMailConf getMail() {
    return mail;
  }
  
  public void setMail(SSMailConf mail) {
    this.mail = mail;
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
