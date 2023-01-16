package com.argz.issue4198

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.argz.issue4198.databinding.ActivityMainBinding
import com.google.firebase.dynamiclinks.PendingDynamicLinkData
import com.google.firebase.dynamiclinks.ktx.*
import com.google.firebase.ktx.Firebase


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // if app is installed, somehow utm is only received
        // through intent.data instead of just intent
        intent.data?.let { it ->
            getDynamicLinkDetails(it)
        } ?: run {
            getDynamicLinkDetails()
        }

        // if app is not installed, utm parameters is retrieved
        // normally through intent

    }

    private fun getDynamicLinkDetails(uri: Uri){
        Firebase.dynamicLinks
            .getDynamicLink(uri)
            .addOnSuccessListener(this) { pendingDynamicLinkData: PendingDynamicLinkData? ->
                var deepLink: Uri? = null
                if (pendingDynamicLinkData != null) {
                    val utmBundle = pendingDynamicLinkData.utmParameters

                    deepLink = pendingDynamicLinkData.link

                    log("calling getDynamicLinkDetails(it)")
                    log("getDynamicLink(intent.data)")
                    log("utm medium: ${utmBundle["utm_medium"]}")
                    log("utm source: ${utmBundle["utm_source"]}")
                    log("utm utm_campaign: ${utmBundle["utm_campaign"]}")
                    log("deeplink: $deepLink")
                    log("----------------------------------------------")
                }

            }
            .addOnFailureListener(this) { e ->
                log("getDynamicLink:onFailure $e")
                log("----------------------------------------------")
            }


    }
    private fun getDynamicLinkDetails() {

        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData: PendingDynamicLinkData? ->
                var deepLink: Uri? = null
                if (pendingDynamicLinkData != null) {
                    val utmBundle = pendingDynamicLinkData.utmParameters

                    deepLink = pendingDynamicLinkData.link

                    log("calling getDynamicLinkDetails()")
                    log("getDynamicLink(intent)")
                    log("utm medium: ${utmBundle["utm_medium"]}")
                    log("utm source: ${utmBundle["utm_source"]}")
                    log("utm utm_campaign: ${utmBundle["utm_campaign"]}")
                    log("deeplink: $deepLink")
                    log("----------------------------------------------")
                }
            }
            .addOnFailureListener(this) { e ->
                log("getDynamicLink:onFailure $e")
                log("----------------------------------------------")
            }
    }

    fun generate_link(view: View) {
        Firebase.dynamicLinks.shortLinkAsync {
            link = Uri.parse("https://www.youtube.com/watch?v=-d7lhYM77_c")
            domainUriPrefix = "https://issue4198.page.link/"
            androidParameters{}
        }.addOnSuccessListener { (shortLink, flowchartLink) ->
            log("generate_link: $shortLink")
            findViewById<EditText>(R.id.et_generated_link).setText(shortLink.toString())
        }.addOnFailureListener {
            log("generate_link error: ${it}")
        }.addOnCompleteListener {
            log("generate_link: addOnCompleteListener")
        }
    }

    fun copy_link(view: View) {
        val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label", findViewById<EditText>(R.id.et_generated_link).text.toString())
        clipboard.setPrimaryClip(clip)
    }

    private fun log(message: String) {
        Log.d(TAG, message)

        val oldLogs: String = binding.tvLogs.text.toString()
        binding.tvLogs.setText("$oldLogs \n $message")
    }
}