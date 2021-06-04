package com.ybatista.magicthegathering.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.ybatista.magicthegathering.R
import com.ybatista.magicthegathering.databinding.FragmentCardDetailBinding

class CardDetailFragment : Fragment() {

    private var cardImg: String = ""
    private var cardText: String = ""

    private lateinit var binding: FragmentCardDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_CARD_IMG)) {
                cardImg = it.getString(ARG_CARD_IMG) ?: ""
            }
            if (it.containsKey(ARG_CARD_TEXT)) {
                cardText = it.getString(ARG_CARD_TEXT)!!
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentCardDetailBinding.inflate(inflater, container, false)
        val rootView = binding.root

        if (cardText.isNotBlank()) {                // may be blank on landscape configuration change
            Glide.with(requireContext()).load(cardImg)
                .placeholder(R.drawable.ic_default_img)
                .into(binding.ivCardImg)
            binding.tvCardText.text = cardText
        } else {
            binding.tvCardText.text = getString(R.string.select_card)
        }

        return rootView
    }

    companion object {
        const val ARG_CARD_NAME = "title"           // to set toolbar title
        const val ARG_CARD_IMG = "card_img"
        const val ARG_CARD_TEXT = "card_text"
    }

}