package org.hdcd.domain;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Member {

	private int userNo;
	
	@NotBlank
	private String userId;
	
	@NotBlank
	private String userPw;
	
	@NotBlank
	private String userName;
	
	private String job;
	private int coin;
	
	private boolean enabled;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	private Date regDate;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	private Date updDate;

	private List<MemberAuth> authList;

}
