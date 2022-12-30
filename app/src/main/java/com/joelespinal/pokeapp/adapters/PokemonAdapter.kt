package com.joelespinal.pokeapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.joelespinal.pokeapp.R
import com.joelespinal.pokeapp.ui.home.HomeViewModel
import me.sargunvohra.lib.pokekotlin.model.Pokemon

    class PokemonAdapter(private val context: Context, private val homeViewModel: HomeViewModel) : ListAdapter<Pokemon, PokemonAdapter.PokemonViewHolder>(CATEGORY_COMPARATOR) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
            return PokemonViewHolder.create(parent, homeViewModel)
        }

        override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
            val currentCategory = getItem(position)
            holder.bind(context, currentCategory, homeViewModel)

        }

        class PokemonViewHolder(itemView: View, pokmeonViewModel: HomeViewModel) :
            RecyclerView.ViewHolder(itemView) {
            private val pokemonImageView: ImageView = itemView.findViewById(R.id.pokemon_item_image)
            private val pokemonName: TextView = itemView.findViewById(R.id.pokemon_item_title)
            private val card: CardView = itemView.findViewById(R.id.card)
            private var itemSelected = false

            fun bind(context: Context, pokemon: Pokemon?, homeViewModel: HomeViewModel) {
                pokemonImageView.setImageResource(R.mipmap.ic_launcher_round)
                Glide.with(context)
                    .load(pokemon?.sprites?.frontDefault)
                    .into(pokemonImageView)

                pokemonName.text = pokemon?.name
                pokemonImageView.setBackgroundColor(R.color.teal_700)

                card.setOnLongClickListener {
                    itemSelected = if (itemSelected) {
                        pokemon.let {
                            homeViewModel.deselectPokemon(it!!)
                            pokemonImageView.setBackgroundColor(R.color.teal_700)
                        }
                        false
                    } else {
                        pokemon.let{
                            homeViewModel.selectPokemon(it!!)
                            pokemonImageView.setBackgroundColor(R.color.black)
                        }
                        true
                    }

                    true
                }
            }

            companion object {
                fun create(
                    parent: ViewGroup,
                    categoriesViewModel: HomeViewModel
                ): PokemonViewHolder {
                    val view: View = LayoutInflater.from(parent.context)
                        .inflate(R.layout.recycler_pokemon_item, parent, false)
                    return PokemonViewHolder(view, categoriesViewModel)
                }
            }
        }

        companion object {
            private val CATEGORY_COMPARATOR = object : DiffUtil.ItemCallback<Pokemon>() {
                override fun areItemsTheSame(old: Pokemon, new: Pokemon): Boolean {
                    return old == new
                }

                override fun areContentsTheSame(old: Pokemon, new: Pokemon): Boolean {
                    return (old.id == new.id)
                }
            }
        }
}