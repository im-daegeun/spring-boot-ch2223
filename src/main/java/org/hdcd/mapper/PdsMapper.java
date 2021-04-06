package org.hdcd.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.hdcd.domain.Pds;

public interface PdsMapper {

	public void create(Pds item) throws Exception;

	public Pds read(int itemId) throws Exception;

	public void update(Pds item) throws Exception;

	public void delete(int itemId) throws Exception;

	public List<Pds> list() throws Exception;

	public void addAttach(String fullName) throws Exception;

	public List<String> getAttach(int itemId) throws Exception;

	public void deleteAttach(int itemId) throws Exception;

	public void replaceAttach(@Param("fullName") String fullName, @Param("itemId") int itemId) throws Exception;

	public void updateAttachDownCnt(String fullName) throws Exception;
	
	public void updateViewCnt(int itemId) throws Exception;

}
