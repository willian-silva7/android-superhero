package com.stellantis.espw

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.squareup.picasso.BuildConfig
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import com.stellantis.espw.api.SuperheroItemResponse
import com.stellantis.espw.databinding.ItemSuperheroBinding

class SuperHeroViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding = ItemSuperheroBinding.bind(view)

    fun bind(superheroItemResponse: SuperheroItemResponse, onItemSelected: (String) -> Unit) {
        binding.tvSuperheroName.text = superheroItemResponse.name

        binding.tvSuperheroName.text = superheroItemResponse.name
        Glide.with(itemView)
            .load(superheroItemResponse.superheroImage.url)
            .into(binding.ivSuperhero)

        binding.root.setOnClickListener {onItemSelected(superheroItemResponse.superheroId)}
    }
}