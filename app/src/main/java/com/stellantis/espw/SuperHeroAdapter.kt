package com.stellantis.espw

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stellantis.espw.api.SuperheroItemResponse

class SuperHeroAdapter(
    private var superheroList: List<SuperheroItemResponse> = emptyList(),
    private val onItemSelected:(String) -> Unit
):
    RecyclerView.Adapter<SuperHeroViewHolder>() {
    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: List<SuperheroItemResponse>){
        superheroList = list
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuperHeroViewHolder {
        return SuperHeroViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_superhero,parent, false)
        )
    }

    override fun getItemCount(): Int {
        return superheroList.size
    }

    override fun onBindViewHolder(viewHolder: SuperHeroViewHolder, position: Int) {
        viewHolder.bind(superheroList[position], onItemSelected)
    }
}