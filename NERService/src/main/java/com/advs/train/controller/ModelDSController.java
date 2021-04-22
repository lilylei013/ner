package com.advs.train.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.advs.train.model.ModelDSInfo;
import com.advs.train.model.TamaleModel;
import com.advs.train.service.ModelDSService;

@RestController
@RequestMapping("/entity-datasource")
public class ModelDSController {
	@Autowired ModelDSService modelDSService;
	
	
	@PostMapping("tamale")
	public ModelDSInfo addOne(TamaleModel model) {
		return modelDSService.createATamaleModelDS(model);
	}
	
	@PostMapping("manual")
	public ModelDSInfo addOne(String entitys) {
		return modelDSService.createAManualModelDS(entitys);
	}
	
	@PostMapping("manual2")
	public ModelDSInfo addOne(String[] entityList) {
		return modelDSService.createAManualModelDS(entityList);
	}
	
	@GetMapping
	public List<ModelDSInfo> getEntityList() {
		return modelDSService.getAllModelDS();
	}

}
