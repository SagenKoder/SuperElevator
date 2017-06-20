package org.pzyko.ironelevator;

import java.lang.reflect.Field;

public final class ReflectionUtils {
	// Prevent accidental construction
	private ReflectionUtils() {
	}

	public static void setValue(Object instance, String fieldName, Object value) throws Exception {
		Field field = instance.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(instance, value);
	}

	public static Object getValue(Object instance, String fieldName) throws Exception {
		Field field = instance.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		return field.get(instance);
	}
}
