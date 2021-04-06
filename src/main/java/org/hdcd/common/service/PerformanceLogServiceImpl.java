package org.hdcd.common.service;

import java.util.List;

import org.hdcd.common.domain.PerformanceLog;
import org.hdcd.common.mapper.PerformanceLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PerformanceLogServiceImpl implements PerformanceLogService {

	@Autowired
	private PerformanceLogMapper mapper;

	@Override
	public void register(PerformanceLog performanceLog) throws Exception {
		mapper.create(performanceLog);
	}
	
	@Override
	public List<PerformanceLog> list() throws Exception {
		return mapper.list();
	}

}
