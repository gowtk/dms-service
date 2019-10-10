package com.dms.document.repositories;

import java.io.IOException;
import java.io.InputStream;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.dms.common.utils.RepositoryUtils;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;

@Repository
public class FilesRepository {

	@Autowired
	private GridFsOperations operations;

	@Autowired
	private MongoDbFactory dbFactory;

	public String uploadFile(MultipartFile file) throws IOException {
		return saveFile(file.getInputStream(), file.getOriginalFilename(), file.getContentType(), null);
	}

	public String saveFile(InputStream content, String filename, String contentType, Document document) {
		return (operations.store(content, filename, contentType, document)).toString();
	}

	public void deleteFile(String fileId) {
		operations.delete(RepositoryUtils.queryForMongoId(fileId));
	}

	public GridFsResource findResource(String fileId) {
		GridFSFile file = operations.findOne(RepositoryUtils.queryForMongoId(fileId));
		if (file != null) {
			GridFSBucket bucket = GridFSBuckets.create(dbFactory.getDb());
			GridFSDownloadStream stream = bucket.openDownloadStream(file.getObjectId());
			return new GridFsResource(file, stream);
		}
		return null;
	}
}
