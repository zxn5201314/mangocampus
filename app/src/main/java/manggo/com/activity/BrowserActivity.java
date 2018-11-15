package manggo.com.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient.CustomViewCallback;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tencent.smtt.utils.TbsLog;

import java.net.MalformedURLException;
import java.net.URL;

import manggo.com.R;
import manggo.com.c5core.X5WebView;
import manggo.com.downloadutil.DownloadUtil;
import manggo.com.util.SwitchAnim;


public class BrowserActivity extends AppCompatActivity {
	/**
	 * 作为一个浏览器的示例展示出来，采用android+web的模式
	 */
	public static final int MSG_INIT_UI = 1;//更新UI的信号
	private X5WebView mWebView;//声明一个x5内核
	private ViewGroup viewParent;//父容器
//	private static final String loadUrl = "http://ldsh.gznu.edu.cn/dsh/waphome";
	private static final String TAG = "Sdk";
	private ValueCallback<Uri> uploadFile;
	private URL mIntentUrl;
	private Toolbar toolbar;//支持该Bar
	private TextView textView;//显示文字
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		Intent intent = getIntent();//获得信使携带的数据
		getIntentData(intent);
        //页面执行需要展示的动画
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
		setContentView(R.layout.activity_browser);
		//设置动画
        SwitchAnim.switchAnimUtil(this,getWindow());
		showBar();
		viewParent = (ViewGroup) findViewById(R.id.webview_frame_id);//获得布局管理器
		handler.sendEmptyMessageDelayed(MSG_INIT_UI, 10);//通知子线程更新UI

	}
	/**
	 * 显示bar
	 */
	private void showBar(){
		textView=(TextView)findViewById(R.id.browser_tv_id);
		toolbar=(Toolbar)findViewById(R.id.browser_toolbar_id);
		setSupportActionBar(toolbar);
		ActionBar bar=getSupportActionBar();
		if(bar!=null){
			bar.setDisplayShowTitleEnabled(false);
			bar.setDisplayHomeAsUpEnabled(true);//设置将该图标设计为可以点击的图标
			bar.setHomeAsUpIndicator(R.drawable.ic_clear_black_30dp);//添加左边图标
		}
	}

	/**
	 * 获得信使携带的数据
	 * @param intent
	 */
	private void getIntentData(Intent intent){
		if (intent != null) {
			try {
				mIntentUrl = new URL(getIntentSendData());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {

			} catch (Exception e) {
			}
		}
		//
		try {
			if (Build.VERSION.SDK_INT>= 11) {
				getWindow().setFlags(
								android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
								android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
			}
		} catch (Exception e) {
		}
	}
	/**
	 * 初始化方法,携带要读取的uri
	 */
	private void init(String uri,String title) {
		mWebView = new X5WebView(this, null);
		//父容器动态添加X5内核
		viewParent.addView(mWebView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
			}
		});

		mWebView.setWebChromeClient(new WebChromeClient() {

			@Override
			public boolean onJsConfirm(WebView arg0, String arg1, String arg2,
                                       JsResult arg3) {
				return super.onJsConfirm(arg0, arg1, arg2, arg3);
			}

			View myVideoView;
			View myNormalView;
			CustomViewCallback callback;
			/**
			 * 全屏播放配置
			 */
			@Override
			public void onShowCustomView(View view, CustomViewCallback customViewCallback) {
				CoordinatorLayout normalView = (CoordinatorLayout) findViewById(R.id.web_filechooser_id);
				ViewGroup viewGroup = (ViewGroup) normalView.getParent();
				viewGroup.removeView(normalView);
				viewGroup.addView(view);
				myVideoView = view;
				myNormalView = normalView;
				callback = customViewCallback;
			}

			@Override
			public void onHideCustomView() {
				if (callback != null) {
					callback.onCustomViewHidden();
					callback = null;
				}
				if (myVideoView != null) {
					ViewGroup viewGroup = (ViewGroup) myVideoView.getParent();
					viewGroup.removeView(myVideoView);
					viewGroup.addView(myNormalView);
				}
			}

			@Override
			public boolean onJsAlert(WebView arg0, String arg1, String arg2,
                                     JsResult arg3) {
				/**
				 * 这里写入你自定义的window alert
				 */
				return super.onJsAlert(null, arg1, arg2, arg3);
			}
		});
		/**
		 * 下载文件的url
		 */
		mWebView.setDownloadListener(new DownloadListener() {
			@Override
			public void onDownloadStart(final String url1, String userAgent, final String contentDisposition, final String mimetype, long contentLength) {
				Log.e(TAG, "要下载的URLurl: " +url1+userAgent+contentDisposition+mimetype);
				new AlertDialog.Builder(BrowserActivity.this).setTitle("是否下载文件？").setPositiveButton("是", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										new DownloadUtil().download(url1,contentDisposition,mimetype,BrowserActivity.this);
										Toast.makeText(
												BrowserActivity.this,
												"开始下载",
												Toast.LENGTH_SHORT).show();
									}
								})
						.setNegativeButton("否", new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										Toast.makeText(
												BrowserActivity.this,
												"点击了取消",
												Toast.LENGTH_SHORT).show();
									}
								}).setOnCancelListener(new DialogInterface.OnCancelListener() {

									@Override
									public void onCancel(DialogInterface dialog) {
										Toast.makeText(
												BrowserActivity.this,
												"取消了下载",
												Toast.LENGTH_SHORT).show();
									}
								}).show();
			}
		});

		WebSettings webSetting = mWebView.getSettings();
		//设置自适应屏幕，两者合用
		webSetting.setUseWideViewPort(true); //将图片调整到适合webview的大小
		webSetting.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
