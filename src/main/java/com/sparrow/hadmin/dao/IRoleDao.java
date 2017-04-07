package com.sparrow.hadmin.dao;

import com.sparrow.hadmin.dao.support.IBaseDao;
import com.sparrow.hadmin.entity.Role;

import org.springframework.stereotype.Repository;

@Repository
public interface IRoleDao extends IBaseDao<Role, Integer> {

}
