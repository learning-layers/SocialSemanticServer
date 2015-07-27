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
package at.tugraz.sss.servs.integrationtest;

import at.kc.tugraz.ss.conf.conf.SSCoreConf;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSEntityCircle;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.servs.integrationtest.knowbraintaggingstudy2015.SSIntegrationTestKnowBrainTaggingStudy2015;
import java.util.List;

public class SSIntegrationTestImpl
extends
  SSServImplWithDBA
implements
  SSIntegrationTestClientI,
  SSIntegrationTestServerI{
  
  private final SSIntegrationTestKnowBrainTaggingStudy2015 knowBrainTaggingStudy2015;
  
  public SSIntegrationTestImpl(final SSIntegrationTestConf conf) throws Exception{
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
    
    knowBrainTaggingStudy2015 = new SSIntegrationTestKnowBrainTaggingStudy2015();
  }
  
//  @Override
//  public void jsonLD(SSSocketCon sSCon, SSServPar parA) throws Exception{
//
//    SSServCallerU.checkKey(parA);
//
//    sSCon.writeRetFullToClient(SSJSONLDRet.get(jsonLD(par)));
//  }
  
  @Override
  public Boolean integrationTestKnowBrainTaggingStudy2015() throws Exception{
    
    try{
      
      final SSUri          adminUri    = SSVocConf.systemUserUri;
      final List<SSUri>    userUris    = knowBrainTaggingStudy2015.getUserUris(SSCoreConf.instGet().getRecomm());
      
      dbSQL.startTrans(true);
      
      final SSEntityCircle circle1     = knowBrainTaggingStudy2015.createCircle1(adminUri, userUris);
      final SSEntityCircle circle2     = knowBrainTaggingStudy2015.createCircle2(adminUri, userUris);
      final SSEntityCircle circle3     = knowBrainTaggingStudy2015.createCircle3(adminUri, userUris);
      final SSEntityCircle circle4     = knowBrainTaggingStudy2015.createCircle4(adminUri, userUris);
      
      knowBrainTaggingStudy2015.changeCircle1Label             (adminUri, circle1);
      knowBrainTaggingStudy2015.removeCircle1User3             (adminUri, circle1, userUris.get(2));
      knowBrainTaggingStudy2015.deleteCircle4                  (adminUri, circle4);
      knowBrainTaggingStudy2015.addCircle1Entities             (userUris, circle1.id);
      knowBrainTaggingStudy2015.addCircle2Entities             (userUris, circle2.id);
      knowBrainTaggingStudy2015.addCircle3Entities             (userUris, circle3.id);
      knowBrainTaggingStudy2015.updateTagRecomm                ();
      knowBrainTaggingStudy2015.getTagRecomms                  (circle1.id, circle2.id, circle3.id, userUris);
      knowBrainTaggingStudy2015.removeTags                     (userUris);
      knowBrainTaggingStudy2015.splitCircle1                   (adminUri, circle1.id, userUris);
      knowBrainTaggingStudy2015.mergeCircle2And3               (adminUri, circle2.id, circle3.id);
      knowBrainTaggingStudy2015.removeUsersFromOldCircles      (adminUri, circle1.id, circle2.id, circle3.id, userUris);
      knowBrainTaggingStudy2015.getUsersCircles                (userUris);
      knowBrainTaggingStudy2015.updateTagRecomm                ();
      knowBrainTaggingStudy2015.getTagRecommsAfterSplitAndMerge(userUris);
      
      dbSQL.commit(true);
      
      return true;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}