package org.hdcd.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CodeDetail {

	private String groupCode;
	private String codeValue;
	private String codeName;
	private int sortSeq;
	private String useYn;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	private Date regDate;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	private Date updDate;

}
