package com.fizhu.androidplayground.features.texttospeech

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.fizhu.androidplayground.R
import com.fizhu.androidplayground.databinding.ActivityTtsBinding
import com.fizhu.androidplayground.utils.toast
import java.util.*

/**
 * Created by fizhu on 25 June 2021
 * https://github.com/Fizhu
 */
class TextToSpeechActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var binding: ActivityTtsBinding
    private lateinit var textToSpeech: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTtsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onInit()
    }

    private fun onInit() {
        textToSpeech = TextToSpeech(this, this)
        with(binding) {
            toolbar.setNavigationOnClickListener { finish() }
            btnSpeak.setOnClickListener {
                val text = binding.et.text.toString()
                if (text.isNotEmpty() && text != "") {
                    setOnClickbutton()
                } else {
                    toast("Insert Text !")
                }
            }
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = textToSpeech.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "onInit: The Language specified is not supported!")
                toast("Failed to initialize TextToSpeech")
                binding.btnSpeak.isEnabled = false
            } else {
                binding.btnSpeak.isEnabled = true
            }
            textToSpeech.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    binding.btnSpeak.setImageDrawable(ContextCompat.getDrawable(this@TextToSpeechActivity,
                        R.drawable.ic_baseline_stop_24))
                }

                override fun onDone(utteranceId: String?) {
                    binding.btnSpeak.setImageDrawable(ContextCompat.getDrawable(this@TextToSpeechActivity,
                        R.drawable.ic_baseline_volume_up_24))
                }

                override fun onError(utteranceId: String?) {
                }

            })
        }
    }

    override fun onPause() {
        if (textToSpeech.isSpeaking) {
            textToSpeech.stop()
        }
        super.onPause()
    }

    private fun setOnClickbutton() {
        if (textToSpeech.isSpeaking) {
            binding.btnSpeak.setImageDrawable(ContextCompat.getDrawable(this,
                R.drawable.ic_baseline_volume_up_24))
            textToSpeech.stop()
        } else {
            binding.btnSpeak.setImageDrawable(ContextCompat.getDrawable(this,
                R.drawable.ic_baseline_stop_24))
            textToSpeech.speak(binding.et.text.toString(), TextToSpeech.QUEUE_FLUSH, null, "")
        }
    }

    override fun onDestroy() {
        textToSpeech.stop()
        textToSpeech.shutdown()
        super.onDestroy()
    }

}