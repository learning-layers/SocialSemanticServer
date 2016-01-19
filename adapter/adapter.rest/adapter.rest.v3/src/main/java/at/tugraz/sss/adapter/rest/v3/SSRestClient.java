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
import at.tugraz.sss.serv.util.*;
import javax.ws.rs.client.*;
import javax.ws.rs.core.*;

public class SSRestClient {
  
  public static void main(String[] args) {
    
    try {
      
      final Client    client = ClientBuilder.newClient();
      final WebTarget target = client.target("http://localhost:8080/sss.adapter.rest.v3/rest/auth/");
      final String    input  = "{\"label\":\"dieter\",\"password\":\"1234\"}";
      
      final SSAuthCheckCredRet response =
        target.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(input, MediaType.APPLICATION_JSON_TYPE), SSAuthCheckCredRet.class);
      
      System.out.println(response);
      
    }catch (Exception e) {
      SSLogU.err(e);
    }
  }
}
