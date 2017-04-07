package com.sparrow.hadmin.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.sparrow.hadmin.entity.support.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * <p>
 * 文章表
 * </p>
 *
 * @author 贤名
 * @since 2016-12-28
 */
@Entity
@Table(name = "tb_article")
public class Article extends BaseEntity {

	/**
	 *
	 */
	private static final long serialVersionUID = -1894163644285296223L;

	/**
	 * 角色id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private Integer id;

	/**
	 * 文章标题
	 */
	private String title;

	/**
	 * 文章描述
	 */
	private String description;

	/**
	 * 角色状态,0：正常；1：删除
	 */
	private Integer status;


	/**
	 * 创建时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/**
	 * 更新时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
