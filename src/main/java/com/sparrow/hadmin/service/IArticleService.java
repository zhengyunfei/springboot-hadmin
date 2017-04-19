package com.sparrow.hadmin.service;

import com.sparrow.hadmin.entity.Article;
import com.sparrow.hadmin.service.support.IBaseService;

/**
 * <p>
 * 用户服务类
 * </p>
 *
 * @author 贤名
 * @since 2016-12-28
 */
public interface IArticleService extends IBaseService<Article, Integer> {

	/**
	 * 增加或者修改文章
	 * @param article
	 */
	void saveOrUpdate(Article article);

    void delete(Integer id);
	java.util.List<Article> findBySortName(String sortName);


}
