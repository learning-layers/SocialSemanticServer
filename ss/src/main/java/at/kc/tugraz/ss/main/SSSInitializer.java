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

import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.socialserver.utils.SSMimeTypeU;
import at.kc.tugraz.ss.activity.serv.SSActivityServ;
import at.kc.tugraz.ss.category.ss.category.serv.SSCategoryServ;
import at.kc.tugraz.ss.cloud.serv.SSCloudServ;
import at.kc.tugraz.ss.conf.conf.SSCoreConf;
import at.kc.tugraz.ss.serv.db.serv.SSDBGraph;
import at.kc.tugraz.ss.serv.db.serv.SSDBSQL;
import at.kc.tugraz.ss.serv.jsonld.serv.SSJSONLD;
import at.kc.tugraz.ss.serv.voc.serv.SSVoc;
import at.kc.tugraz.ss.serv.auth.serv.SSAuthServ;
import at.kc.tugraz.ss.serv.dataimport.serv.SSDataImportServ;
import at.kc.tugraz.ss.serv.datatypes.entity.serv.SSEntityServ;
import at.kc.tugraz.ss.serv.datatypes.learnep.serv.SSLearnEpServ;
import at.kc.tugraz.ss.serv.datatypes.location.serv.SSLocationServ;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.job.dataexport.serv.SSDataExportServ;
import at.kc.tugraz.ss.serv.job.file.sys.local.serv.SSFileSysLocalServ;
import at.kc.tugraz.ss.serv.job.i5cloud.serv.SSI5CloudServ;
import at.kc.tugraz.ss.recomm.serv.SSRecommServ;
import at.kc.tugraz.ss.serv.jobs.evernote.serv.SSEvernoteServ;
import at.kc.tugraz.ss.serv.jsonld.util.SSJSONLDU;
import at.kc.tugraz.ss.serv.lomextractor.serv.SSLOMExtractorServ;
import at.kc.tugraz.ss.serv.modeling.ue.serv.SSModelUEServ;
import at.kc.tugraz.ss.serv.scaff.serv.SSScaffServ;
import at.kc.tugraz.ss.serv.serv.api.SSServImplStartA;
import at.kc.tugraz.ss.service.broadcast.service.SSBroadcasterServ;
import at.kc.tugraz.ss.service.coll.service.SSCollServ;
import at.kc.tugraz.ss.service.disc.service.SSDiscServ;
import at.kc.tugraz.ss.service.filerepo.service.SSFilerepoServ;
import at.kc.tugraz.ss.service.rating.service.SSRatingServ;
import at.kc.tugraz.ss.service.search.service.SSSearchServ;
import at.kc.tugraz.ss.service.solr.service.SSSolrServ;
import at.kc.tugraz.ss.service.tag.service.SSTagServ;
import at.kc.tugraz.ss.service.user.service.SSUserServ;
import at.kc.tugraz.ss.service.userevent.service.SSUEServ;
import at.kc.tugraz.sss.flag.serv.SSFlagServ;

public class SSSInitializer extends SSServImplStartA{
  
  protected static Boolean finished = false;
    
  public SSSInitializer() throws Exception{
    super(null);
  }
  
  @Override
  public void run(){
    
    try{

      /* vocabulary */
      SSVoc.inst.initServ               (SSCoreConf.instGet().getVocConf());

      /* util */
      SSMimeTypeU.init   ();
      
      SSJSONLDU.init(
        SSCoreConf.instGet().getJsonLDConf().uri,
        SSCoreConf.instGet().getVocConf().app, 
        SSCoreConf.instGet().getVocConf().space);
      
      /* db  */
      SSDBGraph.inst.initServ           (SSCoreConf.instGet().getDbGraphConf());
      SSDBSQL.inst.initServ             (SSCoreConf.instGet().getDbSQLConf());
      
      /* entity  */
      SSEntityServ.inst.initServ        (SSCoreConf.instGet().getEntityConf());
      SSUserServ.inst.initServ          (SSCoreConf.instGet().getUserConf());
      SSCollServ.inst.initServ          (SSCoreConf.instGet().getCollConf());
      SSUEServ.inst.initServ            (SSCoreConf.instGet().getUeConf());
      SSTagServ.inst.initServ           (SSCoreConf.instGet().getTagConf());
      
      /* job  */
      SSAuthServ.inst.initServ          (SSCoreConf.instGet().getAuthConf());
      SSEvernoteServ.inst.initServ      (SSCoreConf.instGet().getEvernoteConf());
      SSFilerepoServ.inst.initServ      (SSCoreConf.instGet().getFilerepoConf());
      SSDataImportServ.inst.initServ    (SSCoreConf.instGet().getDataImportConf());
        
      /* json-ld  */
      SSJSONLD.inst.initServ            (SSCoreConf.instGet().getJsonLDConf());
      
      /* entities  */
      SSRatingServ.inst.initServ        (SSCoreConf.instGet().getRatingConf());
      SSCategoryServ.inst.initServ      (SSCoreConf.instGet().getCategoryConf());
      SSDiscServ.inst.initServ          (SSCoreConf.instGet().getDiscConf());
      SSLearnEpServ.inst.initServ       (SSCoreConf.instGet().getLearnEpConf());
      SSLocationServ.inst.initServ      (SSCoreConf.instGet().getLocationConf());
      SSActivityServ.inst.initServ      (SSCoreConf.instGet().getActivityConf());
      
      /* jobs  */
      SSSearchServ.inst.initServ        (SSCoreConf.instGet().getSearchConf());
      SSDataExportServ.inst.initServ    (SSCoreConf.instGet().getDataExportConf());
      SSLOMExtractorServ.inst.initServ  (SSCoreConf.instGet().getLomExtractorConf());
      SSScaffServ.inst.initServ         (SSCoreConf.instGet().getScaffConf());
      SSSolrServ.inst.initServ          (SSCoreConf.instGet().getSolrConf());
      SSModelUEServ.inst.initServ       (SSCoreConf.instGet().getModelConf());
      SSBroadcasterServ.inst.initServ   (SSCoreConf.instGet().getBroadcasterConf());
      SSRecommServ.inst.initServ        (SSCoreConf.instGet().getRecommConf());
      SSFileSysLocalServ.inst.initServ  (SSCoreConf.instGet().getFileSysLocalConf());
      SSI5CloudServ.inst.initServ       (SSCoreConf.instGet().getI5CloudConf());
      SSCloudServ.inst.initServ         (SSCoreConf.instGet().getCloudConf());
      SSFlagServ.inst.initServ          (SSCoreConf.instGet().getFlagConf());

      /* scheduling task */
      SSModelUEServ.inst.schedule   ();
      SSFilerepoServ.inst.schedule  ();
      SSRecommServ.inst.schedule    ();
      
      finished = true;
    }catch(Exception error1){
      SSServErrReg.regErr(error1);
    }finally{
      try{
        finalizeImpl();
      }catch(Exception error2){
        SSLogU.err(error2);
      }
    }
  }

  public static Boolean isFinished(){
    return finished;
  }
  
  @Override
  protected void finalizeImpl() throws Exception{
    finalizeThread();
  }
}
