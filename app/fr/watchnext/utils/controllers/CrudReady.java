package fr.watchnext.utils.controllers;

import java.lang.reflect.Field;

import play.data.Form;

import com.avaje.ebean.Model;
import com.avaje.ebean.PagedList;

public interface CrudReady<T, F> {
	@SuppressWarnings("unchecked")
	default T getInstance() {
		return (T) this;
	}
	
	default Long getId() {
		try {
		    Class<?> c = this.getClass();
		    Field idField = c.getDeclaredField("id");
		    return (Long) idField.get(this);
		} catch (NoSuchFieldException x) {
		    x.printStackTrace();
		} catch (IllegalAccessException x) {
		    x.printStackTrace();
		}
		return null;
	}
	
	Model.Finder<String, T> getFinder();
	
	PagedList<T> getPage(int page, int pageSize, String sortBy, String order, String filter);
	
	@SuppressWarnings("unchecked")
	default Form<F> fillForm(Form<F> form, boolean forceFillingFromInstance) {
		if (form.getClass().isInstance(Form.form(this.getClass()))) {
			Form<T> currentForm = (Form<T>) form;
			if (forceFillingFromInstance == false && form.value().isDefined())
				return form;
			return (Form<F>) currentForm.fill(this.getInstance());
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	default Form<F> fillForm(Form<F> form) {
		if (form.getClass().isInstance(Form.form(this.getClass()))) {
			Form<T> currentForm = (Form<T>) form;
			return (Form<F>) currentForm.fill(this.getInstance());
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	default T getInstanceFromForm(Form<F> form) {
		if (form.getClass().isInstance(Form.form(this.getClass())))
			return ((Form<T>) form).get();
		return null;
	}
	
	
	
}
