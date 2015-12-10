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
package at.tugraz.sss.servs.ocd.api;

import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSServServerI;
import at.tugraz.sss.servs.ocd.datatypes.pars.SSOCDCreateCoverPar;
import at.tugraz.sss.servs.ocd.datatypes.pars.SSOCDCreateGraphPar;
import at.tugraz.sss.servs.ocd.datatypes.pars.SSOCDDeleteCoverPar;
import at.tugraz.sss.servs.ocd.datatypes.pars.SSOCDDeleteGraphPar;
import at.tugraz.sss.servs.ocd.datatypes.pars.SSOCDGetCoversPar;
import at.tugraz.sss.servs.ocd.datatypes.pars.SSOCDGetGraphPar;
import at.tugraz.sss.servs.ocd.datatypes.pars.SSOCDGetGraphsPar;

public interface SSOCDServerI extends SSServServerI{
  
  public String             ocdCreateGraph              (final SSOCDCreateGraphPar par) throws SSErr;
  public String             ocdGetGraphs                (final SSOCDGetGraphsPar   par) throws SSErr;
  public String             ocdGetGraph                 (final SSOCDGetGraphPar    par) throws SSErr;
  public String             ocdDeleteGraph              (final SSOCDDeleteGraphPar par) throws SSErr;
  public String             ocdCreateCover              (final SSOCDCreateCoverPar par) throws SSErr;
  public String             ocdGetCovers                (final SSOCDGetCoversPar   par) throws SSErr;
  public String             ocdDeleteCover              (final SSOCDDeleteCoverPar par) throws SSErr;
  public String             ocdGetAlgorithmNames        () throws Exception;
}
