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

import at.tugraz.sss.serv.conf.*;
import at.tugraz.sss.servs.dataimport.api.SSDataImportServerI;
import at.tugraz.sss.servs.dataimport.datatype.SSDataImportKCProjWikiVorgaengePar;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.errreg.SSServErrReg;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.servs.conf.*;
import at.tugraz.sss.servs.dataimport.impl.*;
import at.tugraz.sss.servs.entity.impl.*;
import at.tugraz.sss.servs.kcprojwiki.api.SSKCProjWikiClientI;
import at.tugraz.sss.servs.kcprojwiki.api.SSKCProjWikiServerI;
import at.tugraz.sss.servs.kcprojwiki.conf.SSKCProjWikiConf;
import at.tugraz.sss.servs.kcprojwiki.datatype.SSKCProjWikiImportPar;
import at.tugraz.sss.servs.kcprojwiki.datatype.SSKCProjWikiVorgang;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class SSKCProjWikiImpl
  extends SSEntityImpl
  implements
  SSKCProjWikiClientI,
  SSKCProjWikiServerI{
  
  private final SSKCProjWikiImportCommons kcProjWikiImportCommons = new SSKCProjWikiImportCommons();
  
  public SSKCProjWikiImpl(){
    super(SSCoreConf.instGet().getKcprojwiki());
  }
  
  @Override
  public void schedule() throws SSErr{
    
    final SSKCProjWikiConf projWikiConf = (SSKCProjWikiConf)conf;
    
    if(
      !projWikiConf.use ||
      !projWikiConf.schedule){
      return;
    }
    
    if(
      SSObjU.isNull(projWikiConf.scheduleOps, projWikiConf.scheduleIntervals)   ||
      projWikiConf.scheduleOps.isEmpty()                                        ||
      projWikiConf.scheduleIntervals.isEmpty()                                  ||
      projWikiConf.scheduleOps.size() != projWikiConf.scheduleIntervals.size()){
      
      SSLogU.warn(SSWarnE.scheduleConfigInvalid, null);
      return;
    }
    
    if(projWikiConf.executeScheduleAtStartUp){
      
      for(String scheduleOp : projWikiConf.scheduleOps){
        
        if(SSStrU.isEqual(scheduleOp, SSVarNames.kcprojwikiImport)){
          
          new SSSchedules().regScheduler(
            SSDateU.scheduleNow(
              new SSKCProjWikiImportTask()));
        }
      }
    }
  }
  
  @Override
  public void kcprojwikiImport(final SSKCProjWikiImportPar par) throws SSErr{
    
    try{
      
      final SSDataImportServerI                dataImportServ = new SSDataImportImpl();
      final Map<String, SSKCProjWikiVorgang>   vorgaenge      =
        dataImportServ.dataImportKCProjWikiVorgaenge(
          new SSDataImportKCProjWikiVorgaengePar(
            par,
            par.user,
            ((SSKCProjWikiConf) conf).vorgaengeFilePath));
      
      final DateFormat wikiDateFormt = new SimpleDateFormat("yyyy/MM/dd");
      final Long       now           = new Date().getTime();
      final String     exportDate    = wikiDateFormt.format(now);
      String           vorgangTitle  = null;
      String           projectTitle  = null;
      
      SSLogU.info("start vorgaenge update");
      
      kcProjWikiImportCommons.start(((SSKCProjWikiConf) conf));
      
      SSKCProjWikiVorgang vorgang = null;
      
      for(Map.Entry<String, SSKCProjWikiVorgang> vorgangEntry : vorgaenge.entrySet()){
        
        try{
          
          vorgang       = vorgangEntry.getValue();
          vorgangTitle  =
            kcProjWikiImportCommons.getVorgangPageTitleByVorgangNumber(
              ((SSKCProjWikiConf) conf),
              vorgang.vorgangNumber);
          
          if(vorgangTitle == null){
            
            vorgangTitle = kcProjWikiImportCommons.createVorgang((SSKCProjWikiConf) conf, vorgang);
            
            if(vorgangTitle == null){
              continue;
            }
            
            projectTitle =
              kcProjWikiImportCommons.getProjectPageTitleByProjectNumber(
                ((SSKCProjWikiConf) conf),
                vorgang.projectNumber);
            
            if(projectTitle == null){
              
              try{
                projectTitle = kcProjWikiImportCommons.createProject((SSKCProjWikiConf) conf,  vorgang);
              }catch(Exception error){
                SSLogU.warn("import for project " + vorgang.projectNumber + " failed", error);
              }
            }
          }
          
          vorgang.exportDate = exportDate;
          
          if(vorgang.totalResources == 0){
            vorgang.progress = 0F;
          }else{
            vorgang.progress = (vorgang.usedResources / vorgang.totalResources) * 100;
          }
          
          kcProjWikiImportCommons.updateVorgangBasics            (((SSKCProjWikiConf) conf), vorgang, vorgangTitle);
          kcProjWikiImportCommons.updateVorgangEmployeeResources (((SSKCProjWikiConf) conf), vorgang, vorgangTitle);
          
          SSLogU.info("updated vorgang " + vorgangTitle + SSStrU.blank + vorgang.vorgangNumber);
          
        }catch(Exception error){
          SSLogU.warn("import failed for vorgang " + vorgangTitle + SSStrU.blank + vorgang.vorgangNumber, error);
        }
      }
      
      kcProjWikiImportCommons.end(((SSKCProjWikiConf) conf));
      
      SSLogU.info("end vorgaenge update");
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}