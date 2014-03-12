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
 package at.kc.tugraz.ss.service.tag.api;

import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;

public interface SSTagClientI {
 
  public void tagAdd                           (final SSSocketCon sSCon, final SSServPar parA) throws Exception;
  public void tagsUserRemove                   (final SSSocketCon sSCon, final SSServPar parA) throws Exception;
  public void tagUserFrequsGet                 (final SSSocketCon sSCon, final SSServPar parA) throws Exception;
}


//@Override 
//  public boolean addTags(
//			SSUri             resource, 
//			List<SSTagString> tags, 
//			SSUri             user, 
//			SSSpaceEnum            space) throws Exception {
//
//		tagM.addTagArray        (true, resource, tags,     user,  space);
//		SSUserEventServ.inst().serv().saveUEAddTag (true, user,     resource, tags, space);
//
//		return true;
//	}