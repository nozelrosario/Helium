package com.app.helium.Helper;

import java.lang.reflect.InvocationTargetException;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class Util {

	//NR: Modified implementation of http://stackoverflow.com/questions/2846545/runtime-casting-from-string-to-other-datatype
	//TODO : implement exception handling
		//cannot use direct class.cast due to http://bugs.java.com/bugdatabase/view_bug.do?bug_id=6456930
		public static <T> T tryParse(Class<T> klazz, String arg) {
	        Exception cause = null;
	        T ret = null;
	        Class<?> wrapper_class = klazz;
	        // convert primitive to its wrapper for calling ValueOf()
	        if(klazz.isPrimitive()) {
	        	if(klazz == boolean.class) {
	        		wrapper_class = Boolean.class;
	        	} else if(klazz == byte.class) {
	        		wrapper_class = Byte.class;
	        	} else if(klazz == char.class) {
	        		wrapper_class = Character.class;
	        	} else if(klazz == double.class) {
	        		wrapper_class = Double.class;
	        	} else if(klazz == float.class) {
	        		wrapper_class = Float.class;
	        	} else if(klazz == int.class) {
	        		wrapper_class = Integer.class;
	        	} else if(klazz == long.class) {
	        		wrapper_class = Long.class;
	        	} else if(klazz == short.class) {
	        		wrapper_class = Short.class;
	        	}
	        }
	  
	        try {
				ret = klazz.cast(
	            		wrapper_class.getDeclaredMethod("valueOf", String.class)
	                .invoke(null, arg)
	            );
	        } catch (NoSuchMethodException e) {
	            cause = e;
	        } catch (IllegalAccessException e) {
	            cause = e;
	        } catch (InvocationTargetException e) {
	            cause = e;
	        }
	        if (cause == null) {
	            return ret;
	        } else {
	            throw new IllegalArgumentException(cause);
	        }
	    }

		
		// Store date-time in UTC within  DB
		public static long convertDateTimeToDBFormat(DateTime date_time) {
			if(! date_time.equals(null)) {
				DateTime utc_date_time = date_time.toDateTime(DateTimeZone.UTC);
				long date_millis = utc_date_time.getMillis();
				return date_millis;
			} else {
				return 0;
			}
			
		}
		
		// Load UTC value into Date-Time Object in User Time-zone
		public static DateTime convertDBFormatToDateTime(long date_time_millis) {
			if(date_time_millis > 0) {
				DateTime date_time = new DateTime(date_time_millis);
				return date_time;
			} else {
				return null;
			}
		}
}
