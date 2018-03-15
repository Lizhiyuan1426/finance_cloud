package com.atguigu.atcrowdfunding.portal.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ServerStartListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ServletContext application = sce.getServletContext();
		String contextPath = application.getContextPath();
		System.out.println(contextPath);
		application.setAttribute("APP_PATH", contextPath);
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {

	}

}
