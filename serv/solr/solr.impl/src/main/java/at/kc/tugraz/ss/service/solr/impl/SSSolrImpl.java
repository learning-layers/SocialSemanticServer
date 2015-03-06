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
package at.kc.tugraz.ss.service.solr.impl;

import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.ss.conf.conf.SSCoreConf;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSConfA;
import at.kc.tugraz.ss.serv.serv.api.SSServImplMiscA;
import at.kc.tugraz.ss.serv.solr.datatypes.pars.SSSolrAddDocPar;
import at.kc.tugraz.ss.serv.solr.datatypes.pars.SSSolrRemoveDocPar;
import at.kc.tugraz.ss.serv.solr.datatypes.pars.SSSolrSearchPar;
import at.kc.tugraz.ss.service.filerepo.conf.SSFileRepoConf;
import at.kc.tugraz.ss.service.solr.api.SSSolrClientI;
import at.kc.tugraz.ss.service.solr.api.SSSolrServerI;
import at.kc.tugraz.ss.service.solr.datatypes.*;
import at.kc.tugraz.ss.service.solr.impl.fct.SSSolrFct;
import java.io.*;
import java.util.*;
import org.apache.solr.client.solrj.impl.*;
import org.apache.solr.client.solrj.request.*;
import org.apache.solr.client.solrj.response.*;
import org.apache.solr.common.util.*;

public class SSSolrImpl extends SSServImplMiscA implements SSSolrClientI, SSSolrServerI{
  
  private final ConcurrentUpdateSolrServer solrUpdater;
  private final String                     localWorkPath;
  
  public SSSolrImpl(final SSConfA conf, final ConcurrentUpdateSolrServer solrCon) throws Exception{
    
    super(conf);
    
    solrUpdater    = solrCon;
    localWorkPath  = SSCoreConf.instGet().getSsConf().getLocalWorkPath();
    
    SSLogU.info("connected to Solr server @ " + ((SSFileRepoConf) conf).getPath() + ".");
  }
  
  @Override
  public void solrAddDoc(final SSServPar parA) throws Exception {
    
//    according to Solr specification by adding a document with an ID already
//	  existing in the index will replace the document (eg. refer to 
//	  http://stackoverflow.com/questions/8494923/solr-block-updating-of-existing-document or
//	  http://lucene.apache.org/solr/api-4_0_0-ALPHA/doc-files/tutorial.html ) 
   
    try{
      final SSSolrAddDocPar            par  = new SSSolrAddDocPar(parA);
      final ContentStreamUpdateRequest csur = new ContentStreamUpdateRequest("/update/extract");
      final NamedList<Object>          response;
      
      csur.addContentStream(new ContentStreamBase.FileStream(new File(localWorkPath + par.id)));

      csur.setParam  ("literal.id", par.id);
      csur.setAction (AbstractUpdateRequest.ACTION.COMMIT, true, true);

      response = solrUpdater.request(csur);

      SSLogU.info("document w/ id " + par.id + " added successfully. ");
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
	
  @Override
  public void solrRemoveDoc(final SSServPar parA) throws Exception{

    try{
      final SSSolrRemoveDocPar par = new SSSolrRemoveDocPar(parA);
      
      solrUpdater.deleteById(par.id);
      solrUpdater.commit();

      SSLogU.info("document w/ id " + par.id + " deleted successfully.");
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  @Override
  public void solrRemoveDocsAll(final SSServPar parA) throws Exception {
    
    try{
      
      NamedList<Object>      nl;
      UpdateResponse         ur;
      
      SSLogU.info("starting to remove all documents from index.");
      
      ur = solrUpdater.deleteByQuery("*:*");
      solrUpdater.commit();
      
      nl = ur.getResponse();
      
      SSLogU.info("removed all documents from index.");
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public List<String> solrSearch(final SSServPar parA) throws Exception {
    
    try{
      final SSSolrSearchPar par           = new SSSolrSearchPar(parA);
      final List<String>    searchResults = new ArrayList<>();
      final SSSolrQueryPars qp            = new SSSolrQueryPars(par.keyword, par.maxResults);
      
      for(SSSolrSearchResult result : SSSolrSearchResult.get(SSSolrFct.query(solrUpdater, qp))){
        searchResults.add(result.id);
      }
      
      return searchResults;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
	}
}

//	@Override
//	public void reindexDocuments() throws Exception { //not yet tested w/ subfolders in webdav!
		
//		int    cnt = 0;
//    String filename;
//		
//    for (String url : SSWebdavServ.inst().serv().getFolderListing()){
//			
//			if (url.endsWith("/")) {
//				log.debug("ignoring a folder");
//				continue;
//			}
//      
//			filename = SSFileUtils.getFilename(url, true);
//			
//      SSWebdavServ.inst().serv().copyWebdavDocToTempFolder(filename);
//
//      updateDocument(filename, SSFileUtils.tempFolder(), false);
//      
//      SSFileUtils.deleteFile(SSFileUtils.tempFolder() + filename);
//
//      cnt++;
//		}
//		
//    server.commit();
//	}