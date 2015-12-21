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
package at.kc.tugraz.ss.serv.datatypes.learnep.datatypes;

import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.SSTextComment;
import java.util.Map;

public class SSLearnEpCircle extends SSEntity{
  
  public Float         xLabel           = null;
  public Float         yLabel           = null;
  public Float         xR               = null;
  public Float         yR               = null;
  public Float         xC               = null;
  public Float         yC               = null;
  
  public static SSLearnEpCircle get(
    final SSLearnEpCircle learnEpCircle, 
    final SSEntity        entity) throws Exception{
    
    return new SSLearnEpCircle(learnEpCircle, entity);
  }
  
  protected SSLearnEpCircle(
    final SSLearnEpCircle learnEpCircle,
    final SSEntity        entity) throws Exception{
    
    super(learnEpCircle, entity);
    
    if(learnEpCircle.xLabel != null){
      this.xLabel             = learnEpCircle.xLabel;
    }else{
      
      if(entity instanceof SSLearnEpCircle){
        this.xLabel = ((SSLearnEpCircle) entity).xLabel;
      }
    }
    
    if(learnEpCircle.yLabel != null){
      this.yLabel            = learnEpCircle.yLabel;
    }else{
      
      if(entity instanceof SSLearnEpCircle){
        this.yLabel = ((SSLearnEpCircle) entity).yLabel;
      }
    }
    
    if(learnEpCircle.xR != null){
      this.xR            = learnEpCircle.xR;
    }else{
      
      if(entity instanceof SSLearnEpCircle){
        this.xR = ((SSLearnEpCircle) entity).xR;
      }
    }
    
    if(learnEpCircle.yR != null){
      this.yR            = learnEpCircle.yR;
    }else{
      
      if(entity instanceof SSLearnEpCircle){
        this.yR = ((SSLearnEpCircle) entity).yR;
      }
    }
    
    if(learnEpCircle.xC != null){
      this.xC            = learnEpCircle.xC;
    }else{
      
      if(entity instanceof SSLearnEpCircle){
        this.xC = ((SSLearnEpCircle) entity).xC;
      }
    }
    
    if(learnEpCircle.yC != null){
      this.yC            = learnEpCircle.yC;
    }else{
      
      if(entity instanceof SSLearnEpCircle){
        this.yC = ((SSLearnEpCircle) entity).yC;
      }
    }
  }
  
  public static SSLearnEpCircle get(
    final SSUri                  id, 
    final SSLabel                label, 
    final SSTextComment          description, 
    final Long                   creationTime, 
    final SSEntity               author,
    final Float                  xLabel, 
    final Float                  yLabel, 
    final Float                  xR, 
    final Float                  yR, 
    final Float                  xC, 
    final Float                  yC) throws Exception{
    
    return new SSLearnEpCircle(
      id,
      label,
      description,
      creationTime,
      author,
      xLabel, 
      yLabel, 
      xR, 
      yR, 
      xC, 
      yC);
  }
  
  protected SSLearnEpCircle(
    final SSUri         id, 
    final SSLabel       label,
    final SSTextComment description,
    final Long          creationTime,
    final SSEntity      author,
    final Float         xLabel, 
    final Float         yLabel, 
    final Float         xR, 
    final Float         yR, 
    final Float         xC, 
    final Float         yC) throws Exception{
    
    super(
      id, 
      SSEntityE.learnEpCircle,
      label, 
      description, 
      creationTime, 
      author);
    
    this.xLabel              = xLabel;
    this.yLabel              = yLabel;
    this.xR                  = xR;
    this.yR                  = yR;
    this.xC                  = xC;
    this.yC                  = yC;
  }
}