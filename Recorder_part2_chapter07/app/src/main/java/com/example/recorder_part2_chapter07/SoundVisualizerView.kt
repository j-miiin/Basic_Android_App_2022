package com.example.recorder_part2_chapter07

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import java.util.logging.Handler

class SoundVisualizerView(
    context: Context,
    attrs: AttributeSet? = null
): View(context, attrs) {

    var onRequestCurrentAmplitude: (() -> Int)? = null

    private val amplitudePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.purple_500)
        strokeWidth = LINE_WIDTH
        strokeCap = Paint.Cap.ROUND
    }
    private var drawingWidth: Int = 0
    private var drawingHeight: Int = 0
    private var drawingAmplitudes: List<Int> = emptyList()
    private var isReplaying: Boolean = false
    private var replayingPosition: Int = 0

    private val visualizeRepeatAction: Runnable = object : Runnable {
        override fun run() {
            if (!isReplaying) {
                // MainActivity에서 bindViews 함수 내의 soundVisualizerView.onRequestCurrentAmplitude 블럭을 호출
                // -> MainAcitivy에서 가진 recorder의 MaxAmplitude 값을 반환하게 됨
                val currentAmplitude = onRequestCurrentAmplitude?.invoke() ?: 0
                // Amplitude, Draw
                drawingAmplitudes = listOf(currentAmplitude) + drawingAmplitudes
            } else {
                replayingPosition++
            }

            invalidate() // -> 호출해줘야 onDraw가 다시 호출되어 갱신이 됨

            handler?.postDelayed(this, ACTION_INTERVAL) // 나를 20밀리세컨드 뒤에 다시 실행
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        drawingWidth = w
        drawingHeight = h
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas ?: return

        val centerY = drawingHeight / 2f
        var offsetX = drawingWidth.toFloat()

        drawingAmplitudes
            .let { amplitudes ->
                if (isReplaying) {
                    amplitudes.takeLast(replayingPosition)
                } else {
                    amplitudes
                }
            }
            .forEach { amplitude ->
            val linelength = amplitude / MAX_AMPLITUDE * drawingHeight * 0.8F

            offsetX -= LINE_SPACE
            if (offsetX < 0) return@forEach

            canvas.drawLine(
                offsetX,
                centerY - linelength / 2F,
                offsetX,
                centerY + linelength / 2F,
                amplitudePaint
            )
        }
    }

    fun startVisualizing(isReplaying: Boolean) {
        this.isReplaying = isReplaying
        handler?.post(visualizeRepeatAction)
    }

    fun stopVisualizing() {
        replayingPosition = 0
        handler?.removeCallbacks(visualizeRepeatAction)
    }

    fun clearVisualization() {
        drawingAmplitudes = emptyList()
        invalidate()
    }

    companion object {
        private const val LINE_WIDTH = 10F
        private const val LINE_SPACE = 15F
        private const val MAX_AMPLITUDE = Short.MAX_VALUE.toFloat()
        private const val ACTION_INTERVAL = 20L
    }
}