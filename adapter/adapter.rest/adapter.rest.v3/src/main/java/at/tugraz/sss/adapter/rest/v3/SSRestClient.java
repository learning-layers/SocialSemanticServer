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
package at.tugraz.sss.adapter.rest.v3;

import at.tugraz.sss.servs.auth.datatype.SSAuthE;
import at.tugraz.sss.servs.auth.datatype.ret.SSAuthCheckCredRet;
import at.tugraz.sss.adapter.rest.v3.app.*;
import at.tugraz.sss.adapter.rest.v3.disc.*;
import at.tugraz.sss.adapter.rest.v3.entity.*;
import at.tugraz.sss.adapter.rest.v3.recomm.*;
import at.tugraz.sss.serv.util.*;
import com.nimbusds.oauth2.sdk.http.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.ws.rs.client.*;
import javax.ws.rs.core.*;
import org.glassfish.jersey.media.multipart.*;
import org.glassfish.jersey.media.multipart.file.*;

public class SSRestClient {
  
  public static final  String host      = "http://localhost:8080/";
  public static final  String restPath  = "sss.adapter.rest.v3/rest/";
  
//  public static final  String host      = "http://test-ll.know-center.tugraz.at/";
//  public static final  String restPath  = "bp.preparation/rest/";
  
//  public static final  String host      = "http://test-ll.know-center.tugraz.at/";
//  public static final  String restPath  = "eval/rest/";
  
//    public static final  String host      = "http://test-ll.know-center.tugraz.at/";
//    public static final  String restPath  = "test/rest/";
  
//  public static final SSAuthE  authMethod = SSAuthE.oidc;
  public static final SSAuthE  authMethod = SSAuthE.csvFileAuth;
  
  private final String oidcToken = "eyJhbGciOiJSUzI1NiJ9.eyJleHAiOjE0NTY0MTI3NjgsImF1ZCI6WyIwM2Q3ZGQwOS1lOTllLTQzZWEtYmQ5My1kMDY2NjE0MjZjOTUiXSwiaXNzIjoiaHR0cHM6XC9cL2FwaS5sZWFybmluZy1sYXllcnMuZXVcL29cL29hdXRoMlwvIiwianRpIjoiODQwMGU0ZDgtYjEwZS00ZGJhLTgwYjItYTQyOWY3NDY3MWRkIiwiaWF0IjoxNDU2Mzk0NzY4fQ.cAh12NG-8H4LCRfFgEM-C5syO6bgpVN-OuWsdnxWuempl74rW3cxGsXpqYmfYJ2fYUeqeC_az_Gl-lhywSKT101d2KAi524L3sShMSRUaDH7RwkgAHrelXlXDGwUiRAulgkDdmZNstK4uEnWpYYDF-YDCYZs-sJjTIqvO0nqJo4";
  private final Client client;
  
  private String key = null;
  
  
  public SSRestClient(final Client client){
    this.client = client;
  }
  
  public static void main(String[] args) throws Exception {
    
    try{
      
      final Client       client = ClientBuilder.newClient();
      final SSRestClient caller = new SSRestClient(client);
      
      client.register(MultiPartFeature.class);
      
      caller.auth();
      
      caller.getLearnEpCircleEntityStructure("918879364224283841");
//      caller.getLivingDoc(URLEncoder.encode("65548/", SSEncodingU.utf8.toString())); //URLEncoder.encode("https://test.learnenv.com/document/65548/", SSEncodingU.utf8.toString()));
      
//      caller.updateRecomm(
//        "dieter",  //realm
//        "someEntity"); //entity
//      
//      caller.getUsersForEntityIgnoreAccessRights(
//        "dieter", //realm
//        "someEntity");//entity
      
//      caller.uploadFile();
//      caller.appsGet();
//      caller.appAdd();
      
//      caller.getEntitiesFilteredAccessible();
//      caller.discCreate();
//      caller.discsGetFiltered();
      
    }catch (Exception error) {
      System.err.println(error);
      throw error;
    }
  }
  
