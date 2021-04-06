package org.hdcd.service;

import java.util.List;

import org.hdcd.domain.ChargeCoin;
import org.hdcd.domain.PayCoin;

public interface CoinService {

	public void charge(ChargeCoin chargeCoin) throws Exception;

	public List<ChargeCoin> list(int userNo) throws Exception;
	
	public List<PayCoin> listPayHistory(int userNo) throws Exception;	

}
