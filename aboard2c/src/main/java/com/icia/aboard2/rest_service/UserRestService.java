package com.icia.aboard2.rest_service;

import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.icia.aboard2.dao.UserRepository;
import com.icia.aboard2.dto.UserDto.ChangeUserPwd;
import com.icia.aboard2.dto.UserDto.LoginUser;
import com.icia.aboard2.entity.User;
import com.icia.aboard2.exception.InvalidPasswordException;
import com.icia.aboard2.exception.UserNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserRestService {
	@Autowired
	private UserRepository dao;
	@Autowired
	private ModelMapper mapper;
	@Autowired
	private PasswordEncoder encoder;
	
	public void pwdChange(ChangeUserPwd u) {
		String pwd = dao.getPwd(u.getId());
		if(pwd==null)								// 비밀번호가 null -> 유저가 없다
			throw new UserNotFoundException(u.getId());
		if(!encoder.matches(u.getOldPwd(), pwd))	// 비밀번호가 틀리다
			throw new InvalidPasswordException();
		User user = new User();
		user.setId(u.getId());
		user.setPwd(encoder.encode(u.getNewPwd()));
		dao.pwdChange(user);
	}
	
	public boolean idCheck(String id) {
		return dao.idCheck(id)==null? true: false;
	}
	public boolean login(LoginUser logi) {
		User user = mapper.map(logi, User.class);
		if(dao.login(user)==null) {
			return false;
		}	
		if(dao.login(user)!=null) {
			String encodedPwd = dao.login(user).get("PWD").toString();
			if(encoder.matches(user.getPwd(), encodedPwd))
				return true;
		}
		return false;
		
	}
	public Map<String, Object> getEmail(String id) {
		return dao.getEmail(id);
	}
}
