package moa.classifiers.rules.streamantminer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class ObjectFactory {
    /**
     * Creates a new object instance of the specified class.
     * 
     * @param <T>
     *            the generic type of the class.
     * @param c
     *            the class of the object to be created.
     * 
     * @return an object of the specified class.
     * 
     * @throws IllegalArgumentException
     *             if there is any error instantiating an object of the
     *             specified class.
     */
    public static <T> T create(Class<T> c) {
	try {
	    return c.getDeclaredConstructor().newInstance();
	} catch (Exception e) {
	    throw new IllegalArgumentException(c.getName()
		    + " could not be instantiated", e);
	}
    }

    /**
     * Creates a new object instance of the specified class.
     * 
     * @param <T>
     *            the generic type of the class.
     * @param c
     *            the class of the object to be created.
     * @param types
     *            the constructor parameter's class types.
     * @param parameters
     *            the constructor parameter's values.
     * 
     * @return an object of the specified class.
     * 
     * @throws IllegalArgumentException
     *             if there is any error instantiating an object of the
     *             specified class.
     */
    public static <T> T create(Class<T> c,
			       Class<?>[] types,
			       Object[] parameters) {
	try {
	    Constructor<T> constructor = c.getConstructor(types);
	    return constructor.newInstance(parameters);
	} catch (NoSuchMethodException e) {
	    throw new IllegalArgumentException("Constructor for '" + c.getName()
		    + "' could not be found", e);
	} catch (InstantiationException e) {
	    throw new IllegalArgumentException(c.getName()
		    + "could not be instantiated", e);
	} catch (InvocationTargetException e) {
	    throw new IllegalArgumentException(c.getName()
		    + "could not be instantiated", e);
	} catch (IllegalAccessException e) {
	    throw new IllegalArgumentException(c.getName()
		    + "could not be instantiated", e);
	} catch (NullPointerException e) {
	    throw new IllegalArgumentException("Class name not specified: null",
					       e);
	}
    }
}