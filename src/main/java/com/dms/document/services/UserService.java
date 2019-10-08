package com.dms.document.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import com.dms.common.constants.FileStorageType;
import com.dms.common.utils.CollectionUtils;
import com.dms.document.domains.DocumentDomain;
import com.dms.document.domains.UserDomain;
import com.dms.document.repositories.DocumentRepository;
import com.dms.document.repositories.UserRepository;
import com.dms.document.views.FileListView;
import com.dms.document.views.UserDetailsView;
import com.dms.web.beans.ListView;
import com.dms.web.beans.Response;
import com.dms.web.beans.Status;
import com.dms.web.beans.Toast;
import com.dms.web.beans.Toastr;

@Service
public class UserService {

	private static final Logger logger = LoggerFactory.getLogger(AwsS3Service.class);

	@Autowired
	private DocumentRepository docsRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private AwsS3Service s3Service;

	@Autowired
	private AwsTextractService textractService;

	public Response<UserDetailsView> viewUserDetails(String username) {

		UserDomain user = userRepo.findByUsername(username);
		if (user != null) {
			UserDetailsView data = new UserDetailsView();
			data.setUsername(username);
			data.setFilesList(prepareFileListView(user));
			return new Response<UserDetailsView>(data);
		} else {
			return new Response<>(Status.nodata, "User not available");
		}

	}

	public Response<UserDetailsView> uploadFiles(MultipartFile file, String username) {
		try {
			if (null != file) {
				String md5Sum = DigestUtils.md5DigestAsHex(file.getBytes());
				DocumentDomain document = docsRepo.findByMd5SumInternal(md5Sum);
				if (null == document) {
					document = createNewDocument(username, md5Sum, file);
					s3Service.uploadFile(document, file);
					textractService.parseDocument(username, document.getId());
				}
				updateUserDetails(username, document);
			}

			Response<UserDetailsView> userDetailsView = viewUserDetails(username);
			userDetailsView.setToast(new Toastr(Toast.success, "Document Upload", "Document upload success!"));
			logger.info("uploadFiles success");
			return userDetailsView;
		} catch (Exception e) {
			logger.error("findMd5Sum", e);
			Response<UserDetailsView> userDetailsView = viewUserDetails(username);
			userDetailsView.setToast(new Toastr(Toast.error, "Document Upload", "Document upload failed!"));
			return userDetailsView;
		}
	}

	private void updateUserDetails(String username, DocumentDomain document) {
		UserDomain user = userRepo.findByUsername(username);
		if (user == null) {
			user = new UserDomain();
			user.setUsername(username);
		}
		Set<String> documents = user.getDocuments();
		if (CollectionUtils.isValid(documents)) {
			documents.add(document.getId());
		} else {
			documents = new HashSet<>(1);
			documents.add(document.getId());
		}
		userRepo.saveUser(user);
	}

	private DocumentDomain createNewDocument(String user, String md5Sum, MultipartFile file) {
		DocumentDomain document = new DocumentDomain();
		document.setStorage(FileStorageType.AWSS3);
		document.setFileName(file.getOriginalFilename());
		document.setContentType(file.getContentType());
		document.setFileSize(file.getSize());
		document.setMd5SumInternal(md5Sum);
		document.setCreatedBy(user);
		String id = docsRepo.saveDocument(document);
		document.setId(id);
		return document;
	}

	private ListView<FileListView> prepareFileListView(UserDomain user) {
		List<DocumentDomain> documents = docsRepo.findDocumentsByIds(user.getDocuments());
		List<FileListView> listView = new ArrayList<FileListView>(documents.size());
		for (DocumentDomain document : documents) {
			listView.add(new FileListView(document));
		}
		return new ListView<FileListView>(listView, listView.size(), null, FileListView.uiColumns, FileListView.uiSort);
	}

}
