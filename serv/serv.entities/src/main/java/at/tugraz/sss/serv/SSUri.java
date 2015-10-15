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
package at.tugraz.sss.serv;

import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
//import org.apache.commons.httpclient.URIException;
//import org.apache.commons.httpclient.util.URIUtil;

public class SSUri extends SSEntityA{
  
  @Override
  public Object jsonLDDesc(){
    return SSLinkU.schemaOrgUrl;
  }
  
  public static Boolean isURI(final String string) throws Exception{
    
//    URIUtil.encodeQuery(string)
    //    new URL(uriString); //import java.net.URL;
    try{
      if(string == null){
        return false;
      }
      
      try{
        new URL(string);
        java.net.URI.create (string);
        URLEncoder.encode   (string, SSEncodingU.utf8.toString());
        
        if(string.length() > 250){
          SSLogU.warn("uri too long (> 250 chars) to be stored in sss");
          return false;
        }
        
      }catch(Exception error){
        return false;
      }
      
      return true;
    }catch(Exception error){
      throw error;
    }
  }
  
  public static List<SSUri> get(final List<String> strings) throws Exception{

    final List<SSUri> result = new ArrayList<>();
    
    if(strings == null){
      return result;
    }
    
    for(String string : strings){
      result.add(get(string));
    }
    
    return result;
  }
  
  public static List<SSUri> get(
    final List<String> strings, 
    final String       uriPrefix) throws Exception{
    
    final List<SSUri> uris = new ArrayList<>();
    
    if(strings == null){
      return uris;
    }
    
    for(String string : strings){
      uris.add(get(string, uriPrefix));
    }
    
    return uris;
  }
  
  public static SSUri get(
    final String string, 
    final String uriPrefix) throws Exception{
    
    String decodedURI;
    
    if(string == null){
      return null;
    }
    
    try{
      decodedURI = SSEncodingU.decode(string, SSEncodingU.utf8);
    }catch(Exception error){
      decodedURI = string;
    }
    
    if(isURI(decodedURI)){
      return get(decodedURI);
    }else{
      return get(uriPrefix + decodedURI);
    }
  }
  
  public static SSUri get(final String string) throws Exception{
    
    if(string == null){
      return null;
    }
    
    return new SSUri(string);
  }
  
  public static SSUri get(
    final SSUri  uri,
    final String append) throws Exception{
    
    if(SSObjU.isNull(uri, append)){
      return null;
    }
    
    return new SSUri(uri.toString() + append);
  }
  
  public static List<SSUri> getDistinctNotNullFromEntities(final List<? extends SSEntity> entities){

    final List<SSUri> result = new ArrayList<>();
    
    if(entities == null){
      return result;
    }
    
    for(SSEntity entity : entities){
      
      if(entity == null){
        continue;
      }
      
      if(!SSStrU.contains(result, entity)){
        result.add(entity.id);
      }
    }
    
    return result;
  }
  
  public static List<SSUri> getDistinctNotNullFromEntities(final SSEntity... entities) throws Exception{

    final List<SSUri> result = new ArrayList<>();
    
    if(entities == null){
      return result;
    }
    
    for(SSEntity entity : entities){
      
      if(entity == null){
        continue;
      }
      
      if(!SSStrU.contains(result, entity)){
        result.add(entity.id);
      }
    }
    
    return result;
  }
  
  public static void addDistinctWithoutNull(
    final List<SSUri>     entities,
    final SSUri           entity){
    
    if(
      SSObjU.isNull  (entities, entity) ||
      SSStrU.contains(entities, entity)){
      return;
    }
    
    entities.add(entity);
  }
  
  public static void addDistinctWithoutNull(
    final List<SSUri>  entities,
    final List<SSUri>  toAddEntities){
    
    if(SSObjU.isNull(entities, toAddEntities)){
      return;
    }
    
    for(SSUri entity : toAddEntities){
      
      if(entity == null){
        continue;
      }
      
      if(!SSStrU.contains(entities, entity)){
        entities.add(entity);
      }
    }
  }
  
  //TODO rename to:  asListWithoutNull
  public static List<SSUri> asListWithoutNullAndEmpty(final SSUri... entities){
   
    final List<SSUri> result = new ArrayList<>();
    
    if(entities == null){
      return result;
    }
    
    for(SSUri entity : entities){
      
      if(entity == null){
        continue;
      }
      
      result.add(entity);
    }
    
    return result;
  }
  
  //TODO rename to:  asListWithoutNull
  public static List<SSUri> asListWithoutNullAndEmpty(final List<SSUri> entities){
   
    final List<SSUri> result = new ArrayList<>();
    
    if(entities == null){
      return result;
    }
    
    for(SSUri entity : entities){
      
      if(entity == null){
        continue;
      }
      
      result.add(entity);
    }
    
    return result;
  }

  private SSUri(final String string) throws Exception{
   
    super(SSStrU.addTrailingSlash(string));
    
    if(!isURI(val)){
      throw new Exception("invalid uri " + val);
    }
  }
}

//public static List<SSUri> distinctWithoutNull(
//    final List<SSUri> uris) throws Exception{
//
//    try{
//      
//      if(uris == null){
//        throw new Exception("pars null");
//      }
//      
//      final List<SSUri> result = new ArrayList<>();
//      
//      for (SSUri uri : uris) {
//        
//        if(
//          uri != null &&
//          !result.contains(uri.toString())){
//          
//          result.add(uri);
//        }
//      }
//      
//      return result;
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }

//  public URI createUncheckedURI(String uri) {
//
//    try {
//      return new URI(uri, false);
//
//    } catch (Exception e) {
//      return null;
//    }
//  }

//  public static List<SSUri> get(final Collection<String> uris) throws Exception{
//    
//    List<SSUri> result = new ArrayList<>();
//    
//    for (String uri : uris) {
//      result.add(get(uri));
//    }
//    
//    return result;
//  }