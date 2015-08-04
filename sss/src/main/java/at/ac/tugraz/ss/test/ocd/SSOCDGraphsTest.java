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
package at.ac.tugraz.ss.test.ocd;

import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSFileU;
import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSServOpTestCaseA;
import at.tugraz.sss.servs.ocd.datatypes.SSOCDCreationTypeE;
import at.tugraz.sss.servs.ocd.datatypes.SSOCDGraphInputE;
import at.tugraz.sss.servs.ocd.datatypes.pars.SSOCDCreateGraphPar;
import at.tugraz.sss.servs.ocd.impl.jerseyclient.SSOCDResource;
import java.io.File;
import org.apache.commons.io.FileUtils;

public class SSOCDGraphsTest extends SSServOpTestCaseA {
  
  public SSOCDGraphsTest(SSConfA conf) {
    super(conf, null, SSServOpE.ocdCreateGraph);
  }
  
  @Override
  protected void test() throws Exception {
    System.out.println (op + " test start");
    
   
//        final SSOCDCreateGraphPar par = new SSOCDCreateGraphPar(
//        null,
//        null,
//        user,
//        "testGraph",
//        "sometype",
//        SSOCDGraphInputE.GRAPH_ML,
//        true,
//        b.toString(),
//        shouldCommit= true));
//        ((SSOCDServerI)SSOCDServ.inst.serv()).ocdCreateGraph(par);
  
    
  }
  
  @Override
  protected void testFromClient() throws Exception {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
  @Override
  protected void setUp() throws Exception {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
}
