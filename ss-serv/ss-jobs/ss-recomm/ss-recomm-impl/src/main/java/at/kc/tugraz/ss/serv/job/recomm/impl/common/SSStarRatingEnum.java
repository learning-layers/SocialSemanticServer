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
package at.kc.tugraz.ss.serv.job.recomm.impl.common;

import at.kc.tugraz.socialserver.utils.SSObjU;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;

public enum SSStarRatingEnum {

  zero  (""),
  one   ("*"),
  two   ("**"),
  three ("***"),
  four  ("****"),
  five  ("*****");

  final String val;
  
  public static SSStarRatingEnum get(String rating) throws Exception{
    
    if(SSObjU.isNull(rating)){
      SSServErrReg.regErrThrow(new Exception("rating value null"));
    }
      
    for(SSStarRatingEnum ratingEnum : SSStarRatingEnum.values()) {
        if(ratingEnum.val.equals(rating)) {
            return ratingEnum;
        }
    }
    
    return three;
  }
  
  public static Integer getNumber(String ratingValue) throws Exception{
    
    SSStarRatingEnum rating = get(ratingValue);
    
    switch (rating){
			case zero:
				return 0;
			case one:
				return 1;
			case two:
				return 2;
			case three:
				return 3;
			case four:
				return 4;
			case five:
				return 5;
			default:
				return 3;
		}
  }
  
  
  public static boolean isZero(SSStarRatingEnum rating){
    
    if(SSObjU.isNull(rating)){
      return false;
    }
        
    return rating.toString().equals(SSStarRatingEnum.zero.toString());
  }
  
  public static boolean isOne(SSStarRatingEnum rating){
    
    if(SSObjU.isNull(rating)){
      return false;
    }
        
    return rating.toString().equals(SSStarRatingEnum.one.toString());
  }
  
  public static boolean isTwo(SSStarRatingEnum rating){
    
    if(SSObjU.isNull(rating)){
      return false;
    }
        
    return rating.toString().equals(SSStarRatingEnum.two.toString());
  }
  
  public static boolean isThree(SSStarRatingEnum rating){
    
    if(SSObjU.isNull(rating)){
      return false;
    }
        
    return rating.toString().equals(SSStarRatingEnum.three.toString());
  }
  
  public static boolean isFour(SSStarRatingEnum rating){
    
    if(SSObjU.isNull(rating)){
      return false;
    }
        
    return rating.toString().equals(SSStarRatingEnum.four.toString());
  }
  
  public static boolean isFive(SSStarRatingEnum rating){
    
    if(SSObjU.isNull(rating)){
      return false;
    }
        
    return rating.toString().equals(SSStarRatingEnum.five.toString());
  }
  
  private SSStarRatingEnum(String val) {
    this.val = val;
  }

  @Override
  public String toString() {
    return val;
  }
}