package com.joelespinal.pokeapp.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.joelespinal.pokeapp.R
import com.joelespinal.pokeapp.adapters.RegionAdapter
import com.joelespinal.pokeapp.databinding.FragmentCategoriesBinding

class CategoriesFragment : Fragment() {

    private var _binding: FragmentCategoriesBinding? = null
    private lateinit var regionViewModel: RegionViewModel
    private lateinit var progressBar: ProgressBar
    private lateinit var regionAdapter: RegionAdapter
    private lateinit var placeholder: FrameLayout

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        regionViewModel =
            ViewModelProvider(this).get(RegionViewModel::class.java)

        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        regionViewModel.getRegionNames()
        progressBar = view.findViewById(R.id.progress)
        placeholder = view.findViewById(R.id.placeholder)
        initRecycleView(view)

    }

    private fun initRecycleView(view: View) {
        context?.let { context ->
            val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_categories)
            regionAdapter = RegionAdapter(context, regionViewModel)
            recyclerView.adapter = regionAdapter
            recyclerView.layoutManager = GridLayoutManager(activity, 2)

            activity?.let { activity ->
                regionViewModel.regions.observe(activity) { regions ->
                    regionAdapter.submitList(regions)
                    if (regions.isEmpty()) {
                        placeholder.visibility = View.VISIBLE
                    } else {
                        placeholder.visibility = View.INVISIBLE
                    }
                }
            }

//            swipeContainer.setOnRefreshListener {
//                selectedRegion?.let {
//                    fromIndex += 10
//                    homeViewModel.getPokemonsByRegion(it.id, fromIndex)
//                    activity?.let { activity ->
//                        homeViewModel.pokemons.observe(activity) { pokemons ->
//                            regionAdapter.submitList(pokemons)
//                            regionAdapter.notifyDataSetChanged()
//                        }
//                    }
//
//                }
//
//                swipeContainer.isRefreshing = false
//            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}