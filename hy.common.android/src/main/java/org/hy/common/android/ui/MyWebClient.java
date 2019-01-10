package org.hy.common.android.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.http.SslError;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import org.hy.common.CycleList;
import org.hy.common.Help;
import org.hy.common.StringHelp;
import org.hy.common.android.R;
import org.hy.common.android.ui.events.MyWebClientListening;

import java.io.IOException;


/**
 * 自定义浏览器
 *
 * Created by ZhengWei(HY) on 2017/8/15.
 */
public class MyWebClient extends WebView
{

    /** 当前服务 */
    private String               currentServer;

    private CycleList<String>    servers;

    private ProgressBar          progressbar;

    /** 浏览器的改变事件监听器 */
    private MyWebClientListening listening;



    private void fireOnFinishedListening()
    {
        if ( this.listening != null )
        {
            this.listening.onFinished(this);
        }
    }



    @Override
    public void loadUrl(String url)
    {
        super.loadUrl(url);
    }



    @Override
    public void goBack()
    {
        super.goBack();
    }



    @Override
    public boolean canGoBack()
    {
        return super.canGoBack();
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyWebClient(Context context)
    {
        super(context);
        this.init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyWebClient(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyWebClient(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr, defStyleAttr);
        this.init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyWebClient(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.init(context);
    }

    /** @deprecated */
    @Deprecated
    public MyWebClient(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing)
    {
        super(context, attrs, defStyleAttr , privateBrowsing);
        this.init(context);
    }



    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt)
    {
        LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressbar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }



    private void init(Context context)
    {
        progressbar = new ProgressBar(this.getContext() ,null , android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , 10));

        Drawable drawable = this.getContext().getResources().getDrawable(R.drawable.mywebclient_progressbar_states);
        progressbar.setProgressDrawable(drawable);
        this.addView(progressbar);

        this.setWebViewClient(new WebViewClient()
        {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                super.onPageStarted(view, url, favicon);
            }


            @Override
            public void onPageFinished(WebView view, String url)
            {
                super.onPageFinished(view, url);
            }


            @Override
            public void onLoadResource(WebView view, String url)
            {
                // 可在此添加拦截功能
                super.onLoadResource(view, url);
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView i_View, String i_URL)
            {
                WebResourceResponse v_Response = null;

                if ( listening != null )
                {
                    v_Response = listening.shouldInterceptRequest(i_URL);
                }

                if ( v_Response == null )
                {
                    v_Response = super.shouldInterceptRequest(i_View, i_URL);
                }

                return v_Response;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);

                String v_Url = "";
                if ( failingUrl != null )
                {
                    v_Url = failingUrl;
                }
                webViewError(view ,v_Url);
            }

            //
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);

                String v_Url = "";
                if ( error != null && error.getUrl() != null )
                {
                    v_Url = error.getUrl();
                }
                webViewError(view ,v_Url);
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);

                String v_Url = "";
                if ( request != null && request.getUrl() != null && request.getUrl().toString() != null )
                {
                    v_Url = request.getUrl().toString();
                }
                webViewError(view ,v_Url);
            }

            /**
             * 页面加载错误时执行的方法，但是在6.0以下，有时候会不执行这个方法
             * @param view
             * @param request
             * @param error
             */
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);

                String v_Url = "";
                if ( request != null && request.getUrl() != null && request.getUrl().toString() != null )
                {
                    v_Url = request.getUrl().toString();
                }
                webViewError(view ,v_Url);
            }
        });


        this.setWebChromeClient(new WebChromeClient()
        {
            public void onProgressChanged(WebView view, int newProgress)
            {
                if (newProgress == 100)
                {
                    progressbar.setVisibility(GONE);
                    fireOnFinishedListening();
                }
                else
                {
                    if (progressbar.getVisibility() == GONE)
                    {
                        progressbar.setVisibility(VISIBLE);
                    }
                    progressbar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title)
            {
                super.onReceivedTitle(view ,title);

                // android 6.0 以下通过title获取
                if (Help.isNull(title) || StringHelp.isContains(title.toLowerCase() ,"error" ,"400" ,"404", "500" ,"501" ,"502" ,"503" ,"504"))
                {
                    webViewError(view ,title);
                }
            }

        });


        this.getSettings().setDefaultTextEncodingName("UTF-8");  // 设置编码
        this.getSettings().setJavaScriptEnabled(true);           // 支持JS
        this.getSettings().setAllowFileAccess(true);             // 支持文件访问
        this.getSettings().setAllowFileAccessFromFileURLs(true); // 支持本地URL访问
        this.getSettings().setDomStorageEnabled(true);           // 是否支持持久化存储，保存到本地
        this.getSettings().setAppCacheEnabled(true);
        this.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        this.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        this.getSettings().setUseWideViewPort(true);
        this.getSettings().setLoadWithOverviewMode(true);
        this.getSettings().setSupportZoom(true);
        this.getSettings().setBuiltInZoomControls(false);
        this.getSettings().setDomStorageEnabled(true);

        if ( !Help.isNull(servers) )
        {
            currentServer = servers.next();
        }
    }



    public CycleList<String> getServers()
    {
        return servers;
    }



    public void setServers(CycleList<String> servers)
    {
        this.servers = servers;

        if ( !Help.isNull(servers) )
        {
            currentServer = servers.next();
        }
    }



    public String getCurrentServer()
    {
        return currentServer;
    }



    private void webViewError(WebView i_View ,String i_Url)
    {
        if ( !Help.isNull(i_Url) && i_Url.indexOf("favicon.ico") >= 0 )
        {
            return;
        }

        boolean v_IsError = true;

        if ( this.listening != null )
        {
            v_IsError = this.listening.webViewError(i_View ,i_Url);
        }

        if ( v_IsError )
        {
            currentServer = servers.next();
            i_View.stopLoading();
            i_View.loadUrl("");
        }
    }



    public MyWebClientListening getMyWebClientListening()
    {
        return listening;
    }



    public void setMyWebClientListening(MyWebClientListening listening)
    {
        this.listening = listening;
    }

}
