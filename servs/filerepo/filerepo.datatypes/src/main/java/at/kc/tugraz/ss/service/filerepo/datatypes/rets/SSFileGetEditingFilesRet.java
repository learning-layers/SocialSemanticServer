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
 package at.kc.tugraz.ss.service.filerepo.datatypes.rets;

import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSJSONLDU;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarU;
import at.tugraz.sss.serv.SSServRetI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSFileGetEditingFilesRet extends SSServRetI{

	public List<String> files  = new ArrayList<>();
	public List<String> labels = new ArrayList<>();
	
	public SSFileGetEditingFilesRet(SSServOpE op, List<String> fileUris, List<String> fileNames){
    
    super(op);
    
    if(fileUris != null){
      this.files = fileUris;
    }
    
    if(fileNames != null){
      this.labels = fileNames;
    }
  }
  
  @Override
  public Map<String, Object> jsonLDDesc(){
    
    Map<String, Object> ld           = new HashMap<>();
    Map<String, Object> fileUrisObj  = new HashMap<>();
    Map<String, Object> fileNamesObj = new HashMap<>();
    
    fileUrisObj.put(SSJSONLDU.id,        SSVarU.xsd + SSStrU.colon + SSStrU.valueString);
    fileUrisObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarU.files, fileUrisObj);
    
    fileNamesObj.put(SSJSONLDU.id,        SSVarU.xsd + SSStrU.colon + SSStrU.valueString);
    fileNamesObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarU.labels, fileNamesObj);
    
    return ld;
  }

  /* getters to allow for json enconding */
  public List<String> getFiles() {
		return files;
	}

	public List<String> getLabels() {
		return labels;
	}
}