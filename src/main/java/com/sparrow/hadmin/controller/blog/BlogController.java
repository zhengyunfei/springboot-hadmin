package com.sparrow.hadmin.controller.blog;
import com.sparrow.hadmin.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/blog")
public class BlogController {

	private IArticleService articleService;

	@Autowired
	public BlogController(IArticleService articleService) {
		this.articleService = articleService;
	}

	/**
	 * 博客主页
	 * @author:郑云飞
	 * @createDate:2017-03-28
	 * @param model
	 * @return
	 */
	@GetMapping("/index")
	public String indexPage(Model model) {
		return "html/blog/index";

	}

	/**
	 * 我的所有博客
	 * @author:郑云飞
	 * @createDate:2017-03-28
	 * @param model
	 * @return
	 */
	@GetMapping("/all")
	public String allMyBlogPage(Model model) {
		return "html/blog/allMyBlog";

	}

	/**
	 * 博客明细页面
	 * @author:郑云飞
	 * @createDate:2017-03-28
	 * @param model
	 * @return
	 */
	@GetMapping("/detail")
	public String detail(Model model) {
		return "html/blog/detail";

	}
	/**
	 * 友情连接
	 * @author:郑云飞
	 * @createDate:2017-03-28
	 * @param model
	 * @return
	 */
	@GetMapping("/link")
	public String link(Model model) {
		return "html/blog/link";

	}
	/**
	 * 关于
	 * @author:郑云飞
	 * @createDate:2017-03-28
	 * @param model
	 * @return
	 */
	@GetMapping("/about")
	public String about(Model model) {
		return "html/blog/about";

	}
	/**
	 * open-source
	 * @author:郑云飞
	 * @createDate:2017-03-28
	 * @param model
	 * @return
	 */
	@GetMapping("/open-source")
	public String openSource(Model model) {
		return "html/blog/openSourceProjects";

	}
	/**
	 * spring-boot
	 * @author:郑云飞
	 * @createDate:2017-03-28
	 * @param model
	 * @return
	 */
	@GetMapping("/spring-boot")
	public String springBoot(Model model) {
		return "html/blog/spring-boot";

	}
}
