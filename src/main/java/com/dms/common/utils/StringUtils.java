package com.dms.common.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.springframework.lang.Nullable;

public final class StringUtils {

	public static final String SPACE_STRING = " ";
	public static final String EMPTY_STRING = "";
	public static final String DOT = ".";

	public static boolean validBoolean(Boolean str, boolean defaultValue) {
		return str == null ? defaultValue : str;
	}

	public static boolean isValid(@Nullable String str) {
		return str != null && !(str.trim()).isEmpty();
	}

	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	public static boolean isEmptyOrNullString(@Nullable String str) {
		return str == null || str.length() == 0 || str.equals("null");
	}

	public static boolean isNotBlank(String str) {
		return !StringUtils.isBlank(str);
	}

	public static boolean isBlank(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(str.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

	public static boolean equals(String str1, String str2) {
		return str1 == null ? str2 == null : str1.equals(str2);
	}

	public static String trim(String str) {
		return str == null ? null : str.trim();
	}

	public static String joinWithEmptySpace(@Nullable String str1, @Nullable String str2) {
		return joinWithString(str1, str2, StringUtils.SPACE_STRING);
	}

	public static String joinFirstCharacters(@Nullable String str1, @Nullable String str2) {
		StringBuilder sb = new StringBuilder(2);
		if (isValid(str1)) {
			sb.append(str1.substring(0, 1));
		}
		if (isValid(str2)) {
			sb.append(str2.substring(0, 1));
		}
		return sb.toString().toUpperCase();
	}

	public static String joinWithString(@Nullable String str1, @Nullable String str2, String split) {
		StringBuilder sb = new StringBuilder(20);
		if (isValid(str1)) {
			sb.append(str1);
		} else if (isValid(str2)) {
			sb.append(str2);
			return sb.toString();
		}
		if (isValid(str2)) {
			sb.append(split);
			sb.append(str2);
		}
		return sb.toString();
	}

	public static String removeSpaces(String value) {
		return null == value ? EMPTY_STRING : value.replaceAll("\\s+", EMPTY_STRING);
	}

	public static List<String> split(String value, String regex) {
		return isEmpty(value) ? Collections.emptyList() : Arrays.asList(value.split(regex));
	}

	public static String toHexString(byte[] array) {
		return DatatypeConverter.printHexBinary(array);
	}

	public static byte[] toByteArray(String s) {
		return DatatypeConverter.parseHexBinary(s);
	}

	public static String parseString(Object object, String defaultValue) {
		return null == object ? defaultValue : object.toString();
	}

	public static String numberToString(Integer number, String defaultValue) {
		return null == number ? defaultValue : number.toString();
	}

	public static boolean parseBoolean(Object object, boolean defaultValue) {
		return null == object ? defaultValue : (Boolean) object;
	}

	public static String fileExtension(String fileName) {
		try {
			return fileName.split("\\.")[1];
		} catch (Exception e) {
			return null;
		}
	}

}
