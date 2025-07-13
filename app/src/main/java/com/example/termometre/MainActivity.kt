package com.example.termometre

import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), SensorEventListener {
    private var manager: SensorManager? = null
    private var tempSensor: Sensor? = null
    private lateinit var thermometre: Thermometre

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        thermometre = Thermometre(this).apply {
            setTemp(20f) // Température par défaut
        }
        setContentView(thermometre)

        manager = getSystemService(SENSOR_SERVICE) as SensorManager
        tempSensor = manager?.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)

        if (tempSensor == null) {
            Toast.makeText(this,
                "Capteur de température non disponible - Utilisation valeur par défaut",
                Toast.LENGTH_LONG).show()
        }
    }

    override fun onResume() {
        super.onResume()
        tempSensor?.let {
            manager?.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        manager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            val temperature = event.values[0]
            thermometre.setTemp(temperature)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menu?.add(Menu.NONE, Menu.FIRST,0,"Afficher Les temperatures");
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            Menu.FIRST->{
                val intent= Intent(this,AfficherTemperature::class.java)
                startActivity(intent)
                return true;
            }
            else->return super.onOptionsItemSelected(item)
        }
    }
}