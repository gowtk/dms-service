package com.dms.document.general;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MetaDataService {

	private static final Logger logger = LoggerFactory.getLogger(MetaDataService.class);

	@Autowired
	private GeneralRepository generalRepository;

	@Autowired
	private ObjectMapper mapper;
	private Map<MetaDataConstants, String> constant;
	private Map<MetaDataConstants, Map<String, Object>> objectMap;
	private boolean loaded = false;

	public String getConstant(MetaDataConstants key) {
		loadMetaDataValues();
		return constant.get(key);
	}

	public Map<String, Object> getMap(MetaDataConstants key) {
		loadMetaDataValues();
		return objectMap.get(key);
	}

	public Map<String, Object> getMap(MetaDataConstants key, Map<String, Object> defaultMap) {
		if (objectMap == null) {
			return defaultMap;
		}
		return objectMap.getOrDefault(key, defaultMap);
	}

	private synchronized void loadMetaDataValues() {
		if (loaded) {
			return;
		} else {
			refreshMetaDataValues();
		}
	}

	public void refreshMetaDataValues() {

		List<MetaDataDomain> metaDataList = generalRepository.findAllMetaData();
		constant = new HashMap<>();
		objectMap = new HashMap<>();

		// Reset metadata
		for (MetaDataDomain metaData : metaDataList) {
			assignMetaDataValue(metaData);
		}

		loaded = true;
		logger.info("All MetaData values are refreshed");
	}

	private void assignMetaDataValue(MetaDataDomain metaData) {
		try {
			if (null != metaData.getConstant()) {
				constant.put(MetaDataConstants.valueOf(metaData.getId()), metaData.getConstant());
				logger.info("{} loaded successfully", metaData);

			} else if (null != metaData.getObjectMap()) {
				objectMap.put(MetaDataConstants.valueOf(metaData.getId()), metaData.getObjectMap());
				logger.info("{} loaded successfully", metaData);

			} else {
				logger.info("{} loading failed.", metaData);
			}

		} catch (Exception e) {
			logger.error("Exception in parsing {}", metaData);
		}
	}

}
