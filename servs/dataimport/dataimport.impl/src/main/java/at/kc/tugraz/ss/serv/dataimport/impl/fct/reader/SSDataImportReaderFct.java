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
package at.kc.tugraz.ss.serv.dataimport.impl.fct.reader;

import at.tugraz.sss.serv.SSEncodingU;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSFileU;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSStrU;
import au.com.bytecode.opencsv.CSVReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

public class SSDataImportReaderFct {

  public static List<String[]> readAllFromCSV(
    final String filePath) throws Exception{
    
    FileInputStream                    in          = null;
    InputStreamReader                  reader      = null;
    CSVReader                          csvReader   = null;
    
    try{
      
      try{
        in = SSFileU.openFileForRead (filePath);
      }catch(FileNotFoundException error){
        throw new Exception("csv file to read users from not found at: " + filePath);
      }
      
      reader    = new InputStreamReader   (in,     Charset.forName(SSEncodingU.utf8.toString()));
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
    
  public static List<String[]> readAllFromCSV(
    final String path,
    final String fileName) throws SSErr{
    
    try{
      return readAllFromCSV(SSFileU.correctDirPath(path) + fileName);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}