package fr.watchnext.utils.controllers;

import java.util.Optional;

import com.fasterxml.jackson.datatype.jdk8.OptionalDoubleSerializer;

import play.mvc.Controller;
import play.mvc.Result;
import fr.watchnext.utils.usual.ControllerHelper;

public class CrudHelper extends Controller {

	public Result display(String controllerName, Long id) {
		return crud(controllerName).map(crud -> {
			return crud.display(id);
		}).orElse(ControllerHelper.manageControllerEmpty(controllerName));
	}
	
	public Result manage(String controllerName) {
		return crud(controllerName).map(crud -> {
			return crud.manage();
		}).orElse(ControllerHelper.manageControllerEmpty(controllerName));
	}
	
	public Result edit(String controllerName, Long id) {
		return crud(controllerName).map(crud -> {
			return crud.edit(id);
		}).orElse(ControllerHelper.manageControllerEmpty(controllerName));
	}
	
	public Result create(String controllerName) {
		return crud(controllerName).map(crud -> {
			return crud.create();
		}).orElse(ControllerHelper.manageControllerEmpty(controllerName));
	}
	
    public Result page(String controllerName,int page, String sortBy, String order, String filter, int pageSize) {
    	return crud(controllerName).map(crud -> {
			return crud.page(page, sortBy, order, filter, pageSize);
		}).orElse(ControllerHelper.manageControllerEmpty(controllerName));
    }
    
    public Result displayAll(String controllerName, int pageSize) {
    	return page(controllerName, 0, "", "", "", pageSize);
    }
    
    private Optional<Crud> crud(String controllerName) {
		Optional<Object> crud = ControllerHelper.getController(controllerName, "crud");
		if (crud.isPresent())
			if (crud.get().getClass() == Crud.class)
				return Optional.of((Crud) crud.get());
		return Optional.empty();
	}
}

