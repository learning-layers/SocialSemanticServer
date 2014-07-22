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
package at.kc.tugraz.ss.recomm.impl;

import at.kc.tugraz.socialserver.utils.SSFileExtU;
import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.socialserver.utils.SSObjU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.recomm.api.SSRecommClientI;
import at.kc.tugraz.ss.recomm.api.SSRecommServerI;
import at.kc.tugraz.ss.recomm.conf.SSRecommConf;
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommTagsPar;
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommTagsUpdatePar;
import at.kc.tugraz.ss.recomm.datatypes.ret.SSRecommTagsRet;
import at.kc.tugraz.ss.recomm.impl.fct.misc.SSRecommFct;
import at.kc.tugraz.ss.serv.serv.api.SSConfA;
import at.kc.tugraz.ss.serv.serv.api.SSServImplMiscA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import engine.TagRecommenderEngine;
import java.io.FileNotFoundException;
import java.util.Map;

public class SSRecommImpl extends SSServImplMiscA implements SSRecommClientI, SSRecommServerI{
  
  private static final TagRecommenderEngine tagRec = new TagRecommenderEngine();
  
  public SSRecommImpl(final SSConfA conf) throws Exception{
    super(conf);
  }
  
  @Override
  public void recommTags(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSRecommTagsRet.get(recommTags(parA), parA.op));
  }
  
  @Override
  public Map<String, Double> recommTags(final SSServPar parA) throws Exception{
    
    try{
      final SSRecommTagsPar par = new SSRecommTagsPar(parA);
      
      if(
        !SSObjU.isNull(par.user) &&
        SSStrU.equals(par.user, par.forUser)){
        throw new Exception("user cannot retrieve tag recommendations for other users");
      }
      
      return tagRec.getTagsWithLikelihood(
        SSStrU.toStr(par.forUser), 
        SSStrU.toStr(par.entity), 
        par.categories,
        par.maxTags);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void recommTagsUpdate(final SSServPar parA) throws Exception{
    
    try{
      
      final SSRecommTagsUpdatePar     par        = new SSRecommTagsUpdatePar(parA);
      final SSRecommConf              recommConf = (SSRecommConf) conf;
      
      SSRecommFct.exportEntityTagCategoryTimestampCombinationsForAllUsers(
        recommConf.fileNameForTagRec,
        recommConf.usePrivateTagsToo);
      
      tagRec.loadFile(
        SSStrU.removeTrailingString(
          recommConf.fileNameForTagRec,
          SSStrU.dot + SSFileExtU.txt));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}