package com.atguigu.atcrowdfunding.common.bean;

import java.util.List;

public class Datas {

	private List<Integer> ids;
	private List<MemberCert> mcs;

	public List<MemberCert> getMcs() {
		return mcs;
	}

	public void setMcs(List<MemberCert> mcs) {
		this.mcs = mcs;
	}

	public List<Integer> getIds() {
		return ids;
	}

	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}

}
