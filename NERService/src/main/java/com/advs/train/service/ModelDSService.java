package com.advs.train.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.advs.train.core.Analyzer;
import com.advs.train.dao.ModelDSInfoMapper;
import com.advs.train.model.ModelDSInfo;
import com.advs.train.model.TamaleModel;
import com.google.gson.Gson;

@Service
public class ModelDSService {
	@Autowired
	ModelDSInfoMapper modelDSDao;
	private static final Logger mLog = Logger.getLogger(ModelDSService.class);

	public ModelDSInfo createATamaleModelDS(TamaleModel tamaleModel) {
		String location = createATamaleModelDS(tamaleModel.getUser(), tamaleModel.getPassword(), tamaleModel.getIp(),
				tamaleModel.getEntrytype());
		ModelDSInfo modelDSInfo = new ModelDSInfo();
		modelDSInfo.setId(ToolUtils.getUUID_8());
		modelDSInfo.setConfig(new Gson().toJson(tamaleModel));
		modelDSInfo.setLocation(location);
		Date now = new Date();
		modelDSInfo.setCreateDate(now);
		modelDSInfo.setCreateUser(tamaleModel.getUser());
		modelDSInfo.setName(tamaleModel.getIp());
		modelDSDao.insert(modelDSInfo);
		return modelDSInfo;
	}

	private String createATamaleModelDS(String user, String password, String ip, String entrytype) {
		String baseUrl = "https://" + ip;
		Analyzer.setBaseUrl(baseUrl);
		String apiUrl = baseUrl + "/restapi/2.1/entry-type/?getdefaulttype=true&entryclass=note&outputformat=json";
		String response = Analyzer.getResponse(apiUrl);
		if (response == null) {
			System.out.println("Wrong user name or password!");
			return "";
		}

		String pathname = "/tmp/ner/tokenized_" + ip;
		for (String entryType : StringUtils.split(entrytype, ",")) {
			Analyzer.processEntryType(entryType, pathname);
		}
		return pathname;
	}

	public List<ModelDSInfo> getAllModelDS() {
		return modelDSDao.selectAll();
	}

	public ModelDSInfo createAManualModelDS(String entitys) {
		String location = createManualModelDS(entitys);
		ModelDSInfo modelDSInfo = new ModelDSInfo();
		modelDSInfo.setId(ToolUtils.getUUID_8());
		modelDSInfo.setConfig(entitys);
		modelDSInfo.setLocation(location);
		Date now = new Date();
		modelDSInfo.setCreateDate(now);
		modelDSInfo.setCreateUser("admin");
		modelDSInfo.setName("manual");
		modelDSDao.insert(modelDSInfo);
		return modelDSInfo;
	}
	
	public ModelDSInfo createAManualModelDS(String[] entitys) {
		String location = createManualModelDS(entitys);
		ModelDSInfo modelDSInfo = new ModelDSInfo();
		modelDSInfo.setId(ToolUtils.getUUID_8());
		modelDSInfo.setConfig(new Gson().toJson(entitys));
		modelDSInfo.setLocation(location);
		Date now = new Date();
		modelDSInfo.setCreateDate(now);
		modelDSInfo.setCreateUser("admin");
		modelDSInfo.setName("manual");
		modelDSDao.insert(modelDSInfo);
		return modelDSInfo;
	}

	private String createManualModelDS(String entitys) {
		String pathname = "/tmp/ner/tokenized_manual";

		List<String> l = new ArrayList<>();
		for (String entity : StringUtils.split(entitys, ",")) {
			for (String t : StringUtils.split(entity, " ")) {
				l.add(t + "\tORGANIZATION");
				l.add(t.toLowerCase() + "\tORGANIZATION");
				l.add(t.toUpperCase() + "\tORGANIZATION");
			}
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
		String now = df.format(new Date());

		File folder = new File(pathname);
		if (!folder.exists()) {
			mLog.info("Creating a folder for tokenization: " + pathname);
			boolean success = folder.mkdir();
			if (!success) {
				mLog.error("Failed to create folder: " + pathname);
				System.exit(-1);
			}
		}
		try (PrintWriter writer = new PrintWriter(folder.getAbsolutePath() + "/" + now + ".txt", "UTF-8")) {
			for (String line : l) {
				writer.println(line);
			}
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			mLog.error("Unable to write file: " + pathname, e);
		}

		return pathname;

	}

	public String createManualModelDS(String[] entityList) {
		String pathname = "/tmp/ner/tokenized_manual";

		List<String> l = new ArrayList<>();
		for (String entity : entityList) {
			for (String t : StringUtils.split(entity, " ")) {
				l.add(t + "\tORGANIZATION");
				l.add(t.toLowerCase() + "\tORGANIZATION");
				l.add(t.toUpperCase() + "\tORGANIZATION");
			}

		}
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
		String now = df.format(new Date());

		File folder = new File(pathname);
		if (!folder.exists()) {
			mLog.info("Creating a folder for tokenization: " + pathname);
			boolean success = folder.mkdir();
			if (!success) {
				mLog.error("Failed to create folder: " + pathname);
				System.exit(-1);
			}
		}
		String file = folder.getAbsolutePath() + "/" + now + ".txt";
		try (PrintWriter writer = new PrintWriter(file, "UTF-8")) {
			for (String line : l) {
				writer.println(line);
			}
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			mLog.error("Unable to write file: " + pathname, e);
		}

		return file;
	}

}
