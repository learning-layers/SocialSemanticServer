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
import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.localwork.serv.SSLocalWorkServ;
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
import org.apache.solr.client.solrj.*;
import org.apache.solr.client.solrj.impl.*;
import org.apache.solr.client.solrj.request.*;
import org.apache.solr.client.solrj.response.*;
import org.apache.solr.common.util.*;

public class SSSolrImpl extends SSServImplMiscA implements SSSolrClientI, SSSolrServerI{
  
  private final SolrServer     server;
  private final SSFileRepoConf localWorkConf;
  
  public SSSolrImpl(final SSFileRepoConf conf) throws Exception{
    
    super(conf);
    
    server         = new CommonsHttpSolrServer(conf.getPath());
    localWorkConf  = (SSFileRepoConf) SSLocalWorkServ.inst.servConf;
    
    SSLogU.info("connected to Solr server @ " + conf.getPath() + ".");
  }
  
  /****** SSSolrServerI ******/

  /** 
	 * according to Solr specification by adding a document with an ID already
	 * existing in the index will replace the document (eg. refer to 
	 * http://stackoverflow.com/questions/8494923/solr-block-updating-of-existing-document or
	 * http://lucene.apache.org/solr/api-4_0_0-ALPHA/doc-files/tutorial.html ) 
	 */
  
  @Override
	public void solrAddDoc(final SSServPar parA) throws Exception {
		
    final SSSolrAddDocPar            par  = new SSSolrAddDocPar(parA);
    final ContentStreamUpdateRequest csur = new ContentStreamUpdateRequest("/update/extract");
    final NamedList<Object>          response;
    
    csur.addFile   (new File(localWorkConf.getPath() + par.id));
    csur.setParam  ("literal.id", par.id);
    csur.setAction (AbstractUpdateRequest.ACTION.COMMIT, true, true);

    response = server.request(csur);

    SSLogU.info("document w/ id " + par.id + " added successfully. ");
	}
	
	@Override
	public void solrRemoveDoc(SSServPar parI) throws Exception {
		
    SSSolrRemoveDocPar par = new SSSolrRemoveDocPar(parI);
    
    try {
			server.deleteById(par.id);
			server.commit();
      
			SSLogU.info("document w/ id " + par.id + " deleted successfully.");
		} catch (Exception error) {
      SSServErrReg.regErrThrow(error);
		}
	}

  @Override
	public void solrRemoveDocsAll(SSServPar parI) throws Exception {
		
    NamedList<Object>      nl;
    UpdateResponse         ur;
      
    SSLogU.info("starting to remove all documents from index.");
    
    ur = server.deleteByQuery("*:*");
    server.commit();
    
    nl = ur.getResponse();
    
    SSLogU.info("removed all documents from index.");
	}
	
  
	@Override
	public List<String> solrSearch(final SSServPar parA) throws Exception {
		
    final SSSolrSearchPar par           = new SSSolrSearchPar(parA);
    final List<String>    searchResults = new ArrayList<String>();
    final SSSolrQueryPars qp            = new SSSolrQueryPars(par.keyword, par.maxResults);
    
    for(SSSolrSearchResult result : SSSolrSearchResult.get(SSSolrFct.query(server, qp))){
      searchResults.add(result.id);
    }
    
		return searchResults;
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