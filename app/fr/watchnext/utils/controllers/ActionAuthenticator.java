package fr.watchnext.utils.controllers;

import play.Logger;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

public abstract class ActionAuthenticator extends Security.Authenticator {
	private static String TOKEN_HEADER = "X-AUTH-TOKEN";
	
    @Override
    public String getUsername(Http.Context ctx) {
    	Logger.info("Getting username in ActionAuthenticator");
        String token = getTokenFromHeader(ctx);
        if (token != null) {
        	String username = retrieveUserNameByToken(token);
            if (username != null) {
                return username;
            }
        } else {
        	Logger.info("Token is null");
        }
        return null;
    }

    @Override
    public Result onUnauthorized(Http.Context context) {
        return super.onUnauthorized(context);
    }

    private String getTokenFromHeader(Http.Context ctx) {
        return ctx.session().get(TOKEN_HEADER);
    }
    
    protected void setTokenInHeader(Http.Context ctx, String token) {
    	ctx.session().put(TOKEN_HEADER, token);
    }
    
    public abstract String retrieveUserNameByToken(String token);
}