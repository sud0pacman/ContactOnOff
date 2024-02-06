package com.sudo_pacman.contactonoff.presenter.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sudo_pacman.contactonoff.data.model.ContactUIData
import com.sudo_pacman.contactonoff.data.model.StatusEnum
import com.sudo_pacman.contactonoff.databinding.ItemContactBinding


class ContactAdapter : ListAdapter<ContactUIData, ContactAdapter.ContactViewHolder>(MyDiffUtl){

    lateinit var clickMore: ((ContactUIData) -> Unit)

    object MyDiffUtl : DiffUtil.ItemCallback<ContactUIData>() {
        override fun areItemsTheSame(oldItem: ContactUIData, newItem: ContactUIData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ContactUIData,
            newItem: ContactUIData
        ): Boolean {
            return oldItem == newItem
        }
    }

    inner class ContactViewHolder(private val binding: ItemContactBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.textName.text = getItem(adapterPosition).firstName
            binding.textNumber.text = getItem(adapterPosition).phone

            when(getItem(adapterPosition).status) {
                StatusEnum.SYNC -> binding.textStatus.visibility = View.GONE
                StatusEnum.ADD  -> {
                    binding.textStatus.visibility  = View.VISIBLE
                    binding.textStatus.text = "Add"
                }
                StatusEnum.DELETE -> {
                    binding.textStatus.visibility = View.VISIBLE
                    binding.textStatus.text = "Delete"
                }
                StatusEnum.EDIT -> {
                    binding.textStatus.visibility = View.VISIBLE
                    binding.textStatus.text = "Edit"
                }
            }


            binding.buttonMore.setOnClickListener {
                clickMore.invoke(currentList[adapterPosition])
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder =
        ContactViewHolder(ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind()
    }

}