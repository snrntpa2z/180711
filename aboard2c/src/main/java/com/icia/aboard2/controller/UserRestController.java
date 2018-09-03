package com.icia.aboard2.controller;


import java.io.FileNotFoundException;
import java.net.URISyntaxException;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icia.aboard2.dto.UserDto.CreateUser;
import com.icia.aboard2.dto.UserDto.FindId;
import com.icia.aboard2.dto.UserDto.LoginUser;
import com.icia.aboard2.dto.UserDto.UpdateUser;
import com.icia.aboard2.service.UserService;



@RestController
public class UserRestController {
	@Autowired
	private UserService service;
	@Autowired
	private ObjectMapper mapper;
	//회원가입
	@PostMapping("/users/join")
	public ResponseEntity<Void> userJoin(String user) throws ParseException {
		JSONObject obj = JsonPaser(user);
		CreateUser create = new CreateUser();
		create.setId(obj.get("id").toString());
		create.setPwd(obj.get("pwd").toString());
		create.setEmail(obj.get("email").toString());
		create.setIrum(obj.get("irum").toString());
		create.setBirthDay(obj.get("realBirth").toString());
		create.setAddress(obj.get("realAddress").toString());
		service.join(create);
		return new ResponseEntity<>(HttpStatus.OK);
		
		
	}
	//아이디중복확인
	@RequestMapping("/users/idCheck/{id}")
	public ResponseEntity<Void> idCheck(@PathVariable String id) {
		boolean result = service.idCheck(id);
		//아이디가 없으면 OK, 있으면 BAD_REQUEST
		if(result)
			return new ResponseEntity<>(HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	//로그인인증
	@PostMapping(value="/users/login",produces = "application/json; charset=UTF-8")
	public ResponseEntity<String> login(String login) throws ParseException, JsonProcessingException, InterruptedException{
		JSONObject obj = JsonPaser(login);
		LoginUser logi = new LoginUser();
		logi.setId(obj.get("id").toString());
		logi.setPwd(obj.get("pwd").toString());
		String loginMsg = service.login(logi);
		System.out.println(loginMsg);
		if(loginMsg=="아이디다름") {
			//쓰레드로 로딩 지연
			Thread.sleep(1000);
			return new ResponseEntity<String>(mapper.writeValueAsString(loginMsg),HttpStatus.OK);
		}
		if(loginMsg=="비밀번호다름") {
			//쓰레드로 로딩 지연
			Thread.sleep(1000);
			return new ResponseEntity<String>(mapper.writeValueAsString(loginMsg),HttpStatus.OK);
		}
		// SUCCESS
		Thread.sleep(1000);
		return new ResponseEntity<String>(mapper.writeValueAsString(loginMsg),HttpStatus.OK);
		
		
}
		//인증번호발송
		@RequestMapping("/users/findId/emailAuth")
		public void emailAuth(String mail) throws FileNotFoundException, URISyntaxException, ParseException, InterruptedException {
			JSONObject obj = JsonPaser(mail);
			String authToken = obj.get("authToken").toString();
			String email = obj.get("email").toString();
			Thread.sleep(1000);
			service.sendMail(email,authToken);
		}
		//비밀번호리셋
		@RequestMapping("/users/findPwd/emailAuth")
		public void pwdEmailAuth(String id) throws FileNotFoundException, URISyntaxException {
			String pwd = service.getRandomPassword(10);
			String email = service.getEmail(id).get("REALEMAIL").toString();
			service.pwdMail(id,pwd,email);
		}
		//아이디 찾기
		@RequestMapping("/users/findId")
		public ResponseEntity<String> findId(String find) throws FileNotFoundException, URISyntaxException, ParseException, JsonProcessingException {
			JSONObject obj = JsonPaser(find);
			FindId findId = new FindId();
			findId.setEmail(obj.get("email").toString());
			findId.setIrum(obj.get("irum").toString());
			
			if(service.findId(findId)!=null) {
				String result = service.findId(findId);
				return new ResponseEntity<String>(mapper.writeValueAsString(result),HttpStatus.OK);
		}	
			String result = "NO";
			// 아이디가 없을 경우
			return new ResponseEntity<String>(mapper.writeValueAsString(result),HttpStatus.OK);
		}
		//비밀번호 찾기
		@RequestMapping(value="/users/findPwd",produces = "application/json; charset=UTF-8")
		public ResponseEntity<String> findPwd(String id) throws JsonProcessingException{
			// service.getEmail <= NULL 일 경우는 ID가 존재하지 않는다
			if(service.getEmail(id)==null) {
				Object result = "해당이메일없음";
				return new ResponseEntity<String>(mapper.writeValueAsString(result),HttpStatus.OK);
			}
			// NULL이 아닐경우 담아서 보냄
			Object result = service.getEmail(id).get("EMAIL");
			return new ResponseEntity<String>(mapper.writeValueAsString(result),HttpStatus.OK);
			
		}
		//회원정보
		@RequestMapping(value="/users/userInfo/{id}",produces = "application/json; charset=UTF-8")
		public String userInfo(@PathVariable String id) throws JsonProcessingException {
			return mapper.writeValueAsString(service.userInfo(id));
		}
		//회원정보수정
		@RequestMapping(value="/users/updateUser")
		public ResponseEntity<Void> updateUser(String update) throws ParseException{
			JSONObject obj = JsonPaser(update);
			UpdateUser u = new UpdateUser();
			u.setEmail(obj.get("email").toString());
			u.setId(obj.get("id").toString());
			u.setPwd(obj.get("pwd").toString());
			service.updateUser(u);
			return new ResponseEntity<Void>(HttpStatus.OK);
			
			
		}
		//알림확인
		@RequestMapping(value="/users/notice",produces = "application/json; charset=UTF-8")
		public ResponseEntity<String> notice(String id) throws Exception{
			System.out.println("내 아이디"+id);
			service.notice(id);
			System.out.println(service.notice(id));
			return new ResponseEntity<String>(mapper.writeValueAsString(service.notice(id)),HttpStatus.OK);
			
		}
		
		// 중복 코드 제거
		private JSONObject JsonPaser(String user) throws ParseException {
			JSONParser jsonParser = new JSONParser();
			JSONObject obj = (JSONObject) jsonParser.parse(user);
			return obj;
		}
}






