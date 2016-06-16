package fr.watchnext.utils.controllers;

import java.lang.reflect.Method;
import java.util.Optional;

import play.api.mvc.Call;
import play.mvc.Controller;
import fr.watchnext.utils.usual.ReflectionHelper;

public class ReverseControllerHelper extends Controller {
    public static Optional<Object> getReverseControllerInstance(String classFQName, String controllerName) {
    	Optional<Class<?>> controller = getReverseController(classFQName);
    	if (controller.isPresent())
    		return ReflectionHelper.getStaticFieldValue(controller.get(), controllerName);
		return Optional.empty();
	}
    
    public static Optional<Class<?>> getReverseController(String classFQName) {
		return ReflectionHelper.loadClassFromCurrentContext(classFQName);
	}
	
    public static Optional<Method> getMethod(Class<?> reverseClass, String methodName, Class<?>...args) {
		return ReflectionHelper.getMethod(reverseClass, methodName, args);
	}
	
    public static Call invoke(Optional<Method> method, Object instance, Object...args) {
		return (Call) ReflectionHelper.invoke(method, instance, args).orElse(null);
	}
	
    public static Call invoke(String classFQName, String controllerName, String methodName, Object...args) {
    	return invoke(classFQName, controllerName, methodName, false, args);
	}
    
    public static Call invokeWithPrimitiveType(String classFQName, String controllerName, String methodName, Object...args) {
		return invoke(classFQName, controllerName, methodName, true, args);
	}
    
    private static Call invoke(String classFQName, String controllerName, String methodName, boolean withPrimitives, Object...args) {
		Class<?>[] argsClass = getArgsClass(withPrimitives, args);
		
		return getReverseControllerInstance(classFQName, controllerName).map(instance -> invoke(getMethod(instance.getClass(), methodName, argsClass), instance, args)).orElse(null);
	}
    
    private static Class<?>[] getArgsClass(Boolean changeToPrimitive, Object...args) {
    	Class<?>[] argsClass = new Class<?>[args.length];
		int i = 0;
		for(Object arg : args) {
			if (changeToPrimitive) {
				argsClass[i++] = getPrimitiveClass(arg);
			} else {
				argsClass[i++] = arg.getClass();
			}
		}
		return argsClass;
    }
    
    private static Class<?> getPrimitiveClass(Object object) {
    	if (object.getClass().equals(Long.class))
    		return long.class;
    	if (object.getClass().equals(Integer.class))
    		return int.class;
    	if (object.getClass().equals(Float.class))
    		return float.class;
    	if (object.getClass().equals(Boolean.class))
    		return boolean.class;
    	if (object.getClass().equals(Double.class))
    		return double.class;
    	if (object.getClass().equals(Byte.class))
    		return byte.class;
    	if (object.getClass().equals(Short.class))
    		return short.class;
    	if (object.getClass().equals(Character.class))
    		return char.class;
    	
    	return object.getClass();
    }
}

