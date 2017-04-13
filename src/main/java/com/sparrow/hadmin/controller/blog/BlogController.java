package com.sparrow.hadmin.controller.blog;
import com.sparrow.hadmin.controller.BaseController;
import com.sparrow.hadmin.entity.Article;
import com.sparrow.hadmin.service.IArticleService;
import com.sparrow.hadmin.service.specification.SimpleSpecificationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping(value = "/blog")
public class BlogController extends BaseController {

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
		//查询所有文章
		SimpleSpecificationBuilder<Article> builder = new SimpleSpecificationBuilder<Article>();
		PageRequest pageRequest=getPageRequest();
		Sort sort=pageRequest.getSort();
		if(null==sort){
			sort = new Sort(Sort.Direction.DESC, "createTime");
			pageRequest=getPageRequest(sort);
		}
		Page<Article> page = articleService.findAll(builder.generateSpecification(), pageRequest);
		model.addAttribute("articleList",page.getContent());
		int currentPage=page.getNumber();
		int totalPages=page.getTotalPages();
		model.addAttribute("currentPage",currentPage+1);
		model.addAttribute("totalPages",totalPages);
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


	/**
	 * 文章详情页面
	 * @author:贤仁
	 * @createDate:2017-03-28
	 * @param model
	 * @param id
	 * @return
	 */
	@GetMapping("/detail/{id}")
	public String detailNews(Model model, @PathVariable Integer id) {
		Article article=articleService.find(id);
		String str = article.getDescription();
		//使用正则表达式检索出所有的<h2>...</h2>内容
		if(!org.springframework.util.StringUtils.isEmpty(str)){
			Matcher m = Pattern.compile("<h2.*?>([\\s\\S]*?)</h2>").matcher(str);
			while(m.find()){
				//删掉<h2></h2>中间的html标签k
				String parstr=deleteAllHTMLTag(m.group(1));
				//替换内容里面所有的h2标签，动态增加id
				article.setDescription(article.getDescription().replace("<h2>"+m.group(1),"<h2 id='"+parstr+"'>"+m.group(1)));
			}
		}
		model.addAttribute("bo",article);
		return "html/blog/detail";

	}
	/**
	 * 删除所有的HTML标签
	 *
	 * @param source 需要进行除HTML的文本
	 * @return
	 */
	public static String deleteAllHTMLTag(String source) {

		if(source == null) {
			return "";
		}

		String s = source;
		/** 删除普通标签  */
		s = s.replaceAll("<(S*?)[^>]*>.*?|<.*? />", "");
		/** 删除转义字符 */
		s = s.replaceAll("&.{2,6}?;", "");
		return s;
	}
}
