package com.sparrow.hadmin.service.impl;

import com.sparrow.hadmin.dao.IArticleDao;
import com.sparrow.hadmin.dao.IArticleSortDao;
import com.sparrow.hadmin.dao.support.IBaseDao;
import com.sparrow.hadmin.entity.Article;
import com.sparrow.hadmin.entity.ArticleSort;
import com.sparrow.hadmin.service.IArticleService;
import com.sparrow.hadmin.service.support.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 文章服务实现类
 * </p>
 *
 * @author 贤名
 * @since 2016-12-28
 */
@Service
public class ArticleServiceImpl extends BaseServiceImpl<Article, Integer> implements IArticleService {
	@Autowired
	private IArticleDao articleDao;
	@Autowired
	private IArticleSortDao articleSortDao;
	@Override
	public IBaseDao<Article, Integer> getBaseDao() {
		return this.articleDao;
	}

	@Override
	public void saveOrUpdate(Article article) {
		if(article.getId() != null){
			Article dbUser = find(article.getId());
			dbUser.setTitle(article.getTitle());
			dbUser.setDescription(article.getDescription());
			dbUser.setUpdateTime(new Date());
			ArticleSort articleSort=articleSortDao.findOne(article.getArticleSort().getId());
			dbUser.setArticleSort(articleSort);
			dbUser.setRemark(article.getRemark());
			dbUser.setSortName(articleSort.getTitle());
			update(dbUser);
		}else{
			article.setCreateTime(new Date());
			article.setUpdateTime(new Date());
			ArticleSort articleSort=articleSortDao.findOne(article.getArticleSort().getId());
			article.setArticleSort(articleSort);
			article.setSortName(articleSort.getTitle());
			save(article);
		}
	}

	@Override
	public void delete(Integer id) {
		Article article = find(id);
		//Assert.state(!"admin".equals(article.getTitle()),"超级管理员用户不能删除");
		super.delete(id);
	}

	@Override
	public List<Article> findBySortName(String sortName) {
		return articleDao.findBySortName(sortName);
	}


}
