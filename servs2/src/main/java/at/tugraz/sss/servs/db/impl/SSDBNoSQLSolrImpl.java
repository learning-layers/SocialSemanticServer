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
package at.tugraz.sss.servs.db.impl;

import at.tugraz.sss.serv.conf.SSConf;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.par.SSDBNoSQLAddDocPar;
import at.tugraz.sss.serv.db.api.SSDBNoSQLI;
import at.tugraz.sss.serv.datatype.par.SSDBNoSQLRemoveDocPar;
import at.tugraz.sss.serv.datatype.par.SSDBNoSQLSearchPar;
import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.errreg.SSServErrReg;
import at.tugraz.sss.serv.datatype.enums.SSSolrSearchFieldE;
import at.tugraz.sss.serv.impl.api.*;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.servs.conf.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrClient;
import org.apache.solr.client.solrj.request.AbstractUpdateRequest;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;
import org.apache.solr.common.util.ContentStreamBase;
import org.apache.solr.common.util.NamedList;

public class SSDBNoSQLSolrImpl
extends SSServImplA
implements SSDBNoSQLI{

//  @Override
//  public boolean integrationTestSolrForSearch() throws SSErr {
//    
//    try{
//      
//      SSLogU.info("start intgration test solr for search");
//      
//      dbNoSQL.addDoc(new SSDBNoSQLAddDocPar("muell.pdf"));
//      
//      final Map<SSSolrSearchFieldE, List<SSSolrKeywordLabel>> wheres   = new HashMap<>();
//      final List<String>                                      keywords = new ArrayList<>();
//      
//      keywords.add("Halbjahr 1");
//      keywords.add("Muell");
//      
//      wheres.put(SSSolrSearchFieldE.docText, SSSolrKeywordLabel.get(keywords));
//        
//      final List<String> result =
//        dbNoSQL.search(
//          new SSDBNoSQLSearchPar(
//            SSSearchOpE.and.toString(),
//            SSSearchOpE.or.toString(),
//            wheres,
//            100));
//      
//      System.out.println(result);
//      
//      dbNoSQL.removeDoc(new SSDBNoSQLRemoveDocPar("muell.pdf"));
//      
//      SSLogU.info("end intgration test solr for search");
//      
//      return true;
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return false;
//    }
//  }
  
//  private static DataSource   connectionPool           = null;
//  public         Connection   connector                = null;
//  private        boolean      gotCon                   = false;
//  private        Integer      numberTimesTriedToGetCon = 0;
  
  protected static ConcurrentUpdateSolrClient solrServer = null;
  
  public SSDBNoSQLSolrImpl(){
    
    super(SSCoreConf.instGet().getDbNoSQL());
    
//    connectToSolr();
  }

  private void connectToSolr() throws SSErr{
    
    try{
      if(solrServer == null){
        //TODO fix
//        solrServer = new ConcurrentUpdateSolrClient(solrConf.uri, 1, 10);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public void addDoc(final SSDBNoSQLAddDocPar par) throws SSErr {
    
//    according to Solr specification by adding a document with an ID already
//	  existing in the index will replace the document (eg. refer to 
//	  http://stackoverflow.com/questions/8494923/solr-block-updating-of-existing-document or
//	  http://lucene.apache.org/solr/api-4_0_0-ALPHA/doc-files/tutorial.html ) 
   
    try{
      final ContentStreamUpdateRequest csur = new ContentStreamUpdateRequest("/update/extract");
      final NamedList<Object>          response;

      csur.addContentStream(new ContentStreamBase.FileStream(new File(SSConf.getLocalWorkPath() + par.id)));

      csur.setParam  ("literal.id",  par.id);
//      csur.setParam  ("stream.type", "application/octet-stream");
      
      csur.setAction (AbstractUpdateRequest.ACTION.COMMIT, true, true);

      response = solrServer.request(csur);

      SSLogU.info("document w/ id " + par.id + " added successfully. ");
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
	
  @Override
  public void removeDoc(final SSDBNoSQLRemoveDocPar par) throws SSErr{

    try{
      
      solrServer.deleteById(par.id);
      solrServer.commit();

      SSLogU.info("document w/ id " + par.id + " deleted successfully.");
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  @Override
  public List<String> search(final SSDBNoSQLSearchPar par) throws SSErr {
    
    try{
      final List<String>    searchResults   = new ArrayList<>();
      final SolrQuery       query           = new SolrQuery();
      final String          globalSearchOp  = par.globalSearchOp.toUpperCase();
      final String          localSearchOp   = par.localSearchOp.toUpperCase();
      String                queryString     = new String();
      
      if(par.wheres.isEmpty()){
        throw new Exception("no search fields given");
      }
        
      for(Map.Entry<SSSolrSearchFieldE, List<SSSolrKeywordLabel>> where : par.wheres.entrySet()){
        
        if(where.getValue().isEmpty()){
          throw new Exception("values for search field empty");
        }

        queryString += where.getKey().val + SSStrU.colon + "(";
        
        for(SSSolrKeywordLabel value : where.getValue()){
          
          if(
            par.useFuzzySearch &&
            !SSStrU.contains(value, SSStrU.blank)){

            queryString += value + SSStrU.tilde + SSStrU.blank + localSearchOp + SSStrU.blank;
          }else{
            queryString += SSStrU.doubleQuote + value + SSStrU.doubleQuote + SSStrU.blank + localSearchOp + SSStrU.blank;
          }
        }
        
        queryString = SSStrU.removeTrailingString(queryString, SSStrU.blank + localSearchOp + SSStrU.blank) + ")" + SSStrU.blank + globalSearchOp + SSStrU.blank;
      }
      
      queryString = SSStrU.removeTrailingString(queryString, SSStrU.blank + globalSearchOp + SSStrU.blank);
      
      query.setQuery(queryString);
      query.setRows (par.maxResults);
    
      for(SSSolrSearchResult result : SSSolrSearchResult.get(solrServer.query(query).getResults())){
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
//	public void reindexDocuments() throws SSErr { //not yet tested w/ subfolders in webdav!
		
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

//@Override
//  public void solrRemoveDocsAll(final SSServPar parA) throws SSErr {
//    
//    try{
//      
//      NamedList<Object>      nl;
//      UpdateResponse         ur;
//      
//      SSLogU.info("starting to remove all documents from index.");
//      
//      ur = solrUpdater.deleteByQuery("*:*");
//      solrUpdater.commit();
//      
//      nl = ur.getResponse();
//      
//      SSLogU.info("removed all documents from index.");
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }
//  }