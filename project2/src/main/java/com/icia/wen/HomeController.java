package com.icia.wen;






import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.icia.service.EventBoardService;
import com.icia.service.MemberService;

import lombok.extern.slf4j.Slf4j;




@Controller
@Slf4j
public class HomeController {
	@Autowired
	private MemberService service;
	@Autowired
	private EventBoardService eService;
	@PostMapping("/")
	public String index1(String id)throws Exception{
		log.info("{}", id);
	
		return "domain/main";
	}
	
	@GetMapping("/")
	public String index(Principal principal, Model model) throws Exception {
		if(principal!=null) {
			String id = principal.getName();
			model.addAttribute("list",service.read(id));
		}
		log.info("{}", eService.photoRead());
		if(eService.photoRead()!=null)
		model.addAttribute("photo", eService.photoRead());
		return "index";
		
		}
	
	
}
