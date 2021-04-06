package org.hdcd.mapper;

import java.util.List;

import org.hdcd.domain.CodeGroup;

public interface CodeGroupMapper {

	public void create(CodeGroup codeGroup) throws Exception;

	public CodeGroup read(String groupCode) throws Exception;

	public void update(CodeGroup codeGroup) throws Exception;

	public void delete(String groupCode) throws Exception;

	public List<CodeGroup> list() throws Exception;

}
