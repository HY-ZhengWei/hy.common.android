package org.hy.common.android.ui.events;

import org.hy.common.android.ui.MyWebClient;





/**
 * 自定义浏览器中的改变事件监听器接口
 *
 * @author  ZhengWei(HY)
 * @version 2017-11-29
 */
public interface MyWebClientListening
{

    /**
     * 页面加载完成触发（可能会执行多次）
     *
     * @param i_MyWebClient
     */
    public void onFinished(MyWebClient i_MyWebClient);

}
