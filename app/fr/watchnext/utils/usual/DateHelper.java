package fr.watchnext.utils.usual;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


/**
 * Helper for date manipulation
 * 
 * @author Raphael
 *
 */

public class DateHelper {
	public static int nbDaysTillToday(Date dateInThePast) {
		return nbDaysBetween(new Date(System.currentTimeMillis()), dateInThePast);
	}
	
	public static int nbDaysFromToday(Date dateInTheFuture) {
		return nbDaysBetween(dateInTheFuture, new Date(System.currentTimeMillis()));
	}
	
	public static int nbDaysBetween(Date date) {
		return nbDaysBetween(new Date(System.currentTimeMillis()), date);
	}
	
	public static int nbDaysBetween(Date date1, Date date2) {
		return Math.round(((differenceBetweenTwoDates(date1, date2)) / 1000 / 3600 / 24));
	}
	
	public static boolean isBeforeNow(Date date) {
		if (date != null)
			return differenceBetweenNowAndADate(date) > 0;
			
		return false;
	}
	
	public static boolean isAfterNow(Date date) {
		return !isBeforeNow(date);
	}
	
	private static long differenceBetweenTwoDates(Date date1, Date date2) {
		return date1.getTime() - date2.getTime();
	}
	
	private static long differenceBetweenNowAndADate(Date date) {
		return differenceBetweenTwoDates(new Date(System.currentTimeMillis()), date);
	}
	
	public static Instant endOfTheDay(Date date) {
		LocalDate currentDate = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).toLocalDate();
		
		LocalDateTime endOfTheDay = currentDate.atTime(23, 59, 59);
	
		return endOfTheDay.atZone(ZoneId.systemDefault()).toInstant();
	}
	
	public static Instant beginningOfTheDay(Date date) {
		LocalDate currentDate = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).toLocalDate();
		
		LocalDateTime startOfTheDay = currentDate.atStartOfDay();
	
		return startOfTheDay.atZone(ZoneId.systemDefault()).toInstant();
	}
	
	public static Date toDate(Instant instant) {
		return new Date(instant.toEpochMilli());
	}
	
	public static Date parseDate(String toParse) {
		LocalDate dateParsed = LocalDate.parse(toParse);
		return new Date(dateParsed.toEpochDay());
	}
	
	public static Date parseDateInMillis(String toParse) {
		return new Date(Long.parseLong(toParse));
	}
}