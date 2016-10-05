package fr.watchnext.utils.usual;

import java.lang.reflect.Field;
import java.util.Optional;

import fr.watchnext.utils.controllers.Crud;
import fr.watchnext.utils.controllers.ControllerAuthenticationHelper;
import play.Logger;
import play.mvc.Result;
import static play.mvc.Results.*;

public class ControllerHelper {
	private final static String CONTROLLER_PACKAGE = "controllers.";
	
	public static Optional<Class<?>> guessControllerByName(String controllerName) {
		Optional<Class<?>> controllerFound;

		StringBuilder fqControllerName = new StringBuilder();
		fqControllerName.append(CONTROLLER_PACKAGE).append(controllerName);
		try {
			controllerFound = Optional.of(Thread.currentThread().getContextClassLoader().loadClass(fqControllerName.toString()));
		} catch (SecurityException | ClassNotFoundException e) {
			controllerFound = Optional.empty();
		}
		return controllerFound;
	}
	
	public static Optional<String> guessControllerNameByModelInstance(Object instance) {
		return guessControllerNameByModelName(instance.getClass().getSimpleName());
	}
	
	public static Optional<String> guessControllerNameByModelName(String modelName) {
		return Optional.ofNullable(pluralize(prepareSpecificModelNamesToPlural(modelName)));
	}
	
    public static Result manageControllerEmpty(String controllerName) {
    	return isAutorized(controllerName).map(authorized -> {
			if (authorized == false)
				return play.mvc.Results.redirect("/login");
			return badRequest("Don't try to hack the URI!!");
		}).orElse(badRequest("Don't try to hack the URI!!"));
    }
    
    
	public static Optional<Crud> crud(String controllerName) {
		Optional<Crud> crudValue;
		crudValue = retrieveInstanceFromController(controllerName, "crud").map(crud -> {
			try {
				Crud crudInstance = (Crud) crud.get(null);
				return Optional.of(crudInstance);
			} catch (Exception e) {
				return Optional.ofNullable((Crud) null);
			}
		}).orElse(Optional.empty());

		return crudValue;
	}
	
	public static Optional<Object> getController(String controllerName, String fieldName) {
		Optional<Object> controllerValue;
		controllerValue = retrieveInstanceFromController(controllerName, fieldName).map(controller -> {
			try {
				Object crudInstance = controller.get(null);
				return Optional.of(crudInstance);
			} catch (Exception e) {
				return Optional.ofNullable(null);
			}
		}).orElse(Optional.empty());

		return controllerValue;
	}
	
	public static Optional<Field> retrieveInstanceFromController(String controllerName, String fieldName) {
		Optional<Field> field;
		
		field = ControllerHelper.guessControllerByName(controllerName).map(clazz -> {
			return isAutorized(controllerName).map(authorized -> {
				if (authorized)
					try {
						Logger.info("-- Controller authorized --");
						return Optional.of(clazz.getField(fieldName));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				Logger.info("-- Controller NOT authorized --");
				return Optional.ofNullable((Field) null);
			}).orElse(Optional.empty());
		}).orElse(Optional.empty());
		
		return field;
	}
    
	private static Optional<Boolean> isAutorized(String controllerName) {
		Optional<Boolean> field;
		
		field = ControllerHelper.guessControllerByName(controllerName).map(clazz -> {
			try {
				if (ControllerAuthenticationHelper.isAuthorized(clazz)) {
					Logger.info("-- Controller authorized --");
					return Optional.of(new Boolean(true));
				} else {
					Logger.info("-- Controller NOT authorized --");
					return Optional.of(new Boolean(false));
				}
			} catch (Exception e) {
				Logger.error("-- Error in retrieving Controller instance --");
				return Optional.ofNullable((Boolean) null);
			}
		}).orElse(Optional.empty());
		
		return field;
	}
	
	private static String prepareSpecificModelNamesToPlural(String modelName) {
		StringBuilder controllerName = new StringBuilder();
		if (modelName.endsWith("y")) {
			controllerName.append(removeLastChar(modelName));
			controllerName.append("ie");
		} else if (modelName.endsWith("ch")) {
			controllerName.append(modelName);
			controllerName.append("e");
		} else {
			controllerName.append(modelName);
		}
		return controllerName.toString();
	}
	
	private static String pluralize(String name) {
		StringBuilder pluralized = new StringBuilder(name);
		return pluralized.append("s").toString();
	}
	
	private Optional<String> guessModelNameByModelInstance(Object instance) {
		if (instance != null) {
			String rawClassName = instance.getClass().getSimpleName();
			StringBuilder classNameFormatted = new StringBuilder(rawClassName); 
			return Optional.of(classNameFormatted.toString()); 
		}
		return Optional.empty();
	}
	
	private static String removeLastChar(String toAlter) {
		int toAlterSize = toAlter.length();
		return toAlter.substring(0, toAlterSize-1);
	}
}
