package com.sparrow.hdmin.service;

import com.sparrow.hdmin.entity.Resource;
import com.sparrow.hdmin.service.support.IBaseService;
import com.sparrow.hdmin.vo.ZtreeView;

import java.util.List;

/**
 * <p>
 * 资源服务类
 * </p>
 *
 * @author 贤名
 * @since 2016-12-28
 */
public interface IResourceService extends IBaseService<Resource, Integer> {

	/**
	 * 获取角色的权限树
	 * @param roleId
	 * @return
	 */
	List<ZtreeView> tree(int roleId);

	/**
	 * 修改或者新增资源
	 * @param resource
	 */
	void saveOrUpdate(Resource resource);

}
