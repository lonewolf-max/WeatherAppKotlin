package com.example.weatherapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.io.IOException
import okhttp3.*

class MainActivity : AppCompatActivity() {

    private lateinit var tvCity: TextView
    private lateinit var tvTemperature: TextView
    private lateinit var tvHumidity: TextView
    private lateinit var tvWindSpeed: TextView
    private lateinit var btnFetchWeather: Button

    private val apiKey = "YOUR_API_KEY"  // Replace with your actual API key
    private val client by lazy { OkHttpClient() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvCity = findViewById(R.id.tvCity)
        tvTemperature = findViewById(R.id.tvTemperature)
        tvHumidity = findViewById(R.id.tvHumidity)
        tvWindSpeed = findViewById(R.id.tvWindSpeed)
        btnFetchWeather = findViewById(R.id.btnFetchWeather)

        btnFetchWeather.setOnClickListener {
            fetchWeatherData("bhilai")  // Example city
        }
    }

    private fun fetchWeatherData(city: String) {
        val url = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$apiKey&units=metric"

        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Network request failed", Toast.LENGTH_SHORT).show()
                }
            }

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call , response: Response) {
                response.use { res ->
                    if (!res.isSuccessful) {
                        runOnUiThread {
                            Toast.makeText(this@MainActivity, "Error: ${res.code}", Toast.LENGTH_SHORT).show()
                        }
                        return
                    }

                    val responseData = res.body?.string()
                    if (responseData != null) {
                        try {
                            val json = JSONObject(responseData)
                            val cityName = json.getString("name")
                            val temp = json.getJSONObject("main").getDouble("temp")
                            val humidity = json.getJSONObject("main").getInt("humidity")
                            val windSpeed = json.getJSONObject("wind").getDouble("speed")

                            runOnUiThread {
                                tvCity.text = "City: $cityName"
                                tvTemperature.text = "Temperature: $temp °C"
                                tvHumidity.text = "Humidity: $humidity %"
                                tvWindSpeed.text = "Wind Speed: $windSpeed m/s"
                            }
                        } catch (_: Exception) {
                            runOnUiThread {
                                Toast.makeText(this@MainActivity, "Parsing error", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        })
    }
}
