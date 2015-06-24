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
package at.tugraz.sss.servs.ocd.impl.jerseyclient;

import at.tugraz.sss.servs.ocd.datatypes.pars.SSOCDCreateGraphPar;
import at.tugraz.sss.servs.ocd.datatypes.pars.SSOCDDeleteGraphPar;
import at.tugraz.sss.servs.ocd.datatypes.pars.SSOCDGetGraphPar;
import at.tugraz.sss.servs.ocd.datatypes.pars.SSOCDGetGraphsPar;
import at.tugraz.sss.servs.ocd.impl.types.SSOCDRestMethodType;
import javax.ws.rs.client.Entity;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.client.JerseyWebTarget;

public final class SSOCDResource {

  //FXIME: move me to application configuration
  private static final String OCD_LINK = "http://127.0.0.1:8080/ocd";
  
  
  /**
   * Defautl Constructor.
   */
  private SSOCDResource() {
    //nothing to do
  }
  
  public static String requestCreateGraph(SSOCDCreateGraphPar parA) {
    JerseyWebTarget pathTarget = getJerseyWebTarget()
            .path(SSOCDRestMethodType.CREATE_GRAPH.getPath())
            .queryParam("name",parA.getGraphName())
            .queryParam("creationType", parA.getCreationType())
            .queryParam("inputFormat", parA.getGraphInputFormat())
            .queryParam("doMakeUndirected", parA.getMakeUndirected());
    
    Entity<String> entity = Entity.entity(parA.getContent(), SSOCDRestMethodType.CREATE_GRAPH.getConsumes());
    return pathTarget.request(SSOCDRestMethodType.CREATE_GRAPH.getAcceptedResponseType()).post(entity, String.class);
    
  }
  
  public static String requestGetAlgorithms (){
    JerseyWebTarget pathTarget = getJerseyWebTarget().path(SSOCDRestMethodType.GET_ALGORITHMS.getPath());
    return pathTarget.request(SSOCDRestMethodType.GET_ALGORITHMS.getAcceptedResponseType()).get(String.class);
  }

  public static String requestGetGraph (SSOCDGetGraphPar parA) {
    String path = String.format(SSOCDRestMethodType.GET_GRAPH.getPath(),parA.getGraphId());
    JerseyWebTarget pathTarget = getJerseyWebTarget().path(path);
    return pathTarget.request(SSOCDRestMethodType.GET_GRAPH.getAcceptedResponseType()).get(String.class);
  }
  
  public static String requestGetGraphs (SSOCDGetGraphsPar parA) {
    JerseyWebTarget pathTarget = getJerseyWebTarget()
            .path(SSOCDRestMethodType.GET_GRAPHS.getPath())
            .queryParam("firstIndex", parA.getFirstIndex())
            .queryParam("length", parA.getLength())
            .queryParam("includeMeta", parA.getIncludeMeta())
            .queryParam("executionStatuses", parA.getExecutionStatuses());
    return pathTarget.request(SSOCDRestMethodType.GET_GRAPHS.getAcceptedResponseType()).get(String.class);
  }
  
  public static String requestDeleteGraph (SSOCDDeleteGraphPar parA) {
    String path = String.format(SSOCDRestMethodType.DELETE_GRAPH.getPath(),parA.getGraphId());
    JerseyWebTarget pathTarget = getJerseyWebTarget().path(path);
    return pathTarget.request(SSOCDRestMethodType.DELETE_GRAPH.getAcceptedResponseType()).delete(String.class);
  }
  public static JerseyWebTarget getJerseyWebTarget () {
    JerseyClient client = JerseyClientBuilder.createClient();
    JerseyWebTarget target = client.target(OCD_LINK);
    return target;
  }

}



