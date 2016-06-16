package fr.watchnext.utils.views;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class Selector<T> {
	private List<T> values;
	private Function<T, Object> keysGetter;
	private Function<T, String> displayValuesGetter;
	private List<String> keys;
	private List<String> valuesToDisplay;
	
	private Selector(List<T> values, Function<T, Object> keysGetter, Function<T, String> displayValuesGetter) {
		this.values = values;
		this.keysGetter = keysGetter;
		this.displayValuesGetter = displayValuesGetter;
		this.keys = new ArrayList<>();
		this.valuesToDisplay = new ArrayList<>();
		for(T value : values) {
			keys.add(keysGetter.apply(value).toString());
			valuesToDisplay.add(displayValuesGetter.apply(value));
		}
	}
	
	public static <I> Selector<I> of(List<I> values, Function<I, Object> keysGetter, Function<I, String> displayValuesGetter) {
		Selector<I> selector = new Selector<I>(values, keysGetter, displayValuesGetter);
		return selector;
	}
	
	public static <I> Selector<I> of(Supplier<List<I>> valuesGetter, Function<I, Object> keysGetter, Function<I, String> displayValuesGetter) {
		return of(valuesGetter.get(), keysGetter, displayValuesGetter);
	}
	
	public List<String> getKeys() {
		return keys;
	}
	
	public List<String> getValuesToDisplay() {
		return valuesToDisplay;
	}

}