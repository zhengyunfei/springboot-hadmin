package com.sparrow.hdmin.dao;

import com.sparrow.hdmin.dao.support.IBaseDao;
import com.sparrow.hdmin.entity.Role;

import org.springframework.stereotype.Repository;

@Repository
public interface IRoleDao extends IBaseDao<Role, Integer> {

}
