package com.sparrow.hadmin.controller.admin.article;

import com.sparrow.hadmin.common.JsonResult;
import com.sparrow.hadmin.controller.BaseController;
import com.sparrow.hadmin.entity.Article;
import com.sparrow.hadmin.service.IArticleService;
import com.sparrow.hadmin.service.IUserService;
import com.sparrow.hadmin.service.specification.SimpleSpecificationBuilder;
import com.sparrow.hadmin.service.specification.SpecificationOperator.Operator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *@deprecated  文章管理
 *@author 贤名
 *
 **/
@Controller
@RequestMapping("/admin/article")
public class ArticleController extends BaseController {

	@Autowired
	private IUserService userService;
	@Autowired
	private IArticleService articleService;
	/**
	 * @deprecated 初始化访问页面
	 * @author 贤仁
	 * @qq 799078779
	 * @param id
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/", "/index" })
	public String index() {
		return "admin/article/index";
	}

	/**
	 *@deprecated  获取json数据集
	 * @author 贤仁
	 * @qq 799078779
	 * @param id
	 * @param map
	 * @return
	 */
	@RequestMapping(value = { "/list" })
	@ResponseBody
	public Page<Article> list() {
		SimpleSpecificationBuilder<Article> builder = new SimpleSpecificationBuilder<Article>();
		String searchText = request.getParameter("searchText");
		if(StringUtils.isNotBlank(searchText)){
			builder.add("nickName", Operator.likeAll.name(), searchText);
		}
		Page<Article> page = articleService.findAll(builder.generateSpecification(), getPageRequest());
		return page;
	}

	/**
	 *@deprecated  新增页面初始化
	 * @author 贤仁
	 * @qq 799078779
	 * @param id
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap map) {
		return "admin/article/form";
	}

	/**
	 *@deprecated  编辑页面初始化
	 * @author 贤仁
	 * @qq 799078779
	 * @param id
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable Integer id,ModelMap map) {
		Article article = articleService.find(id);
		map.put("article", article);
		return "admin/article/form";
	}

	/**
	 *@deprecated  新增或者编辑文章保存
	 * @author 贤仁
	 * @qq 799078779
	 * @param id
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
	 * @author 贤仁
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

}
