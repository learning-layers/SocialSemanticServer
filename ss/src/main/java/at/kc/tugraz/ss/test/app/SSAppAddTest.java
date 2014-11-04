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
package at.kc.tugraz.ss.test.app;

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.ss.datatypes.datatypes.SSTextComment;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.serv.test.api.SSServOpTestCaseA;
import at.kc.tugraz.ss.serv.voc.serv.SSVoc;
import at.kc.tugraz.sss.app.conf.SSAppConf;
import at.kc.tugraz.sss.app.datatypes.SSApp;
import java.util.ArrayList;
import java.util.List;

public class SSAppAddTest extends SSServOpTestCaseA{
  
  public SSAppAddTest(final SSAppConf appConf) {
    super(appConf, SSMethU.appAdd);
  }
  
  @Override
  protected void test() throws Exception {
    
    System.out.println (op + " test start");

    final List<String> downloadLinks = new ArrayList<>();
    final List<String> imageLinks    = new ArrayList<>();
    final List<String> videoLinks    = new ArrayList<>();
    
    downloadLinks.add("http://download1.com");
    downloadLinks.add("http://download2.com");
    
    imageLinks.add("http://imageLink1.com");
    imageLinks.add("http://imageLink2.com");
    
    videoLinks.add("http://videoLink1.com");
    videoLinks.add("http://videoLink2.com");
    
    final SSUri app =
      SSServCaller.appAdd(
        SSVoc.systemUserUri,
        SSLabel.get("Bnp"),
        SSTextComment.get("short desc"),
        SSTextComment.get("descriptionFunctional"),
        SSTextComment.get("descriptionTechnical"),
        SSTextComment.get("descriptionInstall"), 
        SSUri.get("http://downloadIOS.com"), 
        SSUri.get("http://downloadAndroid.com"), 
        SSUri.get("http://downloadGitHub.com"),
        SSUri.get(downloadLinks), 
        SSUri.get(imageLinks),
        SSUri.get(videoLinks));

    System.out.println(app);
    
    System.out.println (op + " test end");
  }
  
  @Override
  protected void testFromClient() throws Exception{
    
  }
  
  @Override
  protected void setUp() throws Exception {
  }
}