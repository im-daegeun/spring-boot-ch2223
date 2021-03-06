package org.hdcd.service;

import java.util.List;

import org.hdcd.domain.Pds;

public interface PdsService {

	public void register(Pds item) throws Exception;

	public Pds read(int itemId) throws Exception;

	public void modify(Pds item) throws Exception;

	public void remove(int itemId) throws Exception;

	public List<Pds> list() throws Exception;

	public List<String> getAttach(int itemId) throws Exception;

	public void updateAttachDownCnt(String fullName) throws Exception;

}
