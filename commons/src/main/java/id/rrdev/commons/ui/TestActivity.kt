package id.rrdev.commons.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import id.rrdev.commons.databinding.ActivityTestBinding
import id.rrdev.commons.utils.PrefManager
import java.util.function.Predicate

class TestActivity : AppCompatActivity() {

    private val binding by lazy { ActivityTestBinding.inflate(layoutInflater) }
    private val prefManager = PrefManager(this, "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}