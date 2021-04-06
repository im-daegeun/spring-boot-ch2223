package org.hdcd.service;

import java.util.List;

import org.hdcd.domain.ChargeCoin;
import org.hdcd.domain.PayCoin;
import org.hdcd.mapper.CoinMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CoinServiceImpl implements CoinService {

	@Autowired
	private CoinMapper mapper;

	@Transactional
	@Override
	public void charge(ChargeCoin chargeCoin) throws Exception {
		mapper.charge(chargeCoin);
		mapper.create(chargeCoin);
	}
	
	@Override
	public List<ChargeCoin> list(int userNo) throws Exception {
		return mapper.list(userNo);
	}
	

	@Override
	public List<PayCoin> listPayHistory(int userNo) throws Exception {
		return mapper.listPayHistory(userNo);
	}	

}
