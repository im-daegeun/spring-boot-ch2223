package org.hdcd.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Board {

	private int boardNo;
	private String title;
	private String content;
	private String writer;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	private Date regDate;

}
