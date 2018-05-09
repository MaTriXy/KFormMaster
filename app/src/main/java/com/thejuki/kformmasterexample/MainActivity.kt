package com.thejuki.kformmasterexample

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Main Activity
 *
 * Examples screen
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonFullScreenActivity.setOnClickListener { startActivity(Intent(this@MainActivity, FullscreenFormActivity::class.java)) }

        buttonPartialScreenActivity.setOnClickListener { startActivity(Intent(this@MainActivity, PartialScreenFormActivity::class.java)) }

        buttonFormListenerActivity.setOnClickListener { startActivity(Intent(this@MainActivity, FormListenerActivity::class.java)) }

        buttonFormListenerJavaActivity.setOnClickListener { startActivity(Intent(this@MainActivity, FormListenerJavaActivity::class.java)) }

        buttonLoginActivity.setOnClickListener { startActivity(Intent(this@MainActivity, LoginFormActivity::class.java)) }

        buttonCustomFormActivity.setOnClickListener { startActivity(Intent(this@MainActivity, CustomFormActivity::class.java)) }

    }
}
