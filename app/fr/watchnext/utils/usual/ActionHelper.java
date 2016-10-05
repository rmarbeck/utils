package fr.watchnext.utils.usual;

import play.mvc.Http.Context;
import play.mvc.Http.Flash;

/**
 * Helper for different actions
 * 
 * @author Raphael
 *
 */

public class ActionHelper {
	private static String FLASH_ORIGIN = "flashscope_origin";
	
	public static void setOriginOfCall(Context ctx) {
		setOriginOfCall(ctx.flash(), ctx.request().uri());
	}
	
	public static void setOriginOfCall(Flash flash, String value) {
		flash.put(FLASH_ORIGIN, value);
	}
	
	public static String getOriginOfCall(Context ctx) {
		return getOriginOfCall(ctx.flash());
	}
	
	public static String getOriginOfCall(Flash flash) {
		return flash.get(FLASH_ORIGIN);
	}
	
	public static void rememberOriginOfLastCall(Context ctx) {
		rememberOriginOfLastCall(ctx.flash());
	}
	
	public static void rememberOriginOfLastCall(Flash flash) {
		setOriginOfCall(flash, getOriginOfCall(flash));
	}
}