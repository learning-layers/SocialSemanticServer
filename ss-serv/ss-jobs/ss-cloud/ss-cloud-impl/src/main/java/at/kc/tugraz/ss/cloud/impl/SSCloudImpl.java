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
package at.kc.tugraz.ss.cloud.impl;

import at.kc.tugraz.ss.cloud.api.SSCloudClientI;
import at.kc.tugraz.ss.cloud.api.SSCloudServerI;
import at.kc.tugraz.ss.cloud.datatypes.par.SSCloudPublishServicePar;
import at.kc.tugraz.ss.cloud.datatypes.ret.SSCloudPublishServiceRet;
import at.kc.tugraz.ss.conf.api.SSCoreConfA;
import at.kc.tugraz.ss.conf.conf.SSCoreConf;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSConfA;
import at.kc.tugraz.ss.serv.serv.api.SSServImplMiscA;
import java.util.ArrayList;

public class SSCloudImpl extends SSServImplMiscA implements SSCloudClientI, SSCloudServerI{

  public SSCloudImpl(final SSConfA conf) throws Exception{

    super(conf);
  }
  
  /* SSCloudClientI */
//  @Override
//  public void collUserParentGet(SSSocketCon sSCon, SSServPar parA) throws Exception{
//
//    SSServCaller.checkKey(parA);
//
//    SSColl collParent = collUserParentGet(parA);
//
//    sSCon.writeRetFullToClient(SSCollUserParentGetRet.get(collParent, parA.op));
//  }

  /* SSCloudServerI */
  @Override
  public SSCloudPublishServiceRet cloudPublishService(final SSServPar parA) throws Exception{

    try{
      final SSCloudPublishServicePar par         = new SSCloudPublishServicePar(parA);
      final SSCoreConfA              confForServ = par.serv.getConfForCloudDeployment(SSCoreConf.copy(), new ArrayList<Class>());
      
      
      
      confForServ.save("file name for new core conf for service deployment");
      
      return SSCloudPublishServiceRet.get("host", 1);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}