package com.atguigu.atcrowdfunding.common.bean;

import java.util.List;

/**
 * @author Lee
 *
 * @param <T>
 */
public class Page<T> {
	private Integer pageNo;
	private Integer totalPageNo;
	private Integer pageSize;
	private Integer totalPageSize;
	private List<T> datas;

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getTotalPageNo() {
//		if (getTotalPageSize() % getPageSize() == 0) {
//			return getTotalPageSize() / getPageSize();
//		} else {
//			return getTotalPageSize() / getPageSize() + 1;
//		}
		return totalPageNo;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public Integer getTotalPageSize() {
		return totalPageSize;
	}

	public void setTotalPageSize(Integer totalPageSize) {
		this.totalPageSize = totalPageSize;
	}

	public List<T> getDatas() {
		return datas;
	}

	public void setDatas(List<T> datas) {
		this.datas = datas;
	}

}
