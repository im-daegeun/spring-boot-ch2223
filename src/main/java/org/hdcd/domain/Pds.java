package org.hdcd.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Pds {

	private int itemId;

	private String itemName;

	private String description;
	
	private String[] files;
	
	private Integer viewCnt;
	
}
