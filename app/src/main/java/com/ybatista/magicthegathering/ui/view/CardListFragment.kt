package com.ybatista.magicthegathering.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.ybatista.magicthegathering.R
import com.ybatista.magicthegathering.databinding.FragmentCardListBinding
import com.ybatista.magicthegathering.domain.Card
import com.ybatista.magicthegathering.ui.adapter.CardLoadStateAdapter
import com.ybatista.magicthegathering.ui.adapter.CardsAdapter
import com.ybatista.magicthegathering.ui.viewModel.CardListViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException

@AndroidEntryPoint
class CardListFragment : Fragment() {

    private val viewModel: CardListViewModel by viewModels()
    private lateinit var binding: FragmentCardListBinding
    private lateinit var adapter: CardsAdapter
    private var itemDetailFragmentContainer: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCardListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val onClickListener = View.OnClickListener { itemView ->
            val item = itemView.tag as Card
            val bundle = Bundle()          //  TODO  use safe args
            bundle.putString(
                CardDetailFragment.ARG_CARD_IMG,
                item.imageUrl
            )
            bundle.putString(
                CardDetailFragment.ARG_CARD_NAME,
                item.name
            )
            bundle.putString(
                CardDetailFragment.ARG_CARD_TEXT,
                item.text
            )

            // Leaving this not using view binding as it relies on if the view is visible the current
            // layout configuration (layout, landscape)
            itemDetailFragmentContainer = view.findViewById(R.id.card_detail_nav_container)

            itemDetailFragmentContainer?.findNavController()
                ?.navigate(R.id.card_detail_fragment, bundle)           // landscape navigation
                ?: run {
                    itemView.findNavController()
                        .navigate(R.id.show_card_detail, bundle)      // portrait navigation
                }
        }

        setupRecyclerView(onClickListener)
        fetchCards()
    }

    private fun setupRecyclerView(onClickListener: View.OnClickListener) {
        adapter = CardsAdapter(onClickListener)
        adapter.addLoadStateListener { loadStateListener(it) }       // handle general loading and error states
        binding.itemList.adapter =
            adapter.withLoadStateFooter(CardLoadStateAdapter { adapter.retry() })      // handle adapter loading and error states
        binding.btRetry.setOnClickListener {
            adapter.retry()
        }
    }

    private fun loadStateListener(states: CombinedLoadStates) {
        binding.apply {

            val loading = states.source.refresh is LoadState.Loading
            val error = states.source.refresh is LoadState.Error

            pbLoading.isVisible = loading
            itemList.isVisible = !error
            llRetry.isVisible = error
            tvError.text = getString(
                if (error && (states.source.refresh as LoadState.Error).error is IOException) {
                    R.string.no_internet_connection
                } else {
                    R.string.results_could_not_be_loaded
                }
            )
        }
    }

    private fun fetchCards() {
        viewModel.cards.observe(viewLifecycleOwner, {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        })
    }
}