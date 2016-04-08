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
 package at.tugraz.sss.servs.dataimport.api;

import at.tugraz.sss.servs.dataimport.datatype.SSDataImportBitsAndPiecesPar;
import at.tugraz.sss.servs.dataimport.datatype.SSDataImportEvalLogFilePar;
import at.tugraz.sss.servs.dataimport.datatype.SSDataImportKCProjWikiVorgaengePar;
import at.tugraz.sss.servs.dataimport.datatype.SSDataImportMediaWikiUserPar;
import at.tugraz.sss.servs.dataimport.datatype.SSDataImportSSSUsersFromCSVFilePar;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.impl.api.SSServServerI;
import at.tugraz.sss.servs.kcprojwiki.datatype.SSKCProjWikiVorgang;
import java.util.List;
import java.util.Map;
import at.tugraz.sss.servs.eval.datatype.SSEvalLogEntry;

public interface SSDataImportServerI extends SSServServerI{
  
  public boolean                          dataImportBitsAndPieces                  (final SSDataImportBitsAndPiecesPar       par) throws SSErr;
  public Map<String, SSKCProjWikiVorgang> dataImportKCProjWikiVorgaenge            (final SSDataImportKCProjWikiVorgaengePar par) throws SSErr;
  public List<SSEvalLogEntry>             dataImportEvalLogFile                    (final SSDataImportEvalLogFilePar         par) throws SSErr;
  public Map<String, String>              dataImportSSSUsersFromCSVFile            (final SSDataImportSSSUsersFromCSVFilePar par) throws SSErr;
  public void                             dataImportMediaWikiUser                  (final SSDataImportMediaWikiUserPar       par) throws SSErr;

//  public boolean             dataImportUserResourceTagFromWikipedia   (final SSServPar parA) throws SSErr;
}