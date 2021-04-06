package org.hdcd.controller;

import java.util.List;
import java.util.Locale;

import org.hdcd.common.util.AuthUtil;
import org.hdcd.domain.ChargeCoin;
import org.hdcd.domain.PayCoin;
import org.hdcd.service.CoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coins")
public class CoinController {

	@Autowired
	private CoinService service;
	
	@Autowired
	private MessageSource messageSource;

	@PreAuthorize("hasRole('ROLE_MEMBER')")
	@RequestMapping(value = "/charge/{amount}", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public ResponseEntity<String> charge(@PathVariable("amount") int amount, @RequestHeader (name="Authorization") String header) throws Exception {
		int userNo = AuthUtil.getUserNo(header);

		ChargeCoin chargeCoin = new ChargeCoin();

		chargeCoin.setUserNo(userNo);
		chargeCoin.setAmount(amount);

		service.charge(chargeCoin);

		String message = messageSource.getMessage("coin.chargingComplete", null, Locale.KOREAN);
		
		return new ResponseEntity<>(message, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_MEMBER')")
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<List<ChargeCoin>> list(@RequestHeader (name="Authorization") String header) throws Exception {
		int userNo = AuthUtil.getUserNo(header);
		
		return new ResponseEntity<>(service.list(userNo), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/pay", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_MEMBER')")
	public ResponseEntity<List<PayCoin>> listPayHistory(@RequestHeader (name="Authorization") String header) throws Exception {
		int userNo = AuthUtil.getUserNo(header);
		
		return new ResponseEntity<>(service.listPayHistory(userNo), HttpStatus.OK);
	}	
	
}
