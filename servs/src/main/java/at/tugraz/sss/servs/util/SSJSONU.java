/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
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

package at.tugraz.sss.servs.util;

import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.common.impl.SSServErrReg;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;

public class SSJSONU{

  public static ObjectMapper createDefaultMapper(){
    
    final ObjectMapper objectMapper = new ObjectMapper();
    
    objectMapper.enable                   (SerializationFeature.INDENT_OUTPUT);
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    objectMapper.configure                (SerializationFeature.WRITE_NULL_MAP_VALUES, true);
    
    //    objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_EMPTY);
    
    return objectMapper;
  }
  
  public static String getValueFromJSON(
    final String jsonStr,
    final String name) throws SSErr{
    
    try{
      final ObjectMapper mapper = new ObjectMapper();
      final JsonNode     node   = mapper.readTree(jsonStr).get(name);
      
      if(node == null){
        return null;
      }
      
      return node.textValue(); //getTextValue();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public static Object obj(
    final String  jsonStr,
    final Class   customClass) throws SSErr{
    
    try{
      return createDefaultMapper().readValue(jsonStr, customClass);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public static String jsonStr(
    final Object obj) throws SSErr{
    
    try{
      return createDefaultMapper().writeValueAsString(obj);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}

//  public static final JsonToken jsonEnd = JsonToken.END_OBJECT;

//  public static List<String> jsonArrayList(final String jsonStr) throws SSErr {
//    
//    ObjectMapper mapper = new ObjectMapper();
//    
//    JavaType     type   = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
//    
//    return mapper.readValue(jsonStr, type);
//  }
  
//  public static Map<String, String> jsonMap(String jsonStr) throws SSErr{
//    return new ObjectMapper().readValue(jsonStr, new TypeReference<Map<String,String>>() { });
//  }
  
//  public static JsonParser jsonParser(String jsonStr) throws SSErr{
//    return new JsonFactory().createJsonParser(jsonStr);
//  }