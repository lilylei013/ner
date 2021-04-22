package com.advs.train.service;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.advs.train.dao.NERModelInfoMapper;
import com.advs.train.model.NERModelInfo;

@Service
public class NERModelService {
	@Autowired
	NERModelInfoMapper nerModelDao;
	private static final Logger mLog = Logger.getLogger(NERModelService.class);
	
	
	public NERModelInfo createAModel(String modelName, String config) {
		String newModelName = modelName;
		if(!newModelName.endsWith(".ser.gz")) {
			newModelName += ".ser.gz";
		}
		ToolUtils.genNewModel(newModelName);
		NERModelInfo nerModelInfo = new NERModelInfo();
		nerModelInfo.setId(ToolUtils.getUUID_8());
		nerModelInfo.setConfig(config);
		Date now = new Date();
		nerModelInfo.setCreateDate(now);
		nerModelInfo.setCreateUser("admin");
		nerModelInfo.setLocation("/home/models/"+newModelName);
		nerModelDao.insert(nerModelInfo);
		return nerModelInfo;
	}

	
	public List<NERModelInfo> getAllNERModel(){
		return nerModelDao.selectAll();
	}
}
