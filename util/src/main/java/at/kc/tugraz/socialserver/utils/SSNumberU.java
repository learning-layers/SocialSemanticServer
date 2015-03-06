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
 package at.kc.tugraz.socialserver.utils;

import java.util.List;

public class SSNumberU{
  
  private SSNumberU(){}
  
  public static boolean isLessThanOrEqual(final double number1, final double number2) {

		if(number1 <= number2){
			return true;
		}else{
			return false;
		}
	}

	public static boolean isGreaterThan(final double number1, final double number2) {

		if(number1 > number2){
			return true;
		}else{
			return false;
		}
	}
  
  public static boolean isLess(final int int1, final int int2) {

    if (int1 < int2) {
      return true;
    }

    return false;
  }

	public static Integer[] toIntegerArray(final List<Integer> toConvert) {
		return (Integer[]) toConvert.toArray(new Integer[toConvert.size()]);
	}
  
  public static boolean equalsNot(final short short1, final short short2) {
    return !equals(short1, short2);
  }
  
  public static boolean equals(final short short1, final short short2){
    return short1 == short2;
  }
}