package com.example.MnM.boundedContext.mbtiTest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MbtiTestController {
	@GetMapping("/mbtiTest")
	public String testPage() {
		return "mbtiTest/index";
	}

	@GetMapping("/mbtiInfo")
	public String infoPage() {
		return "mbtiTest/info";
	}
}
