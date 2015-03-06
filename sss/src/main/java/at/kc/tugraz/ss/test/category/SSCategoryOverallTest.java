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
package at.kc.tugraz.ss.test.category;

import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.ss.category.conf.SSCategoryConf;
import at.kc.tugraz.ss.category.datatypes.SSCategory;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSSpaceE;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.serv.test.api.SSServOverallTestCaseA;
import at.kc.tugraz.ss.serv.voc.serv.SSVoc;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import java.util.List;

public class SSCategoryOverallTest extends SSServOverallTestCaseA{

  public SSCategoryOverallTest(SSCategoryConf conf){
    super(conf);
  }
  
  @Override
  public void test() throws Exception{
    
    SSLogU.info(SSCategoryOverallTest.class.getName() + " start");

    SSServCaller.categoriesRemove(
      null, 
      null, 
      null, 
      null, 
      true);
    
    List<String>       predefinedCats      = 
      SSServCaller.categoriesPredefinedGet(SSVoc.systemUserUri);
    
    System.out.println(predefinedCats);
    
    predefinedCats = new ArrayList<>();
    
    predefinedCats.add("dieter");
      
    SSServCaller.categoriesPredefinedAdd(
      SSVoc.systemUserUri, 
      predefinedCats, 
      true);
    
    predefinedCats      = 
      SSServCaller.categoriesPredefinedGet(SSVoc.systemUserUri);
    
    System.out.println(predefinedCats);
    
    List<SSUri> categoriesAdded =
      SSServCaller.categoriesAdd(
        SSVoc.systemUserUri,
        SSUri.get("http://google.com"),
        Arrays.asList("dieter1"),
        SSSpaceE.sharedSpace,
        new Date().getTime(),
        true);
    
    System.out.println(categoriesAdded);
    
    List<SSCategory> categoriesRetrieved =
      SSServCaller.categoriesUserGet(
        SSVoc.systemUserUri,
        null,
        new ArrayList<>(),
        new ArrayList<>(),
        null,
        null);
    
    System.out.println(categoriesRetrieved);
    
    SSLogU.info(SSCategoryOverallTest.class.getName() + " end");
  }
}
