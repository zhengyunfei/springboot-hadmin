package com.sparrow.hadmin.dao;

import com.sparrow.hadmin.dao.support.IBaseDao;
import com.sparrow.hadmin.entity.Article;
import org.springframework.stereotype.Repository;

@Repository
public interface IArticleDao extends IBaseDao<Article, Integer> {
    java.util.List<Article> findBySortName(String sortName);

}
