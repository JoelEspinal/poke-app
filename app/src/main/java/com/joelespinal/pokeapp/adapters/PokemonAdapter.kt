package com.joelespinal.pokeapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.joelespinal.pokeapp.R
import com.joelespinal.pokeapp.ui.pokemon.PokemonViewModel
import me.sargunvohra.lib.pokekotlin.model.Pokemon

    class PokemonAdapter(private val context: Context, private val homeViewModel: PokemonViewModel) : ListAdapter<Pokemon, PokemonAdapter.PokemonViewHolder>(CATEGORY_COMPARATOR) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
            return PokemonViewHolder.create(parent, homeViewModel)
        }

        override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
            val currentCategory = getItem(position)
            holder.bind(context, currentCategory, homeViewModel)

        }

        class PokemonViewHolder(itemView: View, pokmeonViewModel: PokemonViewModel) :
            RecyclerView.ViewHolder(itemView) {
            private val pokemonImageView: ImageView = itemView.findViewById(R.id.category_item_image)
            private val pokemonName: TextView = itemView.findViewById(R.id.category_item_name)
            private val card: CardView = itemView.findViewById(R.id.card)
            private var itemSelected = false

            fun bind(context: Context, pokemon: Pokemon?, homeViewModel: PokemonViewModel) {
                pokemonImageView.setImageResource(R.mipmap.ic_launcher_round)
                Glide.with(context)
                    .load(pokemon?.sprites?.frontDefault)
                    .into(pokemonImageView)

                pokemonName.text = pokemon?.name
                pokemonImageView.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.teal_700
                    )
                )

                card.setOnClickListener() {
                    itemSelected = if (itemSelected) {
                        pokemon.let {
                            homeViewModel.deselectPokemon(it!!)
                            pokemonImageView.setBackgroundColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.teal_700
                                )
                            )
                        }
                        false
                    } else {
                       if ( homeViewModel.selectedPokemoms.value?.size!! < 6) {
                           pokemon.let{
                               homeViewModel.selectPokemon(it!!)
                               pokemonImageView.setBackgroundColor(
                                   ContextCompat.getColor(
                                       context,
                                       R.color.black
                                   )
                               )
                           }
                       }
                        true
                    }

                    true
                }
            }

            companion object {
                fun create(
                    parent: ViewGroup,
                    categoriesViewModel: PokemonViewModel
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