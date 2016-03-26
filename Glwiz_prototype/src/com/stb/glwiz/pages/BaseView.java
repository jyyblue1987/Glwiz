package com.stb.glwiz.pages;



public interface BaseView {
	public static final String INTENT_EXTRA = "intent_extra";
	
	public void initProgress();
	public void showProgress(String title, String message);
	public void changeProgress(String title, String message);
	public void hideProgress();
	public void finishView();
}
