package com.hyjx.peoplelibrary.tree;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.hyjx.peoplelibrary.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 底部工具栏数据适配器
 *
 * @author ljx 2015-11-10
 *
 */
public class ToolbarAdapter extends BaseAdapter {
	// 创建上下文对象
	private Context con;
	// 创建一个map类型的集合用于装载所有的节点
	private List<Map<String, Object>> alls = new ArrayList<Map<String, Object>>();
	// 创建打气筒用于加载布局
	private LayoutInflater lif;
	// 创建一个整型的集合，用于存储可点击的节点的集合
	private List<Integer> posClickable = new ArrayList<Integer>();

	// / 设置绑定字段
	public static final String NAME = "name";// 文字
	public static final String IMAGE = "image";// 图片

	/**
	 * 构造函数
	 *
	 * @param context
	 * @param name_array 菜单文字数组
	 * @param image_array 菜单图片数组
	 * @param position 可以点击的菜单位置数组
	 */
	public ToolbarAdapter(Context context, String[] name_array,
                          int[] image_array, int[] position) {
		this.con = context;
		this.lif = (LayoutInflater) con
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// 对name数组和Image数组进行判断，如果不为空则进行遍历，分别将元素添加到map集合中去，
		// 最后将map集合添加到all集合中去
		if (name_array != null && name_array.length > 0) {
			for (int i = 0; i < name_array.length; i++) {
				Map<String, Object> item = new HashMap<String, Object>();
				item.put(NAME, name_array[i]);
				// 如果图标数组不为空，并且图标数组的长度和名字数组的长度一样，则将相应的图标添加到对应的节点上。
				if (image_array != null
						&& image_array.length == name_array.length) {
					item.put(IMAGE, image_array[i]);
				}
				alls.add(item);
			}
		}

		if (position != null && position.length > 0) {
			for (int i = 0; i < position.length; i++) {
				posClickable.add(position[i]);
			}
		}

	}

	// 获取节点的个数
	@Override
	public int getCount() {
		return alls.size();
	}

	// 获取具体的某一个节点对象
	@Override
	public Object getItem(int position) {
		return alls.get(position);
	}

	// 获取节点所对应Position
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	// 获取某一节点的文字信息
	public String getBh(int position) {
		Map<String, Object> item = alls.get(position);
		return item.get(NAME).toString();
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		if (view == null) {
			view = this.lif.inflate(R.layout.list_tree_item_toolbar, null);
			// 获取要加载节点
			Map<String, Object> mapItem = alls.get(position);
			ViewHolder holder = new ViewHolder();
			holder.tvText = (TextView) view.findViewById(R.id.jkts_item_text);

			if (mapItem != null) {
				String name = mapItem.get(NAME).toString();
				holder.tvText.setText(name);
			}
			view.setFocusable(false);
			view.setClickable(false);

			// 控制是否可以点击和获得焦点
			if (!posClickable.contains(position)) {
				view.setFocusable(true);
				view.setClickable(true);
			}
		}
		return view;
	}

	/**
	 *
	 * 列表项控件集合
	 *
	 */
	private class ViewHolder {
		private TextView tvText;
	}
}
