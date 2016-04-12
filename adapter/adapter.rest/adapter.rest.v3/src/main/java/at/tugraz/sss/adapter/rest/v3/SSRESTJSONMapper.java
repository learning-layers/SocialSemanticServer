/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2016, Graz University of Technology - KTI (Knowledge Technologies Institute).
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

import at.tugraz.sss.servs.util.*;
import com.fasterxml.jackson.databind.*;
import javax.ws.rs.ext.*;

@Provider
public class SSRESTJSONMapper implements ContextResolver<ObjectMapper> {
  
  final ObjectMapper defaultObjectMapper;
  
  public SSRESTJSONMapper() {
    defaultObjectMapper = SSJSONU.createDefaultMapper();
  }
  
  @Override
  public ObjectMapper getContext(final Class<?> type) {
    return defaultObjectMapper;
  }
  
  
  //    private static ObjectMapper createCombinedObjectMapper() {
//        return new ObjectMapper()
//                .configure(SerializationFeature.WRAP_ROOT_VALUE, true)
//                .configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true)
//                .setAnnotationIntrospector(createJaxbJacksonAnnotationIntrospector());
//    }
  
//    private static AnnotationIntrospector createJaxbJacksonAnnotationIntrospector() {
//
//        final AnnotationIntrospector jaxbIntrospector = new JaxbAnnotationIntrospector(TypeFactory.defaultInstance());
//        final AnnotationIntrospector jacksonIntrospector = new JacksonAnnotationIntrospector();
//
//        return AnnotationIntrospector.pair(jacksonIntrospector, jaxbIntrospector);
//    }
}