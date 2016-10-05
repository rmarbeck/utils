package fr.watchnext.utils.controllers;

import java.lang.reflect.Field;
import java.util.Optional;

import fr.watchnext.utils.usual.ControllerHelper;
import play.mvc.Controller;
import play.mvc.Result;

public class AuthenticateHelper extends Controller {

	public Result prepareLogin(String controllerName, String origin) {
		return getLogin(controllerName).map(login -> {
			return login.prepareLogin(origin);
		}).orElse(badRequest("Don't try to hack the URI!!"));
	}
	
	public Result login(String controllerName) {
		return getLogin(controllerName).map(login -> {
			return login.login();
		}).orElse(badRequest("Don't try to hack the URI!!"));
	}
	
	public Result logout(String controllerName) {
		return getLogin(controllerName).map(login -> {
			return login.logout();
		}).orElse(badRequest("Don't try to hack the URI!!"));
	}
    
	private Optional<Login> getLogin2(String controllerName) {
		Optional<Login> loginValue;
		loginValue = retrieveLoginInstance(controllerName).map(login -> {
			try {
				return Optional.of((Login) login.get(null));
			} catch (Exception e) {
				return Optional.ofNullable((Login) null);
			}
		}).orElse(Optional.empty());

		return loginValue;
	}
	
    private Optional<Login> getLogin(String controllerName) {
		Optional<Object> login = ControllerHelper.getController(controllerName, "login");
		if (login.isPresent())
			if (login.get().getClass() == Login.class)
				return Optional.of((Login) login.get());
		return Optional.empty();
	}
	
	private Optional<Field> retrieveLoginInstance(String controllerName) {
		return ControllerHelper.retrieveInstanceFromController(controllerName, "login");
	}
}

