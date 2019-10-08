package com.dms.document.general;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.dms.common.constants.RepositoryConstants;
import com.dms.common.utils.RepositoryUtils;

@Repository
public class GeneralRepository {

	private static final String SEQUENCE = "sequence";

	@Autowired
	private MongoOperations operations;

	public List<MetaDataDomain> findAllMetaData() {
		return operations.findAll(MetaDataDomain.class);
	}

	public long nextSequenceId(SequenceConstants sequenceId, Number inc) {
		Query query = RepositoryUtils.queryForMongoId(sequenceId.toString());
		Update update = new Update().inc(SEQUENCE, inc);
		FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true);
		return operations.findAndModify(query, update, options, SequenceDomain.class).getSequence();
	}

	public void saveMetaData(MetaDataDomain metaDataDomain) {
		operations.save(metaDataDomain);
	}

	public void updateMetaData(MetaDataConstants id, String key, String value) {
		MetaDataDomain metaDataDomain = operations.findOne(RepositoryUtils.queryForMongoId(id.toString()), MetaDataDomain.class);
		Map<String, Object> objectMap = metaDataDomain.getObjectMap();
		objectMap.put(key, value);
		Update update = new Update();
		update.set(RepositoryConstants.OBJECT_MAP, objectMap);
		operations.updateFirst(RepositoryUtils.queryForMongoId(id.toString()), update, MetaDataDomain.class);
	}

}