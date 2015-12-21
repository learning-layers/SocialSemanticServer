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
package at.tugraz.sss.serv.util;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public enum SSEncodingU{
  
  utf8                       ("UTF-8"),
  md5                        ("MD5"),
  en                         ("en"),
  hu                         ("hu"), 
  ro                         ("ro"),
  fr                         ("fr"),
  de                         ("de"),
  et                         ("et"),
  ru                         ("ru"),
  el                         ("el"),
  no                         ("no"),
  lv                         ("lv"),
  es                         ("es");
  
  private final String value;
  
  private SSEncodingU(String newValue) {
    value = newValue;
  }
  
  @Override
  public String toString(){
    return value;
  }
  
  public static String decode(
    final String      toDecode, 
    final SSEncodingU format) throws Exception{
    
    return URLDecoder.decode(toDecode, SSStrU.toStr(format));
  }
  
  public static List<String> decode(
    final List<String> toDecodes, 
    final SSEncodingU  format) throws Exception{
    
    final List<String> decodeds = new ArrayList<>();
    
    for(String toDecode : toDecodes){
      decodeds.add(URLDecoder.decode(toDecode, SSStrU.toStr(format)));
    }
    
    return decodeds;
  }
}