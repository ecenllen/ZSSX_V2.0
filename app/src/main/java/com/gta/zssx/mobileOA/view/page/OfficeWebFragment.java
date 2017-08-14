package com.gta.zssx.mobileOA.view.page;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gta.utils.fragment.BaseFragment;
import com.gta.zssx.R;
import com.gta.zssx.pub.util.LogUtil;

/**
 * Created by lan.zheng on 2016/11/5.
 * 公文公告都是通过接口去获取列表，不需要WEBVIEW
 */
@Deprecated
public class OfficeWebFragment extends BaseFragment {
    private String jumpUrl;
    private WebView webView;

    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notice_web, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
    }

    private void initView(){
        jumpUrl = "https://www.baidu.com/";
        webView = (WebView)view.findViewById(R.id.notice_web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY); //取消滚动条白边效果
        webView.setWebChromeClient(webChromeClient);
        webView.setWebViewClient(webViewClient);
    }

    public void initData(){
        webView.clearHistory();
        webView.loadUrl(jumpUrl);
        webView.addJavascriptInterface(new JavaScriptInterface(), "listener");
    }

    private WebChromeClient webChromeClient = new WebChromeClient()
    {
        @Override
        public void onProgressChanged(WebView view, int newProgress)
        {
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title)
        {
            super.onReceivedTitle(view, title);
        }
    };

    private WebViewClient webViewClient = new WebViewClient()
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            LogUtil.Log("lenita","shouldOverrideUrlLoading url = "+url);
            view.loadUrl(url);
            /*if (url.startsWith("call://openPdf"))
            {
                String[] result = url.split(",");
                if (result.length == 2)
                {
                    downloadPDF(result[1]);
                }
            } else if(url.startsWith("tel:")){
                *//**暂时不做点击电话*//*
                *//*try{
                    Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse(url));
                    startActivity(intent);
                }catch (Exception e){
                   e.printStackTrace();
                }*//*
            }else
            {
                *//**也不需要对标题做处理，因为后台给标题*//*
                *//*progressView.reset();
                if (!url.toLowerCase().startsWith(Config.BASE_URL))
                {
                    view.getSettings().setUseWideViewPort(true);
                    view.setInitialScale(25);
                } else
                {
                    view.getSettings().setUseWideViewPort(false);
                    view.setInitialScale(100);
                }*//*
                view.loadUrl(url);
            }*/
            return true;
        }
    };

    final class JavaScriptInterface {
        @JavascriptInterface
        public void backListener() {
            webView.post(new Runnable() {
                @Override
                public void run() {
                    LogUtil.Log("lenita1", "NewsCenterWebFragment-->backListener(),webView.canGoBack() = " + webView.canGoBack());
                    if (webView.canGoBack()) {
                        LogUtil.Log("lenita1", "NewsCenterWebFragment-->webView.canGoBack()");
                        webView.goBack();
                    }else {
                        LogUtil.Log("lenita1", "NewsCenterWebFragment-->FINISH()");
                    }
                }
            });
        }
    }

    public boolean canGoBack() {
        return webView != null && webView.canGoBack();
    }

    public void goBack() {
        if (webView != null) {
            webView.goBack();
        }
    }
}

