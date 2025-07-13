package com.example.termometre

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.Menu
import android.view.MenuItem
import android.view.View

class Thermometre(context: Context) : View(context) {
    private  var gateWay:DB_Thermometre=DB_Thermometre(context,"Thermometre",null,1);
    private var temp: Float = 0.0f
    private var lastSavedTime: Long = 0
     init {
         gateWay.openDb();
     }


    fun setTemp(temp: Float) {
        this.temp = temp;
        invalidate()
    }

//    class Thermometre(context: Context, temp: Float) : View(context) {
//        private var temp: Float = temp
//    }

    override fun onDraw(canvas: Canvas) {


        super.onDraw(canvas)
        val p = Paint()

        // Températures
        val tempMinC = -30f
        val tempMaxC = 50f
        val tempRangeC = tempMaxC - tempMinC
        val tempCurrentC = temp

        // Dimensions
        val centerX = width / 2f
        val thermometerTop = height / 6f
        val thermometerBottom = (height - 70f) * 0.8f
        val thermometerHeight = thermometerBottom - thermometerTop
        val bulbRadius = 60f

        // Calcul position liquide
        val normalizedTemp = ((tempCurrentC - tempMinC) / tempRangeC).coerceIn(0f, 1f)
        val liquidTop = thermometerBottom - (normalizedTemp * thermometerHeight)

        // Dessin liquide
        p.color = if (tempCurrentC >= 25) Color.RED else Color.BLUE
        canvas.drawRect(
            centerX - 30f, liquidTop,
            centerX + 30f, thermometerBottom,
            p
        )

        // Dessin bulbe
        canvas.drawCircle(centerX, thermometerBottom + bulbRadius, bulbRadius, p)

        // Contour thermomètre
        p.apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeWidth = 5f
        }
        canvas.drawRect(centerX - 35f, thermometerTop, centerX + 35f, thermometerBottom, p)
        canvas.drawCircle(centerX, thermometerBottom + bulbRadius, bulbRadius, p)

        // Graduations
        val scalePaint = Paint().apply {
            color = Color.BLACK
            textSize = 25f
        }

        for (tC in tempMinC.toInt()..tempMaxC.toInt() step 2) {
            val y = thermometerBottom - ((tC - tempMinC) / tempRangeC) * thermometerHeight
            val tF = (tC * 9/5 + 32)

            if (tC % 10 == 0) {
                // Graduation principale
                canvas.drawLine(centerX - 35f, y, centerX - 60f, y, scalePaint)
                canvas.drawText("$tC°C", centerX + 90f, y + 10, scalePaint)

                canvas.drawLine(centerX + 35f, y, centerX + 60f, y, scalePaint)
                canvas.drawText("${tF}°F", centerX - 90f, y + 10, scalePaint)
            } else {
                // Petite graduation
                canvas.drawLine(centerX - 35f, y, centerX - 45f, y, scalePaint)
                canvas.drawLine(centerX + 35f, y, centerX + 45f, y, scalePaint)
            }
        }

        // Affichage température actuelle
        val tempCurrentF = tempCurrentC * 9/5 + 32
        val tempText = "%.1f°C / %.1f°F".format(tempCurrentC, tempCurrentF)
        Paint().apply {
            color = Color.BLACK
            textSize = 50f
            textAlign = Paint.Align.CENTER
        }.also { textPaint ->
            canvas.drawText("Température: $tempText", centerX, thermometerTop - 50, textPaint)
        }
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastSavedTime > 1000) {
            val tempF = temp * 9/5 + 32
            gateWay.addTemperature(tempCurrentC,tempCurrentF,java.sql.Date(System.currentTimeMillis()));
            lastSavedTime = currentTime
        }
//        gateWay.addTemperature(tempCurrentC,tempCurrentF,java.sql.Date(System.currentTimeMillis()));

    }

    override fun onDetachedFromWindow() {
        gateWay.closeDb() // Important pour libérer les ressources
        super.onDetachedFromWindow()
    }



}