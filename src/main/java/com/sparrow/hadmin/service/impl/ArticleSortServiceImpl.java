package com.sparrow.hadmin.service.impl;

import com.sparrow.hadmin.dao.IArticleSortDao;
import com.sparrow.hadmin.dao.support.IBaseDao;
import com.sparrow.hadmin.entity.ArticleSort;
import com.sparrow.hadmin.service.IArticleSortService;
import com.sparrow.hadmin.service.support.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 文章服务实现类
 * </p>
 *
 * @author 贤云
 * @since 2016-12-28
 */
@Service
public class ArticleSortServiceImpl extends BaseServiceImpl<ArticleSort, Integer> implements IArticleSortService {
	@Autowired
	private IArticleSortDao articleSortDao;
	@Override
	public IBaseDao<ArticleSort, Integer> getBaseDao() {
		return this.articleSortDao;
	}

	@Override
	public void saveOrUpdate(ArticleSort article) {
		if(article.getId() != null){
			ArticleSort dbUser = find(article.getId());
			dbUser.setTitle(article.getTitle());
			dbUser.setDescription(article.getDescription());
			dbUser.setUpdateTime(new Date());
			update(dbUser);
		}else{
			article.setCreateTime(new Date());
			article.setUpdateTime(new Date());
			save(article);
		}
	}

	@Override
	public void delete(Integer id) {
		ArticleSort article = find(id);
		//Assert.state(!"admin".equals(article.getTitle()),"超级管理员用户不能删除");
		super.delete(id);
	}



}
