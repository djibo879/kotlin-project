package com.example.termometre

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class TemperatureAdapter(private val context:Context,private val resource:Int,private val listeTemp:ArrayList<ThermometreEnregistrement>):
    ArrayAdapter<ThermometreEnregistrement>(context,resource,listeTemp) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view=convertView?: LayoutInflater.from(context).inflate(resource,parent,false);

        val positionTemp=listeTemp[position];
        val tC=view.findViewById<TextView>(R.id.t1);
        val tF=view.findViewById<TextView>(R.id.t2);
        val date=view.findViewById<TextView>(R.id.t3);
        tC.text = "%.1f°C".format(positionTemp.TemperatureC)
        tF.text = "%.1f°F".format(positionTemp.TemperatureF)
        date.text=positionTemp.Horodatage.toString();
        return view;
    }

}