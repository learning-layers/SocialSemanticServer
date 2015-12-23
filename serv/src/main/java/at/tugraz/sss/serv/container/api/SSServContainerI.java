package at.tugraz.sss.serv.container.api;

import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.impl.api.SSServImplA;
import at.tugraz.sss.serv.impl.api.SSServImplStartA;
import at.tugraz.sss.serv.conf.api.SSConfA;
import at.tugraz.sss.serv.conf.api.SSCoreConfA;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.datatype.enums.SSErrE;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class SSServContainerI{
 
  public        SSConfA                                         conf                            = null;
  public        final Class                                     servImplClientInteraceClass;
  public        final Class                                     servImplServerInteraceClass;
  protected     SSErr                                           servImplCreationError           = null;
  
  private final ThreadLocal<SSServImplA> servImplsByServByThread = new ThreadLocal<SSServImplA>(){
    
    @Override
    protected SSServImplA initialValue(){
      
      try{
        return createServImplForThread();
      }catch (Exception error){
        SSLogU.err(error);
        servImplCreationError = SSErr.get(SSErrE.servImplCreationFailed, error);
        return null;
      }
    }
  };
  
  public void destroy(){
    servImplsByServByThread.remove();
  }
  
  protected SSServContainerI(
    final Class servImplClientInteraceClass,
    final Class servImplServerInteraceClass){
    
    this.servImplClientInteraceClass = servImplClientInteraceClass;
    this.servImplServerInteraceClass = servImplServerInteraceClass;
  }
  
  protected abstract SSServImplA      createServImplForThread   () throws SSErr;
  public    abstract SSCoreConfA      getConfForCloudDeployment (final SSCoreConfA coreConfA, final List<Class> configuredServs) throws Exception;
  public    abstract void             initServ                  () throws Exception;
  public    abstract void             schedule                  () throws Exception;
  public    abstract SSServContainerI regServ                   () throws Exception;
  
  protected SSCoreConfA getConfForCloudDeployment(
    final Class       servI,
    final SSCoreConfA coreConf,
    final List<Class> configuredServs) throws Exception{
    
    if(configuredServs.contains(servI)){
      return coreConf;
    }
    
    //TODO dtheiler: implementation missing here
//    for(SSServContainerI serv : servs.values()){
//      
//      if(servI.isInstance(serv)){
//        
//        configuredServs.add(servI);
//        
//        return serv.getConfForCloudDeployment(coreConf, configuredServs);
//      }
//    }
    
    throw new Exception("service not registered");
  }
  
  public List<String> publishClientOps() throws Exception{
    
    final List<String> clientOps = new ArrayList<>();
    
    if(servImplClientInteraceClass == null){
      return clientOps;
    }
    
    final Method[]      methods   = servImplClientInteraceClass.getMethods();
    
    for(Method method : methods){
      clientOps.add(method.getName());
    }
    
    return clientOps;
  }
  
  public SSServImplA getServImpl() throws SSErr{
    
//    try{
      
      if(!conf.use){
        throw SSErr.get(SSErrE.servNotRunning);
      }
      
      final SSServImplA servTmp = servImplsByServByThread.get();
      
      if(servImplCreationError != null){
        throw servImplCreationError;
      }
      
      SSServImplStartA.regServImplUsedByThread (servTmp);
      
      return servTmp;
//      SSLogU.err(error);
//      throw error;
  }
}