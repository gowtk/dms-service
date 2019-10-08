package com.dms.common.utils;

public final class NumberUtils {

	private NumberUtils() {
	}

	public static Double checkValidDouble(String s) {
		try {
			return null == s ? 0.0 : Double.valueOf(s);
		} catch (NumberFormatException e) {
			return 0.0;
		}
	}

	public static Integer checkValidInteger(String s) {
		try {
			return null == s ? 0 : Integer.valueOf(s);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public static Integer checkValidInteger(Integer i) {
		return checkValidInteger(i, 0);
	}

	public static Integer checkValidInteger(Integer i, int defaultValue) {
		return i == null ? defaultValue : i;
	}

	public static Integer checkPositiveInteger(Integer i, int defaultValue) {
		return i == null || i <= 0 ? defaultValue : i;
	}

	public static String formatDouble(double d) {
		if (d == (long) d)
			return String.format("%d", (long) d);
		else
			return String.format("%s", d);
	}

	public static boolean validatePositiveDouble(Double d) {
		if (null != d && d > 0.0) {
			return true;
		}
		return false;
	}

}
