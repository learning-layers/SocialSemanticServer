/**
 * Copyright 2013 Graz University of Technology - KTI (Knowledge Technologies Institute)
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
package at.kc.tugraz.ss.datatypes.datatypes;

import at.kc.tugraz.socialserver.utils.SSLinkU;
import at.kc.tugraz.socialserver.utils.SSObjU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import java.util.*;

public class SSUri extends SSEntityA{
  
  public static String toStr(SSUri uri){
    return SSStrU.toString(uri);
  }
  
  public static SSUri get(final String uri) throws Exception{
    
    //    new URL(uriString); //import java.net.URL;
    
    if(!java.net.URI.create(uri).isAbsolute()){
      throw new Exception("uri not java.net.URI conform");
    }
    
    return new SSUri(uri);
  }
  
  public static SSUri get(final SSUri uri, final String append) throws Exception{
    
    //    new URL(uriString); //import java.net.URL;
    
    SSUri newUri = null;
    
    if(
      SSObjU.isNull  (uri)         ||
      SSObjU.isNull  (append)){
      return null;
    }
    
    //    uri = strU.removeSlashFromEnd(uri);
    
    if (java.net.URI.create(uri.toString() + append).isAbsolute()) {
      newUri = new SSUri(uri.toString() + append);
    }else{
      SSServErrReg.regErrThrow(new Exception("coudn't create SSUri from " + uri + " and " + append));
    }
    
    return newUri;
  }
  
  public static List<SSUri> get(final List<String> strings) throws Exception{

    final List<SSUri> result = new ArrayList<SSUri>();
    
    for(String string : strings){
      result.add(get(string));
    }
    
    return result;
  }
  
  public static List<SSUri> getDistinct(final List<String> strings) throws Exception{

    final List<SSUri> result = new ArrayList<SSUri>();
    
    for(String string : SSStrU.distinct(strings)){
      result.add(get(string));
    }
    
    return result;
  }
  
  public static Boolean equals(
    final SSUri  uri1, 
    final SSUri  uri2) throws Exception{
    
    if(SSObjU.isNull(uri1, uri2)) {
      return false;
    }
    
    return SSStrU.equals(uri1.toString(), uri2.toString());
  }
    
  public static Boolean equals(
    final SSUri  uri1, 
    final String uri2) throws Exception{
    
    if(SSObjU.isNull(uri1, uri2)) {
      return false;
    }
    
    return SSStrU.equals(uri1.toString(), uri2);
  }

  public static List<String> toStrWithoutSlash(final List<SSUri> uris){

    if(uris == null){
      return new ArrayList<String>();
    }
    
    final List<String> result = new ArrayList<String>();
    
    for(SSUri uri : uris){
      result.add(toStrWithoutSlash(uri));
    }
    
    return result;
  }
  
  public static String toStrWithoutSlash(final SSUri uri){
    
    if(uri == null){
      return null;
    }
    
    return SSStrU.removeTrailingSlash(uri.toString());
  }
  
  public static List<SSUri> distinct(List<SSUri> uris) throws Exception{
    
    List<String>     foundEntities = new ArrayList<String>();
    List<SSUri>      result        = new ArrayList<SSUri>();
    
    for (SSUri entity : uris){
      
      if(SSStrU.contains(foundEntities, entity.toString())) {
        continue;
      }
      
      result.add        (entity);
      foundEntities.add (entity.toString());
    }
    
    return result;
  }
  
  private SSUri(final String value){
    super(SSStrU.addTrailingSlash(value));
  }
  
  @Override
  public Object jsonLDDesc(){
    return SSLinkU.schemaOrgUrl;
  }
}

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
//    List<SSUri> result = new ArrayList<SSUri>();
//    
//    for (String uri : uris) {
//      result.add(get(uri));
//    }
//    
//    return result;
//  }