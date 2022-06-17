package com.adamfils.acronimyser.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adamfils.acronimyser.R
import com.adamfils.acronimyser.databinding.WordItemBinding
import com.adamfils.acronimyser.model.LFWords
import com.adamfils.acronimyser.model.WordModel
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo

class WordsAdapter : RecyclerView.Adapter<WordVH>() {

    private var data = ArrayList<WordModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordVH {
        val binding = LayoutInflater.from(parent.context).inflate(R.layout.word_item, parent, false)
        return WordVH(binding)
    }

    override fun onBindViewHolder(holder: WordVH, position: Int) {
        holder.bind(data[0].lfs[position])
        holder.itemView.setOnClickListener {
            YoYo.with(Techniques.Pulse).duration(500).playOn(it)
        }
    }

    override fun getItemCount(): Int {
        return if (data.size > 0 && data[0].lfs.size > 0) {
            data[0].lfs.size
        } else
            0
    }

    fun setList(list: ArrayList<WordModel>) {
        data = list
        notifyDataSetChanged()
    }
}

class WordVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val binding = WordItemBinding.bind(itemView)

    fun bind(word: LFWords) {
        binding.wordFullName.text = word.lf
        val frequency = "Frequency: ${word.freq}"
        binding.wordFrequency.text = frequency
        val since = "Since: ${word.since}"
        binding.wordSince.text = since
        val variant = "Variations (${word.vars.size})"
        binding.wordVariations.text = ""
        if (word.vars.size > 0) {
            binding.wordVariations.append(variant + "\n")
            for (v in word.vars) {
                binding.wordVariations.append(v.lf + "\n")
                binding.wordVariations.append("Freq: ${v.freq}\n")
                if (word.vars.indexOf(v) == word.vars.size - 1) {
                    binding.wordVariations.append("Since: ${v.since}")
                } else {
                    binding.wordVariations.append("Since: ${v.since}\n\n")
                }
            }
        } else {
            binding.wordVariations.text = variant
        }
    }

}