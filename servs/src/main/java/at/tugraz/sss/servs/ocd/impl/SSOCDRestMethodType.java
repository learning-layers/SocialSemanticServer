/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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

/**
 * @author Dieter Theiler
 */

package at.tugraz.sss.servs.ocd.impl;

//import com.sun.research.ws.wadl.HTTPMethods;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

public enum SSOCDRestMethodType {
  
  GET_ALGORITHMS("getAlgorithmNames","algorithms", HttpMethod.GET, MediaType.TEXT_XML_TYPE, MediaType.TEXT_PLAIN_TYPE),
  CREATE_GRAPH("createGraph", "graphs", HttpMethod.POST, MediaType.TEXT_XML_TYPE, MediaType.TEXT_PLAIN_TYPE),
  GET_GRAPHS("getGraphs", "graphs", HttpMethod.GET, MediaType.TEXT_XML_TYPE, MediaType.TEXT_PLAIN_TYPE),
  GET_GRAPH("getGraph", "graphs/%s", HttpMethod.GET, MediaType.TEXT_PLAIN_TYPE, MediaType.TEXT_PLAIN_TYPE),
  DELETE_GRAPH("deleteGraph", "graphs/%s", HttpMethod.DELETE, MediaType.TEXT_XML_TYPE, MediaType.TEXT_PLAIN_TYPE),
  CREATE_COVER("createCover", "covers/graphs/%s", HttpMethod.POST, MediaType.TEXT_XML_TYPE, MediaType.TEXT_PLAIN_TYPE),
  GET_COVERS("getCovers", "covers", HttpMethod.GET, MediaType.TEXT_XML_TYPE, MediaType.TEXT_PLAIN_TYPE),
  GET_COVER("getCover", "covers/%s/graphs/%s", HttpMethod.GET, MediaType.TEXT_XML_TYPE, MediaType.TEXT_PLAIN_TYPE),
  DELETE_COVER("deleteCover", "covers/%s/graphs/%s", HttpMethod.DELETE, MediaType.TEXT_XML_TYPE, MediaType.TEXT_PLAIN_TYPE),
  RUN_ALGORITHM("runAlgorithm", "covers/graphs/%s/algorithms", HttpMethod.POST, MediaType.TEXT_XML_TYPE, MediaType.TEXT_PLAIN_TYPE),
  RUN_GROUND_TRUTH_BENCHMARK("runGroundTruthBenchmark", "graphs/benchmarks", HttpMethod.POST, MediaType.TEXT_XML_TYPE, MediaType.TEXT_PLAIN_TYPE),
  RUN_STATISTICAL_MEASURE("runStatisticalMeasure", "covers/%s/graphs/%s/metrics/statistical", HttpMethod.POST, MediaType.TEXT_XML_TYPE, MediaType.TEXT_PLAIN_TYPE),
  RUN_KNOWLEDGE_DRIVEN_MEASURE("runKnowledgeDrivenMeasure", "covers/%s/graphs/%s/metrics/knowledgedriven/groundtruth/%s", HttpMethod.POST, MediaType.TEXT_XML_TYPE, MediaType.TEXT_PLAIN_TYPE);
  
  

  private String methodName = null;
  private String path = null;
  private String httpMethod = null;
  private MediaType acceptedResponseType = null;
  private MediaType consumes = null;

  private SSOCDRestMethodType(String methodName, String path, String httpMethod, MediaType acceptedResponseType, MediaType consumes) {
    this.methodName = methodName;
    this.path = path;
    this.httpMethod = httpMethod;
    this.acceptedResponseType = acceptedResponseType;
    this.consumes = consumes;
  }

  public String getMethodName() {
    return methodName;
  }

  public String getPath() {
    return path;
  }

  public String getHttpMethod() {
    return httpMethod;
  }

  public MediaType getAcceptedResponseType() {
    return acceptedResponseType;
  }

  public MediaType getConsumes() {
    return consumes;
  }
}
