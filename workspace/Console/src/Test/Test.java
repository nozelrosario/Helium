package Test;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Test {

	public Test() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Hello");
		DateTimeZone tz = DateTimeZone.getDefault();
		System.out.println(tz.toString());
		DateTime utc = new DateTime(DateTimeZone.UTC);
		System.out.println(utc.toString());
		DateTimeZone cur_tz = DateTimeZone.forID("Asia/Kolkata");
		DateTime indiaDateTime = utc.toDateTime(cur_tz);
		System.out.println(indiaDateTime.getMillis());
		DateTimeFormatter fmt = DateTimeFormat.forPattern("dd, MMMM, yyyy : hh:mm:ss");
		System.out.println(fmt.print(indiaDateTime.getMillis()));
		
		DateTime d1 = new DateTime(DateTimeZone.getDefault());
		DateTime d2 = d1.plusSeconds(1);
		long diff = d2.getMillis() - d1.getMillis();
		System.out.println(d2.getMillis() + " : " + d1.getMillis() );
		System.out.println(diff);
	}

}
