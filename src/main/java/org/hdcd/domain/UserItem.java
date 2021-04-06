package org.hdcd.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserItem {

	private int userItemNo;
	private int userNo;
	
	private int itemId;
	private String itemName;
	private int price;
	private String description;
	private String pictureUrl;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	private Date regDate;

}
