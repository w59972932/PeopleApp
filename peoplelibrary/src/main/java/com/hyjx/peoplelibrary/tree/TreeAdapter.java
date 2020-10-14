package com.hyjx.peoplelibrary.tree;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyjx.peoplelibrary.R;

import java.util.ArrayList;
import java.util.List;


public class TreeAdapter extends BaseAdapter {
	private Context con;
	private LayoutInflater lif;
	private List<Node> all = new ArrayList<Node>();//展示
	private List<Node> cache = new ArrayList<Node>();//缓存
	private TreeAdapter tree = this;
	boolean hasCheckBox;
	private int expandIcon = -1;//展开图标
	private int collapseIcon = -1;//收缩图标
	boolean isCheckBox = true;//默认多选 false为单选

	/**
	 * 构造方法多选
	 */
	public TreeAdapter(Context context, Node rootNode){
		this.con = context;
		this.lif = (LayoutInflater)con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		addNode(rootNode);
	}
	/**
	 * 构造方法单选
	 */
	public TreeAdapter(Context context, Node rootNode, boolean isCheckBox){
		this.con = context;
		this.lif = (LayoutInflater)con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.isCheckBox = isCheckBox;
		addNode(rootNode);
	}
	/**
	 * 把一个节点上的所有的内容都挂上去
	 * @param node
	 *
	 */
	public void addNode(Node node){
		all.add(node);
		cache.add(node);
		if(node.isLeaf())return;
		for(int i = 0;i<node.getChildren().size();i++){
			addNode(node.getChildren().get(i));
		}
	}

	private void checkNode(Node node, boolean isChecked){
		node.setChecked(isChecked);
		for(int i=0;i<node.getChildren().size();i++){
			checkNode(node.getChildren().get(i), isChecked);
		}
	}
	/**
	 * 获取所有选中节点
	 * @return
	 *
	 */
	public List<Node> getSeletedNodes(){
		List<Node> checks =new ArrayList<Node>()	;
		for(int i = 0;i<cache.size();i++){
			Node n =(Node)cache.get(i);
			if(n.isChecked())
				checks.add(n);
		}
		return checks;
	}
	/**
	 * 设置是否有复选框
	 * @param hasCheckBox
	 *
	 */
	public void setCheckBox(boolean hasCheckBox){
		this.hasCheckBox = hasCheckBox;
	}

	public void setExpandedCollapsedIcon(int expandIcon,int collapseIcon){
		this.expandIcon = expandIcon;
		this.collapseIcon = collapseIcon;
	}
	/**
	 * 控制展开缩放某节点
	 * @param location
	 *
	 */
	public void ExpandOrCollapse(int location){
		Node n = all.get(location);//获得当前视图需要处理的节点
		if(n!=null)//排除传入参数错误异常
		{
			if(!n.isLeaf()){
				n.setExpanded(!n.isExpanded());// 由于该方法是用来控制展开和收缩的，所以取反即可
				filterNode();//遍历一下，将所有上级节点展开的节点重新挂上去
				this.notifyDataSetChanged();//刷新视图
			}
		}

	}
	/**
	 * 设置展开等级
	 * @param level
	 *
	 */
	public void setExpandLevel(int level){
		all.clear();
		for(int i = 0;i<cache.size();i++){
			Node n = cache.get(i);
			if(n.getLevel()<=level){
				if(n.getLevel()<level)
					n.setExpanded(true);
				else
					n.setExpanded(false);
				all.add(n);
			}
		}

	}
	/* 清理all,从缓存中将所有父节点不为收缩状态的都挂上去*/
	public void filterNode(){
		all.clear();
		for(int i = 0;i<cache.size();i++){
			Node n = cache.get(i);
			if(!n.isParentCollapsed()||n.isRoot())//凡是父节点不收缩或者不是根节点的都挂上去
				all.add(n);
		}
	}
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return all.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int location) {
		// TODO Auto-generated method stub
		return all.get(location);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int location) {
		// TODO Auto-generated method stub
		return location;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int location, View view, ViewGroup viewgroup) {

		ViewHolder holder = null;
		if(view == null){
			view = lif.inflate(R.layout.list_item, null);
			holder = new ViewHolder();
			holder.chbSelect = (CheckBox)view.findViewById(R.id.list_tree_listview_item_chbSelect);
			holder.chbSelect.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Node n = (Node)v.getTag();
					if(isCheckBox){
						checkNode(n,((CheckBox)v).isChecked());
					}else{
						checkNode(n,false);
						if(n.getLevel() != 0){
							checkNode(n.getParent(),false);
						}
						n.setChecked(true);
					}
					tree.notifyDataSetChanged();
				}

			});
			holder.ivIcon = (ImageView)view.findViewById(R.id.list_tree_listview_item_ivIcon);
			holder.tvText = (TextView)view.findViewById(R.id.list_tree_listview_item_tvText);
			holder.ivExEc = (ImageView)view.findViewById(R.id.list_tree_listview_item_ivExEc);
			view.setTag(holder);
		}else{
			holder = (ViewHolder)view.getTag();
		}
		Node n = all.get(location);
		if(n!=null){
			holder.chbSelect.setTag(n);
			holder.chbSelect.setChecked(n.isChecked());

			if(n.isLeaf()){//叶节点 显示 无孩子的图标
				holder.ivExEc.setVisibility(View.GONE);
			}
			else{
				holder.ivExEc.setVisibility(View.VISIBLE);
				if(n.isExpanded()){
					if(expandIcon != -1)
						holder.ivExEc.setImageResource(expandIcon);
				}
				else {
					if(collapseIcon != -1)
						holder.ivExEc.setImageResource(collapseIcon);
				}
			}
			//设置是否显示复选框
			if(n.hasCheckBox()){
				holder.chbSelect.setVisibility(View.VISIBLE);
			}
			else{
				holder.chbSelect.setVisibility(View.GONE);
			}

			// 自己修改的
			if(n.getIcon()==-1){
//				holder.ivIcon.setImageResource(R.drawable.outline_list_expand);
				holder.ivIcon.setVisibility(View.VISIBLE);
			}else{
				holder.ivIcon.setVisibility(View.VISIBLE);
				holder.ivIcon.setImageResource(n.getIcon());
			}
			//显示文本
			holder.tvText.setText(n.getText());
			// 控制缩进
			view.setPadding(35*n.getLevel(), 3,3, 3);
		}
		return view;
	}

	public class ViewHolder{
		CheckBox chbSelect;
		ImageView ivIcon;
		TextView tvText;
		ImageView ivExEc;
	}
}
