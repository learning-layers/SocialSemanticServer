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
import at.kc.tugraz.ss.serv.jobs.evernote.conf.SSEvernoteConf;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.tugraz.sss.serv.SSSearchOpE;
import at.tugraz.sss.serv.SSDBNoSQLAddDocPar;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBNoSQLRemoveDocPar;
import at.tugraz.sss.serv.SSDBNoSQLSearchPar;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSSolrKeywordLabel;
import at.tugraz.sss.serv.SSSolrSearchFieldE;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.servs.integrationtest.knowbraintaggingstudy2015.SSIntegrationTestBitsAndPiecesStudyFall2015;
import at.tugraz.sss.servs.mail.SSMailServerI;
import at.tugraz.sss.servs.mail.datatype.par.SSMailsReceivePar;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
  public Boolean integrationTestEvernoteEmailIn() throws Exception{
    
    try{
      
      final SSMailServerI  mailServ      = (SSMailServerI)   SSServReg.getServ (SSMailServerI.class);
      final SSEvernoteConf evernoteConf  = SSCoreConf.instGet().getEvernote();
      final List<SSEntity> mails         = new ArrayList<>();
      String email;
      String password;
      
      for(int counter = 0; counter < evernoteConf.emailInUsers.size(); counter++){
        
        email    = evernoteConf.emailInUsers.get     (counter);
        password = evernoteConf.emailInPasswords.get (counter);
        
        mails.clear();
        
        SSEntity.addEntitiesDistinctWithoutNull(
          mails, 
          mailServ.mailsReceive(
            new SSMailsReceivePar(
              SSVocConf.systemUserUri,
              email,
              password,
              true,
              false)));
      }
      
      return true;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(true)){
          
          SSServErrReg.reset();
          
          return integrationTestEvernoteEmailIn();
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
          
          return integrationTestBitsAndPiecesStudyFall2015();
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
  public Boolean integrationTestSolrForSearch() throws Exception {
    
    try{
      
      SSLogU.info("start intgration test solr for search");
      
      dbNoSQL.addDoc(
        new SSDBNoSQLAddDocPar(
          SSCoreConf.instGet().getSss().getLocalWorkPath(), 
          "muell.pdf")); 
      
      final Map<SSSolrSearchFieldE, List<SSSolrKeywordLabel>> wheres   = new HashMap<>();
      final List<String>                                      keywords = new ArrayList<>();
      
      keywords.add("Halbjahr 1");
      keywords.add("Muell");
      
      wheres.put(SSSolrSearchFieldE.docText, SSSolrKeywordLabel.get(keywords));
        
      final List<String> result =
        dbNoSQL.search(
          new SSDBNoSQLSearchPar(
            SSSearchOpE.and.toString(),
            SSSearchOpE.or.toString(),
            wheres,
            100));
      
      System.out.println(result);
      
      dbNoSQL.removeDoc(new SSDBNoSQLRemoveDocPar("muell.pdf"));
      
      SSLogU.info("end intgration test solr for search");
      
      return true;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}