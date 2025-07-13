package com.example.termometre

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AfficherTemperature : AppCompatActivity() {
    private  lateinit var listeView: ListView;
    private val TemperatureArrayList = ArrayList<ThermometreEnregistrement>();
    private lateinit var  gateWay:DB_Thermometre;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_afficher_temperature)

        gateWay=DB_Thermometre(this,"Thermometre",null,1);

        gateWay.openDb();

        listeView = findViewById(R.id.listeview)

        var allTemperature:List<ThermometreEnregistrement> = gateWay.selectAllTemperature();

        var id:Int=0;
        for( temp in allTemperature)
        {
            val tC=temp.TemperatureC;
            val tF=temp.TemperatureF;
            val date=temp.Horodatage;
            TemperatureArrayList.add(ThermometreEnregistrement(id,tC,tF,date));
            id++;
        }
        val adapter=TemperatureAdapter(this,R.layout.affichertemperature,TemperatureArrayList);
        listeView.adapter=adapter;
    }
    override fun onDestroy() {
        gateWay.closeDb()
        super.onDestroy()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menu?.add(Menu.NONE, Menu.FIRST,0,"Thermometre");
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            Menu.FIRST->{
                val intent= Intent(this,MainActivity::class.java)
                startActivity(intent)
                return true;
            }
            else->return super.onOptionsItemSelected(item)
        }
    }
}