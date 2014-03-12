/**
 * Copyright 2013 Graz University of Technology - KTI (Knowledge Technologies Institute)
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
package at.kc.tugraz.ss.serv.db.impl;

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.serv.serv.api.SSServConfA;
import at.kc.tugraz.ss.serv.db.api.SSDBGraphI;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityA;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.serv.db.datatypes.graph.SSQueryResult;
import at.kc.tugraz.ss.serv.db.datatypes.graph.SSStatement;
import at.kc.tugraz.ss.serv.serv.api.SSServImplDBA;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
//import org.openrdf.query.BindingSet;
//import org.openrdf.query.QueryLanguage;
//import org.openrdf.query.TupleQuery;
//import org.openrdf.query.TupleQueryResult;
//import org.openrdf.repository.Repository;
//import org.openrdf.repository.RepositoryConnection;
//import org.openrdf.repository.RepositoryResult;
//import virtuoso.sesame2.driver.VirtuosoRepository;

public class SSDBGraphVirtuosoImpl extends SSServImplDBA implements SSDBGraphI{
  
//  private static Repository                      rdfRepository  = null;
//  private static org.openrdf.model.ValueFactory  openrdfFactory = null;
//  private RepositoryConnection                   connector      = null;
  
  public SSDBGraphVirtuosoImpl(final SSServConfA conf) throws Exception{
    
    super(conf);
    
//    if(SSObjU.isNull(rdfRepository)){
//      rdfRepository  = new VirtuosoRepository("jdbc:virtuoso://" + ((SSDBGraphConf)conf).getHost() + SSStrU.colon + ((SSDBGraphConf)conf).getPort(), ((SSDBGraphConf)conf).getUsername(), ((SSDBGraphConf)conf).getPassword()); //, true
//      
//      rdfRepository.initialize();
//      
//      openrdfFactory = rdfRepository.getValueFactory();
//    }
//    
//    connector = rdfRepository.getConnection();
//    
//    connector.setAutoCommit(false);
  }
  
  @Override
  protected void finalizeImpl() throws Exception{
  }
  
  @Override
  public List<SSMethU> publishClientOps() throws Exception{
    return new ArrayList<SSMethU>();
  }
  
  @Override
  public List<SSMethU> publishServerOps() throws Exception{
    
    List<SSMethU> serverOps = new ArrayList<SSMethU>();
    
    Method[] methods = SSDBGraphI.class.getMethods();
    
    for(Method method : methods){
      serverOps.add(SSMethU.get(method.getName()));
    }
    
    return serverOps;
  }
  
  @Override
  public void handleClientOp(SSSocketCon sSCon, SSServPar par) throws Exception{
    throw new UnsupportedOperationException(SSStrU.empty);
  }
  
  @Override
  public Object handleServerOp(SSServPar par) throws Exception{
    return SSDBGraphI.class.getMethod(SSMethU.toStr(par.op), SSServPar.class).invoke(this, par);
  }
  
  @Override
  public Boolean dbGraphAdd(
    SSUri              subject,
    SSUri              predicate,
    SSEntityA          object,
    SSUri              context) throws Exception {
    
    throw new UnsupportedOperationException();
    
//    if(dbGraphHas(subject, predicate, object, context)) {
//      return false;
//    }
//    
//    connector.add(createOpenrdfURI(subject), createOpenrdfURI(predicate), createOpenrdfValue(object), createOpenrdfURI(context));
//    
//    return true;
  }
  
  @Override
  public void dbGraphDeleteAll(SSUri context) throws Exception{
    dbGraphRemove(null, null, null, context);
  }
  
  @Override
  public Boolean dbGraphAdd(
    SSStatement statement,
    SSUri       context) throws Exception {
    
    return dbGraphAdd(statement.subject, statement.predicate, statement.object, context);
  }
  
  @Override
  public void dbGraphRemove(
    SSStatement statement,
    SSUri       context) throws Exception {
    
    dbGraphRemove(statement.subject, statement.predicate, statement.object, context);
  }
  
  @Override
  public void dbGraphRemove(
    SSUri                  subject,
    SSUri                  predicate,
    SSEntityA              object,
    SSUri                  context) throws Exception {
  
    throw new UnsupportedOperationException();
//    connector.remove(createOpenrdfURI(subject), createOpenrdfURI(predicate), createOpenrdfValue(object), createOpenrdfURI(context));
  }
  
  @Override
  public Boolean dbGraphHas(
    SSUri               subject,
    SSUri               predicate,
    SSEntityA           object,
    SSUri               context) throws Exception {
    
    throw new UnsupportedOperationException();
//    return connector.hasStatement(createOpenrdfURI(subject), createOpenrdfURI(predicate), createOpenrdfValue(object), true, createOpenrdfURI(context));
  }
  
  @Override
  public Boolean dbGraphNotHas(
    SSUri        subject,
    SSUri        predicate,
    SSEntityA    object,
    SSUri        context) throws Exception{
    
    return !dbGraphHas(subject, predicate, object, context);
  }
  
  @Override
  public Boolean dbGraphHas(
    SSStatement statement,
    SSUri       context) throws Exception {
    
    return dbGraphHas(statement.subject, statement.predicate, statement.object, context);
  }
  
  @Override
  public List<SSStatement> dbGraphGet(
    SSUri               subject,
    SSUri               predicate,
    SSEntityA           object,
    SSUri               context) throws Exception{
    
    throw new UnsupportedOperationException();
//    RepositoryResult<org.openrdf.model.Statement> statements =
//      connector.getStatements(
//      createOpenrdfURI   (subject),
//      createOpenrdfURI   (predicate),
//      createOpenrdfValue (object),
//      true,
//      createOpenrdfURI   (context));
//    
//    SSStatement                 state;
//    
//    List<SSStatement> result = new ArrayList<SSStatement>();
//    
//    try {
//      
//      while (statements.hasNext()) {
//        
//        org.openrdf.model.Statement st    = statements.next();
//        
//        if (st.getObject() instanceof org.openrdf.model.URI) {
//          
//          state = SSStatement.get(
//            SSUri.get              (SSStrU.toString(st.getSubject())),
//            SSUri.get              (SSStrU.toString(st.getPredicate())),
//            SSUri.get              (SSStrU.toString(st.getObject())));
//          
//        } else {
//          
//          state = SSStatement.get(
//            SSUri.get             (SSStrU.toString            (st.getSubject())),
//            SSUri.get             (SSStrU.toString            (st.getPredicate())),
//            SSLiteral.get         (SSStrU.removeDoubleQuotes  (SSStrU.toString(st.getObject()))));
//        }
//        
//        result.add(state);
//      }
//    } finally {
//      statements.close();
//    }
//    
//    return result;
  }
  
  @Override
  public SSQueryResult dbGraphQuery(String sparqlQuery) throws Exception {
    
    return null;
//    TupleQuery resultsTable = connector.prepareTupleQuery(QueryLanguage.SPARQL, sparqlQuery);
//    
//    connector.commit();
//    
//    TupleQueryResult bindings = resultsTable.evaluate();
//    SSQueryResult result = new SSQueryResult();
//    
//    for (int row = 0; bindings.hasNext(); row++) {
//      
//      BindingSet pairs = bindings.next();
//      List<String> names = bindings.getBindingNames();
//      SSQueryResultItem item = new SSQueryResultItem();
//      
//      for (int i = 0; i < names.size(); i++) {
//        
//        String name = names.get(i);
//        org.openrdf.model.Value value = pairs.getValue(name);
//        
//        item.setBinding(name, SSStrU.toString(value));
//      }
//      
//      result.add(item);
//    }
//    
//    return result;
  }
  
  @Override
  public void dbGraphCommit(Boolean changed, Boolean shouldCommit) throws Exception{
    
    throw new UnsupportedOperationException();
//    if(
//      changed &&
//      shouldCommit){
//      
//      try{
//        connector.commit();
//      }catch(Exception error){
//        SSServErrReg.regErrThrow(new Exception("dbal virtuso commit failed"));
//      }
//    }
  }
  
  @Override
  public void dbGraphRollBackAndThrow(
    Boolean    ableToCommit,
    Exception  error) throws Exception{
    
    throw new UnsupportedOperationException();
//    if(!ableToCommit){
//      throw error;
//    }
//    
//    try{
//      SSServErrReg.regErr(error, "do dbal graph rollback for");
//      
//      connector.rollback();
//      
//    }catch(Exception error1){
//      SSServErrReg.regErr(error1, "dbal graph rollback failed");
//    }
//    
//    throw error;
  }
  
  @Override
  public Boolean dbGraphContextExists(SSUri uri) throws Exception {
    
    throw new UnsupportedOperationException();
    
//    long size = connector.size(createOpenrdfURI(uri));
//    if (size > 0) {
//      return true;
//    }
//    return false;
  }
  
//  private org.openrdf.model.URI createOpenrdfURI(SSUri uri) throws Exception{
//    
//    if(SSObjU.isNull(uri)){
//      return null;
//    }
//    
//    return openrdfFactory.createURI(uri.toString());
//  }
  
//  private org.openrdf.model.Value createOpenrdfValue(SSEntityA entity) throws Exception {
//    
//    if(SSObjU.isNull(entity)){
//      return null;
//    }
//    
//    if(entity instanceof SSUri){
//      return createOpenrdfURI((SSUri)entity);
//    }
//    
//    if(entity instanceof SSLiteral){
//      
//      if (((SSLiteral)entity).isnumeric){
//        return openrdfFactory.createLiteral(Double.valueOf(entity.toString()));
//      }else{
//        return openrdfFactory.createLiteral(entity.toString());
//      }
//    }
//    
//    SSServErrReg.regErrThrow(new Exception("Cannot create open rdf value from type other than SSUri and SSLiteral"));
//    return null;
//  }
}
