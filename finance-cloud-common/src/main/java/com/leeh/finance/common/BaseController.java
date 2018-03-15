package com.atguigu.atcrowdfunding.common;

import com.atguigu.atcrowdfunding.common.bean.AjaxStatus;

/**
 * @ClassName: BaseController
 * @Description:TODO
 * @author: Lee
 * @date: 2018年1月26日 上午11:36:23
 */
public class BaseController {

	ThreadLocal<AjaxStatus> threadLocal = new ThreadLocal<>();

	/**
	 * 从当前线程创建AjaxStatus对象
	 */
	protected void start() {
		threadLocal.set(new AjaxStatus());
	}

	/**
	 * 返回AjaxStatus对象
	 * 
	 * @return
	 */
	protected Object end() {
		AjaxStatus result = threadLocal.get();
		// 清除当前线程的对象
		threadLocal.remove();
		return result;
	}

	/**
	 * 给AjaxStatus的page属性设置值
	 * 
	 * @param page
	 */
	protected void data(Object page) {
		AjaxStatus result = threadLocal.get();
		result.setPage(page);
	}

	/**
	 * 操作成功
	 */
	protected void success() {
		success(true);
	}

	/**
	 * 操作失败
	 */
	protected void fail() {
		success(false);
	}

	/**
	 * 
	 * @param flag
	 *            传入的条件
	 */
	protected void success(boolean flag) {
		threadLocal.get().setSuccess(flag);
	}
}
