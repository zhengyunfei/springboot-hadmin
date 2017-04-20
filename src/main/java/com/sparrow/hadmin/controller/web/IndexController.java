package com.sparrow.hadmin.controller.web;
import com.sparrow.hadmin.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController extends BaseController{
	private Logger logger = LoggerFactory.getLogger(getClass());
	/**
	 * 系统首页
	 * /和/index都可以访问此方法
	 * author:贤云
	 * @return
	 */
	@RequestMapping(value={"/","/index"})
	public String index(){
		logger.debug("进入首页");
		return "index";
	}
}
