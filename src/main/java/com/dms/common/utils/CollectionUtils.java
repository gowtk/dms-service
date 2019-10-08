package com.dms.common.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class CollectionUtils {

	private CollectionUtils() {
	}

	public static boolean isEmpty(Collection<?> collection) {
		return (collection == null || collection.isEmpty());
	}

	public static boolean isEmpty(Map<?, ?> map) {
		return (map == null || map.isEmpty());
	}

	public static boolean isValid(Collection<?> collection) {
		return !isEmpty(collection);
	}

	public static boolean isValid(Map<?, ?> map) {
		return !isEmpty(map);
	}

	public static boolean containsAny(Collection<?> source, Collection<?> candidates) {
		if (isEmpty(source) || isEmpty(candidates)) {
			return false;
		}
		for (Object candidate : candidates) {
			if (source.contains(candidate)) {
				return true;
			}
		}
		return false;
	}

	public static final <T> List<T> emptyList() {
		return Collections.emptyList();
	}

	public static final <T> Set<T> emptySet() {
		return Collections.emptySet();
	}

	public static final <K, V> Map<K, V> emptyMap() {
		return Collections.emptyMap();

	}

	public static <T> List<T> emptyList(List<T> collection) {
		return null == collection ? Collections.emptyList() : collection;
	}

	public static <T> List<T> addList(List<T> l1, List<T> l2) {
		if (null != l1) {
			if (null != l2) {
				l1.addAll(l2);
			}
			return l1;
		} else if (null != l2) {
			return l2;
		} else {
			return null;
		}
	}

}
