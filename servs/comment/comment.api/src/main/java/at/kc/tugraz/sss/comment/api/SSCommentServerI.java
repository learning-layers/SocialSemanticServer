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
package at.kc.tugraz.sss.comment.api;

import at.kc.tugraz.sss.comment.datatypes.*;
import at.kc.tugraz.sss.comment.datatypes.par.SSCommentEntitiesGetPar;
import at.kc.tugraz.sss.comment.datatypes.par.SSCommentsAddPar;
import at.kc.tugraz.sss.comment.datatypes.par.SSCommentsGetPar;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.impl.api.SSServServerI;
import java.util.List;

public interface SSCommentServerI extends SSServServerI{

  public List<SSComment>     commentsGet          (final SSCommentsGetPar        par) throws SSErr;
  public List<SSUri>         commentEntitiesGet   (final SSCommentEntitiesGetPar par) throws SSErr;
  public SSUri               commentsAdd          (final SSCommentsAddPar        par) throws SSErr;
}
