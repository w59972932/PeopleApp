package com.hyjx.peoplelibrary.tree;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;


import com.hyjx.peoplelibrary.R;
import com.hyjx.peoplelibrary.utils.JsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/***
 * @author ljx 2015-11-10
 */
public class TreeActivity extends Activity implements OnItemClickListener {

	ListView code_list;
	private Button bt_clear;
	private Button bt_confirm;
	TreeActivity oThis = this;
	List<Map<String, String>> userList = new ArrayList<Map<String, String>>();
	Map<String, Object> departList = new HashMap();
	private String isCheckPerson = "";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_tree);

		code_list = (ListView)findViewById(R.id.list_tree_code_list);
		bt_clear = (Button) findViewById(R.id.bt_clear);
		bt_confirm = (Button) findViewById(R.id.bt_confirm);
		code_list.setOnItemClickListener(this);

		setToolBar();
		if(getIntent().getStringExtra("departList") != null && !"".equals(getIntent().getStringExtra("departList"))){
			departList = JsonUtil.parseJson(getIntent().getStringExtra("departList"));
		}
		isCheckPerson = getIntent().getStringExtra("isCheckPerson");
		setDepart();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
		// 这句话写在最后面
		((TreeAdapter)parent.getAdapter()).ExpandOrCollapse(position);
	}

	// 设置一层节点,可以通过循环或递归方式添加节点
	private void setDepart(){

		// 创建根节点
		Node root = new Node(departList.get("departName").toString(),departList.get("departCode").toString());
		root.setIcon(R.drawable.icon_department);//设置图标
		root.setCheckBox(false);//设置节点前有无复选框

		List<Map<String, Object>> ryList = (List<Map<String, Object>>) departList.get("children");

		for(int i=0;i<ryList.size();i++){
			// 创建1级子节点
			Node n = new Node(ryList.get(i).get("userName").toString(),ryList.get(i).get("userId").toString());
			n.setParent(root);//设置父节点
			n.setIcon(R.drawable.icon_police);
			n.setCheckBox(true);
			if(isCheckPerson.contains(ryList.get(i).get("userId").toString())){
				n.setChecked(true);
			}
			root.add(n);
		}
		TreeAdapter ta = new TreeAdapter(oThis,root);
		// 设置整个树是否显示复选框
//		ta.setCheckBox(true);
		// 设置展开和折叠时图标
		ta.setExpandedCollapsedIcon(R.drawable.tree_ex, R.drawable.tree_ec);
		// 设置默认展开级别
		ta.setExpandLevel(1);
		code_list.setAdapter(ta);
	}


	// 设置三层节点,可以通过循环或递归方式添加节点
//	private void setThreePreson(){
//
//		// 创建根节点
//		Node root = new Node("怀远县市场监督管理局","340321");
//		root.setIcon(R.drawable.icon_department);//设置图标
//		root.setCheckBox(false);//设置节点前有无复选框
//
//		List<Map<String,String>> departData = new ArrayList<Map<String, String>>();
//
//		for(int i=0;i<departData.size();i++){
//			// 创建1级子节点
//			Node n = new Node(departData.get(i).get("DEPART_NAME"),departData.get(i).get("DEPART_ID"));
//			n.setParent(root);//设置父节点
//			n.setIcon(R.drawable.icon_department);
//			List<Map<String,String>> bmData = (List<Map<String, String>>) getIntent().getSerializableExtra("departList");
//			for(int z=0;z<bmData.size();z++){
//				Node nTwo = new Node(bmData.get(z).get("DEPART_NAME"),bmData.get(z).get("DEPART_ID"));
//				nTwo.setParent(n);
//				nTwo.setIcon(R.drawable.icon_department);
//
//				List<Map<String,String>> ryData = (List<Map<String, String>>) getIntent().getSerializableExtra("userList");
//				if(ryData != null){
//					for(int k=0;k<ryData.size();k++){
//						Node nThree = new Node(ryData.get(k).get("USER_NAME"),ryData.get(k).get("USER_ID"));
//						nThree.setParent(nTwo);
//						nThree.setIcon(R.drawable.icon_police);
//						nTwo.add(nThree);
//					}
//				}
//				n.add(nTwo);
//			}
//			root.add(n);
//		}
//
//
//		TreeAdapter ta = new TreeAdapter(oThis,root);
//		// 设置整个树是否显示复选框
//		ta.setCheckBox(true);
//		// 设置展开和折叠时图标
//		ta.setExpandedCollapsedIcon(R.drawable.tree_ex, R.drawable.tree_ec);
//		// 设置默认展开级别
//		ta.setExpandLevel(1);
//		code_list.setAdapter(ta);
//	}

	// 设置底部工具栏
	private void setToolBar(){
		bt_clear.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				oThis.finish();
			}
		});

		bt_confirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				List<Node> nodes = ((TreeAdapter) code_list.getAdapter()).getSeletedNodes();
				String jcrName = "";
				String jcrId = "";

				for (int i = 0; i < nodes.size(); i++) {
					Node n = nodes.get(i);
					if("depart".equals(getIntent().getStringExtra("type"))){
						jcrName += n.getText() + ",";
						jcrId += n.getValue() + ",";
					}else{
						if (n.isLeaf()) {
							jcrName += n.getText() + ",";
							jcrId += n.getValue() + ",";
						}
					}
				}
				Intent intent = new Intent();
				if (!"".equals(jcrName)) {
					intent.putExtra("jcrName", jcrName.substring(0, jcrName.length() - 1));
					intent.putExtra("jcrId", jcrId.substring(0, jcrId.length() - 1));
				} else {
					intent.putExtra("jcrName", "");
					intent.putExtra("jcrId", "");
				}
				setResult(1018, intent);
				oThis.finish();
			}
		});
	}
}