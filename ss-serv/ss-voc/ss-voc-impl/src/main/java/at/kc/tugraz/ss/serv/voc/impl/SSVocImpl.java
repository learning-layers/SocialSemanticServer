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
package at.kc.tugraz.ss.serv.voc.impl;

import at.kc.tugraz.ss.serv.voc.api.SSVocI;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.socialserver.utils.*;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.serv.api.SSServImplMiscA;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

 public class SSVocImpl extends SSServImplMiscA implements SSVocI{
 
  public SSVocImpl(final SSVocConf conf) throws Exception{
    super(conf);
  }
  
  @Override
  public List<SSMethU> publishClientOps() throws Exception{
    return new ArrayList<SSMethU>();
  }
  
  @Override
  public List<SSMethU> publishServerOps() throws Exception{
    
    List<SSMethU> serverOps = new ArrayList<SSMethU>();
    
    Method[] methods = SSVocI.class.getMethods();
    
    for(Method method : methods){
      serverOps.add(SSMethU.get(method.getName()));
    }
    
    return serverOps;
  }
  
  @Override
  public void handleClientOp(SSSocketCon sSCon, SSServPar par) throws Exception{
    throw new UnsupportedOperationException(SSStrU.empty);
  }
  
  @Override
  public Object handleServerOp(SSServPar par) throws Exception{
    return SSVocI.class.getMethod(SSMethU.toStr(par.op), SSServPar.class).invoke(this, par);
  }

  @Override
  public SSUri vocURIPrefixGet(SSServPar parA) throws Exception{
    return SSUri.get(SSLinkU.prefixHttp + ((SSVocConf)conf).getSpaceAndApp());
  }
}

//
//  @Override
//  public boolean isNotSSDisc(
//    SSUri uri) throws Exception {
//  
//    return !isSSDisc(uri);
//  }
//  
//  @Override
//  public boolean isSSDisc(
//    SSUri uri) throws Exception {
//
//    return entityU.isDisc(getSSEntityTypeFromUri(uri));
//  }
// 
//  public static synchronized String getIdFromFileUri(
//    SSUri  fileUriOrId, 
//    String virtualFileRepo) {
//
//    if (strU.isSame(strU.subString(fileUriOrId, 0, strU.length(virtualFileRepo)), virtualFileRepo)) {
//
//      return strU.subString(fileUriOrId, strU.length(virtualFileRepo), strU.length(fileUriOrId));
//    }
//    
//    return fileUriOrId;
//  }
//  public static synchronized String getFileUriFromId(
//          String fileUriOrId) {
//
//    String virtualFileRepoNs = getGraphUri(SSVocNamespace.file).toString();
//    
//    if (strU.isSame(
//            strU.subString(
//            fileUriOrId,
//            0,
//            strU.length(virtualFileRepoNs)),
//            virtualFileRepoNs)) {
//
//      return fileUriOrId;
//    }
//
//    if (SSObjectUtils.isNotNull(fileUriOrId)) {
//
//      return virtualFileRepoNs + fileUriOrId;
//    }
//
//    return fileUriOrId;
//  }
//	/**
//	 * 0 return whether the resource is not of type person/user <br>
//	 */
//	public boolean isResourceOfTypeDifferentFromPerson(String resourceType) {
//
//		if(
//				resourceType                                                     != null  &&
//				resourceType.isSame(GC.stringEmpty)                                          == false &&
//				resourceType.isSame(ModelGC.TYPE_DEFAULT)              == false &&
//				resourceType.isSame(ModelGC.TYPE_PERSON)               == false &&
//				resourceType.isSame(ModelGC.TYPE_USER)                 == false){
//			return true;
//		}
//
//		return false;
//	}