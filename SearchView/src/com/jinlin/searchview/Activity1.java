/********************************************************** 
 * @文件名称：MainActivity.java 
 * @创建时间：2014年11月21日 下午8:12:24
 * @修改历史：2014年11月21日
 **********************************************************/
package com.jinlin.searchview;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

/**
 * @author J!nl!n
 * @date 2014年11月21日
 * @time 下午8:12:24
 * @type MainActivity.java
 * @todo
 */
public class Activity1 extends Activity implements OnClickListener{
	protected static final String TAG = Activity1.class.getSimpleName();

	// 容器布局
	private LinearLayout container;

	// 标题栏布局
	private View titleView;

	private View searchView;

	private ListView listView;

	// 输入框是否获取到焦点
	private static boolean isFocused = false;

	// 图标居中输入框
	private EditText et_search;

	// 真是输入框
	private EditText et_input;

	// 取消按钮
	private Button btn_cancel;

	// 显示或隐藏软键盘
	private enum KeyBoardType {
		show, hide;
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity1);
		initView();
	}

	private List<String> getData() {
		List<String> data = new ArrayList<String>();
		for (int i = 0; i < 100; i++) {
			data.add("测试数据" + i);
		}
		return data;
	}

	/**
	 * 【未做验证】这里需要注意在removeView之后，标题栏设置的监听事件是否失去，倘若在点击取消调用addView方法之后是否需要重新设置监听
	 */
	private void initView() {
		// 找到整个容器的布局
		container = ((LinearLayout) findViewById(R.id.container));
		// 获取标题栏布局
		titleView = findViewById(R.id.title_header);
		
		listView = (ListView) findViewById(R.id.listview);
		listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, getData()));
		
		// 找到隐藏的搜索布局
		searchView = findViewById(R.id.floatview);

		et_search = (EditText) findViewById(R.id.et_search);
		et_input = (EditText) findViewById(R.id.et_input);
		// 取消按钮
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		// 取消按钮设置监听
		btn_cancel.setOnClickListener(this);

		et_search.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					isFocused = true;
					// 显示隐藏的布局
					showSearchView(titleView);
					// 对软键盘及焦点的操作
					doSomething();
					break;
				}
				return false;
			}
		});
	}

	private void doSomething() {
		et_search.setFocusable(false);
		if (isFocused) {
			Toast.makeText(Activity1.this, "获取焦点", Toast.LENGTH_SHORT).show();
			et_input.setFocusable(true);
			et_input.setFocusableInTouchMode(true);
			et_input.requestFocus();
			// 显示软键盘
			operateKeyboard(KeyBoardType.show, container);
		}
	}
	
	/**
	 * 隐藏软键盘
	 * 
	 * @param v
	 */
	private void operateKeyboard(KeyBoardType type, View v) {
		InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (type == KeyBoardType.show)
			imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
		else
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		setKeyboardDoneListener();
	}

	private void setKeyboardDoneListener() {
		// 软键盘回车完成监听
		et_input.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					operateKeyboard(KeyBoardType.hide, v);
					Log.d("1111", "done");

					// do something
					Toast.makeText(Activity1.this, et_input.getText().toString().trim(), Toast.LENGTH_SHORT).show();
					return true;
				}
				return false;
			}
		});
	}

	public void showSearchView(View view) {
		
		Animation translateAnimation = new TranslateAnimation(0, 0, 0, -getHeight(view));
		translateAnimation.setDuration(300);
		container.startAnimation(translateAnimation);
		translateAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				TranslateAnimation anim = new TranslateAnimation(0, 0, 0, 0);
				container.setAnimation(anim);
				
				titleView.setVisibility(View.GONE);
				titleView.setPadding(0, -getHeight(titleView), 0, 0);
				// 显示隐藏的布局
				searchView.setVisibility(View.VISIBLE);
			}
		});
	}
	
	/**
	 * 
	 */
	private int getHeight(View view) {
		return view.getHeight();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		Toast.makeText(this, "高度是：" + getHeight(titleView), Toast.LENGTH_SHORT).show();
	}
	
	private void resetUI(View view) {
		operateKeyboard(KeyBoardType.hide, view);
		// 继续隐藏搜索布局
		searchView.setVisibility(View.GONE);
		
		titleView.setPadding(0, 0, 0, 0);
		titleView.setVisibility(View.VISIBLE);
		
		Animation translateAnimation = new TranslateAnimation(0, 0, -getHeight(titleView), 0);
		translateAnimation.setDuration(300);
		container.startAnimation(translateAnimation);
		translateAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {
				
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {

			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				TranslateAnimation anim = new TranslateAnimation(0, 0, 0, 0);
				container.setAnimation(anim);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_cancel:
			isFocused = false;
			resetUI(v);
			break;

		default:
			break;
		}
	}

}
