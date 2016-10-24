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
package at.kc.tugraz.sss.video.api;

import at.tugraz.sss.serv.SSUri;
import at.kc.tugraz.sss.video.datatypes.SSVideo;
import at.kc.tugraz.sss.video.datatypes.SSVideoAnnotation;
import at.kc.tugraz.sss.video.datatypes.par.SSVideoAnnotationGetPar;
import at.kc.tugraz.sss.video.datatypes.par.SSVideoAnnotationsGetPar;
import at.kc.tugraz.sss.video.datatypes.par.SSVideoUserAddPar;
import at.kc.tugraz.sss.video.datatypes.par.SSVideoUserAnnotationAddPar;
import at.kc.tugraz.sss.video.datatypes.par.SSVideoUserGetPar;
import at.kc.tugraz.sss.video.datatypes.par.SSVideosUserGetPar;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSServServerI;
import java.util.List;

public interface SSVideoServerI extends SSServServerI{
  
  public SSUri                   videoAdd            (final SSVideoUserAddPar            par) throws Exception;
  public SSVideo                 videoGet            (final SSVideoUserGetPar            par) throws Exception;
  public List<SSEntity>          videosGet           (final SSVideosUserGetPar           par) throws Exception;
  public SSUri                   videoAnnotationAdd  (final SSVideoUserAnnotationAddPar  par) throws Exception;
  public SSVideoAnnotation       videoAnnotationGet  (final SSVideoAnnotationGetPar      par) throws Exception;
  public List<SSEntity>          videoAnnotationsGet (final SSVideoAnnotationsGetPar    par) throws Exception;
}