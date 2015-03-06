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
package at.kc.tugraz.socialserver.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class SSEncodingU{
  
  public static final String utf8                       = "UTF-8";
  public static final String md5                        = "MD5";
  public static final String en                         = "en";
  public static final String hu                         = "hu"; 
  public static final String ro                         = "ro";
  public static final String fr                         = "fr";
  public static final String de                         = "de";
  public static final String et                         = "et";
  public static final String ru                         = "ru";
  public static final String el                         = "el";
  public static final String no                         = "no";
  public static final String lv                         = "lv";
  public static final String es                         = "es";

  public static String decode(final String toDecode) throws Exception{
    return URLDecoder.decode(toDecode, SSEncodingU.utf8);
  }
  
  public static List<String> decode(final List<String> toDecodes) throws Exception{
    
    final List<String> decodeds = new ArrayList<>();
    
    for(String toDecode : toDecodes){
      decodeds.add(URLDecoder.decode(toDecode, SSEncodingU.utf8));
    }
    
    return decodeds;
  }
  
  private SSEncodingU(){}
}
