package org.hdcd.controller;

import java.util.List;

import org.hdcd.common.util.AuthUtil;
import org.hdcd.domain.Board;
import org.hdcd.domain.Member;
import org.hdcd.service.BoardService;
import org.hdcd.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/boards")
public class BoardController {

	@Autowired
	private BoardService service;
	
	@Autowired
	private MemberService memberService;
	
	@PreAuthorize("hasRole('ROLE_MEMBER')")
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<Board> register(@Validated @RequestBody Board board, @RequestHeader (name="Authorization") String header) throws Exception {		
		int userNo = AuthUtil.getUserNo(header);
		log.info("register userNo = " + userNo);
	
		Member member = memberService.read(userNo);
		
		log.info("register member.getUserId() = " + member.getUserId());
		
		board.setWriter(member.getUserId());
		
		service.register(board);

		log.info("register board.getBoardNo() = " + board.getBoardNo());
		
		return new ResponseEntity<>(board, HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<List<Board>> list() throws Exception {
		log.info("list");
		
		return new ResponseEntity<>(service.list(), HttpStatus.OK);
		
	}

	@RequestMapping(value = "/{boardNo}", method = RequestMethod.GET)
	public ResponseEntity<Board> read(@PathVariable("boardNo") int boardNo) throws Exception {
		log.info("read");
		
		Board board = service.read(boardNo);
			
		return new ResponseEntity<>(board, HttpStatus.OK);
	}	

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MEMBER')")
	@RequestMapping(value = "/{boardNo}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> remove(@PathVariable("boardNo") int boardNo) throws Exception {
		log.info("remove");
		
		service.remove(boardNo);

		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}	

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MEMBER')")
	@RequestMapping(value = "/{boardNo}", method = RequestMethod.PUT)
	public ResponseEntity<Void> modify(@PathVariable("boardNo") int boardNo, @Validated @RequestBody Board board) throws Exception {
		log.info("modify");
		
		board.setBoardNo(boardNo);
		service.modify(board);
		
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}	
	
}
