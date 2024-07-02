package com.stellantis.espw

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.stellantis.espw.DetailSuperheroActivity.Companion.EXTRA_ID
import com.stellantis.espw.api.ApiService
import com.stellantis.espw.api.SuperHeroDataResponse
import com.stellantis.espw.databinding.ActivityMainBinding
import com.stellantis.espw.settings.SettingsActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var retrofit: Retrofit
    private lateinit var adapter: SuperHeroAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        retrofit = getRetrofit()
        initUI()
    }

    private fun initUI() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchByName(query.orEmpty())
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        adapter = SuperHeroAdapter {navigateToDetail(it)}
        binding.rvSuperhero.setHasFixedSize(true)
        binding.rvSuperhero.layoutManager = LinearLayoutManager(this)
        binding.rvSuperhero.adapter = adapter

        binding.btSettings.setOnClickListener{
            navigateToSettings()
        }
    }

    private fun searchByName(query: String) {
        binding.progressBar.isVisible = true
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val myResponse: Response<SuperHeroDataResponse> = retrofit.create(ApiService::class.java).getSuperheroes(query)

                if (myResponse.isSuccessful) {
                    val response: SuperHeroDataResponse? = myResponse.body()
                    if (response != null) {
                        Log.i("testHero", response.toString())
                        runOnUiThread {
                            adapter.updateList(response.responseList)
                            binding.progressBar.isVisible = false
                        }
                    } else {
                        Log.i("testHero", "Response body is null")
                    }
                } else {
                    Log.i("testHero", "Unsuccessful response: ${myResponse.code()}")
                }
            } catch (e: Exception) {
                Log.e("testHero", "Error: ${e.message}", e)
            }
        }
    }


    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://superheroapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun navigateToDetail(id: String){
        val intent = Intent(this, DetailSuperheroActivity::class.java)
        intent.putExtra(EXTRA_ID, id)
        startActivity(intent)
    }

    private fun navigateToSettings(){
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }
}