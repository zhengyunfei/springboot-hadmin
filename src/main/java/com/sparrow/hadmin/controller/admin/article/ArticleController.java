package com.sparrow.hadmin.controller.admin.article;

import com.sparrow.hadmin.common.JsonResult;
import com.sparrow.hadmin.controller.BaseController;
import com.sparrow.hadmin.entity.Article;
import com.sparrow.hadmin.service.IArticleService;
import com.sparrow.hadmin.service.IArticleSortService;
import com.sparrow.hadmin.service.IUserService;
import com.sparrow.hadmin.service.specification.SimpleSpecificationBuilder;
import com.sparrow.hadmin.service.specification.SpecificationOperator.Operator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *@deprecated  文章管理
 *@author 贤云
 *
 **/
@Controller
@RequestMapping("/admin/article")
public class ArticleController extends BaseController {

	@Autowired
	private IUserService userService;
	@Autowired
	private IArticleService articleService;
	@Autowired
	private IArticleSortService articleSortService;
	/**
	 * @deprecated 初始化访问页面
	 * @author 贤云
	 * @qq 799078779
	 * @return
	 */
	@RequestMapping(value = { "/", "/index" })
	public String index() {
		return "admin/article/index";
	}

	/**
	 *@deprecated  获取json数据集
	 * @author 贤云
	 * @qq 799078779
	 * @return
	 */
	@RequestMapping(value = { "/list" })
	@ResponseBody
	public Page<Article> list() {
		SimpleSpecificationBuilder<Article> builder = new SimpleSpecificationBuilder<Article>();
		String searchText = request.getParameter("searchText");
		if(StringUtils.isNotBlank(searchText)){
			builder.add("title", Operator.likeAll.name(), searchText);
		}
		PageRequest pageRequest=getPageRequest();
		Sort sort=pageRequest.getSort();
		if(null==sort){
			sort = new Sort(Sort.Direction.DESC, "createTime");
			pageRequest=getPageRequest(sort);
		}
		Page<Article> page = articleService.findAll(builder.generateSpecification(), pageRequest);
		return page;
	}

	/**
	 *@deprecated  新增页面初始化
	 * @author 贤云
	 * @qq 799078779
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap map) {
		//获取所有分类信息
		map.addAttribute("articleSorts",articleSortService.findAll());
		return "admin/article/form";
	}

	/**
	 *@deprecated  编辑页面初始化
	 * @author 贤云
	 * @qq 799078779
	 * @param id
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable Integer id,ModelMap map) {
		Article article = articleService.find(id);
		map.put("article", article);
		//获取所有分类信息
		map.addAttribute("articleSorts",articleSortService.findAll());
		return "admin/article/form";
	}

	/**
	 *@deprecated  新增或者编辑文章保存
	 * @author 贤云
	 * @qq 799078779
	 * @param map
	 * @return
	 */
	@RequestMapping(value= {"/edit"} ,method = RequestMethod.POST)
	@ResponseBody
	public JsonResult edit(Article article, ModelMap map){
		try {
			articleService.saveOrUpdate(article);
		} catch (Exception e) {
			return JsonResult.failure(e.getMessage());
		}
		return JsonResult.success();
	}

	/**
	 *@deprecated  根据文章id删除文章信息
	 * @author 贤云
	 * @qq 799078779
	 * @param id
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult delete(@PathVariable Integer id,ModelMap map) {
		try {
			articleService.delete(id);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResult.failure(e.getMessage());
		}
		return JsonResult.success();
	}


	/**
	 * 文章详情页面
	 * @author:贤云
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
