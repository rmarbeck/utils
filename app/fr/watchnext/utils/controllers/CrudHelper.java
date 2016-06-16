package fr.watchnext.utils.controllers;

import java.lang.reflect.Field;
import java.util.Optional;

import play.Application;
import play.Logger;
import play.ApplicationLoader.Context;
import play.mvc.Controller;
import play.mvc.Result;

public class CrudHelper extends Controller {

	public Result display(String controllerName, Long id) {
		return crud(controllerName).map(crud -> {
			return crud.display(id);
		}).orElse(badRequest("Don't try to hack the URI!!"));
	}
	
	public Result manage(String controllerName) {
		return crud(controllerName).map(crud -> {
			return crud.manage();
		}).orElse(badRequest("Don't try to hack the URI!!"));
	}
	
	public Result edit(String controllerName, Long id) {
		return crud(controllerName).map(crud -> {
			return crud.edit(id);
		}).orElse(badRequest("Don't try to hack the URI!!"));
	}
	
	public Result create(String controllerName) {
		return crud(controllerName).map(crud -> {
			return crud.create();
		}).orElse(badRequest("Don't try to hack the URI!!"));
	}
	
    public Result page(String controllerName,int page, String sortBy, String order, String filter, int pageSize) {
    	return crud(controllerName).map(crud -> {
			return crud.page(page, sortBy, order, filter, pageSize);
		}).orElse(badRequest("Don't try to hack the URI!!"));
    }
    
    public Result displayAll(String controllerName, int pageSize) {
    	return page(controllerName, 0, "", "", "", pageSize);
    }
    
	private Optional<Crud> crud(String controllerName) {
		Optional<Crud> crudValue;
		crudValue = retrieveCrudInstance(controllerName).map(crud -> {
			try {
				return Optional.of((Crud) crud.get(null));
			} catch (Exception e) {
				return Optional.ofNullable((Crud) null);
			}
		}).orElse(Optional.empty());

		return crudValue;
	}
	
	private Optional<Field> retrieveCrudInstance(String controllerName) {
		Optional<Field> crudField;
		
		crudField = guessControllerByName(controllerName).map(clazz -> {
			try {
				return Optional.of(clazz.getField("crud"));
			} catch (Exception e) {
				return Optional.ofNullable((Field) null);
			}
		}).orElse(Optional.empty());
		
		return crudField;
	}

	private static Optional<Class<?>> guessControllerByName(String controllerName) {
		Optional<Class<?>> controllerFound;

		StringBuilder fqControllerName = new StringBuilder();
		fqControllerName.append("controllers.").append(controllerName);
		try {
			controllerFound = Optional.of(Thread.currentThread().getContextClassLoader().loadClass(fqControllerName.toString()));
		} catch (SecurityException | ClassNotFoundException e) {
			controllerFound = Optional.empty();
		}
		return controllerFound;
	}
	
	protected static Optional<String> guessControllerNameByModelInstance(Object instance) {
		return guessControllerNameByModelName(instance.getClass().getSimpleName());
	}
	
	public static Optional<String> guessControllerNameByModelName(String modelName) {
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
		
		return Optional.ofNullable(controllerName.append("s").toString());
	}
	
	protected Optional<String> guessModelNameByModelInstance(Object instance) {
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

