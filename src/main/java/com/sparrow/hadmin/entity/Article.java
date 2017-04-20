package com.sparrow.hadmin.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.sparrow.hadmin.entity.support.BaseEntity;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.Date;

/**
 * <p>
 * 文章表
 * </p>
 *
 * @author 贤云
 * @since 2016-12-28
 */
@Entity
@Table(name = "tb_article")
public class Article extends BaseEntity{

	/**
	 *
	 */
	private static final long serialVersionUID = -1894163644285296223L;

	/**
	 *id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private Integer id;

	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinTable(name = "tb_article_sort_relation", joinColumns = { @JoinColumn(name = "article_id") }, inverseJoinColumns = { @JoinColumn(name = "sort_id") })
	private  ArticleSort articleSort;
	/**
	 * 文章标题
	 */
	private String title;
	/**
	 * 文章作者
	 */
	private String author;
	/**
	 * 文章缩略图
	 */
	private String pic;
	/**
	 * 标签云
	 */
	private String label;
	/**
	 * 文章描述
	 */
	private String description;

	/**
	 * 文章备注
	 */
	private String remark;
	/**
	 * 角色状态,0：正常；1：删除
	 */
	private Integer status;

	private String sortName;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public ArticleSort getArticleSort() {
		return articleSort;
	}

	public void setArticleSort(ArticleSort articleSort) {
		this.articleSort = articleSort;
	}

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

	public String getSortName() {
		if(null!=articleSort&&!StringUtils.isEmpty(articleSort.getTitle())){
			return articleSort.getTitle();
		}
		return "";
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}
}
