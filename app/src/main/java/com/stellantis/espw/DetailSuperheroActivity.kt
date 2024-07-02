package com.stellantis.espw

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.stellantis.espw.api.ApiService
import com.stellantis.espw.api.Powerstats
import com.stellantis.espw.api.SuperHeroDataResponse
import com.stellantis.espw.api.SuperHeroDetailResponse
import com.stellantis.espw.databinding.ActivityDetailSuperheroBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.roundToInt

class DetailSuperheroActivity: AppCompatActivity() {

    companion object{
        const val EXTRA_ID = "extra_id"
    }

    private lateinit var binding: ActivityDetailSuperheroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailSuperheroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val id: String = intent.getStringExtra(EXTRA_ID).orEmpty()
        getSuperheroInformation(id = id)
    }

    private fun getSuperheroInformation(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val superheroDetail = getRetrofit().create(ApiService::class.java).getSuperheroDetail(id = id)
                if (superheroDetail.body() != null) {
                    runOnUiThread { createUI(superheroDetail.body()!!)}
                }
            } catch (e: Exception) {
                Log.e("testHero", "Error: ${e.message}", e)
            }
        }
    }

    private fun createUI(superhero: SuperHeroDetailResponse) {
        Log.d("DetailSuperheroActivity", "Superhero Name: ${superhero.name}")
        Log.d("DetailSuperheroActivity", "Superhero Image URL: ${superhero.image.url}")

        Glide.with(this)
            .load(superhero.image.url)  // Acessando a URL da imagem corretamente
            .into(binding.ivSuperhero)

        binding.tvSuperheroName.text = superhero.name

        prepareStats(superhero.powerstats)
        binding.tvSuperheroRealName.text = superhero.biography.fullName
        binding.tvPublisher.text = superhero.biography.publisher
    }

    private fun prepareStats(powerStats: Powerstats) {
        updateHeight(binding.viewCombat, powerStats.combat)
        updateHeight(binding.viewSpeed, powerStats.speed)
        updateHeight(binding.viewStrength, powerStats.strength)
        updateHeight(binding.viewIntelligence, powerStats.intelligence)
        updateHeight(binding.viewPower, powerStats.power)
        updateHeight(binding.viewDurability, powerStats.durability)
    }

    private fun updateHeight(view: View, stat: String) {
        val params = view.layoutParams
        params.height = pxToDp(stat.toFloat())
        view.layoutParams = params
    }

    private fun pxToDp(px: Float): Int{
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, resources.displayMetrics).roundToInt()
    }


    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://superheroapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}