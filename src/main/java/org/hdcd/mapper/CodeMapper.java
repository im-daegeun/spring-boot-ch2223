package org.hdcd.mapper;

import java.util.List;

import org.hdcd.common.domain.CodeLabelValue;

public interface CodeMapper {
	
	public List<CodeLabelValue> getCodeGroupList() throws Exception;

	public List<CodeLabelValue> getCodeList(String groupCode) throws Exception;
	
}
