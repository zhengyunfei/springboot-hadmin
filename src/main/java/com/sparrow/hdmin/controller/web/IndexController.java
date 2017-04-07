package com.sparrow.hdmin.controller.web;

import java.util.List;

import com.sparrow.hdmin.controller.BaseController;
import com.sparrow.hdmin.entity.User;
import com.sparrow.hdmin.service.IUserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController extends BaseController{

	@Autowired
	private IUserService userService;

	private Logger logger = LoggerFactory.getLogger(getClass());

	@RequestMapping(value={"/","/index"})
	public String index(){
		List<User> users = userService.findAll();
		logger.debug(users.toString());
		return "index";
	}
}
