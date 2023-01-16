package com.joelespinal.pokeapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.joelespinal.pokeapp.R
import com.joelespinal.pokeapp.activities.REGION_ID
import com.joelespinal.pokeapp.ui.categories.RegionViewModel
import me.sargunvohra.lib.pokekotlin.model.Region

class RegionAdapter(private val context: Context, private val regionViewModel: RegionViewModel) : ListAdapter<Region, RegionAdapter.RegionViewHolder>(CATEGORY_COMPARATOR) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegionViewHolder {
            return RegionViewHolder.create(parent, regionViewModel)
        }

        override fun onBindViewHolder(holder: RegionViewHolder, position: Int) {
            val currentCategory = getItem(position)
            holder.bind(context, currentCategory, regionViewModel)

        }

        class RegionViewHolder(itemView: View, regionViewModel: RegionViewModel) :
            RecyclerView.ViewHolder(itemView) {
            private val regionImageView: ImageView = itemView.findViewById(R.id.category_item_image)
            private val regionName: TextView = itemView.findViewById(R.id.category_item_name)
            private val card: CardView = itemView.findViewById(R.id.card)

            fun bind(context: Context, region: Region?, homeViewModel: RegionViewModel) {
                regionImageView.setImageResource(R.mipmap.regions)

                regionName.text = region?.name
                regionImageView.setBackgroundColor(getColor(context, R.color.teal_700))

                card.setOnClickListener() {
                    val pokemonIntent = Intent(context, com.joelespinal.pokeapp.activities.PokemonActivity::class.java)
                    pokemonIntent.putExtra(REGION_ID, region?.id)
                    context.startActivity(pokemonIntent)
                }
            }

            companion object {
                fun create(
                    parent: ViewGroup,
                    categoriesViewModel: RegionViewModel
                ): RegionViewHolder {
                    val view: View = LayoutInflater.from(parent.context)
                        .inflate(R.layout.recycler_category_item, parent, false)
                    return RegionViewHolder(view, categoriesViewModel)
                }
            }
        }

        companion object {
            private val CATEGORY_COMPARATOR = object : DiffUtil.ItemCallback<Region>() {
                override fun areItemsTheSame(old: Region, new: Region): Boolean {
                    return old == new
                }

                override fun areContentsTheSame(old: Region, new: Region): Boolean {
                    return (old.id == new.id)
                }
            }
        }
}