package com.ybatista.magicthegathering.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ybatista.magicthegathering.R
import com.ybatista.magicthegathering.databinding.CardItemBinding
import com.ybatista.magicthegathering.domain.Card

class CardsAdapter(
    private val onClickListener: View.OnClickListener
) :
    PagingDataAdapter<Card, CardsAdapter.ViewHolder>(diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            CardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.tag = item
        holder.bind()

        Glide.with(holder.itemView).load(item?.imageUrl)
            .placeholder(R.drawable.ic_default_img)
            .into(holder.cardImg)
        holder.cardName.text = item?.name
        holder.cardType.text = item?.type
    }

    inner class ViewHolder(binding: CardItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val cardImg: ImageView = binding.ivCard
        val cardName: TextView = binding.tvCardName
        val cardType: TextView = binding.tvCardType

        fun bind() {
            itemView.setOnClickListener(onClickListener)
        }
    }

    companion object {
        private val diff = object : DiffUtil.ItemCallback<Card>() {
            override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean {
                return oldItem == newItem
            }

        }
    }

}