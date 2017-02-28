package com.craftsman.roy.sample.web;

import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.craftsman.roy.sample.vo.HttpResult;
import com.google.common.collect.Lists;

/**
 * 
 * @author luoyh
 * @date Feb 23, 2017
 */
@Controller
public class HomeController {

	
	@RequestMapping("")
	public String index(ModelMap model, Model  dd) {
		List<HttpResult> list = Lists.newArrayList();
		for (int i = 0; i < 10; i ++) {
			HttpResult hr = new HttpResult();
			hr.setCode(new Random().nextInt(9012113));
			hr.setMsg("Hello world!!" + Math.random());
			list.add(hr);
		}
		model.put("data", list);
		model.put("timestamp", System.currentTimeMillis());
		return "index";
	}
}

