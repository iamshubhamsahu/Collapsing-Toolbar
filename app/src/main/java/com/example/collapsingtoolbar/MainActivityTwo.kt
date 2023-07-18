package com.example.collapsingtoolbar

import android.R.attr
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.squareup.picasso.Picasso


class MainActivityTwo : AppCompatActivity(), AppBarLayout.OnOffsetChangedListener {

    private val PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f
    private val PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f
    private val ALPHA_ANIMATIONS_DURATION = 200
    val imageUri = "https://i.imgur.com/VIlcLfg.jpg"
    private val PERMISSION_REQUEST_INTERNET = 1


    private var mIsTheTitleVisible = false
    private var mIsTheTitleContainerVisible = true

    private lateinit var appbar: AppBarLayout
    private lateinit var collapsing: CollapsingToolbarLayout
    private lateinit var coverImage: ImageView
    private lateinit var framelayoutTitle: FrameLayout
    private lateinit var linearlayoutTitle: LinearLayout
    private lateinit var toolbar: Toolbar
    private lateinit var textviewTitle: TextView
    private lateinit var avatar: SimpleDraweeView


    @SuppressLint("MissingInflatedId")
    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fresco.initialize(this)
        setContentView(R.layout.activity_main_two)

        appbar = findViewById(R.id.appbarTwo)
        collapsing = findViewById(R.id.collapsing)
        coverImage = findViewById(R.id.imageview_placeholder)
        framelayoutTitle = findViewById(R.id.framelayout_title)
        linearlayoutTitle = findViewById(R.id.linearlayout_title)
        toolbar = findViewById(R.id.toolbarTwo)
        textviewTitle = findViewById(R.id.textview_title)
        avatar = findViewById(R.id.avatarOne)

        toolbar.title = ""
        appbar.addOnOffsetChangedListener(this)
        setSupportActionBar(toolbar)
        startAlphaAnimation(textviewTitle, 0, View.INVISIBLE)

        requestInternetPermission()

        Picasso.get().load(imageUri)
            .into(avatar)


        if (isInternetAvailable()) {
            showToast("Internet Connected")
        } else {
            showToast("No Internet Connected")

        }


        coverImage.setImageResource(R.drawable.cover)
    }

    private fun requestInternetPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.INTERNET
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.INTERNET),
                PERMISSION_REQUEST_INTERNET
            )
        } else {
            showToast("Internet permission already granted")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_INTERNET) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showToast("Internet permission granted")
            } else {
                showToast("Internet permission denied")
            }
        }
    }

    private fun showToast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            val nwInfo = connectivityManager.activeNetworkInfo ?: return false
            return nwInfo.isConnected
        }
    }

    private fun startAlphaAnimation(v: View, duration: Int, visibility: Int) {
        val alphaAnimation =
            if (attr.visibility == View.VISIBLE)
                AlphaAnimation(0f, 1f)
            else
                AlphaAnimation(1f, 0f)

        alphaAnimation.duration = attr.duration.toLong()
        alphaAnimation.fillAfter = true
        v.startAnimation(alphaAnimation)
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, offset: Int) {
        val maxScroll = appBarLayout!!.totalScrollRange
        val percentage = Math.abs(offset).toFloat() / maxScroll.toFloat()
        handleAlphaOnTitle(percentage)
        handleToolbarTitleVisibility(percentage)
    }

    private fun handleToolbarTitleVisibility(percentage: Float) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if (!mIsTheTitleVisible) {
                startAlphaAnimation(textviewTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(textviewTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private fun handleAlphaOnTitle(percentage: Float) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {

            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(linearlayoutTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE)
                mIsTheTitleContainerVisible = false
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(linearlayoutTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE)
                mIsTheTitleContainerVisible = true
            }
        }
    }


}