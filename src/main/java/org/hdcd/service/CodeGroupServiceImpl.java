package org.hdcd.service;

import java.util.List;

import org.hdcd.domain.CodeGroup;
import org.hdcd.mapper.CodeGroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CodeGroupServiceImpl implements CodeGroupService {

	@Autowired
	private CodeGroupMapper mapper;

	@Override
	public void register(CodeGroup codeGroup) throws Exception {
		mapper.create(codeGroup);
	}

	@Override
	public CodeGroup read(String groupCode) throws Exception {
		return mapper.read(groupCode);
	}

	@Override
	public void modify(CodeGroup codeGroup) throws Exception {
		mapper.update(codeGroup);
	}

	@Override
	public void remove(String groupCode) throws Exception {
		mapper.delete(groupCode);
	}

	@Override
	public List<CodeGroup> list() throws Exception {
		return mapper.list();
	}

}
