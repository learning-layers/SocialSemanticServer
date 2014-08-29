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
package at.kc.tugraz.ss.adapter.rest;

import at.kc.tugraz.socialserver.utils.SSFileU;
import at.kc.tugraz.socialserver.utils.SSMimeTypeU;
import at.kc.tugraz.socialserver.utils.SSSystemU;
import at.kc.tugraz.ss.adapter.rest.conf.SSAdapterRestConf;
import at.kc.tugraz.ss.serv.jsonld.util.SSJSONLDU;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.core.Application;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class SSRestMain extends Application {
  
  public SSRestMain() throws Exception{
   
    final ResourceConfig resourceConfig = new ResourceConfig(SSAdapterRest.class);
    resourceConfig.register(MultiPartFeature.class);
    
    //    SSLogU.info("rest enter");
    SSAdapterRestConf.instSet (SSFileU.dirCatalinaBase() + SSSystemU.dirNameConf + "ss-adapter-rest-conf.yaml");
    
    /* util */
    SSMimeTypeU.init();
    SSJSONLDU.init(
      SSAdapterRestConf.instGet().getJsonLDConf().uri,
      SSAdapterRestConf.instGet().getVocConf().app,
      SSAdapterRestConf.instGet().getVocConf().space);
    
    /* json-ld */
//    SSJSONLD.inst.initServ(SSAdapterRestConf.instGet().getJsonLDConf());
    
    SSAdapterRest.conf = SSAdapterRestConf.instGet().getSsConf();
  }
  
  @Override
  public Set<Class<?>> getClasses() {

    final Set<Class<?>> classes = new HashSet<>();
    
    classes.add(MultiPartFeature.class);
    classes.add(SSAdapterRest.class);

    return classes;
  }
}