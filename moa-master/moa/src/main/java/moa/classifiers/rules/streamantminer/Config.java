package moa.classifiers.rules.streamantminer;

import java.util.HashMap;


/**
 * The <code>Config</code> holds all configuration parameters for the execution
 * of an algorithm.
 * 
 * @author Fernando Esteban Barril Otero
 */
public final class Config {
    /**
     * The singleton instance.
     */
    public static final Config CONFIG = new Config();

    /**
     * Mapping of <code>ConfigKey</code> object and values.
     */
    private HashMap<ConfigKey<?>, Object> mapping = new HashMap<>();

    /**
     * Private constructor.
     */
    private Config() {
    }

    /**
     * Sets the specified value to the <code>ConfigKey</code> object.
     * 
     * @param <T>
     *            the type of the key.
     * @param key
     *            the <code>ConfigKey</code> object.
     * @param value
     *            the value to set.
     */
    public <T> void set(ConfigKey<T> key, T value) {
	mapping.put(key, value);
    }

    /**
     * Returns the value associated with the specified <code>ConfigKey</code>
     * object.
     * 
     * @param <T>
     *            the type of the key.
     * @param key
     *            the <code>ConfigKey</code> object.
     * 
     * @return the value associated with the specified <code>ConfigKey</code>
     *         object.
     */
    @SuppressWarnings("unchecked")
    public <T> T get(ConfigKey<T> key) {
	T value = (T) mapping.get(key);

	if (value == null) {
	    throw new IllegalStateException("ConfigKey " + key
		    + " has not been set.");
	}

	return value;
    }

    /**
     * Returns <code>true</code> if the specified <code>ConfigKey</code> object
     * has been set.
     * 
     * @param <T>
     *            the type of the key.
     * @param key
     *            the <code>ConfigKey</code> object.
     * 
     * @return <code>true</code> if the specified <code>ConfigKey</code> object
     *         has been set; <code>false</code> otherwise.
     */
    public <T> boolean isPresent(ConfigKey<T> key) {
	return mapping.containsKey(key);
    }

    /**
     * Struct-like class to represent a configuration key.
     * 
     * @param <T>
     *            The type of objects that can be associated with the key.
     */
    public static final class ConfigKey<T> {
    }
}