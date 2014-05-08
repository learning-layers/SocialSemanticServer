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
package at.kc.tugraz.ss.datatypes.datatypes.entity;

import at.kc.tugraz.socialserver.utils.SSLinkU;
import at.kc.tugraz.socialserver.utils.SSObjU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import java.util.*;
import org.apache.commons.httpclient.util.URIUtil;

public class SSUri extends SSEntityA{
  
  public static SSUri get(final String uri) throws Exception{
    
    //    new URL(uriString); //import java.net.URL;
    if(uri == null){
      return null;
    }
    
    if(!java.net.URI.create(URIUtil.encodeQuery(uri)).isAbsolute()){
      throw new Exception("uri not java.net.URI conform");
    }
    
    return new SSUri(uri);
  }
  
  public static SSUri get(final SSUri uri, final String append) throws Exception{
    
    //    new URL(uriString); //import java.net.URL;
    
    SSUri newUri = null;
    
    if(SSObjU.isNull(uri, append)){
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
    
    if(strings == null){
      return result;
    }
    
    for(String string : strings){
      result.add(get(string));
    }
    
    return result;
  }
  
  public static List<SSUri> distinct(
    final List<SSUri> uris) throws Exception{
    
    if(uris == null){
      return uris;
    }
    
    final List<String>     foundEntities = new ArrayList<String>();
    final List<SSUri>      result        = new ArrayList<SSUri>();
    
    for(SSUri uri : uris){
      
      if(
        uri == null ||
        foundEntities.contains(uri.toString())) {
        continue;
      }
      
      result.add        (uri);
      foundEntities.add (uri.toString());
    }
    
    return result;
  }
  
  public static void addDistinct(
    final List<SSUri> uris,
    final SSUri       uriToAdd){
    
    if(
      SSObjU.isNull(uris, uriToAdd) ||
      contains     (uris, uriToAdd)){
      return;
    }
    
    uris.add(uriToAdd);
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

  private SSUri(final String value) throws Exception{
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