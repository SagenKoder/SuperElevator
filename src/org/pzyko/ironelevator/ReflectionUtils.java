package org.pzyko.ironelevator;

import java.lang.reflect.Field;

/**
 * <b>ReflectionUtils</b>
 * <p>
 * This class provides useful methods which makes dealing with reflection much
 * easier, especially when working with Bukkit
 * <p>
 * You are welcome to use it, modify it and redistribute it under the following
 * conditions:
 * <ul>
 * <li>Don't claim this class as your own
 * <li>Don't remove this disclaimer
 * </ul>
 * <p>
 * <i>It would be nice if you provide credit to me if you use this class in a
 * published project</i>
 * 
 * @author DarkBlade12
 * @version 1.1
 */
public final class ReflectionUtils {
	// Prevent accidental construction
	private ReflectionUtils() {
	}

	/**
	 * sets a value of an {@link Object} via reflection
	 *
	 * @param instance instance the class to use
	 * @param fieldName the name of the {@link Field} to modify
	 * @param value the value to set
	 * @throws Exception
	 */
	public static void setValue(Object instance, String fieldName, Object value) throws Exception {
		Field field = instance.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(instance, value);
	}

	/**
	 * get a value of an {@link Object}'s {@link Field}
	 *
	 * @param instance the target {@link Object}
	 * @param fieldName name of the {@link Field}
	 * @return the value of {@link Object} instance's {@link Field} with the
	 *        name of fieldName
	 * @throws Exception
	 */
	public static Object getValue(Object instance, String fieldName) throws Exception {
		Field field = instance.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		return field.get(instance);
	}
}