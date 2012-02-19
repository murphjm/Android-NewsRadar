package com.skeletonapp.android.presenters;

public interface IBasePresenter<T> {
	public T getView();
	public void setView(T view);
}
