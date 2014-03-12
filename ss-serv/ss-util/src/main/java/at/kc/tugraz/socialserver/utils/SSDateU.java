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
 package at.kc.tugraz.socialserver.utils;

import java.util.*;

public class SSDateU{
  
  public static final long   minuteInMilliSeconds                     = 60000;
  public static final long   dayInMilliSeconds                        = 86400000;
  public static final int    fONE_DAY                                 = 1;
  public static final int    fTIME                                    = 4;
  public static final int    fZERO_MINUTES                            = 0;
  
  private SSDateU(){}
  
	public static Long dateAsLong(){
		return new Date().getTime();
	}
  
  public static Long dateAsNano(){
		return System.nanoTime();
	}
  
  public static void scheduleAtFixedRate(final TimerTask updater, final Date date, final long timeBetween){
    
    Timer  timer = new Timer(); 
    
    timer.scheduleAtFixedRate(updater, date, timeBetween); 
  }
  
  public static Date getDateForTomorrowMorning() {

    Calendar tomorrow = new GregorianCalendar();
    Calendar result;

    tomorrow.add(Calendar.DATE, fONE_DAY);

    result = new GregorianCalendar(
            tomorrow.get(Calendar.YEAR),
            tomorrow.get(Calendar.MONTH),
            tomorrow.get(Calendar.DATE),
            fTIME,
            fZERO_MINUTES);

    return result.getTime();
  }
  
  public static Date getDateForNextMinute() {

    Calendar dateForNextMinute = new GregorianCalendar();

    dateForNextMinute.add(Calendar.MINUTE, 1);

    return dateForNextMinute.getTime();
  }
}