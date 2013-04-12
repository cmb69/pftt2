package com.mostc.pftt.model.ui;

public interface UITestPack {
	String getNameAndVersionInfo();
	String getBaseURL();
	boolean isDevelopment();
	String getNotes();
	void start(IUITestBranch runner);
}
