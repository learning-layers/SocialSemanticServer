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

import at.kc.tugraz.ss.serv.ss.auth.datatypes.enums.*;
import at.kc.tugraz.ss.serv.ss.auth.datatypes.ret.*;
import at.tugraz.sss.adapter.rest.v3.disc.*;
import at.tugraz.sss.adapter.rest.v3.entity.*;
import at.tugraz.sss.serv.util.*;
import java.util.*;
import javax.ws.rs.client.*;
import javax.ws.rs.core.*;

public class SSRestClient {
  
  public static final  String host      = "http://localhost:8080/";
  public static final  String restPath  = "sss.adapter.rest.v3/rest/";
  
//  public static final  String host      = "http://test-ll.know-center.tugraz.at/";
//  public static final  String restPath  = "bp.preparation/rest/";
  
//  public static final  String host      = "http://test-ll.know-center.tugraz.at/";
//  public static final  String restPath  = "eval/rest/";
  
//  public static final SSAuthEnum  authMethod = SSAuthEnum.oidc;
  public static final SSAuthEnum  authMethod = SSAuthEnum.csvFileAuth;
  
  private final String oidcToken = "";
  private final Client client;
  
  private String key = null;
  
  
  public SSRestClient(final Client client){
    this.client = client;
  }
  
  public static void main(String[] args) {
    
    try{
      
      final Client       client = ClientBuilder.newClient();
      final SSRestClient caller = new SSRestClient(client);
      
      caller.auth();
      
//      caller.getEntitiesFilteredAccessible();
//      caller.discCreate();
      caller.discsGetFiltered();
      
    }catch (Exception error) {
      SSLogU.err(error);
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
      SSLogU.err(error);
    }
  }
  
  public void discCreate(){
    
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
      SSLogU.err(error);
    }
  }
  
  public void getEntitiesFilteredAccessible(){
    
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
      SSLogU.err(error);
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
      SSLogU.err(error);
    }
  }
}