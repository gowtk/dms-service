package com.dms.document.services;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.stereotype.Service;

import com.amazonaws.services.textract.AmazonTextract;
import com.amazonaws.services.textract.model.DetectDocumentTextRequest;
import com.amazonaws.services.textract.model.DetectDocumentTextResult;
import com.amazonaws.services.textract.model.Document;
import com.dms.AwsAuth;
import com.dms.document.domains.DocumentDomain;
import com.dms.document.domains.TextractResults;
import com.dms.document.repositories.DocumentRepository;
import com.dms.document.repositories.FilesRepository;

@Service
public class AwsTextractService {

	@Autowired
	private AwsAuth authService;

	@Autowired
	private DocumentRepository docsRepo;

	@Autowired
	private FilesRepository filesRepo;

	private static final Logger logger = LoggerFactory.getLogger(AwsTextractService.class);

	public void parseDocument(String documentId) {
		DocumentDomain doc = docsRepo.findById(documentId);
		GridFsResource file = filesRepo.findResource(doc.getFileId());
		if ("application/pdf".equals(file.getContentType())) {
			try {
				PDDocument document = PDDocument.load(file.getInputStream());
				int numberOfPages = document.getNumberOfPages();
				logger.info("PDF file converted to image of page size : {}", numberOfPages);
				PDFRenderer renderer = new PDFRenderer(document);
				List<TextractResults> textractResults = new ArrayList<>(numberOfPages);
				AmazonTextract textract = authService.initiateTextractClient();
				for (int i = 0; i < numberOfPages; i++) {
					BufferedImage image = renderer.renderImage(i, 10, ImageType.BINARY);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write(image, "jpg", baos);
					baos.flush();
					ByteBuffer bytes = ByteBuffer.wrap(baos.toByteArray());
					baos.close();
					DetectDocumentTextRequest request = new DetectDocumentTextRequest().withDocument(new Document().withBytes(bytes));
					DetectDocumentTextResult result = textract.detectDocumentText(request);
					textractResults.add(new TextractResults(result.getBlocks(), i + 1));
					logger.info("Textract Parsed file indx : {}", i);
				}
				authService.closeTextractClient(textract);
				document.close();
				doc.setNumberOfPages(numberOfPages);
				doc.setTextractResults(textractResults);
				docsRepo.updateTextractDetails(doc);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
