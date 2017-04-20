package com.sparrow.hadmin.service.impl;

import com.sparrow.hadmin.common.utils.RandomColor;
import com.sparrow.hadmin.dao.IArticleDao;
import com.sparrow.hadmin.dao.IArticleSortDao;
import com.sparrow.hadmin.dao.support.IBaseDao;
import com.sparrow.hadmin.entity.Article;
import com.sparrow.hadmin.entity.ArticleSort;
import com.sparrow.hadmin.service.IArticleService;
import com.sparrow.hadmin.service.support.impl.BaseServiceImpl;
import com.sparrow.hadmin.vo.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

/**
 * <p>
 * 文章服务实现类
 * </p>
 *
 * @author 贤云
 * @since 2016-12-28
 */
@Service
public class ArticleServiceImpl extends BaseServiceImpl<Article, Integer> implements IArticleService {
	@Autowired
	@PersistenceContext
	private EntityManager entityManager;
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
			dbUser.setAuthor(article.getAuthor());
			dbUser.setLabel(article.getLabel());
			dbUser.setPic(article.getPic());
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

	@Override
	public List<Tags> findTags(String tag) {
		List<Tags> result=new ArrayList<Tags>();
		String sql="SELECT  GROUP_CONCAT(label) FROM tb_article WHERE label IS NOT NULL";
		if(!org.springframework.util.StringUtils.isEmpty(sql)){
			sql+=" AND INSTR(label,'"+tag+"')>0";
		}
		List<String> list = entityManager
				.createNativeQuery(sql)
				.getResultList();
		String args[]=list.get(0).split(",");
		Set<String> set=new HashSet<>();
		for(int i=0;i<args.length;i++){
			set.add(args[i]);
		}

		for (String str : set) {
			Tags tags=new Tags();
			tags.setColor(RandomColor.getColor());
			tags.setName(str);
			result.add(tags);
		}

		return result;
	}

	@Override
	public List<Article> findAllByLabel(String label) {
		String sql="SELECT id,create_time,title,STATUS,label,pic,remark FROM tb_article";
		if(!org.springframework.util.StringUtils.isEmpty(label)){
			sql+=" WHERE INSTR(label,'"+label+"')>0";
		}
		List<Object []> resultList = entityManager
				.createNativeQuery(sql)
				.getResultList();
		List<Article> list=new ArrayList<Article>();
		for(Object[] object:resultList){
			Article article=new Article();
			article.setId((Integer) object[0]);
			article.setCreateTime((Date) object[1]);
			article.setTitle((String) object[2]);
			article.setStatus((Integer) object[3]);
			article.setLabel((String) object[4]);
			article.setPic((String) object[5]);
			article.setRemark((String) object[6]);
			list.add(article);
		}
		return list;
	}


}
