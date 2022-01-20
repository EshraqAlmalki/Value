package com.tuwaiq.value

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.tuwaiq.value.timelineUserActive.NEWS_URL

class WebViewNewsActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onBackPressed() {

        if (webView.canGoBack())
            webView.goBack()
        else
            super.onBackPressed()

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view_news)


        webView = findViewById(R.id.web_view)

        val healthNews = intent.getStringExtra(NEWS_URL) ?: "https://live-fitness-and-health-news.p.rapidapi.com/"

        webView.webChromeClient = object :WebChromeClient(){
            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)

                this@WebViewNewsActivity.supportActionBar?.title = title
            }
        }

        webView.settings.javaScriptEnabled = true
        webView.loadUrl(healthNews)
    }
}