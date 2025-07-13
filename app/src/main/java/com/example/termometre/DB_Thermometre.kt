package com.example.termometre

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.sql.Date

class DB_Thermometre(context: Context?, name:String?, factory: SQLiteDatabase.CursorFactory?, version:Int):
    SQLiteOpenHelper(context,name,factory,version)  {

        private lateinit var db:SQLiteDatabase;

    override fun onCreate(sqLiteDatabase: SQLiteDatabase)  {
        var requette1="CREATE TABLE IF NOT EXISTS Thermometre(Id Integer PRIMARY KEY AUTOINCREMENT,TemperatureC Float,TemperatureF Float,Horodatage Date)";
        sqLiteDatabase?.execSQL(requette1);
    }
    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase?,p1:Int,p2:Int) {
        var dropRequette="DROP TABLE IF EXISTS Thermometre";
        sqLiteDatabase?.execSQL(dropRequette);
        if(sqLiteDatabase!=null)
        {
            onCreate(sqLiteDatabase);
        }
    }
    fun openDb()
    {
        db=writableDatabase;
    }

    fun closeDb()
    {
        db.close();
    }

    fun addTemperature(TemperatureC:Float,TemperatureF:Float,Horodatage:Date){

        // premiere methode pour ajouter un element dans une table
        val requetteAjoute2="INSERT INTO Thermometre (TemperatureC,TemperatureF,Horodatage) values(?,?,?) ";
        //si tu veux ajouter un element il va toujours falloir que  tu utilises un arrayOf
        db.execSQL(requetteAjoute2, arrayOf(TemperatureC,TemperatureF,Horodatage));

        //deuxieme methode pour ajouter un element dans une table
        val cv= ContentValues().apply {
            put("TemperatureC",TemperatureC)
            put("TemperatureF",TemperatureF)
            put("Horodatage",Horodatage.time)
        }
        db.insert("Thermometre",null,cv);
    }

    fun selectAllTemperature(): List<ThermometreEnregistrement> {
        val requeteAllTemperature = mutableListOf<ThermometreEnregistrement>()

        db.rawQuery("SELECT * FROM Thermometre", null).use { cursor ->
            val idIndex = cursor.getColumnIndexOrThrow("Id")
            val tempCIndex = cursor.getColumnIndexOrThrow("TemperatureC")
            val tempFIndex = cursor.getColumnIndexOrThrow("TemperatureF")
            val horodatageIndex = cursor.getColumnIndexOrThrow("Horodatage")

            while (cursor.moveToNext()) {
                val thermo = ThermometreEnregistrement(
                    Id = cursor.getInt(idIndex),
                    TemperatureC = cursor.getFloat(tempCIndex),
                    TemperatureF = cursor.getFloat(tempFIndex),
                    Horodatage = Date(cursor.getLong(horodatageIndex))
                )
                requeteAllTemperature.add(thermo)
            }
        }
        return requeteAllTemperature
    }
}