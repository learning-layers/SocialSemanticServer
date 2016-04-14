/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
 /**
 * @author Dieter Theiler
 */

package at.tugraz.sss.servs.util;

import java.util.*;
import java.util.concurrent.*;

public class SSDateU{

  public static final int    secondInMilliseconds                     = 1000;
  public static final int    dayInMinutes                             = 1440;
  public static final long   minuteInMilliSeconds                     = 60000;
  public static final long   dayInMilliSeconds                        = 86400000;
  public static final int    fONE_DAY                                 = 1;
  public static final int    fTIME                                    = 4;
  public static final int    fZERO_MINUTES                            = 0;
  
  private SSDateU(){/* Do nothing because of only JSON Jackson needs this */ }
  
	public static Long dateAsLong(){
		return new Date().getTime();
	}
  
  public static Long dateAsNano(){
		return System.nanoTime();
	}
  
  public static ScheduledExecutorService scheduleNow(final Runnable task){
    
    final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    
    scheduler.schedule(task, 0, TimeUnit.MILLISECONDS);
    
    return scheduler;
  }
  
  public static ScheduledExecutorService scheduleWithFixedDelay(
    final Runnable task,
    final Date     startDate,
    final long     timeBetween){
    
    final ScheduledExecutorService scheduler     = Executors.newSingleThreadScheduledExecutor();
    long                           initialDelay  = startDate.getTime() - new Date().getTime();
    
    if(initialDelay < 0){
      initialDelay = 0;
    }
    
    scheduler.scheduleWithFixedDelay(task, initialDelay, timeBetween, TimeUnit.MILLISECONDS);
    
    return scheduler;
  }
  
  public static Date getDatePlusMinutes(final Integer minutes){
    
    final Calendar now = new GregorianCalendar();
    
    now.add(Calendar.MINUTE, minutes);
    
    return new GregorianCalendar(
      now.get(Calendar.YEAR),
      now.get(Calendar.MONTH),
      now.get(Calendar.DATE),
      now.get(Calendar.HOUR_OF_DAY),
      now.get(Calendar.MINUTE)).getTime();
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
  
  public static Date getDateForNextHalfMinute() {

    Calendar dateForNextHalfMinute = new GregorianCalendar();

    dateForNextHalfMinute.add(Calendar.SECOND, 30);

    return dateForNextHalfMinute.getTime();
  }
}