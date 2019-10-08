package com.dms.document.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.dms.common.constants.RepositoryConstants;
import com.dms.common.utils.RepositoryUtils;
import com.dms.document.domains.DocumentDomain;

@Repository
public class DocumentRepository {

	@Autowired
	private MongoOperations operations;

	public DocumentDomain findById(String id) {
		Query query = RepositoryUtils.queryForMongoId(id);
		return operations.findOne(query, DocumentDomain.class);
	}

	public List<DocumentDomain> findDocumentsByIds(Set<String> ids) {
		Query query = RepositoryUtils.queryForMongoIds(ids);
		return operations.find(query, DocumentDomain.class);
	}

	public String saveDocument(DocumentDomain document) {
		return operations.save(document).getId().toString();
	}

	public DocumentDomain findByMd5SumInternal(String md5Sum) {
		Query query = Query.query(Criteria.where(RepositoryConstants.DOCUMENT_MD5SUM_INTERNAL).is(md5Sum));
		return operations.findOne(query, DocumentDomain.class);
	}

	public List<DocumentDomain> findDocumentsByUser(String user) {
		Query query = Query.query(Criteria.where(RepositoryConstants.CREATED_BY).is(user));
		return operations.find(query, DocumentDomain.class);
	}

	public void updateS3Details(DocumentDomain document) {
		Query query = RepositoryUtils.queryForMongoId(document.getId());
		Update update = new Update();
		update.set(RepositoryConstants.DOCUMENT_S3_NAME, document.getS3Name());
		update.set(RepositoryConstants.DOCUMENT_S3_BUCKET, document.getS3Bucket());
		update.set(RepositoryConstants.DOCUMENT_MD5SUM_EXTERNAL, document.getMd5SumExternal());
		operations.updateFirst(query, update, DocumentDomain.class);
	}

	public void updateTextractDetails(DocumentDomain document) {
		Query query = RepositoryUtils.queryForMongoId(document.getId());
		Update update = new Update();
		update.set(RepositoryConstants.DOCUMENT_TEXTRACT_DETECT, document.getDetectingTextResult());
		update.set(RepositoryConstants.DOCUMENT_TEXTRACT_ANALYSE, document.getAnalyzingTextResult());
		operations.updateFirst(query, update, DocumentDomain.class);

	}

}
