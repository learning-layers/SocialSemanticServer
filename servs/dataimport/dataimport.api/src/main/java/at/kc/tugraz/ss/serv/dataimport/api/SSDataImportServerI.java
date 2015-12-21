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
 package at.kc.tugraz.ss.serv.dataimport.api;

import at.kc.tugraz.ss.serv.dataimport.datatypes.pars.SSDataImportBitsAndPiecesPar;
import at.kc.tugraz.ss.serv.dataimport.datatypes.pars.SSDataImportEvalLogFilePar;
import at.kc.tugraz.ss.serv.dataimport.datatypes.pars.SSDataImportKCProjWikiProjectsPar;
import at.kc.tugraz.ss.serv.dataimport.datatypes.pars.SSDataImportKCProjWikiVorgaengePar;
import at.kc.tugraz.ss.serv.dataimport.datatypes.pars.SSDataImportMediaWikiUserPar;
import at.kc.tugraz.ss.serv.dataimport.datatypes.pars.SSDataImportSSSUsersFromCSVFilePar;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.impl.api.SSServServerI;
import at.tugraz.sss.servs.kcprojwiki.datatype.SSKCProjWikiProject;
import at.tugraz.sss.servs.kcprojwiki.datatype.SSKCProjWikiVorgang;
import java.util.List;
import java.util.Map;
import sss.serv.eval.datatypes.SSEvalLogEntry;

public interface SSDataImportServerI extends SSServServerI{
  
  public Boolean                          dataImportBitsAndPieces                  (final SSDataImportBitsAndPiecesPar       par) throws SSErr;
  public Map<String, SSKCProjWikiVorgang> dataImportKCProjWikiVorgaenge            (final SSDataImportKCProjWikiVorgaengePar par) throws SSErr;
  public Map<String, SSKCProjWikiProject> dataImportKCProjWikiProjects             (final SSDataImportKCProjWikiProjectsPar  par) throws SSErr;
  public List<SSEvalLogEntry>             dataImportEvalLogFile                    (final SSDataImportEvalLogFilePar         par) throws SSErr;
  public Map<String, String>              dataImportSSSUsersFromCSVFile            (final SSDataImportSSSUsersFromCSVFilePar par) throws SSErr;
  public void                             dataImportMediaWikiUser                  (final SSDataImportMediaWikiUserPar       par) throws SSErr;

//  public Boolean             dataImportUserResourceTagFromWikipedia   (final SSServPar parA) throws Exception;
}