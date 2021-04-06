package org.hdcd.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.hdcd.common.util.AuthUtil;
import org.hdcd.domain.Item;
import org.hdcd.domain.Member;
import org.hdcd.service.ItemService;
import org.hdcd.service.MemberService;
import org.hdcd.service.UserItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping("/items")
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	@Value("${upload.path}")
	private String uploadPath;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private UserItemService userItemService;

	@Autowired
	private MessageSource messageSource;	
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<List<Item>> list() throws Exception {
		return new ResponseEntity<>(itemService.list(), HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<Item> register(@RequestPart("item") String itemString, @RequestPart("file") MultipartFile originalImageFile, @RequestPart("file2") MultipartFile previewImageFile) throws Exception {
		log.info("itemString: " + itemString);
		
		Item item = new ObjectMapper().readValue(itemString, Item.class);
		
		String itemName = item.getItemName();
		String description = item.getDescription();
		
		if(itemName != null) {
			log.info("item.getItemName(): " + itemName);
			
			item.setItemName(itemName);
		}
		
		if(description != null) {
			log.info("item.getDescription(): " + description);
			
			item.setDescription(description);
		}	
		
		item.setPicture(originalImageFile);
		item.setPreview(previewImageFile);
		
		MultipartFile pictureFile = item.getPicture();
		MultipartFile previewFile = item.getPreview();
		
		if(pictureFile != null) {
			log.info("register pictureFile != null " + pictureFile.getOriginalFilename());
		}
		else {
			log.info("register pictureFile == null ");
		}
		
		String createdPictureFilename = uploadFile(pictureFile.getOriginalFilename(), pictureFile.getBytes());
		String createdPreviewFilename = uploadFile(previewFile.getOriginalFilename(), previewFile.getBytes());

		item.setPictureUrl(createdPictureFilename);
		item.setPreviewUrl(createdPreviewFilename);

		itemService.register(item);

		log.info("register member.getItemId() = " + item.getItemId());
		
		Item createdItem = new Item();
		createdItem.setItemId(item.getItemId());
		
		return new ResponseEntity<>(createdItem, HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/{itemId}", method = RequestMethod.PUT)
	public ResponseEntity<Item> modify(@PathVariable("itemId") int itemId, @RequestPart("item") String itemString, @RequestPart(name = "file", required = false) MultipartFile originalImageFile, @RequestPart(name = "file2", required = false) MultipartFile previewImageFile) throws Exception {
		log.info("itemString: " + itemString);
		
		Item item = new ObjectMapper().readValue(itemString, Item.class);
		
		item.setItemId(itemId);
		
		String itemName = item.getItemName();
		String description = item.getDescription();
		
		if(itemName != null) {
			log.info("item.getItemName(): " + itemName);
			
			item.setItemName(itemName);
		}
		
		if(description != null) {
			log.info("item.getDescription(): " + description);
			
			item.setDescription(description);
		}	
		
		item.setPicture(originalImageFile);
		item.setPreview(previewImageFile);		
		
		MultipartFile pictureFile = item.getPicture();

		if (pictureFile != null && pictureFile.getSize() > 0) {
			String createdFilename = uploadFile(pictureFile.getOriginalFilename(), pictureFile.getBytes());

			item.setPictureUrl(createdFilename);
		}
		else {
			Item oldItem = this.itemService.read(item.getItemId());
			item.setPictureUrl(oldItem.getPictureUrl());
		}
		
		MultipartFile previewFile = item.getPreview();

		if (previewFile != null && previewFile.getSize() > 0) {
			String createdFilename = uploadFile(previewFile.getOriginalFilename(), previewFile.getBytes());

			item.setPreviewUrl(createdFilename);
		}
		else {
			Item oldItem = this.itemService.read(item.getItemId());
			item.setPreviewUrl(oldItem.getPreviewUrl());
		}

		itemService.modify(item);

		Item createdItem = new Item();
		createdItem.setItemId(item.getItemId());

		return new ResponseEntity<>(createdItem, HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/{itemId}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> remove(@PathVariable("itemId") int itemId) throws Exception {
		itemService.remove(itemId);

		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
	
	private String uploadFile(String originalName, byte[] fileData) throws Exception {
		UUID uid = UUID.randomUUID();

		String createdFileName = uid.toString() + "_" + originalName;

		File target = new File(uploadPath, createdFileName);

		FileCopyUtils.copy(fileData, target);

		return createdFileName;
	}

	@RequestMapping("/display")
	public ResponseEntity<byte[]> displayFile(@RequestParam("itemId") int itemId) throws Exception {
		InputStream in = null;
		ResponseEntity<byte[]> entity = null;

		String fileName = itemService.getPicture(itemId);
		
		log.info("displayFile itemId = " + itemId);
		log.info("displayFile fileName = " + fileName);

		try {
			String formatName = fileName.substring(fileName.lastIndexOf(".") + 1);

			MediaType mType = getMediaType(formatName);

			HttpHeaders headers = new HttpHeaders();

			in = new FileInputStream(uploadPath + File.separator + fileName);

			if (mType != null) {
				headers.setContentType(mType);
			}

			entity = new ResponseEntity<byte[]>(IOUtils.toByteArray(in), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
		} finally {
			in.close();
		}
		return entity;
	}
	
	@RequestMapping("/preview")
	public ResponseEntity<byte[]> previewFile(@RequestParam("itemId") int itemId) throws Exception {
		InputStream in = null;
		ResponseEntity<byte[]> entity = null;

		String fileName = itemService.getPreview(itemId);
		
		log.info("displayFile itemId = " + itemId);
		log.info("displayFile fileName = " + fileName);

		try {
			String formatName = fileName.substring(fileName.lastIndexOf(".") + 1);

			MediaType mType = getMediaType(formatName);

			HttpHeaders headers = new HttpHeaders();

			in = new FileInputStream(uploadPath + File.separator + fileName);

			if (mType != null) {
				headers.setContentType(mType);
			}

			entity = new ResponseEntity<byte[]>(IOUtils.toByteArray(in), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
		} finally {
			in.close();
		}
		return entity;
	}
	
	private MediaType getMediaType(String formatName){
		if(formatName != null) {
			if(formatName.equals("JPG")) {
				return MediaType.IMAGE_JPEG;
			}
			
			if(formatName.equals("GIF")) {
				return MediaType.IMAGE_GIF;
			}
			
			if(formatName.equals("PNG")) {
				return MediaType.IMAGE_PNG;
			}
		}
		
		return null;
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MEMBER')")
	@RequestMapping(value = "/download/{itemId}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> downloadFile(@PathVariable("itemId") int itemId, @RequestHeader (name="Authorization") String header) throws Exception {
		
		int userNo = AuthUtil.getUserNo(header);
		log.info("downloadFile userNo = " + userNo);
		
		InputStream in = null;
		ResponseEntity<byte[]> entity = null;

		String fullName = itemService.getPicture(itemId);

		try {
			HttpHeaders headers = new HttpHeaders();

			in = new FileInputStream(uploadPath + File.separator + fullName);
			
			String fileName = fullName.substring(fullName.indexOf("_") + 1);
			
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			
			headers.add("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1") + "\"");
			
			entity = new ResponseEntity<byte[]>(IOUtils.toByteArray(in), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
		} finally {
			in.close();
		}
		
		return entity;
	}

	@RequestMapping(value = "/{itemId}", method = RequestMethod.GET)
	public ResponseEntity<Item> read(@PathVariable("itemId") int itemId) throws Exception {
		Item item = itemService.read(itemId);
	
		return new ResponseEntity<>(item, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/buy/{itemId}", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public ResponseEntity<String> buy(@PathVariable("itemId") int itemId, @RequestHeader (name="Authorization") String header) throws Exception {
		int userNo = AuthUtil.getUserNo(header);
		
		log.info("buy userNo = " + userNo);
		
		Member member = memberService.read(userNo);
	    
		member.setCoin(memberService.getCoin(userNo));
	    
		Item item = itemService.read(itemId);
	    
		userItemService.register(member, item);
	
		String message = messageSource.getMessage("item.purchaseComplete", null, Locale.KOREAN);
		
		return new ResponseEntity<>(message, HttpStatus.OK);
	}	
	
}
