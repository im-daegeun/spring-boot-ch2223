package org.hdcd.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChargeCoin {

	private int historyNo;
	private int userNo;
	private int amount;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	private Date regDate;

}
