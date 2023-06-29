package com.example.MnM.boundedContext.mbtiTest.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@PreAuthorize("isAuthenticated()")
public class MbtiTestController {

	@GetMapping("/mbtiTest")
	public String testPage() {

		return "mbtiTest/mbti";
	}

}
