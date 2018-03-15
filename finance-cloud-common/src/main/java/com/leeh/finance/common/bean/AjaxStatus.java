package com.atguigu.atcrowdfunding.common.bean;

/**
 * @author Lee
 *
 */
public class AjaxStatus {
	private boolean success;
	private Object page;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Object getPage() {
		return page;
	}

	public void setPage(Object page) {
		this.page = page;
	}

}
