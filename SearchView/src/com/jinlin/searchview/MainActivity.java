/********************************************************** 
 * @文件名称：MainActivity.java 
 * @创建时间：2014年11月22日 下午9:05:06
 * @修改历史：2014年11月22日
 **********************************************************/
package com.jinlin.searchview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author J!nl!n
 * @date 2014年11月22日
 * @time 下午9:05:06
 * @type MainActivity.java
 * @todo
 */
public class MainActivity extends Activity implements OnClickListener {
	private TextView tv_top_title;
	private LinearLayout container;
	private Button[] buttons;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tv_top_title = (TextView) findViewById(R.id.tv_top_title);
		tv_top_title.setText("仿qq搜索实现");
		
		container = (LinearLayout) findViewById(R.id.container);

		buttons = new Button[3];

		for (int i = 0; i < 3; i++) {
			buttons[i] = new Button(this);
			buttons[i].setText("第" + (i + 1) + "种方式");
			buttons[i].setOnClickListener(this);
			buttons[i].setId(i);
			RelativeLayout.LayoutParams btParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 80); // 设置按钮的宽度和高度
			container.addView(buttons[i], btParams);
		}

	}

	@Override
	public void onClick(View v) {
		Class<?>[] activities = {Activity1.class, Activity2.class, Activity3.class};
		
		for (int i = 0; i < 3; i++) {
			if (v.getId() == buttons[i].getId()) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, activities[i]);
				startActivity(intent);
			}
		}
//		MainActivity.this.finish();
	}

}
