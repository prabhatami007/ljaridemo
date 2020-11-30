package com.vision2020.ui.nida

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient

import com.vision2020.R

/**
 * A simple [Fragment] subclass.
 */
class NidaCurriculumFragment : Fragment() {

    private val webView: WebView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Get the custom view for this fragment layout
        val view = inflater!!.inflate(R.layout.fragment_nida_curriculum,container,false)

       // var title = "KotlinApp"
        val webView = view.findViewById<WebView>(R.id.webView)
        webView.webViewClient = WebViewClient()
        webView.loadUrl("https://teens.drugabuse.gov/")
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true


        return view
    }


}
