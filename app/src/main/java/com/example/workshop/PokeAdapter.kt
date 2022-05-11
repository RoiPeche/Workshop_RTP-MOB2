package com.example.workshop

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PokeAdapter(var dataSet: MutableList<MainActivity.Pokemon>) :
    RecyclerView.Adapter<PokeAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val pokemonName: TextView = view.findViewById(R.id.pokemonName)
        val pokemonID: TextView = view.findViewById(R.id.pokemonID)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.pokemon_list_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.pokemonName.text = dataSet[position].info.name
        viewHolder.pokemonID.text = dataSet[position].info.id.toString()
    }

    override fun getItemCount() = dataSet.size
}