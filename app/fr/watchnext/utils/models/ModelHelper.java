package fr.watchnext.utils.models;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.avaje.ebean.Expr;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Model;
import com.avaje.ebean.PagedList;

public class ModelHelper {
	
	public static <T> Optional<T> toOptional(T nullableValue) {
    	if (nullableValue == null)
    		return Optional.empty();
        return Optional.of(nullableValue);
	}
	
	public static <T> List<T> notNullList(List<T> nullableValue) {
    	if (nullableValue == null)
    		return new ArrayList<T>();
        return nullableValue;
	}
	
    public static <T> PagedList<T> pageDisjunction(Model.Finder<String,T> find, int page, int pageSize, String sortBy, String order, String filter, List<String> fields, String defaultOrderClause) {
        return 
        		orClause(find.where(), filter, fields)
                .orderBy(getOrderClause(sortBy, order, defaultOrderClause))
                .findPagedList(page, pageSize);
    }
    
    public static <T> PagedList<T> pageConjunction(Model.Finder<String,T> find, int page, int pageSize, String sortBy, String order, String filter, List<String> fields, String defaultOrderClause) {
        return 
        		andClause(find.where(), filter, fields)
                .orderBy(getOrderClause(sortBy, order, defaultOrderClause))
                .findPagedList(page, pageSize);
    }
    
    private static <T> ExpressionList<T> andClause(ExpressionList<T> startClause, String filter, List<String> fields) {
    	return addToClause(startClause.conjunction(), filter, fields);
    }
    
    private static <T> ExpressionList<T> orClause(ExpressionList<T> startClause, String filter, List<String> fields) {
    	return addToClause(startClause.disjunction(), filter, fields);
    }
    
    private static <T> ExpressionList<T> addToClause(ExpressionList<T> intermediateClause, String filter, List<String> fields) {
    	ExpressionList<T> buildingClause = intermediateClause;
    	for(String field : fields)
    		buildingClause = buildingClause.add(Expr.ilike(field, "%"+filter+"%"));
    	return buildingClause;
    }
    
    private static String getOrderClause(String sortBy, String order, String defaultClause) {
    	if (isNotNullNOrEmpty(sortBy) && isNotNullNOrEmpty(order))
    		if (order.toLowerCase().equals("desc") || order.toLowerCase().equals("asc"))
    			return sortBy+ " " + order;
    	return defaultClause;
    }
    
    private static boolean isNullOrEmpty(String valueToTest) {
    	return valueToTest == null || "".equals(valueToTest);
    }
    
    private static boolean isNotNullNOrEmpty(String valueToTest) {
    	return ! isNullOrEmpty(valueToTest);
    }
}