  public void getLearnEpCircleEntityStructure(final String episodeID) throws Exception{
    
    try{
      
      final HTTPRequest      hrq;
      final HTTPResponse     hrs;

      hrq = new HTTPRequest(HTTPRequest.Method.GET, new URL(host + restPath + "learneps/" + URLEncoder.encode(episodeID, SSEncodingU.utf8.toString()) + "/structure/circles/entities"));
      hrq.setAuthorization("Bearer " + key);
      
      hrs = hrq.send();
      
      System.out.println(hrs);
//      final WebTarget target = client.target(host + restPath + "livingdocs/https%2F");
//      final Invocation.Builder builder = target.request(MediaType.APPLICATION_JSON_TYPE);
//          
//      builder.header(HttpHeaders.AUTHORIZATION, "Bearer " + key);
//        
//      final String response       = builder.get(String.class);
 
      System.out.println("learn ep circle entity structure)");
      System.out.println("----------");
      System.out.println(hrs.getContent());
    }catch (Exception error) {
      System.err.println(error);
      throw error;
    }
  }
  public void getLivingDoc(final String livingDocID) throws Exception{
    
    try{
      
      final HTTPRequest      hrq;
      final HTTPResponse     hrs;
      
      hrq                     = new HTTPRequest(HTTPRequest.Method.GET, new URL(host + restPath + "livingdocs/" + URLEncoder.encode("65548", SSEncodingU.utf8.toString())));
      hrq.setAuthorization("Bearer " + key);
      
      hrs = hrq.send();
      
      System.out.println(hrs);
//      final WebTarget target = client.target(host + restPath + "livingdocs/https%2F");
//      final Invocation.Builder builder = target.request(MediaType.APPLICATION_JSON_TYPE);
//          
//      builder.header(HttpHeaders.AUTHORIZATION, "Bearer " + key);
//        
//      final String response       = builder.get(String.class);
 
      System.out.println("living doc)");
      System.out.println("----------");
//      System.out.println(response);
    }catch (Exception error) {
      System.err.println(error);
      throw error;
    }
  }
  
  public void getUsersForEntityIgnoreAccessRights(
    final String realm, 
    final String entity){
    
    try{
      
      final WebTarget          target = client.target(host + restPath + "recomm/users/ignoreaccessrights/realm/" + realm + "/entity/" + entity);
      final Invocation.Builder builder = target.request(MediaType.APPLICATION_JSON_TYPE);
      
      builder.header(HttpHeaders.AUTHORIZATION, "Bearer " + key);
       
      final String         response       = builder.get(String.class);
      
      System.out.println("recommended users for entity (ignoring access rights)");
      System.out.println("-----------------------------------------------------");
      System.out.println(response);
    }catch (Exception error) {
      System.err.println(error);
      throw error;
    }
  }
  
  public void updateRecomm(
    final String realm, 
    final String entity) throws Exception{
    
    try{
      
      final WebTarget          target = client.target(host + restPath + "recomm/update");
      final Invocation.Builder builder = target.request(MediaType.APPLICATION_JSON_TYPE);
      
      builder.header(HttpHeaders.AUTHORIZATION, "Bearer " + key);
       
      final SSRecommUpdateRESTPar par = new SSRecommUpdateRESTPar();
      
      par.realm = realm;
      
      par.setForUser("misc");
      par.setEntity (entity);
      
      par.tags = new ArrayList<>();
      
      par.tags.add("one");
      par.tags.add("two");
        
      final String json = SSJSONU.jsonStr(par);
      
      System.out.println(json);

      final Entity<String> formattedInput = Entity.entity(json, MediaType.APPLICATION_JSON_TYPE);
      final String         response       = builder.put(formattedInput, String.class);
      
      System.out.println("updated recomm");
      System.out.println("---------------");
      System.out.println(response);
    }catch (Exception error) {
      System.err.println(error);
      throw error;
    }
  }
  
