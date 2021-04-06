package org.hdcd.common.mapper;

import java.util.List;

import org.hdcd.common.domain.PerformanceLog;

public interface PerformanceLogMapper {

	public void create(PerformanceLog performanceLog) throws Exception;
	
	public List<PerformanceLog> list() throws Exception;

}
