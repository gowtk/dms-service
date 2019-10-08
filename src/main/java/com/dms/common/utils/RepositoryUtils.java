package com.dms.common.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Collation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.dms.common.constants.FileStorageType;
import com.dms.common.constants.RepositoryConstants;

public final class RepositoryUtils {

	private RepositoryUtils() {

	}

	public static Criteria frameExactSort(String uiQuery, Map<String, String> repoKeys) {

		if (StringUtils.isValid(uiQuery)) {
			try {
				String[] parts = uiQuery.split(",");
				String repoValue = parts[1];
				String repoKey = repoKeys.get(parts[0]);
				return Criteria.where(repoKey).is(repoValue);
			} catch (Exception e) {
				return null;
			}
		} else {
			return null;
		}

	}

	public static Collation englishCollation() {
		return Collation.of("en").strength(Collation.ComparisonLevel.secondary());
	}

	public static Criteria regexCriteria(String name, String searchText) {
		return Criteria.where(name).regex(searchText, "i");
	}

	private static Sort frameSortingQuery(Sort sort, Map<String, String> repoKeys) {
		List<Order> orders = new ArrayList<>();
		sort.forEach(value -> {
			String property = repoKeys.get(value.getProperty());
			if (StringUtils.isValid(property)) {
				orders.add(new Order(value.getDirection(), property));
			}
		});
		return new Sort(orders);
	}

	public static Query framePageableSearchQuery(Pageable pageable, Criteria criteria, Map<String, String> repositoryKeys) {
		if (null != pageable) {
			return Query.query(criteria).with(frameSortingQuery(pageable.getSort(), repositoryKeys)).skip(pageable.getOffset()).limit(pageable.getPageSize())
					.collation(englishCollation());
		} else {
			return Query.query(criteria);
		}

	}

	public static Criteria frameSearchCriteria(String searchText) {
		if (StringUtils.isValid(searchText)) {
			return regexCriteria(RepositoryConstants.KEYWORDS, searchText);
		} else {
			return new Criteria();
		}
	}

	public static Criteria frameSearchCriteriaWithKey(String key, String searchText) {
		if (StringUtils.isValid(searchText) && StringUtils.isValid(key)) {
			return regexCriteria(key, searchText);
		} else {
			return new Criteria();
		}
	}

	public static Query queryForMongoId(String mongoId) {
		return Query.query(Criteria.where(RepositoryConstants.MONGO_ID).is(mongoId));
	}

	public static Query queryForMongoIds(Collection<String> mongoIds) {
		return Query.query(Criteria.where(RepositoryConstants.MONGO_ID).in(mongoIds));
	}

	public static String appendeStorageType(String fileId, FileStorageType type) {
		return fileId + type.getDbName();
	}

	public static String removeStorageType(String fileId, FileStorageType type) {
		return fileId.replace(fileId, type.getDbName());
	}

}
