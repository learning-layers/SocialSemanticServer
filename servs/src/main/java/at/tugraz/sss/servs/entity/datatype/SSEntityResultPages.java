/**
 * Code contributed to the Learning Layers project
 * http://www.learning-layers.eu
 * Development is partly funded by the FP7 Programme of the European Commission under
 * Grant Agreement FP7-ICT-318209.
 * Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
  
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

package at.tugraz.sss.servs.entity.datatype;

import at.tugraz.sss.servs.util.SSStrU;
import io.swagger.annotations.*;
import java.util.ArrayList;
import java.util.List;

@ApiModel
public class SSEntityResultPages{
  
  @ApiModelProperty
  public List<List<SSUri>>    pages        = new ArrayList<>();
  
  public void setPages(final List<List<String>> pages) throws SSErr{
    
    for(List<String> page : pages){
      this.pages.add(SSUri.get(page));
    }
  }
  
  public List<List<String>> getPages(){
    
    final List<List<String>> tmp = new ArrayList<>();
    
    for(List<SSUri> page : pages){
      tmp.add(SSStrU.removeTrailingSlash(page));
    }
    
    return tmp;
  }
  
  @ApiModelProperty
  public Long                 creationTime = null;
  
  @ApiModelProperty
  public String               pagesID      = null;
  
  public static SSEntityResultPages get(
    final List<List<SSUri>>    pages,
    final Long                 creationTime,
    final String               pagesID){
    
    return new SSEntityResultPages(pages, creationTime, pagesID);
  }
  
  public SSEntityResultPages(){/* Do nothing because of only JSON Jackson needs this */ }
  
  protected SSEntityResultPages(
    final List<List<SSUri>>    pages,
    final Long                 creationTime,
    final String               pagesID){
    
    this.pages        = pages;
    this.creationTime = creationTime;
    this.pagesID      = pagesID;
  }
}