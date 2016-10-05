package fr.watchnext.utils.models;

public abstract class SimpleLogin {
	public String origin;
	
	public String getOrigin() {
		return origin;
	}
	
	public String validate() {
		if (isAuthorized())
			return null;
		return "auth.failed";
    }
	
	public abstract boolean isAuthorized();
	
	public abstract String getToken();
}
