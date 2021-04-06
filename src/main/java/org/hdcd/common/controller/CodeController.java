package org.hdcd.common.controller;

import java.util.List;

import org.hdcd.common.domain.CodeLabelValue;
import org.hdcd.service.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping("/codes")
public class CodeController {
	
	@Autowired
	private CodeService codeService;
	
	@RequestMapping(value = "/codeGroup", method = RequestMethod.GET)
	public ResponseEntity<List<CodeLabelValue>> codeGroupList() throws Exception {
		log.info("codeGroupList");
		
		return new ResponseEntity<>(codeService.getCodeGroupList(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/job", method = RequestMethod.GET)
	public ResponseEntity<List<CodeLabelValue>> jobList() throws Exception {
		log.info("jobList");
		
		String groupCode = "A01";
		List<CodeLabelValue> jobList = codeService.getCodeList(groupCode);
		
		return new ResponseEntity<>(jobList, HttpStatus.OK);
	}
	
}
