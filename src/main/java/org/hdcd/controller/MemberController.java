package org.hdcd.controller;

import java.util.List;
import java.util.Locale;

import org.hdcd.common.util.AuthUtil;
import org.hdcd.domain.Member;
import org.hdcd.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping("/users")
public class MemberController {

	@Autowired
	private MemberService service;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private MessageSource messageSource;
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<Member> register(@Validated @RequestBody Member member) throws Exception {
		log.info("member.getUserName() = " + member.getUserName());
		
		String inputPassword = member.getUserPw();
		member.setUserPw(passwordEncoder.encode(inputPassword));
		
		service.register(member);

		log.info("register member.getUserNo() = " + member.getUserNo());
		
		return new ResponseEntity<>(member, HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<List<Member>> list() throws Exception {
		return new ResponseEntity<>(service.list(), HttpStatus.OK);
	}

	@RequestMapping(value = "/{userNo}", method = RequestMethod.GET)
	public ResponseEntity<Member> read(@PathVariable("userNo") int userNo) throws Exception {
		Member member = service.read(userNo);
		
		return new ResponseEntity<>(member, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/{userNo}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> remove(@PathVariable("userNo") int userNo) throws Exception {
		service.remove(userNo);

		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
	
	@RequestMapping(value = "/{userNo}", method = RequestMethod.PUT)
	public ResponseEntity<Void> modify(@PathVariable("userNo") int userNo, @Validated @RequestBody Member member) throws Exception {
		log.info("modify : member.getUserName() = " + member.getUserName());
		log.info("modify : userNo = " + userNo);
		
		member.setUserNo(userNo);
		service.modify(member);
		
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/setup", method = RequestMethod.POST, produces="text/plain;charset=UTF-8")
	public ResponseEntity<String> setupAdmin(@Validated @RequestBody Member member) throws Exception {
		log.info("setupAdmin : member.getUserName() = " + member.getUserName());
		log.info("setupAdmin : service.countAll() = " + service.countAll());
		
		if(service.countAll() == 0) {
			String inputPassword = member.getUserPw();
			member.setUserPw(passwordEncoder.encode(inputPassword));
			
			member.setJob("00");
			
			service.setupAdmin(member);
	
			return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
		}
		
		String message = messageSource.getMessage("common.cannotSetupAdmin", null, Locale.KOREAN);
		
		return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MEMBER')")
	@RequestMapping(value = "/myinfo", method = RequestMethod.GET)
	public ResponseEntity<Member> getMyInfo(@RequestHeader (name="Authorization") String header) throws Exception {		
		int userNo = AuthUtil.getUserNo(header);
		log.info("register userNo = " + userNo);
	
		Member member = service.read(userNo);
		
		member.setUserPw("");
		
		return new ResponseEntity<>(member, HttpStatus.OK);
	}

}
