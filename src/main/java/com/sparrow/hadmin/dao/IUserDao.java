package com.sparrow.hadmin.dao;

import com.sparrow.hadmin.dao.support.IBaseDao;
import com.sparrow.hadmin.entity.User;

import org.springframework.stereotype.Repository;

@Repository
public interface IUserDao extends IBaseDao<User, Integer> {

	User findByUserName(String username);

}