  public void uploadFile(){
    
    try{
      final WebTarget        target           = client.target(host + restPath + "files/upload/");
      final MultiPart        multiPart        = new MultiPart();
      
      multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);
      
      final FileDataBodyPart fileDataBodyPart =
        new FileDataBodyPart(
          "file",
          new File("C:\\dieter\\Clipboard01.jpg"),
          MediaType.APPLICATION_OCTET_STREAM_TYPE);
      
      multiPart.bodyPart(fileDataBodyPart);
      
      Response response = target.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(multiPart, multiPart.getMediaType()));
        
//        target.request(MediaType.MULTIPART_FORM_DATA).post(Entity.entity(multiPart, multiPart.getMediaType()));
      
      System.out.println(response.getStatus() + " " + response.getStatusInfo()+ " " + response);
      
    }catch (Exception error) {
      System.err.println(error);
      throw error;
    }
  }
  
  public void appAdd() throws Exception{
    
    try{
      final WebTarget          target = client.target(host + restPath + "apps/");
      final Invocation.Builder builder = target.request(MediaType.APPLICATION_JSON_TYPE);
      
      builder.header(HttpHeaders.AUTHORIZATION, "Bearer " + key);
          
      final SSAppAddRESTPar par = new SSAppAddRESTPar();
      
      par.setLabel                 ("The learning toolbox app containing all the stacks");
      par.setDescriptionShort      ("LearningToolboxApp");
      par.setDescriptionFunctional ("We add stacks containing tiles and they can be played here");
      par.setDescriptionTechnical  ("AwSome comment");
//      par.setDownloadIOS           ("SSUri");
//      par.setDownloadAndroid       ("SSUri");
//      par.setFork                  ("SSUri");
      
      final Entity<String> formattedInput = Entity.entity(SSJSONU.jsonStr(par), MediaType.APPLICATION_JSON_TYPE);
      final String         response       = builder.post(formattedInput, String.class);

      System.out.println("appAdd");
      System.out.println("------");
      System.out.println(response);
    
    }catch (Exception error) {
      System.err.println(error);
      throw error;
    }
  }
     
  public void appsGet(){
    
    try{
      final WebTarget          target = client.target(host + restPath + "apps/");
      final Invocation.Builder builder = target.request(MediaType.APPLICATION_JSON_TYPE);
      
      builder.header(HttpHeaders.AUTHORIZATION, "Bearer " + key);
          
      final String         response       = builder.get(String.class);

      System.out.println("appsGet");
      System.out.println("-------");
      System.out.println(response);
    
    }catch (Exception error) {
      System.err.println(error);
      throw error;
    }
  }
  
  public void discsGetFiltered(){
    
    try{
      final WebTarget          target = client.target(host + restPath + "discs/filtered/http%253A%252F%252Fsss.eu%252F77962444117334693");
      final Invocation.Builder builder = target.request(MediaType.APPLICATION_JSON_TYPE);
      
      builder.header(HttpHeaders.AUTHORIZATION, "Bearer " + key);
          
      final String         json           = "{\"setLikes\":true,\"setEntries\":false}";
      final Entity<String> formattedInput = Entity.entity(json /*SSJSONU.jsonStr(par)*/, MediaType.APPLICATION_JSON_TYPE);
      final String         response       = builder.post(formattedInput, String.class);

      System.out.println(response);
    
    }catch (Exception error) {
      System.err.println(error);
      throw error;
    }
  }
  
  public void discCreate() throws Exception{
    
    try{
      final WebTarget          target = client.target(host + restPath + "discs/");
      final Invocation.Builder builder = target.request(MediaType.APPLICATION_JSON_TYPE);
      
      builder.header(HttpHeaders.AUTHORIZATION, "Bearer " + key);
      
      final SSDiscEntryAddRESTPar par      = new SSDiscEntryAddRESTPar();
      final List<String>          entities = new ArrayList<>();
      final List<String>          targets  = new ArrayList<>();
      
      par.setLabel      ("Initial discussion for shared episode");
      par.setDescription("<p>Hey go. Let's go.</p>");
      
      entities.add("http://sss.eu/710668960821648561");
      
      par.setEntities(entities);
      
      targets.add("http://sss.eu/667159587087257512");
      
      par.setTargets(targets);
      
      par.setType("qa");
      
      par.addNewDisc = true;

      final String json = "{\"label\":\"Initial discussion for shared episode\",\"description\":\"<p>Hey go. Let's go.</p>\",\"entities\":[\"http://sss.eu/710668960821648561\"],\"targets\":[\"http://sss.eu/667159587087257512\"],\"type\":\"qa\",\"addNewDisc\":true}";

      final Entity<String> formattedInput = Entity.entity(json /*SSJSONU.jsonStr(par)*/, MediaType.APPLICATION_JSON_TYPE);
      final String         response       = builder.post(formattedInput, String.class);
      
      System.out.println(response);
      
    }catch(Exception error){
      System.err.println(error);
      throw error;
    }
  }
  
  public void getEntitiesFilteredAccessible() throws Exception{
    
    try{
      final WebTarget target = client.target(host + restPath + "entities/filtered/accessible/");
      final Invocation.Builder builder = target.request(MediaType.APPLICATION_JSON_TYPE);
          
      builder.header(HttpHeaders.AUTHORIZATION, "Bearer " + key);
        
      final SSEntitiesAccessibleGetRESTPar par = new SSEntitiesAccessibleGetRESTPar();
      final List<String>                   types   = new ArrayList<>();
      final List<String>                   authors = new ArrayList<>();
      
//      {"startTime":1442072006290,"endTime":1453293235346,"authors":["http://sss.eu/1690149260961113"],"types":["entity","file","evernoteResource","evernoteNote","evernoteNotebook","placeholder"],"setTags":true,"setFlags":true,"pageSize":0}
      
      types.add("entity");
      types.add("uploadedFile");
      types.add("evernoteResource");
      types.add("evernoteNote");
      types.add("evernoteNotebook");
      types.add("placeholder");
      
      par.setTypes(types);
      
      authors.add("http://sss.eu/1690149260961113");
      
      par.setAuthors(authors);
      
      par.startTime      = 1442072006290L;
      par.endTime        = 1453293235346L;
      par.pageSize       = 0;
      par.setTags        = true;
      par.setFlags       = true;
      
      final Entity<String> formattedInput = Entity.entity(SSJSONU.jsonStr(par), MediaType.APPLICATION_JSON_TYPE);
      final String         response       = builder.post(formattedInput, String.class);

    }catch (Exception error) {
      System.err.println(error);
      throw error;
    }
  }
  
  public void auth(){
    
    try{
      
      final WebTarget          target  = client.target(host + restPath + "auth/");
      final Invocation.Builder builder = target.request(MediaType.APPLICATION_JSON_TYPE);
      SSAuthCheckCredRet       ret     = null;
      
      switch(authMethod){
        
        case csvFileAuth:{
          
          final String input  = "{\"label\":\"bn-testuser8@know-center.at\",\"password\":\"1234\"}";
          final Entity<String> formattedInput = Entity.entity(input, MediaType.APPLICATION_JSON_TYPE);
          
          ret = builder.post(formattedInput, SSAuthCheckCredRet.class);
          
          break;
        }
        
        case oidc:{
      
          builder.header(HttpHeaders.AUTHORIZATION, "Bearer " + oidcToken);
          
          ret = builder.get(SSAuthCheckCredRet.class);
          
          break;
        }
        
        default: throw new UnsupportedOperationException();
      }
      
      key = ret.key;
      
    }catch (Exception error) {
      System.err.println(error);
      throw error;
    }
  }
}

