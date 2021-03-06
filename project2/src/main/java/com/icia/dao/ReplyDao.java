package com.icia.dao;

import java.util.List;

import com.icia.vo.Reply;

public interface ReplyDao {
	public void insert(Reply reply)throws Exception;
	public void delete(int cno)throws Exception;
	public void update(Reply reply)throws Exception;
	public List<Reply> list(int product_id)throws Exception;
	public void eBoardinsert(Reply reply)throws Exception;
	public void eBoarddelete(int cno)throws Exception;
	public void eBoardupdate(Reply reply)throws Exception;
	public List<Reply> eBoardlist(int ebno)throws Exception;
}
