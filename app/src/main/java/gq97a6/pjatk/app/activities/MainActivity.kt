package gq97a6.pjatk.app.activities

import android.graphics.Color
import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import gq97a6.pjatk.app.G
import gq97a6.pjatk.app.Storage.rootFolder
import gq97a6.pjatk.app.activities.fragments.FragmentManager
import gq97a6.pjatk.app.activities.fragments.LoginFragment
import gq97a6.pjatk.app.activities.fragments.TimetableFragment
import gq97a6.pjatk.app.compose.Colors
import gq97a6.pjatk.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var b: ActivityMainBinding

    var onBackPressedBoolean: () -> Boolean = { false }

    companion object {
        lateinit var fm: FragmentManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        //Initialize fragment manager
        fm = FragmentManager(this@MainActivity)

        //Handle onBackPress
        onBackPressedDispatcher.addCallback(this) {
            if (!onBackPressedBoolean() && !fm.popBackStack()) finish()
        }

        if (!G.areInitialized) {
            rootFolder = filesDir.canonicalPath.toString()
            G.initialize()
        }

        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false
        Colors.background.let {
            window.statusBarColor = Color.rgb(it.red, it.green, it.blue)
        }

        fm.replaceWith(
            if (G.settings.login != "" && G.settings.pass != "") LoginFragment()
            else LoginFragment(),
            false,
            null
        )
    }
}