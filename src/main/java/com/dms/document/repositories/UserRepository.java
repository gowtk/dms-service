package com.dms.document.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.dms.common.constants.RepositoryConstants;
import com.dms.common.utils.RepositoryUtils;
import com.dms.document.domains.UserDomain;

@Repository
public class UserRepository {

	@Autowired
	private MongoOperations operations;

	public UserDomain findById(String id) {
		Query query = RepositoryUtils.queryForMongoId(id);
		return operations.findOne(query, UserDomain.class);
	}

	public UserDomain findByUsername(String username) {
		Query query = Query.query(Criteria.where(RepositoryConstants.USER_USERNAME).is(username));
		return operations.findOne(query, UserDomain.class);
	}

	public String saveUser(UserDomain user) {
		return operations.save(user).getId().toString();
	}

}
