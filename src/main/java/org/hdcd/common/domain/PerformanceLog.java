package org.hdcd.common.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PerformanceLog {

	private String signatureName;
	private String signatureTypeName;
	private long durationTime;
	private Date regDate;

}
