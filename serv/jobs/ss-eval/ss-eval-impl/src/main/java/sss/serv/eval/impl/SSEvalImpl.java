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
package sss.serv.eval.impl;

import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.serv.api.SSConfA;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import sss.serv.eval.api.SSEvalClientI;
import sss.serv.eval.api.SSEvalServerI;
import sss.serv.eval.conf.SSEvalConf;
import sss.serv.eval.impl.fct.sql.SSEvalSQLFct;

public class SSEvalImpl extends SSServImplWithDBA implements SSEvalClientI, SSEvalServerI{

//  private final SSLearnEpGraphFct       graphFct;
  private final SSEvalSQLFct sqlFct;
  private final SSEvalConf   evalConf;

  public SSEvalImpl(final SSConfA conf, final SSDBSQLI dbSQL) throws Exception{

    super(conf, null, dbSQL);

    sqlFct     = new SSEvalSQLFct(this);
    evalConf   = (SSEvalConf) conf;
  }

//  @Override
//  public void learnEpsGet(SSSocketCon sSCon, SSServPar par) throws Exception{
//
//    SSServCaller.checkKey(par);
//
//    sSCon.writeRetFullToClient(SSLearnEpsGetRet.get(learnEpsGet(par), par.op));
//  }

//  @Override
//  public List<SSLearnEp> learnEpsGet(final SSServPar parA) throws Exception{
//
//    try{
//      final SSLearnEpsGetPar par      = new SSLearnEpsGetPar(parA);
//      final List<SSLearnEp>  learnEps = sqlFct.getLearnEps(par.user);
//
//      for(SSLearnEp learnEp : learnEps){
//
//        learnEp.circleTypes.addAll(
//          SSServCaller.circleTypesGet(
//            par.user,
//            par.user,
//            learnEp.id, 
//            true));
//        
//         learnEp.read = 
//           SSServCaller.entityReadGet(
//             par.user, 
//             learnEp.id);
//         
//         for(SSUri user : sqlFct.getLearnEpUserURIs(learnEp.id)){
//           
//           learnEp.users.add(
//             SSServCaller.entityDescGet(
//               par.user, 
//               user, 
//               false,
//               false, 
//               false, 
//               false, 
//               false, 
//               false, 
//               false));
//         }
//         
//         if(!learnEpConf.useEpisodeLocking){
//           learnEp.locked       = false;
//           learnEp.lockedByUser = false;
//         }else{
//           
//           learnEp.locked       = SSLearnEpAccessController.isLocked (learnEp.id);
//           learnEp.lockedByUser = SSLearnEpAccessController.hasLock  (par.user, learnEp.id);
//         }
//      }
//
//      return learnEps;
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }
}
