package com.neko.marquee.text

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.AttributeSet
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatTextView
import io.nekohasekai.sagernet.R
import java.util.Calendar

/**
 * Custom TextView that displays speech based on time
 * and automatically changes when the clock changes.
 */
class Greetings @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private val timeChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            // Will be called every time the hour changes, or when the time is changed
            if (intent?.action == Intent.ACTION_TIME_TICK ||
                intent?.action == Intent.ACTION_TIME_CHANGED ||
                intent?.action == Intent.ACTION_TIMEZONE_CHANGED
            ) {
                updateGreeting()
            }
        }
    }

    init {
        updateGreeting()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        // Register the receiver for hourly updates.
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_TIME_TICK)
            addAction(Intent.ACTION_TIME_CHANGED)
            addAction(Intent.ACTION_TIMEZONE_CHANGED)
        }
        context.registerReceiver(timeChangeReceiver, filter)
        updateGreeting()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        // Avoid memory leaks
        context.unregisterReceiver(timeChangeReceiver)
    }

    override fun isFocused(): Boolean = true

    private fun updateGreeting() {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        @StringRes val greetingResId = when (hour) {
            in 4..8 -> R.string.uwu_greeting_morning
            in 9..15 -> R.string.uwu_greeting_afternoon
            in 16..20 -> R.string.uwu_greeting_evening
            in 21..23 -> R.string.uwu_greeting_night
            else -> R.string.uwu_greeting_late_night
        }
        setText(greetingResId)
    }
}
