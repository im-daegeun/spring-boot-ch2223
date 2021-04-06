package org.hdcd.service;

import java.util.List;

import org.hdcd.domain.Pds;
import org.hdcd.mapper.PdsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PdsServiceImpl implements PdsService {

	@Autowired
	private PdsMapper mapper;

	@Transactional
	@Override
	public void register(Pds item) throws Exception {
		mapper.create(item);

		String[] files = item.getFiles();

		if (files == null) {
			return;
		}

		for (String fileName : files) {
			mapper.addAttach(fileName);
		}
	}

	@Override
	public Pds read(int itemId) throws Exception {
		mapper.updateViewCnt(itemId);
		
		return mapper.read(itemId);
	}

	@Transactional
	@Override
	public void modify(Pds item) throws Exception {
		mapper.update(item);

		int itemId = item.getItemId();

		mapper.deleteAttach(itemId);

		String[] files = item.getFiles();

		if (files == null) {
			return;
		}

		for (String fileName : files) {
			mapper.replaceAttach(fileName, itemId);
		}
	}

	@Transactional
	@Override
	public void remove(int itemId) throws Exception {
		mapper.deleteAttach(itemId);
		mapper.delete(itemId);
	}

	@Override
	public List<Pds> list() throws Exception {
		return mapper.list();
	}

	@Override
	public List<String> getAttach(int itemId) throws Exception {
		return mapper.getAttach(itemId);
	}

	@Override
	public void updateAttachDownCnt(String fullName) throws Exception {
		mapper.updateAttachDownCnt(fullName);
	}

}
