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
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEp;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityCircle;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.servs.integrationtest.knowbraintaggingstudy2015.SSIntegrationTestBitsAndPiecesStudyFall2015;
import at.tugraz.sss.servs.integrationtest.knowbraintaggingstudy2015.SSIntegrationTestKnowBrainTaggingStudy2015;
import java.util.List;

public class SSIntegrationTestImpl
extends
  SSServImplWithDBA
implements
  SSIntegrationTestClientI,
  SSIntegrationTestServerI{
  
  public SSIntegrationTestImpl(final SSIntegrationTestConf conf) throws Exception{
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
  }
  
  @Override
  public Boolean integrationTestBitsAndPiecesStudyFall2015() throws Exception{
    
    try{
      
      final SSIntegrationTestBitsAndPiecesStudyFall2015 bitsAndPiecesStudyFall2015 = new SSIntegrationTestBitsAndPiecesStudyFall2015();

      final SSEntity       dieterUser                 = bitsAndPiecesStudyFall2015.getDieterUser();
      final SSEntity       testUser7                  = bitsAndPiecesStudyFall2015.getTestUser7();
      
      bitsAndPiecesStudyFall2015.doLearnEpManagement(testUser7.id);
       
      final List<SSEntity> userEvents           = bitsAndPiecesStudyFall2015.getUserEvents(dieterUser.id);
      
      System.out.println(userEvents);
      
      final List<String>   predefinedCategories = bitsAndPiecesStudyFall2015.getPredefinedCategories(dieterUser.id);
      
      System.out.println(predefinedCategories);
      
      final List<SSEntity> users                = bitsAndPiecesStudyFall2015.getUsers(dieterUser.id);
      
      System.out.println(users);
      
      final List<SSEntity> learnEps             = bitsAndPiecesStudyFall2015.getLearningEpisodes(dieterUser.id);
      
      System.out.println(learnEps);
      
      final List<SSEntity> messages = bitsAndPiecesStudyFall2015.getMessages(dieterUser.id);

      System.out.println(messages);
      
      final List<SSEntity> activities = bitsAndPiecesStudyFall2015.getActivities(dieterUser.id);

      System.out.println(activities);
      
       dbSQL.startTrans(true);
      
      final SSUri     learnEpURI = bitsAndPiecesStudyFall2015.createLearnEpWithEntitiesAndCircles   (dieterUser.id);
      final SSLearnEp learnEp    = bitsAndPiecesStudyFall2015.getLearnEp                            (dieterUser.id, learnEpURI);
      
      bitsAndPiecesStudyFall2015.updateLearnEpEntity (dieterUser.id, learnEp);
      bitsAndPiecesStudyFall2015.updateLearnEpCircle (dieterUser.id, learnEp);
      
      final SSLearnEp learnEpUpdated    = bitsAndPiecesStudyFall2015.getLearnEp(dieterUser.id, learnEpURI);
      
      dbSQL.commit(true);
      
      return true;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(true)){
          
          SSServErrReg.reset();
          
          return integrationTestKnowBrainTaggingStudy2015();
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(true);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
    
  @Override
  public Boolean integrationTestKnowBrainTaggingStudy2015() throws Exception{
    
    try{
      final SSIntegrationTestKnowBrainTaggingStudy2015  knowBrainTaggingStudy2015 =  new SSIntegrationTestKnowBrainTaggingStudy2015();

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
      knowBrainTaggingStudy2015.splitCircle1                   (adminUri, circle1.id, userUris);
      knowBrainTaggingStudy2015.mergeCircle2And3               (adminUri, circle2.id, circle3.id);
      knowBrainTaggingStudy2015.removeTags                     (userUris);
      knowBrainTaggingStudy2015.removeUsersFromOldCircles      (adminUri, circle1.id, circle2.id, circle3.id, userUris);
      knowBrainTaggingStudy2015.getUsersCircles                (userUris);
      knowBrainTaggingStudy2015.updateTagRecomm                ();
      knowBrainTaggingStudy2015.getTagRecommsAfterSplitAndMerge(userUris);
      
      dbSQL.commit(true);
      
      return true;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(true)){
          
          SSServErrReg.reset();
          
          return integrationTestKnowBrainTaggingStudy2015();
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(true);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}