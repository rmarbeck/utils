package fr.watchnext.utils.controllers;

import play.api.mvc.Call;
import play.mvc.Controller;
import play.mvc.Result;

public class ReverseAuthenticateHelperAdapter extends Controller {
	public final static String authenticateHelperReverseClassCanonicalName = "fr.watchnext.utils.controllers.routes";
	public final static String authenticateHelperReverseFieldName = "AuthenticateHelper";

	public static Call prepareLogin(String controllerName, String origin) {
		return invoke("prepareLogin", controllerName, origin);
	}
	
	public static Call login(String controllerName) {
		return invoke("login", controllerName);
	}
	
	public static Call logout(String controllerName) {
		return invoke("logout", controllerName);
	}
    
	private static Call invoke(String methodName, Object...args) {
		return ReverseControllerHelper.invokeWithPrimitiveType(authenticateHelperReverseClassCanonicalName, authenticateHelperReverseFieldName, methodName, args);
	}
}