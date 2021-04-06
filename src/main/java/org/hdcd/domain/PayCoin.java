package org.hdcd.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PayCoin {

	private int historyNo;
	private int userNo;
	private int itemId;
	private String itemName;
	private int amount;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	private Date regDate;

}