////TODO: move me to application configuration
//  private static final String OCD_LINK = "http://127.0.0.1:8080/ocd";
//  
//  
//  /**
//   * Defautl Constructor.
//   */
//  private SSOCDResource() {
//    //nothing to do
//  }
//  
//  public static String requestCreateGraph(SSOCDCreateGraphPar parA) {
//    JerseyWebTarget pathTarget = getJerseyWebTarget()
//            .path(SSOCDRestMethodType.CREATE_GRAPH.getPath())
//            .queryParam("name",parA.getGraphName())
//            .queryParam("creationType", parA.getCreationType())
//            .queryParam("inputFormat", parA.getGraphInputFormat())
//            .queryParam("doMakeUndirected", parA.getMakeUndirected());
//    
//    Entity<String> entity = Entity.entity(parA.getContent(), SSOCDRestMethodType.CREATE_GRAPH.getConsumes());
//    return pathTarget.request(SSOCDRestMethodType.CREATE_GRAPH.getAcceptedResponseType()).post(entity, String.class);
//    
//  }
//  
//  public static String requestGetAlgorithms (){
//    JerseyWebTarget pathTarget = getJerseyWebTarget().path(SSOCDRestMethodType.GET_ALGORITHMS.getPath());
//    return pathTarget.request(SSOCDRestMethodType.GET_ALGORITHMS.getAcceptedResponseType()).get(String.class);
//  }
//
//  public static String requestGetGraph (SSOCDGetGraphPar parA) {
//    String path = String.format(SSOCDRestMethodType.GET_GRAPH.getPath(),parA.getGraphId());
//    JerseyWebTarget pathTarget = getJerseyWebTarget().path(path);
//    return pathTarget.request(SSOCDRestMethodType.GET_GRAPH.getAcceptedResponseType()).get(String.class);
//  }
//  
//  public static String requestGetGraphs (SSOCDGetGraphsPar parA) {
//    JerseyWebTarget pathTarget = getJerseyWebTarget()
//            .path(SSOCDRestMethodType.GET_GRAPHS.getPath())
//            .queryParam("firstIndex", parA.getFirstIndex())
//            .queryParam("length", parA.getLength())
//            .queryParam("includeMeta", parA.getIncludeMeta())
//            .queryParam("executionStatuses", parA.getExecutionStatuses());
//    return pathTarget.request(SSOCDRestMethodType.GET_GRAPHS.getAcceptedResponseType()).get(String.class);
//  }
//  
//  public static String requestDeleteGraph (SSOCDDeleteGraphPar parA) {
//    String path = String.format(SSOCDRestMethodType.DELETE_GRAPH.getPath(),parA.getGraphId());
//    JerseyWebTarget pathTarget = getJerseyWebTarget().path(path);
//    return pathTarget.request(SSOCDRestMethodType.DELETE_GRAPH.getAcceptedResponseType()).delete(String.class);
//  }
//  public static JerseyWebTarget getJerseyWebTarget () {
//    JerseyClient client = JerseyClientBuilder.createClient();
//    JerseyWebTarget target = client.target(OCD_LINK);
//    return target;
//  }


