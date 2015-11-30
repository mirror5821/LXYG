package dev.mirror.library.adapter;

import java.util.List;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class DevListBaseAdapter<T> extends BaseAdapter {
	private List<T> mList;
	
	public DevListBaseAdapter(Context context,List<T> list){
		this.mList = list;
	}
	
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public T getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return initView(position, convertView, parent);
	}
	
	public abstract View initView(int position, View convertView, ViewGroup parent);
	
	public void addItem(T item){
		mList.add(item);
	}
	
	public void addItem(T item,int position){
		mList.add(position, item);
	}
	
	public void removeItem(int position){
		mList.remove(position);
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static class ViewHolder{
		public static <T extends View> View get(View view,int id){
			SparseArray viewHolder = (SparseArray)view.getTag();
			if(viewHolder == null){
				viewHolder = new SparseArray();
				view.setTag(viewHolder);
			}
			
			View childView = (View)viewHolder.get(id);
			if(childView == null){
				childView = view.findViewById(id);
				viewHolder.put(id, childView);
			}
			return childView;
			
		}
	}
	
}
