package org.hdcd.controller;

import java.util.List;

import org.hdcd.domain.Notice;
import org.hdcd.service.NoticeService;
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
@RequestMapping("/notices")
public class NoticeController {
	
	@Autowired
	private NoticeService service;

	@RequestMapping(value = "/{noticeNo}", method = RequestMethod.GET)
	public ResponseEntity<Notice> read(@PathVariable("noticeNo") int noticeNo) throws Exception {
		Notice notice = service.read(noticeNo);
			
		return new ResponseEntity<>(notice, HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<List<Notice>> list() throws Exception {
		log.info("list");
		
		return new ResponseEntity<>(service.list(), HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<Notice> register(@Validated @RequestBody Notice notice) throws Exception {
		log.info("register");
		
		service.register(notice);
		
		log.info("register notice.getNoticeNo() = " + notice.getNoticeNo());
		
		return new ResponseEntity<>(notice, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/{noticeNo}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> remove(@PathVariable("noticeNo") int noticeNo) throws Exception {
		service.remove(noticeNo);

		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/{noticeNo}", method = RequestMethod.PUT)
	public ResponseEntity<Void> modify(@PathVariable("noticeNo") int noticeNo, @Validated @RequestBody Notice notice) throws Exception {
		notice.setNoticeNo(noticeNo);
		service.modify(notice);
		
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

}
