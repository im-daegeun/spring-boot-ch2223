package org.hdcd.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Notice {

	private int noticeNo;
	private String title;
	private String content;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	private Date regDate;

}
