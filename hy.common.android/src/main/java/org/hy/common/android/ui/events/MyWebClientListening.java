package org.hy.common.android.ui.events;

import android.webkit.WebView;
import org.hy.common.android.ui.MyWebClient;





/**
 * 自定义浏览器中的改变事件监听器接口
 *
 * @author  ZhengWei(HY)
 * @version 2017-11-29
 *          2018-01-03  添加：当页面加载异常时触发
 */
public interface MyWebClientListening
{

    /**
     * 页面加载完成触发（可能会执行多次）
     *
     * @param i_MyWebClient
     */
    public void onFinished(MyWebClient i_MyWebClient);



    /**
     * 当页面加载异常时触发
     *
     * @param i_View
     * @param i_Url
     * @return       是否为异常
     */
    public boolean webViewError(WebView i_View ,String i_Url);

}
