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
package at.tugraz.sss.serv;

import at.tugraz.sss.serv.SSFileU;
import at.tugraz.sss.serv.SSLogU;
import java.io.FileWriter;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

public abstract class SSCoreConfA {
  
  protected static SSCoreConfA instSet(
    final String pathToFile, 
    final Class  confClass) throws Exception{

    return (SSCoreConfA) getYaml().loadAs(SSFileU.openFileForRead(pathToFile), confClass);
  }
  
  private static Yaml getYaml(){
  
    DumperOptions options = new DumperOptions();
    
    options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
    options.setAllowUnicode(true);
    
    return new Yaml(new SSCoreConfARepresenter(), options);
  }
  
  private static class SSCoreConfARepresenter extends Representer {

    @Override
    protected NodeTuple representJavaBeanProperty(
      Object    javaBean, 
      Property  property, 
      Object    propertyValue, 
      Tag       customTag){
      
//      if (javaBean instanceof ExternalsrcConf && "protocol".equals(property.getName())) {
//        return null;
//      }

      return super.representJavaBeanProperty(javaBean, property, propertyValue, customTag);
    }
  }
  
  public void save(
    final String pathToFile) throws Exception{
    
    FileWriter fileWriter = null;
    
    try{
      
      final Yaml yaml = getYaml();
      
      fileWriter = new FileWriter(SSFileU.openOrCreateFileWithPathForWrite(pathToFile).getFD());
      
      yaml.dump(this, fileWriter);
      
    }catch (Exception error) {
      SSLogU.errThrow(error);
    }finally{
      
      if(fileWriter != null){
        fileWriter.close();
      }
    }
  }
}  