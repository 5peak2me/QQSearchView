/********************************************************** 
 * @文件名称：Activity2.java 
 * @创建时间：2014年11月22日 下午9:54:31
 * @修改历史：2014年11月22日
 **********************************************************/
package com.jinlin.searchview;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author J!nl!n
 * @date 2014年11月22日
 * @time 下午9:54:31
 * @type Activity2.java
 * @todo
 */
public class Activity2 extends Activity implements OnClickListener, OnItemClickListener,
		PopupWindow.OnDismissListener {
	private TextView tv_top_title;
	
	private ListView listView;
	private View headerView;
	private TextView tv_search;

	// show and hide
	private RelativeLayout mainLayout;
	private RelativeLayout titleBarLayout;
	private int moveHeight;
	private int statusBarHeight;

	// search popupwindow
	private PopupWindow popupWindow;
	private View searchView;
	private EditText searchEditText;
	private TextView cancelTextView;
	private ListView filterListView;
	private View alphaView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity2);

		tv_top_title = (TextView) findViewById(R.id.tv_top_title);
		tv_top_title.setText("我是第二种");
		initCtrl();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.tv_search:
			showSearchBar();
			break;
		case R.id.popup_window_tv_cancel:
			dismissPopupWindow();
			break;
		case R.id.popup_window_v_alpha:
			dismissPopupWindow();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> viewGroup, View view, int position, long arg3) {
		switch (viewGroup.getId()) {
		case R.id.lv:
			if (position == 0) {
				showSearchBar();
			}
			break;
		case R.id.popup_window_lv:
			Toast.makeText(Activity2.this, "click-" + position, Toast.LENGTH_LONG).show();
			break;
		}
	}

	@Override
	public void onDismiss() {
		resetUI();
	}

	private void initCtrl() {
		listView = (ListView) findViewById(R.id.lv);
		LayoutInflater mInflater = LayoutInflater.from(this);
		headerView = mInflater.inflate(R.layout.activity2_3_search, null);
		listView.addHeaderView(headerView);
		listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getData()));
		listView.setOnItemClickListener(this);
		tv_search = (TextView) headerView.findViewById(R.id.tv_search);
		tv_search.setOnClickListener(this);

		mainLayout = (RelativeLayout) findViewById(R.id.main);
		titleBarLayout = (RelativeLayout) findViewById(R.id.title_bar_layout);

		searchView = mInflater.inflate(R.layout.popup_window_search, null);
		searchEditText = (EditText) searchView.findViewById(R.id.popup_window_et_search);
		searchEditText.setFocusable(true);
		searchEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.toString().equals("")) {
					alphaView.setVisibility(View.VISIBLE);
					filterListView.setVisibility(View.GONE);
				} else {
					alphaView.setVisibility(View.GONE);
					filterListView.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		cancelTextView = (TextView) searchView.findViewById(R.id.popup_window_tv_cancel);
		cancelTextView.setOnClickListener(this);
		filterListView = (ListView) searchView.findViewById(R.id.popup_window_lv);
		filterListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getData2()));
		filterListView.setOnItemClickListener(this);
		alphaView = searchView.findViewById(R.id.popup_window_v_alpha);
		alphaView.setOnClickListener(this);

		popupWindow = new PopupWindow(searchView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOnDismissListener(this);
	}

	private List<String> getData() {
		List<String> data = new ArrayList<String>();
		for (int i = 0; i < 100; i++) {
			data.add("测试数据" + i);
		}
		return data;
	}

	private List<String> getData2() {
		List<String> data = new ArrayList<String>();
		for (int i = 0; i < 100; i++) {
			data.add("搜索数据" + i);
		}
		return data;
	}

	private void getStatusBarHeight() {
		Rect frame = new Rect();
		getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		statusBarHeight = frame.top;
	}

	private void showSearchBar() {
		getStatusBarHeight();
		moveHeight = titleBarLayout.getHeight();
		Animation translateAnimation = new TranslateAnimation(0, 0, 0, -moveHeight);
		translateAnimation.setDuration(300);
		mainLayout.startAnimation(translateAnimation);
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
				mainLayout.setAnimation(anim);
				titleBarLayout.setVisibility(View.GONE);
				titleBarLayout.setPadding(0, -moveHeight, 0, 0);

				popupWindow.showAtLocation(mainLayout, Gravity.CLIP_VERTICAL, 0, statusBarHeight);
				openKeyboard();
			}
		});

	}

	private void openKeyboard() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}, 0);
	}

	private void dismissPopupWindow() {
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
		}
	}

	private void resetUI() {
		titleBarLayout.setPadding(0, 0, 0, 0);
		titleBarLayout.setVisibility(View.VISIBLE);
		Animation translateAnimation = new TranslateAnimation(0, 0, -moveHeight, 0);
		translateAnimation.setDuration(300);
		mainLayout.startAnimation(translateAnimation);
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
				mainLayout.setAnimation(anim);
				// titleBarLayout.setPadding(0, 0, 0, 0);

			}
		});
	}

}
