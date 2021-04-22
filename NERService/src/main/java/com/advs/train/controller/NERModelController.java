package com.advs.train.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.advs.train.model.NERModelInfo;
import com.advs.train.service.NERModelService;

@RestController
@RequestMapping("/ner-model")
public class NERModelController {
	@Autowired
	NERModelService nerModelService;
	
	@PostMapping
	public NERModelInfo addOne(String modelName, String description) {
		return nerModelService.createAModel(modelName, description);
	}
	
	@GetMapping
	public List<NERModelInfo> getAll(){
		return nerModelService.getAllNERModel();
	}

}
