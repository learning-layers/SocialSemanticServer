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
 package at.kc.tugraz.ss.service.filerepo.datatypes;

import at.kc.tugraz.socialserver.utils.SSObjU;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityA;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSFileRepoFileAccessProperty extends SSEntityA{

	public               SSUri                  file                     = null;
  private static final int                    fileAccessPropertyMaxWritingMinutesCount = 5;
	private              List<SSEntityA>        readers                  = new ArrayList<SSEntityA>();
	private              SSUri                  writer                   = null;
	private              int                    writingMinutesLeft       = 0;
	
	public SSFileRepoFileAccessProperty(SSUri file) throws Exception{
		
    super(file);
    
    if(file == null){
      throw new MalformedURLException("FileAccessProperty cannot be created with null uri!");
    }
    
    //TODO dtheiler: check whether previous call was necessary: SSUri.get(file.toString());
		this.file = file;
	}

  public boolean canNotWrite(SSUri user){
    return !canWrite(user);
  }
  
	public boolean canWrite(SSUri user){
		
		if(SSObjU.isNull(writer)){
			return true;
		}
		
		if(
				SSUri.equals(writer, user) &&
				writingMinutesLeft > 0){
			
			return true;
		}
		
		return false;
	}
	
	public SSUri getWriter(){
		return writer;
	}

	public boolean addReader(SSUri user){
		
		if(!SSUri.contains(readers, user)){
			readers.add(user);
		}
		
		removeWriter(user);
		
		return true;
	}
	
	public boolean removeReader(SSUri user){
		
		if(!SSUri.contains(readers, user)){
			return true;
		}
		
		readers.remove(user);
				
		return true;
	}
	
	public boolean addWriter(SSUri user){
		
		if(canNotWrite(user)){
			return false;
		}
		
		if(SSObjU.isNull(writer)){
			
			writingMinutesLeft = fileAccessPropertyMaxWritingMinutesCount;
		}
			
		writer = user;
		
		return true;
	}
	
	public boolean removeWriter(
			SSUri user){
		
		if(canNotWrite(user)){
			return false;
		}
		
		writingMinutesLeft = fileAccessPropertyMaxWritingMinutesCount;
		writer             = null;
		
		return true;
	}
	
	public int getWritingMinutesLeft(SSUri user){
		
		if(SSObjU.isNull(writer)){
			return fileAccessPropertyMaxWritingMinutesCount;
		}
		
		if(SSUri.equals(writer, user)){
			return writingMinutesLeft;
		}
		
		return 0;
	}	

	public void updateWritingMinutes() {
		
		if(writingMinutesLeft == 0){
			return;
		}
		
		writingMinutesLeft = writingMinutesLeft - 1;
		
		if(writingMinutesLeft == 0){
			
			writer = null;
		}
	}
  
  @Override
  public Object jsonLDDesc() {
  
    Map<String, Object> ld = new HashMap<String, Object>();
    
    //TODO dtheiler
    
    return ld;
  }  
}