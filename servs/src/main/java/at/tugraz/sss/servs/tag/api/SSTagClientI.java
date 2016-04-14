/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
 /**
 * @author Dieter Theiler
 */

package at.tugraz.sss.servs.tag.api;

import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSClientE;
import at.tugraz.sss.servs.entity.datatype.SSServPar; 
import at.tugraz.sss.servs.entity.datatype.SSServRetI;

public interface SSTagClientI {

  public SSServRetI tagAdd                       (final SSClientE clientType, final SSServPar parA) throws SSErr;
  public SSServRetI tagsAdd                      (final SSClientE clientType, final SSServPar parA) throws SSErr;
  public SSServRetI tagsRemove                   (final SSClientE clientType, final SSServPar parA) throws SSErr;
  public SSServRetI tagsGet                      (final SSClientE clientType, final SSServPar parA) throws SSErr;
  public SSServRetI tagEntitiesForTagsGet        (final SSClientE clientType, final SSServPar parA) throws SSErr;
  public SSServRetI tagFrequsGet                 (final SSClientE clientType, final SSServPar parA) throws SSErr;
}


//@Override 
//  public boolean addTags(
//			SSUri             resource, 
//			List<SSTagString> tags, 
//			SSUri             user, 
//			SSSpaceEnum            space) throws SSErr {
//
//		tagM.addTagArray        (true, resource, tags,     user,  space);
//		SSUserEventServ.inst().serv().saveUEAddTag (true, user,     resource, tags, space);
//
//		return true;
//	}