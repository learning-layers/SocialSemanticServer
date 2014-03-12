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
 package at.kc.tugraz.ss.serv.db.api;

import at.kc.tugraz.ss.datatypes.datatypes.SSEntityA;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.serv.db.datatypes.graph.*;
import java.util.*;

public interface SSDBGraphI{
 
  public SSQueryResult dbGraphQuery(
    String queryString) throws Exception;
  
  public void dbGraphCommit(
    Boolean changed, 
    Boolean shouldCommit) throws Exception;
  
  public void dbGraphDeleteAll(
    SSUri context) throws Exception;
  
  public List<SSStatement> dbGraphGet(
    SSUri     subject,
    SSUri     predicate, 
    SSEntityA object,
    SSUri     context) throws Exception;
    
  public Boolean dbGraphHas(
    SSUri        subject, 
    SSUri        predicate, 
    SSEntityA    object,
    SSUri        context) throws Exception;
  
  public Boolean dbGraphHas(
    SSStatement statement, 
    SSUri       context)  throws Exception;

  public Boolean dbGraphNotHas(
    SSUri        subject, 
    SSUri        predicate, 
    SSEntityA    object,
    SSUri        context) throws Exception;

  public Boolean dbGraphAdd(
    SSUri      subject,
    SSUri      predicate,
    SSEntityA  object,
    SSUri      context) throws Exception;
  
  public Boolean dbGraphAdd(
    SSStatement statement,
    SSUri       context) throws Exception;
		
  public void dbGraphRemove(
    SSStatement statement,
    SSUri       context) throws Exception;
  
   public void dbGraphRemove(         
    SSUri               subject,       
    SSUri               predicate,  
    SSEntityA           object,
    SSUri               context) throws Exception;
  
  public void dbGraphRollBackAndThrow(
    Boolean    ableToCommit,
    Exception  error) throws Exception;

  public Boolean dbGraphContextExists(
    SSUri uri) throws Exception;
  
//	public void setContext(String uri) throws Exception {
//		if (lockContext == false) {
//			this.context = ValueFactory.createURI(uri);//+SSConfig.configName+"/");
//			// lockContext = true;
//			// lockContext muss im rollback bzw. commit wieder freigegeben
//			// werden!
//		} else {
//			SSLogU.logAndThrow(new Exception("Context is locked!"));
//		}
//	}
//
//	public void setContext(URI uri) throws Exception {
//		this.context = uri;
//	}
//
//	public boolean lockContext() {
//		if (this.lockContext == false) {
//			this.lockContext = true;
//			return true;
//		} else
//			return false;
//	}
//	
//	public void unlockContext()
//	{
//		this.lockContext = false;
//	}
}
