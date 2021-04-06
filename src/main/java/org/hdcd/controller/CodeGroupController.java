package org.hdcd.controller;

import java.util.List;

import org.hdcd.domain.CodeGroup;
import org.hdcd.service.CodeGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.java.Log;

@Log
@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/codegroups")
public class CodeGroupController {
	
	@Autowired
	private CodeGroupService service;
	
	@RequestMapping(value = "/{groupCode}", method = RequestMethod.GET)
	public ResponseEntity<CodeGroup> read(@PathVariable("groupCode") String groupCode) throws Exception {
		CodeGroup codeGroup = service.read(groupCode);
			
		return new ResponseEntity<>(codeGroup, HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<List<CodeGroup>> list() throws Exception {
		log.info("list");
		
		return new ResponseEntity<>(service.list(), HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<CodeGroup> register(@Validated @RequestBody CodeGroup codeGroup) throws Exception {
		log.info("register");
		
		service.register(codeGroup);
		
		log.info("register codeGroup.getGroupCode() = " + codeGroup.getGroupCode());
		
		return new ResponseEntity<>(codeGroup, HttpStatus.OK);
	}

	@RequestMapping(value = "/{groupCode}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> remove(@PathVariable("groupCode") String groupCode) throws Exception {
		service.remove(groupCode);

		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/{groupCode}", method = RequestMethod.PUT)
	public ResponseEntity<Void> modify(@PathVariable("groupCode") String groupCode, @Validated @RequestBody CodeGroup codeGroup) throws Exception {
		codeGroup.setGroupCode(groupCode);
		service.modify(codeGroup);
		
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

}
