package fr.watchnext.utils.controllers;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import play.Logger;
import play.mvc.Http.Context;
import play.mvc.Security;
import fr.watchnext.utils.usual.ReflectionHelper;

public class ControllerAuthenticationHelper {
	@SuppressWarnings("rawtypes")
	private final static Class SECURITY_INTERFACE = Security.Authenticated.class;
	private final static String METHOD_NAME = "getUsername";
	
	public static boolean isAuthorized(Class<?> controllerName, Context ctx) {
		Logger.info("Checking if controller is auhtenticated or not");
		if(isControllerSecured(controllerName)) {
			Logger.info("controller secured");
			return getSecurityImplementationClassName(controllerName).map(className -> {
				return callMethodToCheckAuthentication(className, ctx) != null;
			}).orElse(false);
		} else {
			Logger.info("controller not secured");
			return true;
		}
	}
	
	public static boolean isAuthorized(Class<?> controllerName) {
		return isAuthorized(controllerName, play.mvc.Http.Context.current());
	}
	
	private static boolean isControllerSecured(Class<?> controllerName) {
		if (getSecurityAnnotation(controllerName) != null)
			return true;
		return false;
	}
	
	@SuppressWarnings("unchecked")
	private static Annotation getSecurityAnnotation(Class<?> controllerName) {
		return controllerName.getAnnotation(SECURITY_INTERFACE);
	}
	
	private static Optional<String> getSecurityImplementationClassName(Class<?> className) {
		try {
			Object value = getSecurityAnnotation(className).annotationType().getMethod("value").invoke(getSecurityAnnotation(className), (Object[])null);
			return Optional.of(extractClassName(value.toString()));
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Optional.empty();
		}
	}
	
	private static String extractClassName(String annotationTypeAsString) {
		return annotationTypeAsString.substring(6);
	}
	
	private static String callMethodToCheckAuthentication(String className, Context ctx) {
		Optional<Object> authenticationResult = null;
		try {
			authenticationResult = ReflectionHelper.loadClassFromCurrentContext(className).map(clazz -> {
				return ReflectionHelper.createInstanceWithDefaultConstructor(clazz).map(instance -> {
					Class<?>[] cArg = new Class[1];
			        cArg[0] = Context.class;
			        return ReflectionHelper.getMethod(instance, METHOD_NAME, cArg).map(method -> {
			        	return ReflectionHelper.invoke(method, instance, ctx);
			        });
				}).get();
			}).get();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (authenticationResult != null && authenticationResult.isPresent()) {
			Logger.info("Authentication result : " + authenticationResult.toString());
			return authenticationResult.toString();
		}
		Logger.info("Authentication result is null");
		return null;
	}
}
