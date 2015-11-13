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
package at.tugraz.sss.servs.kcprojwiki.impl;

import at.kc.tugraz.ss.serv.dataimport.api.SSDataImportServerI;
import at.kc.tugraz.ss.serv.dataimport.datatypes.pars.SSDataImportKCProjWikiProjectsPar;
import at.kc.tugraz.ss.serv.dataimport.datatypes.pars.SSDataImportKCProjWikiVorgaengePar;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.servs.kcprojwiki.api.SSKCProjWikiClientI;
import at.tugraz.sss.servs.kcprojwiki.api.SSKCProjWikiServerI;
import at.tugraz.sss.servs.kcprojwiki.conf.SSKCProjWikiConf;
import at.tugraz.sss.servs.kcprojwiki.datatype.SSKCProjWikiImportPar;
import at.tugraz.sss.servs.kcprojwiki.datatype.SSKCProjWikiProject;
import at.tugraz.sss.servs.kcprojwiki.datatype.SSKCProjWikiVorgang;
import java.util.Map;

public class SSKCProjWikiImpl
extends SSServImplWithDBA
implements
  SSKCProjWikiClientI,
  SSKCProjWikiServerI{
  
  private final SSKCProjWikiConf projWikiConf;
  
  public SSKCProjWikiImpl(final SSConfA conf) throws Exception{
    
    super(conf, null, null);
    
    this.projWikiConf  = (SSKCProjWikiConf) conf;
  }
  
  @Override
  public void kcprojwikiImport(final SSKCProjWikiImportPar par) throws Exception{
    
    try{
      
      final SSDataImportServerI                dataImportServ = (SSDataImportServerI) SSServReg.getServ(SSDataImportServerI.class);
      final SSKCProjWikiImportFct              importFct      = new SSKCProjWikiImportFct(projWikiConf);
      final Map<String, SSKCProjWikiVorgang>   vorgaenge      =
        dataImportServ.dataImportKCProjWikiVorgaenge(
          new SSDataImportKCProjWikiVorgaengePar(
            par.user,
            projWikiConf.vorgaengeFileName));
      
      importFct.start();
      
      for(Map.Entry<String, SSKCProjWikiVorgang> vorgang : vorgaenge.entrySet()){

        vorgang.getValue().title = importFct.getVorgangPageTitleByVorgangNumber   (vorgang.getValue().vorgangNumber);
        
        importFct.changeVorgangBasics    (vorgang.getValue().title);
        importFct.changeVorgangResources (vorgang.getValue().title);
      }
      
//      final Map<String, SSKCProjWikiProject> projects =
//        dataImportServ.dataImportKCProjWikiProjects(
//          new SSDataImportKCProjWikiProjectsPar(
//            par.user,
//            projWikiConf.projectsFileName));
//      
//      for(Map.Entry<String, SSKCProjWikiProject> project : projects.entrySet()){
//        
//        project.getValue().title = importFct.getProjectPageTitleByProjectNumber   (project.getValue().projectNumber); //"20143516"
//        
//        importFct.changeVorgangBasics    (project.getValue().title);
//        importFct.changeVorgangResources (project.getValue().title);
//      }
      
      importFct.end();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}