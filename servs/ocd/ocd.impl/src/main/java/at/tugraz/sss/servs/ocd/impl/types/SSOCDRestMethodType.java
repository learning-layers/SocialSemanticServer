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
package at.tugraz.sss.servs.ocd.impl.types;

import com.sun.research.ws.wadl.HTTPMethods;
import javax.ws.rs.core.MediaType;

public enum SSOCDRestMethodType {
  
  GET_ALGORITHMS("getAlgorithmNames","algorithms", HTTPMethods.GET, MediaType.TEXT_XML_TYPE, MediaType.TEXT_PLAIN_TYPE),
  CREATE_GRAPH("createGraph", "graphs", HTTPMethods.POST, MediaType.TEXT_XML_TYPE, MediaType.TEXT_PLAIN_TYPE),
  GET_GRAPHS("getGraphs", "graphs", HTTPMethods.GET, MediaType.TEXT_XML_TYPE, MediaType.TEXT_PLAIN_TYPE),
  GET_GRAPH("getGraph", "graphs/%s", HTTPMethods.GET, MediaType.TEXT_PLAIN_TYPE, MediaType.TEXT_PLAIN_TYPE),
  DELETE_GRAPH("deleteGraph", "graphs/%s", HTTPMethods.DELETE, MediaType.TEXT_XML_TYPE, MediaType.TEXT_PLAIN_TYPE),
  CREATE_COVER("createCover", "covers/graphs/%s", HTTPMethods.POST, MediaType.TEXT_XML_TYPE, MediaType.TEXT_PLAIN_TYPE),
  GET_COVERS("getCovers", "covers", HTTPMethods.GET, MediaType.TEXT_XML_TYPE, MediaType.TEXT_PLAIN_TYPE),
  GET_COVER("getCover", "covers/%s/graphs/%s", HTTPMethods.GET, MediaType.TEXT_XML_TYPE, MediaType.TEXT_PLAIN_TYPE),
  DELETE_COVER("deleteCover", "covers/%s/graphs/%s", HTTPMethods.DELETE, MediaType.TEXT_XML_TYPE, MediaType.TEXT_PLAIN_TYPE),
  RUN_ALGORITHM("runAlgorithm", "covers/graphs/%s/algorithms", HTTPMethods.POST, MediaType.TEXT_XML_TYPE, MediaType.TEXT_PLAIN_TYPE),
  RUN_GROUND_TRUTH_BENCHMARK("runGroundTruthBenchmark", "graphs/benchmarks", HTTPMethods.POST, MediaType.TEXT_XML_TYPE, MediaType.TEXT_PLAIN_TYPE),
  RUN_STATISTICAL_MEASURE("runStatisticalMeasure", "covers/%s/graphs/%s/metrics/statistical", HTTPMethods.POST, MediaType.TEXT_XML_TYPE, MediaType.TEXT_PLAIN_TYPE),
  RUN_KNOWLEDGE_DRIVEN_MEASURE("runKnowledgeDrivenMeasure", "covers/%s/graphs/%s/metrics/knowledgedriven/groundtruth/%s", HTTPMethods.POST, MediaType.TEXT_XML_TYPE, MediaType.TEXT_PLAIN_TYPE);
  
  

  private String methodName = null;
  private String path = null;
  private HTTPMethods httpMethod = null;
  private MediaType acceptedResponseType = null;
  private MediaType consumes = null;

  private SSOCDRestMethodType(String methodName, String path, HTTPMethods httpMethod, MediaType acceptedResponseType, MediaType consumes) {
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

  public HTTPMethods getHttpMethod() {
    return httpMethod;
  }

  public MediaType getAcceptedResponseType() {
    return acceptedResponseType;
  }

  public MediaType getConsumes() {
    return consumes;
  }

}