// test createGraph
//      SSOCDCreateGraphPar createGraphPar = new SSOCDCreateGraphPar();
//      String content = new String(Files.readAllBytes(Paths.get("docaTestUnweightedEdgeList.txt")));
//      createGraphPar.setContent(content);
//      createGraphPar.setCreationType(SSOCDCreationTypeE.UNDEFINED);
//      createGraphPar.setGraphInputFormat(SSOCDGraphInputE.UNWEIGHTED_EDGE_LIST);
//      createGraphPar.setGraphName("graph1");
//      createGraphPar.setMakeUndirected(Boolean.FALSE);
//      
//      //String response = SSOCDResource.requestCreateGraph(cDCreateGraphPar);
//      
//      //String response = SSOCDResource.requestGetAlgorithms();
//      
//      // test getGraph
//      SSOCDGetGraphPar getGraphPar = new SSOCDGetGraphPar();
//      getGraphPar.setGraphId("4");
//      getGraphPar.setGraphOutput(SSOCDGraphOutputE.WEIGHTED_EDGE_LIST);
//      String response = SSOCDResource.requestGetGraph(getGraphPar);
//      
//      // test deleteGraph
//      SSOCDDeleteGraphPar deleteGraphPar = new SSOCDDeleteGraphPar();
//      deleteGraphPar.setGraphId("7");
//      //String response = SSOCDResource.requestDeleteGraph(deleteGraphPar);
//      System.out.println(response);