package com.skeletonapp.android.presenters;

import com.skeletonapp.android.R;
import com.skeletonapp.views.IBaseView;

import android.app.Activity;
import android.content.Intent;

public abstract class PresenterBase<T extends IBaseView> implements IBasePresenter<T>
{
	private T _view;

	public PresenterBase(T view)
	{
		_view = view;
	}

//	protected IAPIClient getBreezyClient()
//	{
//		return _breezyClient;
//	}

	public void logout()
	{
		// TODO Auto-generated method stub

	}

	public void trackViewOpen()
	{
		// TODO Auto-generated method stub

	}

	public void trackViewClosed()
	{
		// TODO Auto-generated method stub

	}

	public T getView()
	{
		return _view;
	}

	public void setView(T view)
	{
		_view = view;
	}

	protected void openActivity(Class<? extends Activity> clazz)
	{
		Intent intent = new Intent(getView().getContext(), clazz);
		openActivity(intent);
	}

	protected void openActivity(Intent intent)
	{
		getView().getContext().startActivity(intent);
		getView().getContext().overridePendingTransition(R.anim.view_transition_in_left, R.anim.view_transition_out_left);
	}
}
