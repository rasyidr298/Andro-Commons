package id.rrdev.commons.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.rrdev.commons.databinding.ActivityTestBinding

class TestActivity : AppCompatActivity() {

    private val binding by lazy { ActivityTestBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}