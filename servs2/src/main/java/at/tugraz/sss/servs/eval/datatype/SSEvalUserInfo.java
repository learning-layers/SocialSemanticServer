/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
package at.tugraz.sss.servs.eval.datatype;

import at.tugraz.sss.servs.entity.datatype.SSLabel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSEvalUserInfo {
  
  public SSLabel                                             label                         = null;

  public Map<String, List<SSEvalImportInfo>>                     importInfos                   = new HashMap<>();
  
  public Map<String, SSEvalWorkedOnOwnBitInfo>                   workedOnOwnBitInfos           = new HashMap<>();
  public Map<String, SSEvalWorkedOnOwnEpisodeInfo>               workedOnOwnEpisodeInfos       = new HashMap<>();
  public List<SSEvalEpisodeCreationInfo>                         createdEpisodeInfos           = new ArrayList<>();
  
  public List<SSEvalEpisodeShareInfo>                            sharedEpisodeInfos            = new ArrayList<>();
  public Map<String, SSEvalWorkedOnReceivedSharedEpisodeInfo>    workedOnReceivedEpisodeInfos  = new HashMap<>();
  public Map<String, SSEvalWorkedOnReceivedSharedBitInfo>        workedOnReceivedBitInfos      = new HashMap<>();
  
  public List<SSEvalStartDiscussionInfo>                         startDiscussionInfos          = new ArrayList<>();
  public Map<String, SSEvalWorkedOnReceivedDiscussionInfo>       workedOnReceivedDiscussionInfos = new HashMap<>();
}
