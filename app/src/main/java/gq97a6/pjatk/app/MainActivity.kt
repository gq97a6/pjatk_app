package gq97a6.pjatk.app

import android.os.Bundle
import androidx.activity.addCallback
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import gq97a6.pjatk.app.databinding.ActivityMainBinding
import gq97a6.pjatk.app.fragments.FragmentManager
import gq97a6.pjatk.app.fragments.MainScreenFragment

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

        //Initialize fragment manager and begin setup
        fm = FragmentManager(this@MainActivity)
        fm.replaceWith(MainScreenFragment(), false, null)

        //Handle onBackPress
        onBackPressedDispatcher.addCallback(this) {
            if (!onBackPressedBoolean() && !fm.popBackStack()) finish()
        }
    }
}