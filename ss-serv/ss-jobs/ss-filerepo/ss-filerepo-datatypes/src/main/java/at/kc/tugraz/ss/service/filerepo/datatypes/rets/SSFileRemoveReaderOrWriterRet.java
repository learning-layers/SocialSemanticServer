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
 package at.kc.tugraz.ss.service.filerepo.datatypes.rets;

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.serv.datatypes.SSServRetI;
import java.util.HashMap;
import java.util.Map;

public class SSFileRemoveReaderOrWriterRet extends SSServRetI{

	public String  fileUriOrId              = null;
	public boolean wasSuccessful            = false;
	
	public SSFileRemoveReaderOrWriterRet(String newFileUriOrId, SSMethU op){
    
    super(op);
		this.fileUriOrId = newFileUriOrId;
	}
  
  @Override
  public Map<String, Object> jsonLDDesc(){
    
    Map<String, Object> ld = new HashMap<String, Object>();
    
    ld.put(SSVarU.fileUriOrId,        SSVarU.xsd + SSStrU.colon + SSStrU.valueString);
    ld.put(SSVarU.wasSuccessful,      SSVarU.xsd + SSStrU.colon + SSStrU.valueBoolean);
    
    return ld;
  }
	
	public boolean getWasSuccessful(){
		return wasSuccessful;
	}
	
	public String getFileUriOrId(){
		return fileUriOrId;
	}
}
