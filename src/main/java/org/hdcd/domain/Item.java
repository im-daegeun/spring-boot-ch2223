package org.hdcd.domain;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Item {

	private int itemId;

	private String itemName;

	private int price;

	private String description;
	
	@JsonIgnore
	private MultipartFile picture;
	
	private String pictureUrl;

	@JsonIgnore
	private MultipartFile preview;
	
	private String previewUrl;

}
