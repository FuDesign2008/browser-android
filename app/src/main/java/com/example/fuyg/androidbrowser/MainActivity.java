package com.example.fuyg.androidbrowser;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "WebBrowser";
    private static final String HOME_PAGE = "http://www.baidu.com/";
    private static boolean isReadyExit = false;
    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isReadyExit = false;
        }
    };
    private WebView browser;
    private WebSettings settings;
    private BrowserClient client;
    private BrowserChromeClient chromeClient;

    // url input layout
    private LinearLayout webUrlLayoutInput;
    private EditText webUrlInput;
    private Button webUrlGoto;
    private Button webUrlCancel;
    private ProgressBar progressBar;


    // url and search bar
    private LinearLayout webUrlLayoutShow;
    private Button webUrlFavorite;
    private TextView webUrlTitle;

    // bottom toolbar
    private Button prevButton;
    private Button nextButton;
    private Button toolsButton;
    private Button windowButton;
    private Button homeButton;


    private ButtonClickedListener buttonClickedListener;
    private WebUrlInputChangedListener webUrlInputChangedListener;
    private GestureDetector gestureDetector;
    private GestureListener gestureListener;
    private ToolsPopupWindow toolsPopupWindow;

    private String url = "";
    private String title = "";
    private FavoriteManager favoriteManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        browser = (WebView) findViewById(R.id.browser);
        settings = browser.getSettings();
        client = new BrowserClient();
        browser.setWebViewClient(client);
        chromeClient = new BrowserChromeClient();
        browser.setWebChromeClient(chromeClient);
        gestureListener = new GestureListener();
        gestureDetector = new GestureDetector(this, gestureListener);
        browser.setOnTouchListener(new WebViewTouchListener());
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setJavaScriptEnabled(true);

        buttonClickedListener = new ButtonClickedListener();
        webUrlInputChangedListener = new WebUrlInputChangedListener();

        // web url input bar
        webUrlInput = (EditText) findViewById(R.id.web_url_input);
        webUrlGoto = (Button) findViewById(R.id.web_url_goto);
        webUrlCancel = (Button) findViewById(R.id.web_url_cancel);
        progressBar = (ProgressBar) findViewById(R.id.web_progress_bar);
        webUrlLayoutInput = (LinearLayout) findViewById(R.id.web_url_layout);

        webUrlInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_UP && i == KeyEvent.KEYCODE_ENTER) {
                    Log.d(TAG, "url on key enter: " + url);
                    browser.loadUrl(url);
                    return true;
                }
                return false;
            }
        });
        webUrlInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d(TAG, "web url input id: " + webUrlInput.getId());
                Log.d(TAG, "focus changed: " + v.getId() + " " + hasFocus);
                if (!hasFocus) {
                    changeStateOfWebUrlLayout(false);
                }
            }
        });
        webUrlInput.addTextChangedListener(webUrlInputChangedListener);
        webUrlGoto.setOnClickListener(buttonClickedListener);
        webUrlCancel.setOnClickListener(buttonClickedListener);

        // web url and search bar
        webUrlLayoutShow = (LinearLayout) findViewById(R.id.web_url_layout_normal);
        webUrlFavorite = (Button) findViewById(R.id.web_url_show_favorite);
        webUrlTitle = (TextView) findViewById(R.id.web_url_show_title);
        webUrlFavorite.setOnClickListener(buttonClickedListener);
        webUrlTitle.setOnClickListener(buttonClickedListener);

        // bottom toolbar
        prevButton = (Button) findViewById(R.id.prev_button);
        nextButton = (Button) findViewById(R.id.next_button);
        toolsButton = (Button) findViewById(R.id.tools_button);
        windowButton = (Button) findViewById(R.id.window_button);
        homeButton = (Button) findViewById(R.id.home_button);

        prevButton.setOnClickListener(buttonClickedListener);
        nextButton.setOnClickListener(buttonClickedListener);
        toolsButton.setOnClickListener(buttonClickedListener);
        windowButton.setOnClickListener(buttonClickedListener);
        homeButton.setOnClickListener(buttonClickedListener);



    }

    private void changeStateOfWebUrlLayout(boolean showInput) {

        if (showInput) {
            webUrlLayoutInput.setVisibility(View.VISIBLE);
            webUrlLayoutShow.setVisibility(View.GONE);
            webUrlInput.requestFocus();
            showSoftKeyboard(webUrlInput);

        } else {
            webUrlInput.clearFocus();
            webUrlLayoutInput.setVisibility(View.GONE);
            webUrlLayoutShow.setVisibility(View.VISIBLE);
            hideSoftKeyboard();
        }
    }

    private void changeStateOfWebGoto(boolean showGoto) {
        if (showGoto) {
            webUrlGoto.setVisibility(View.VISIBLE);
            webUrlCancel.setVisibility(View.GONE);
        } else {
            webUrlGoto.setVisibility(View.GONE);
            webUrlCancel.setVisibility(View.VISIBLE);
        }
    }


    private void changeStateOfBottomToolbar() {
        prevButton.setEnabled(browser.canGoBack());
        nextButton.setEnabled(browser.canGoForward());
    }

    private boolean showSoftKeyboard(View targetView) {
        Log.d(TAG, "show soft keyboard");
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.showSoftInput(targetView, InputMethodManager.SHOW_FORCED);
    }

    private boolean hideSoftKeyboard() {
        Log.d(TAG, "hide soft keyboard");
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



    @Override
    public void onBackPressed() {
        if (browser.canGoBack()) {
            browser.goBack();
        } else {
            if (isReadyExit) {
                finish();
                System.exit(0);
            } else {
                isReadyExit = true;
                Toast.makeText(
                        getApplicationContext(),
                        "再按一次退出程序",
                        Toast.LENGTH_SHORT
                ).show();

                handler.sendEmptyMessageDelayed(0, 2000);
            }
        }
    }

    private class BrowserClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);

            switch (errorCode) {
                case WebViewClient.ERROR_HOST_LOOKUP:
                    if (!failingUrl.startsWith("http://www.baidu.com")) {
                        url = "http://www.baidu.com/baidu?word=" + failingUrl;
                        Log.d(TAG, "error, redirect to: " + url);
                        browser.loadUrl(url);
                    }

                    break;
                case WebViewClient.ERROR_UNSUPPORTED_AUTH_SCHEME:
                    new AlertDialog.Builder(MainActivity.this).setTitle("警告")
                            .setMessage("不支持的sheme: " + failingUrl)
                            .create()
                            .show();
                    break;
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            webUrlInput.setText(url);
            webUrlLayoutInput.setVisibility(View.GONE);
            webUrlLayoutShow.setVisibility(View.GONE);

            changeStateOfBottomToolbar();
        }
    }

    private class WebViewTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (view.getId() == R.id.browser) {
                return gestureDetector.onTouchEvent(motionEvent);
            }
            return false;
        }
    }

    private class BrowserChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);

            if (newProgress == 100) {
                progressBar.setVisibility(View.GONE);
            } else {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(newProgress);
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            MainActivity.this.title = title;
            webUrlTitle.setText(title);
        }
    }

    private class ButtonClickedListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int viewId = view.getId();

            switch (viewId) {
                case R.id.web_url_goto:
                    Log.d(TAG, "url: " + url);
                    browser.loadUrl(url);
                    break;
                case R.id.web_url_cancel:
                    webUrlInput.setText("");
                    break;
                case R.id.prev_button:
                    if (browser.canGoBack()) {
                        browser.goBack();
                    }
                    break;
                case R.id.next_button:
                    if (browser.canGoForward()) {
                        browser.goForward();
                    }
                    break;
                case R.id.window_button:
                    // TODO
                    break;
                case R.id.home_button:
                    browser.loadUrl(HOME_PAGE);
                    break;

                case R.id.tools_button:
                    if (toolsPopupWindow == null) {
                        Activity activity = (Activity) view.getContext();
                        toolsPopupWindow = new ToolsPopupWindow(activity,
                                getWindowManager().getDefaultDisplay().getWidth() - 30,
                                getWindowManager().getDefaultDisplay().getHeight() / 3
                        );
                    }

                    LayoutInflater toolsInflater = LayoutInflater.from(getApplicationContext());
                    View toolsView = toolsInflater.inflate(R.layout.tools, null);
                    toolsPopupWindow.showAtLocation(
                            toolsView,
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                            0,
                            toolsButton.getHeight() + 20
                    );

                    Button refresh = (Button) toolsPopupWindow.getView(R.id.tools_normal_refresh);
                    refresh.setOnClickListener(this);
                    break;
                case R.id.tools_normal_refresh:
                    browser.loadUrl(url);
                    if (toolsPopupWindow != null) {
                        toolsPopupWindow.dismiss();
                    }
                    break;
                case R.id.web_url_show_favorite:

                    if (favoriteManager == null) {
                        Activity activity = (Activity) view.getContext();
                        favoriteManager = new FavoriteManager(activity);
                    }
                    favoriteManager.addFavorite(title, url);
                    break;
                case R.id.web_url_show_title:
                    changeStateOfWebUrlLayout(true);
                    break;
            }

        }
    }

    private class WebUrlInputChangedListener implements TextWatcher {
        @Override
        public void afterTextChanged(Editable editable) {
            // do nothing
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // do nothing
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            url = charSequence.toString();

            if (!(url.startsWith("http://") || url.startsWith("https://"))) {
                url = "http://" + url;
            }

            Log.d(TAG, "on text changed: " + url);

            if (URLUtil.isNetworkUrl(url) && URLUtil.isValidUrl(url)) {
                changeStateOfWebGoto(true);
            } else {
                changeStateOfWebGoto(false);
            }
        }
    }

    private class GestureListener implements GestureDetector.OnGestureListener {

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            Log.d(TAG, "on fling, scrollY : " + browser.getScrollY());
            webUrlLayoutInput.setVisibility(View.GONE);
            if (browser.getScrollY() <= 0) {
                webUrlLayoutShow.setVisibility(View.VISIBLE);
            } else {
                webUrlLayoutShow.setVisibility(View.GONE);
            }
            return true;
        }

        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {
            // TODO
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {
            // TODO
        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return false;
        }
    }

}
