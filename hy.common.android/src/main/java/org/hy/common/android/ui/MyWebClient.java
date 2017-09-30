package org.hy.common.android.ui;

import android.annotation.TargetApi;
import android.content.Context;
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


/**
 * Created by ZhengWei(HY) on 2017/8/15.
 */
public class MyWebClient extends WebView
{

    /** 当前服务 */
    private String            currentServer;

    private CycleList<String> servers;

    private ProgressBar       progressbar;



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
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                webViewError(view);
            }

            //
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                webViewError(view);
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                webViewError(view);
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
                webViewError(view);
            }
        });


        this.setWebChromeClient(new WebChromeClient()
        {
            public void onProgressChanged(WebView view, int newProgress)
            {
                if (newProgress == 100)
                {
                    progressbar.setVisibility(GONE);
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
                    webViewError(view);
                }
            }

        });


        this.getSettings().setDefaultTextEncodingName("UTF-8");  // 设置编码
        this.getSettings().setJavaScriptEnabled(true);           // 支持JS
        this.getSettings().setAllowFileAccess(true);             // 支持文件访问
        this.getSettings().setAllowFileAccessFromFileURLs(true); // 支持本地URL访问
        this.getSettings().setDomStorageEnabled(true);           // 是否支持持久化存储，保存到本地
        this.getSettings().setAppCacheEnabled(false);
        this.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

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



    private void webViewError(WebView i_View)
    {
        currentServer = servers.next();
        i_View.stopLoading();
        i_View.loadUrl("");
    }

}
