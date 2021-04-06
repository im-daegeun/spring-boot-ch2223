package org.hdcd.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.hdcd.common.util.AuthUtil;
import org.hdcd.domain.UserItem;
import org.hdcd.exception.NotMyItemException;
import org.hdcd.service.UserItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping("/useritems")
public class UserItemController {

	@Autowired
	private UserItemService service;

	@Value("${upload.path}")
	private String uploadPath;
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MEMBER')")
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<List<UserItem>> list(@RequestHeader (name="Authorization") String header) throws Exception {
		log.info("read : header " + header);
		
		int userNo = AuthUtil.getUserNo(header);
		
		log.info("read : userNo " + userNo);
		
		return new ResponseEntity<>(service.list(userNo), HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MEMBER')")
	@RequestMapping(value = "/{userItemNo}", method = RequestMethod.GET)
	public ResponseEntity<UserItem> read(@PathVariable("userItemNo") int userItemNo) throws Exception {
		UserItem userItem = service.read(userItemNo);
		
		return new ResponseEntity<>(userItem, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MEMBER')")
	@RequestMapping("/download/{userItemNo}")
	public ResponseEntity<byte[]> download(@PathVariable("userItemNo") int userItemNo, @RequestHeader (name="Authorization") String header) throws Exception {
		log.info("download userItemNo = " + userItemNo);
		
		UserItem userItem = service.read(userItemNo);
		
		int userNo = AuthUtil.getUserNo(header);
		log.info("download userNo = " + userNo);
		
		if(userItem.getUserNo() != userNo) {
			throw new NotMyItemException("It is Not My Item.");
		}
		
		String fullName = userItem.getPictureUrl();

		InputStream in = null;
		ResponseEntity<byte[]> entity = null;

		try {
			HttpHeaders headers = new HttpHeaders();

			in = new FileInputStream(uploadPath + File.separator + fullName);

			String fileName = fullName.substring(fullName.indexOf("_") + 1);

			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.add("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1") + "\"");

			entity = new ResponseEntity<byte[]>(IOUtils.toByteArray(in), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
		} finally {
			in.close();
		}

		return entity;
	}

}
