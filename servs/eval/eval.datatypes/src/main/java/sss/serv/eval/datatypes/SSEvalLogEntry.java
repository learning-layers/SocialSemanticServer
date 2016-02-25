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
package sss.serv.eval.datatypes;

import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.SSToolContextE;
import java.util.ArrayList;
import java.util.List;

public class SSEvalLogEntry {
  
  public Long                                   timestamp                 = null;
  public SSToolContextE                         toolContext               = null;
  public SSLabel                                userLabel                 = null;
  public SSLabel                                userEmail                 = null;
  public SSEvalLogE                             logType                   = null;
  public SSUri                                  entity                    = null;
  public SSEntityE                              entityType                = null;
  public SSLabel                                entityLabel               = null;
  public String                                 content                   = null;
  public String                                 tagType                   = null;
  public List<SSCircleE>                        circleTypes               = new ArrayList<>();
  public String                                 selectedBitsMeasure       = null;
  public List<SSUri>                            entityIDs                 = new ArrayList<>();
  public List<SSLabel>                          entityLabels              = new ArrayList<>();
  public List<SSLabel>                          userLabels                = new ArrayList<>();
  public List<SSUri>                            notSelectedEntityIds      = new ArrayList<>();
  public List<SSLabel>                          notSelectedEntityLabels   = new ArrayList<>();
}
