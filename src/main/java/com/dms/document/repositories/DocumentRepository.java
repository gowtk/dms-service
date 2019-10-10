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

	public DocumentDomain findByMd5(String md5) {
		Query query = Query.query(Criteria.where(RepositoryConstants.DOCUMENT_MD5).is(md5));
		return operations.findOne(query, DocumentDomain.class);
	}

	public List<DocumentDomain> findDocumentsByUser(String user) {
		Query query = Query.query(Criteria.where(RepositoryConstants.CREATED_BY).is(user));
		return operations.find(query, DocumentDomain.class);
	}

	public void updateGridFsDetails(DocumentDomain document) {
		Query query = RepositoryUtils.queryForMongoId(document.getId());
		Update update = new Update();
		update.set(RepositoryConstants.DOCUMENT_FILE_ID, document.getFileId());
		operations.updateFirst(query, update, DocumentDomain.class);
	}

	public void updateTextractDetails(DocumentDomain document) {
		Query query = RepositoryUtils.queryForMongoId(document.getId());
		Update update = new Update();
		update.set(RepositoryConstants.DOCUMENT_FILE_NO_OF_PAGES, document.getNumberOfPages());
		update.set(RepositoryConstants.DOCUMENT_TEXTRACT_RESULTS, document.getTextractResults());
		operations.updateFirst(query, update, DocumentDomain.class);
	}

}
