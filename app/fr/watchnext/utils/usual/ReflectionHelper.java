package fr.watchnext.utils.usual;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import play.Logger;
import play.api.mvc.Call;

public class ReflectionHelper {

	public static List<String> guessPossibleAccessorMethodsForAField(String fieldName) {
		if (fieldName == null || fieldName.equals(""))
			return new ArrayList<String>();
		
		List<String> possibleAccessorMethods = new ArrayList<String>();
		String capitalizedFieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1).toLowerCase();
		
		possibleAccessorMethods.add("get"+capitalizedFieldName);
		possibleAccessorMethods.add("is"+capitalizedFieldName);
		possibleAccessorMethods.add("has"+capitalizedFieldName);
		possibleAccessorMethods.add("is_"+capitalizedFieldName);
		possibleAccessorMethods.add("has_"+capitalizedFieldName);
		
		return possibleAccessorMethods;
	}
	
	public static Optional<?> getValue(Object object, List<String> fieldPossibleAccessorMethods) {
		if ( object == null
				|| fieldPossibleAccessorMethods == null
				|| fieldPossibleAccessorMethods.size() == 0 )
			return Optional.empty();
		
		for (String accessor : fieldPossibleAccessorMethods) {
			Optional<?> value = getValue(object, accessor);
			if (value.isPresent())
				return value;
		}
		return Optional.empty();

	}
	
	public static Optional<?> getValue(Object object, String accessorMethod) {
		if ( object == null
				|| accessorMethod == null
				|| accessorMethod.equals("") )
			return Optional.empty();
		
		Class<?> c = object.getClass();
		try {
			return Optional.ofNullable(c.getMethod(accessorMethod).invoke(object));
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			Logger.error("Error when getting value", e);
			return Optional.empty();
		}
	}
	
	
	public static Optional<String> getStringValue(Object object, List<String> fieldPossibleAccessorMethods) {
		return getStringValue(getValue(object, fieldPossibleAccessorMethods));
	}
	
	public static Optional<String> getStringValue(Object object, String fieldAccessorMethod) {
		return getStringValue(getValue(object, fieldAccessorMethod));
	}
	
	private static Optional<String> getStringValue(Optional<?> object) {
		if (!object.isPresent())
			return Optional.empty();
		
		String typeOfReturn = object.get().getClass().getName();
		switch (typeOfReturn) {
		  case "java.lang.String":
			  return Optional.of((java.lang.String) object.get());
		  case "java.lang.Long":
			  return Optional.of(Long.toString((java.lang.Long) object.get()));
		  case "java.util.Date":
			  return Optional.of(Long.toString(((java.util.Date) object.get()).getTime()));
		  case "java.lang.Boolean":
			  return Optional.of(((java.lang.Boolean) object.get()).toString());
		  case "java.lang.Integer" :
			  return Optional.of(object.get().toString());
		  default:
			  if (typeOfReturn.startsWith("models."))
				  return Optional.of(object.get().toString());
			  return Optional.of("unsupported type ["+typeOfReturn +"] - " +object.get().toString());
		}
	}
	
	public static Optional<Class<?>> loadClassFromCurrentContext(String className) {
		try {
			return Optional.of(Thread.currentThread().getContextClassLoader().loadClass(className));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}
	
	public static Optional<Field> getField(Class<?> clazz, String fieldName) {
		try {
			return Optional.of(clazz.getField(fieldName));
		} catch (SecurityException | NoSuchFieldException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}
	
	public static Optional<Object> getStaticFieldValue(Class<?> clazz, String fieldName) {
		try {
			return Optional.of(clazz.getField(fieldName).get(null));
		} catch (SecurityException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}
	
	public static Optional<Method> getMethod(Class<?> clazz, String methodName, Class<?>...argsClass) {
		try {
			return Optional.of(clazz.getMethod(methodName, argsClass));
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}
	
	public static Optional<Object> invoke(Optional<Method> method, Object o, Object...args) {
		if (method.isPresent())
			try {
				return Optional.of(method.get().invoke(o, args));
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
		return Optional.empty();
	}
	
	public static Optional<Object> invoke(Optional<Method> method, Class<?> clazz, Object...args) {
		try {
			return invoke(method, clazz.newInstance(), args);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}
}
