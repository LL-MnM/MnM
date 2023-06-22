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
	// TODO: 각각 결과 페이지 만들기, 소셜미디어 공유 or 링크 공유 추가
}
