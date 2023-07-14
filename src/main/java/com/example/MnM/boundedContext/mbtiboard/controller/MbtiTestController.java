package com.example.MnM.boundedContext.mbtiboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MbtiTestController {

	@GetMapping("/mbtiTest")
	public String testPage() {

		return "mbtiTest/mbti";
	}

}
