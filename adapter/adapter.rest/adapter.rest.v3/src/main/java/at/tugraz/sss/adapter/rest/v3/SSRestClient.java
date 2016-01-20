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

import at.kc.tugraz.ss.serv.ss.auth.datatypes.ret.*;
import at.tugraz.sss.adapter.rest.v3.entity.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.ret.*;
import at.tugraz.sss.serv.util.*;
import java.util.*;
import javax.ws.rs.client.*;
import javax.ws.rs.core.*;

public class SSRestClient {
  
  public static final  String host      = "http://localhost:8080/";
  public static final  String restPath  = "sss.adapter.rest.v3/rest/";
  
  private final Client client;
  
  public SSRestClient(final Client client){
    this.client = client;
  }
  
  public static void main(String[] args) {
    
    try{
      
      final Client       client = ClientBuilder.newClient();
      final SSRestClient caller = new SSRestClient(client);
      final String       key    = caller.auth();
      
      caller.getEntitiesFilteredAccessible(key);
      
    }catch (Exception error) {
      SSLogU.err(error);
    }
  }
  
  public void getEntitiesFilteredAccessible(final String key){
    
    try{
      final WebTarget target = client.target(host + restPath + "entities/filtered/accessible/");
      final Invocation.Builder builder = target.request(MediaType.APPLICATION_JSON_TYPE);
          
      builder.header(HttpHeaders.AUTHORIZATION, "Bearer " + key);
        
      final SSEntitiesAccessibleGetRESTPar par = new SSEntitiesAccessibleGetRESTPar();
      final List<String> types   = new ArrayList<>();
      final List<String> authors = new ArrayList<>();
      
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
      
      final Entity<String>             formattedInput = Entity.entity(SSJSONU.jsonStr(par), MediaType.APPLICATION_JSON_TYPE);
      final SSEntitiesAccessibleGetRet ret            = builder.post(formattedInput, SSEntitiesAccessibleGetRet.class);
      
      for(SSEntity entity : ret.entities){
        System.out.println(entity);
      }
      
    }catch (Exception error) {
      SSLogU.err(error);
    }
  }
  
//  public void getEntitiesFilteredAccessible(final String key){
//    
//    try{
//      final WebTarget target = client.target(host + restPath + "entities/filtered/accessible/");
//      final String    input  = "{\"startTime\":1442072006290,\"endTime\":1453206465227,\"authors\":[\"http://sss.eu/1690149260961113\"],\"types\":[\"entity\",\"file\",\"evernoteResource\",\"evernoteNote\",\"evernoteNotebook\",\"placeholder\"],\"setTags\":true,\"setFlags\":true,\"pageSize\":0}";
//      final SSEntitiesAccessibleGetRESTPar par = new SSEntitiesAccessibleGetRESTPar();
//      final List<String> types   = new ArrayList<>();
//      final List<String> authors = new ArrayList<>();
//      
//      types.add("entity");
//      types.add("file");
//      types.add("evernoteResource");
//      types.add("evernoteNote");
//      types.add("evernoteNotebook");
//      types.add("placeholder");
//      
//      par.setTypes(types);
//      
//      authors.add("http://sss.eu/1690149260961113");
//      
//      par.setAuthors(authors);
//      
//      par.startTime      = 1442072006290L;
//      par.endTime        = 1453206465227L;
//      par.pageSize       = 0;
//      par.setTags        = true;
//      par.setFlags       = true;
////      par.pagesID             = null;
////      par.pageNumber             = 1;
////      entity(par, MediaType.APPLICATION_JSON_TYPE)
//      Entity<String> entity = Entity.entity(SSJSONU.jsonStr(par), MediaType.APPLICATION_JSON);
//      
//      final Invocation.Builder builder = target.request(MediaType.APPLICATION_JSON_TYPE);
//      
//      builder.accept (MediaType.APPLICATION_JSON);
//      builder.header (HttpHeaders.AUTHORIZATION, "Bearer " + key);
//      
//      Response post = builder.post   (entity);
//      
////      final SSEntitiesAccessibleGetRet ret = builder.p(SSEntitiesAccessibleGetRet.class);
//      
////      for(SSEntity entity : ret.entities){
////        System.out.println(entity);
////      }
//      
//      System.out.println(post);
//    }catch (Exception error) {
//      SSLogU.err(error);
//    }
//  }
  
  public String auth(){
    
    try{
      final WebTarget target = client.target(host + restPath + "auth/");
      final String    input  = "{\"label\":\"bn-testuser8@know-center.at\",\"password\":\"1234\"}";
      
      final SSAuthCheckCredRet ret =
        target.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(input, MediaType.APPLICATION_JSON_TYPE), SSAuthCheckCredRet.class);
      
      return ret.key;
    }catch (Exception error) {
      SSLogU.err(error);
      return null;
    }
  }
}