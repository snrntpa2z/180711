package com.icia.aboard2.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.icia.aboard2.dao.AttachRepository;
import com.icia.aboard2.dao.BoardRepository;
import com.icia.aboard2.dto.BoardDto;
import com.icia.aboard2.dto.BoardDto.CreateBoard;
import com.icia.aboard2.entity.Attachment;
import com.icia.aboard2.entity.Board;
import com.icia.aboard2.exception.BoardNotFoundException;
import com.icia.aboard2.util.ABoard2Contstants;
import com.icia.aboard2.util.pagination.Page;
import com.icia.aboard2.util.pagination.Pageable;
import com.icia.aboard2.util.pagination.Pagination;
import com.icia.aboard2.util.pagination.PagingUtil;

@Service
public class BoardService {
	@Autowired
	private BoardRepository boardDao;
	@Autowired
	private AttachRepository attachDao;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private Gson gson;
	
	//전체리스트
	public String listAll(){
		return gson.toJson(boardDao.listAll());
	}
	// 리스트
	public Map<String, Object> list(int page,String categoriName) {
		Pageable pageable = new Pageable();
		pageable.setPage(page);
		Integer count = boardDao.count(categoriName);
		Map<String, Object> map = new HashMap<>();
		Pagination pagination = PagingUtil.getPagination(pageable, count);
		map.put("list", boardDao.list(pagination.getStartRow(), pagination.getEndRow(), categoriName));
		map.put("pagination", pagination);
		return map;
	}
	// 글쓰기
	public boolean write(CreateBoard create)  {
		Board board = modelMapper.map(create, Board.class);
		boardDao.write(board);
		return true;
	}
	// 글읽기
	@Transactional
	public Map<String, Object> read(Integer bno, String categoriName) {
		int result = boardDao.increaseReadCnt(bno);
		if(result==0)
			throw new BoardNotFoundException();
		
		
		return boardDao.read(bno,categoriName);
	}
	// 글삭제
	public void delete(int bno) {
		boardDao.delete(bno);
	}
	// 추천
	public int recommend(String id, int bno, boolean alreadyRecommend) {
		int result = boardDao.recommend(bno);
		if(result==0)
			throw new BoardNotFoundException();
		return boardDao.getRecommendCnt(bno);
	}
	// 신고
	public int report(String id, int bno, boolean alreadyReport) {
		int result = boardDao.report(bno);
		if(result==0)
			throw new BoardNotFoundException();
		return boardDao.getReportCnt(bno);
	}
	// 파일
	public Attachment getAttachment(int bno, int ano) throws FileNotFoundException {
		Attachment attachment = attachDao.getAttachment(bno, ano);
		if(attachment==null)
			throw new FileNotFoundException("원본 파일을 찾을 수 없습니다 ");
		return attachment;
	}
}



