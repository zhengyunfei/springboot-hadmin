package com.sparrow.hdmin.dao;

import com.sparrow.hdmin.dao.support.IBaseDao;
import com.sparrow.hdmin.entity.User;

import org.springframework.stereotype.Repository;

@Repository
public interface IUserDao extends IBaseDao<User, Integer> {

	User findByUserName(String username);

}
