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
package at.kc.tugraz.ss.test.like;

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.like.conf.SSLikeConf;
import at.kc.tugraz.ss.like.datatypes.SSLikes;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.serv.test.api.SSServOpTestCaseA;
import at.kc.tugraz.ss.serv.voc.serv.SSVoc;

public class SSLikesGetTest extends SSServOpTestCaseA{
  
  public SSLikesGetTest(final SSLikeConf likeConf) {
    super(likeConf, null, SSMethU.likesGet);
  }
  
  @Override
  protected void test() throws Exception {
    
    System.out.println (op + " test start");
   
    final SSUri entity = 
      SSServCaller.likeUserSet(
        SSVoc.systemUserUri, 
        SSUri.get("http://www.google.at"), 
        -1);
    
    final SSLikes likes = 
      SSServCaller.likesUserGet(
        SSVoc.systemUserUri,
        SSVoc.systemUserUri,
        SSUri.get("http://www.google.at"));
    
    System.out.println (op + " test end");
  }
  
  @Override
  protected void testFromClient() throws Exception{
    
  }
  
  @Override
  protected void setUp() throws Exception {
  }
}