package common.list.adapter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;



public class MyListAdapter extends BaseAdapter {
	private List<JSONObject> lstData = new ArrayList<JSONObject>();
	private int mResource; 
	private LayoutInflater mInflater;
	protected ItemCallBack m_callBack = null;
	protected Context		m_context = null;
	
	
	public MyListAdapter(Context context, List<JSONObject> data,
			int resource, ItemCallBack callback) {
		super();
		lstData = data;
		mResource = resource;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		m_callBack = callback;
		
		m_context = context;
	}
	
	public List<JSONObject> getData()
	{
		return lstData;
	}
	
	public void setData(List<JSONObject> list)
	{
		if( list == null )
			lstData = new ArrayList<JSONObject>();
		else
			lstData = list;
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {		
		int count = lstData.size();

		return count;
	}

	@Override
	public JSONObject getItem(int i) {
		if(lstData==null || lstData.size()<=i) {
			return null;
		}
		return lstData.get(i);
	}

	@Override
	public long getItemId(int i) {
		// TODO Auto-generated method stub
		return i;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewgroup) {
		// TODO Auto-generated method stub
		if( view == null )
			view = mInflater.inflate(mResource, null);
				
		if( view != null )
			loadItemViews(view, i);
		
		return view;
	}
	
	public void addItemList(List<JSONObject> data)
	{
		if( data == null || data.size() < 1 )
			return;
		
		if( lstData == null )
		{
			lstData = data;
		}
		else
		{
			for( int i = 0; i < data.size(); i++ )
				lstData.add(data.get(i));			
		}
		this.notifyDataSetChanged();
	}
	
	public void replaceItemList(List<JSONObject> data, int start)
	{
		if( data == null || data.size() < 1 )
			return;
		
		if( lstData == null )
		{
			lstData = data;
		}
		else
		{
			if( start >= lstData.size() )
				lstData.addAll(data);
			else
			{
				int count = Math.min(lstData.size() - start, data.size());
				for(int i = 0; i < count; i++ )
				{
					lstData.remove(start);
				}
				lstData.addAll(start, data);
			}
			
		}
		this.notifyDataSetChanged();
	}
	protected void loadItemViews(View rowView, int position)
	{
		
	}
}
