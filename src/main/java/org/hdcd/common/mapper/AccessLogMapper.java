package org.hdcd.common.mapper;

import java.util.List;

import org.hdcd.common.domain.AccessLog;

public interface AccessLogMapper {

	public void create(AccessLog accessLog) throws Exception;

	public List<AccessLog> list() throws Exception;

}
