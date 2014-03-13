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
package at.kc.tugraz.ss.serv.dataimport.impl.fct.reader;

import at.kc.tugraz.socialserver.utils.SSEncodingU;
import at.kc.tugraz.socialserver.utils.SSFileU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import au.com.bytecode.opencsv.CSVReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

public class SSDataImportReaderFct {

  public static List<String[]> readAllFromCSV(
    final String path, 
    final String fileName) throws Exception{
    
    FileInputStream                    in          = null;
    InputStreamReader                  reader      = null;
    CSVReader                          csvReader   = null;
    
    try{
      in        = SSFileU.openFileForRead (SSFileU.correctDirPath(path) + fileName);
      reader    = new InputStreamReader   (in,     Charset.forName(SSEncodingU.utf8));
      csvReader = new CSVReader           (reader, SSStrU.semiColon.charAt(0));
      
      return csvReader.readAll();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      
      if(in != null){
        in.close();
      }
      
      if(reader != null){
        reader.close();
      }
      
      if(csvReader != null){
        csvReader.close();
      }
    }
  }
}
