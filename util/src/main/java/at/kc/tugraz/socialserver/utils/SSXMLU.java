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

import org.w3c.dom.Node;

public class SSXMLU{
  
  private SSXMLU(){}
  
  public static final String       coverage                 = "coverage";
  public static final String       value                    = "value";
  public static final String       technical                = "technical";
  public static final String       classification           = "classification";
  public static final String       general                  = "general";
  public static final String       URI                      = "URI";
  public static final String       learningResourceType     = "learningResourceType";
  public static final String       title                    = "title";
  public static final String       metaMetadata             = "metaMetadata";
  public static final String       catalog                  = "catalog";
  public static final String       identifier               = "identifier";
  public static final String       keyword                  = "keyword";
  public static final String       id                       = "id";
  public static final String       language                 = "language";
  public static final String       entity                   = "entity";
  public static final String       contribute               = "contribute";
  public static final String       educational              = "educational";
  public static final String       description              = "description";
  public static final String       format                   = "format";
  public static final String       string                   = "string";
  public static final String       entry                    = "entry";
  public static final String       role                     = "role";
 
  public static boolean isNotNode(Node node){
    return !isNode(node);
  }
  
  public static boolean isNode(Node node){
    
    if(
      SSObjU.isNull       (node) ||
      SSNumberU.equalsNot (node.getNodeType(), Node.ELEMENT_NODE)){
      return false;
    }
    
    return true;
  }
}
