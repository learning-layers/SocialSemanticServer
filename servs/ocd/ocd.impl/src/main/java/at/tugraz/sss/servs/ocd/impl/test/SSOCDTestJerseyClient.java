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
package at.tugraz.sss.servs.ocd.impl.test;

import at.tugraz.sss.servs.ocd.datatypes.SSOCDCreationTypeE;
import at.tugraz.sss.servs.ocd.datatypes.SSOCDGraphInputE;
import at.tugraz.sss.servs.ocd.datatypes.SSOCDGraphOutputE;
import at.tugraz.sss.servs.ocd.datatypes.pars.SSOCDCreateGraphPar;
import at.tugraz.sss.servs.ocd.datatypes.pars.SSOCDDeleteGraphPar;
import at.tugraz.sss.servs.ocd.datatypes.pars.SSOCDGetGraphPar;
import at.tugraz.sss.servs.ocd.impl.jerseyclient.SSOCDResource;
import java.nio.file.Files;
import java.nio.file.Paths;


public class SSOCDTestJerseyClient {
  
  
  public static void main(String[] args) throws Exception {
    
    // test createGraph
    SSOCDCreateGraphPar createGraphPar = new SSOCDCreateGraphPar();
    String content = new String(Files.readAllBytes(Paths.get("docaTestUnweightedEdgeList.txt")));
    createGraphPar.setContent(content);
    createGraphPar.setCreationType(SSOCDCreationTypeE.UNDEFINED);
    createGraphPar.setGraphInputFormat(SSOCDGraphInputE.UNWEIGHTED_EDGE_LIST);
    createGraphPar.setGraphName("graph1");
    createGraphPar.setMakeUndirected(Boolean.FALSE); 
    
    //String response = SSOCDResource.requestCreateGraph(cDCreateGraphPar);
    
    //String response = SSOCDResource.requestGetAlgorithms();
    
    // test getGraph
    SSOCDGetGraphPar getGraphPar = new SSOCDGetGraphPar();
    getGraphPar.setGraphId("4");
    getGraphPar.setGraphOutput(SSOCDGraphOutputE.WEIGHTED_EDGE_LIST);
    String response = SSOCDResource.requestGetGraph(getGraphPar);
    
    // test deleteGraph
    SSOCDDeleteGraphPar deleteGraphPar = new SSOCDDeleteGraphPar();
    deleteGraphPar.setGraphId("7");
    //String response = SSOCDResource.requestDeleteGraph(deleteGraphPar);
    System.out.println(response);
    
    
  }
  
}
