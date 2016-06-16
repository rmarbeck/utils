package fr.watchnext.utils.controllers;

import play.api.mvc.Call;
import play.mvc.Controller;

public class ReverseCrudHelperAdapter extends Controller {
	public final static String crudHelperReverseClassCanonicalName = "fr.watchnext.utils.controllers.routes";
	public final static String crudHelperReverseFieldName = "CrudHelper";

	public static Call display(String controllerName, long id) {
		return invoke("display", controllerName, id);
	}
	
	public static Call manage(String controllerName) {
		return invoke("manage", controllerName);
	}
	
	public static Call edit(String controllerName, long id) {
		return invoke("edit", controllerName, id);
	}
	
	public static Call create(String controllerName) {
		return invoke("create", controllerName);
	}
	
    public static Call page(String controllerName,int page, String sortBy, String order, String filter, int pageSize) {
    	return invoke("page", controllerName, page, sortBy, order, filter, pageSize);
    }
    
    public static Call displayAll(String controllerName, int pageSize) {
    	return invoke("displayAll", controllerName, pageSize);
    }
    
	private static Call invoke(String methodName, Object...args) {
		return ReverseControllerHelper.invokeWithPrimitiveType(crudHelperReverseClassCanonicalName, crudHelperReverseFieldName, methodName, args);
	}
}

