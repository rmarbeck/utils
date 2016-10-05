package fr.watchnext.utils.controllers;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.twirl.api.Html;
import play.twirl.api.Template2;
import fr.watchnext.utils.models.SimpleLogin;
import fr.watchnext.utils.usual.ActionHelper;


public class Login<T extends SimpleLogin, U extends ActionAuthenticator> extends Controller  {
	private final Form<T> form;
	private U authenticator;
	private Template2<Form<T>, String, Html> loginTemplate;
	private String defaultPostLoginUrl;
	private String logoutUrl;
	
    public Login(U authenticator, Template2<Form<T>, String, Html> loginTemplate, Form<T> form, String logoutUrl, String defaultPostLoginUrl) {
    	this.authenticator = authenticator;
    	this.loginTemplate = loginTemplate;
    	this.form = form;
    	this.logoutUrl = logoutUrl;
        this.defaultPostLoginUrl = defaultPostLoginUrl;
    }
    
    public static <A extends SimpleLogin, B extends ActionAuthenticator> Login<A, B> of(B authenticator, Template2<Form<A>, String, Html> loginTemplate, Form<A> form, String logoutUrl, String defaultPostLoginUrl) {
    	return new Login<A, B>(authenticator, loginTemplate, form, logoutUrl, defaultPostLoginUrl);
    }
	
    public Result login() {
        Form<T> loginForm = form.bindFromRequest();
        if (loginForm.hasErrors()) {
        	return badRequest(loginTemplate.render(loginForm, ActionHelper.getOriginOfCall(ctx())));
        } else {
        	String origin = loginForm.get().getOrigin();
            session().clear();
            authenticator.setTokenInHeader(ctx(), loginForm.get().getToken());
            if (origin != null && !"".equals(origin))
            	return redirect(origin);
            return redirect(defaultPostLoginUrl);
        }
    }
    
    public Result prepareLogin(String originInQueryString) {
        return ok(loginTemplate.render(form, originToUse(originInQueryString)));
    }
    
    private String originToUse(String originInQueryString) {
    	String originToUse = originInQueryString;
    	if (originToUse == null)
    		originToUse = ActionHelper.getOriginOfCall(ctx());
    	return originToUse;
    }
    
    public Result logout() {
        session().clear();
        flash("warning", "You've been logged out");
        return redirect(logoutUrl);
    }
}