//缩放操作
		webSetting.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
		webSetting.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
		webSetting.setDisplayZoomControls(false); //隐藏原生的缩放控件
		webSetting.setAllowFileAccess(true);
		webSetting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		webSetting.setSupportZoom(true);
		webSetting.setBuiltInZoomControls(true);
		webSetting.setUseWideViewPort(true);
		webSetting.setSupportMultipleWindows(false);
		// webSetting.setLoadWithOverviewMode(true);
		webSetting.setAppCacheEnabled(true);
		// webSetting.setDatabaseEnabled(true);
		webSetting.setDomStorageEnabled(true);
		webSetting.setJavaScriptEnabled(true);
		webSetting.setGeolocationEnabled(true);
		webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
		webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
		webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
		webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0).getPath());
		webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
		webSetting.setJavaScriptCanOpenWindowsAutomatically(false);//禁止自动弹出窗口
		textView.setText(title);
		long time = System.currentTimeMillis();
		if (mIntentUrl == null) {
			mWebView.loadUrl(getIntentSendData());
		} else {
			mWebView.loadUrl(mIntentUrl.toString());
		}
		TbsLog.d("time-cost", "cost time: "
				+ (System.currentTimeMillis() - time));
		CookieSyncManager.createInstance(this);
		CookieSyncManager.getInstance().sync();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mWebView != null && mWebView.canGoBack()) {
				mWebView.goBack();
				if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 16)
				return true;
			} else
				return super.onKeyDown(keyCode, event);
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		TbsLog.d(TAG, "onActivityResult, requestCode:" + requestCode
				+ ",resultCode:" + resultCode);

		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 0:
				if (null != uploadFile) {
					Uri result = data == null || resultCode != RESULT_OK ? null
							: data.getData();
					uploadFile.onReceiveValue(result);
					uploadFile = null;
				}
				break;
			default:
				break;
			}
		} else if (resultCode == RESULT_CANCELED) {
			if (null != uploadFile) {
				uploadFile.onReceiveValue(null);
				uploadFile = null;
			}

		}

	}

	@Override
	protected void onNewIntent(Intent intent) {
		if (intent == null || mWebView == null || intent.getData() == null)
			return;
		mWebView.loadUrl(getIntentSendData());
	}

	@Override
	protected void onDestroy() {
		if (handler != null)
			handler.removeCallbacksAndMessages(null);
		if (mWebView != null)
			mWebView.destroy();
		super.onDestroy();
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_INIT_UI:
				init(getIntentSendData(),getIntentSendDataTitle());
				break;
			}
			super.handleMessage(msg);
		}
	};
    /**
     * 获得上一个activity传递的数据
     */

    private String getIntentSendData(){
        String uri=null;
        Intent intent=getIntent();
        uri=intent.getStringExtra("EAS");
        Log.e("获得携带的数据为:",uri);
        return uri;
    }
    /**
     * 获得上一个activity传递的数据
     */

    private String getIntentSendDataTitle(){
        String title=null;
        Intent intent=getIntent();
        title=intent.getStringExtra("title");
        Log.e("获得携带的数据为:",title);
        return title;
    }

    /**
     * 重写菜单方法
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_bar,menu);
        return true;
    }
    /**
     * 重写菜单点击的方法
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

}
