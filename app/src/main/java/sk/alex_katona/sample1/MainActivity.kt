package sk.alex_katona.sample1

import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import sk.alex_katona.sample1.common.BaseActivity

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
    }

}