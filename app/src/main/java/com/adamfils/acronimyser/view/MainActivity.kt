package com.adamfils.acronimyser.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import com.adamfils.acronimyser.adapter.WordsAdapter
import com.adamfils.acronimyser.databinding.ActivityMainBinding
import com.adamfils.acronimyser.model.WordErrorType
import com.adamfils.acronimyser.utils.DialogUtils.showDialog
import com.adamfils.acronimyser.viewmodel.WordViewModel
import es.dmoral.toasty.Toasty

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: WordsAdapter
    private lateinit var viewModel: WordViewModel
    private var canClose = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = WordsAdapter()
        binding.wordList.adapter = adapter
        viewModel = WordViewModel(this)
        viewModel.getWordList().observe(this) {
            if (it.size == 0) {
                binding.errorLayout.visibility = View.VISIBLE
            } else {
                binding.errorLayout.visibility = View.GONE
            }
            adapter.setList(it)
        }
        viewModel.getError().observe(this) {
            binding.errorLayout.visibility = View.VISIBLE
            when (it) {
                WordErrorType.NO_RESULTS -> {
                    binding.wordList.context.showDialog(
                        "No results found",
                        "Please try another word",
                        "OK"
                    )
                }
                WordErrorType.NETWORK_ERROR -> {
                    binding.wordList.context.showDialog(
                        "Network error",
                        "Please try again later",
                        "OK"
                    )
                }
            }
        }
        viewModel.getQuery().observe(this) {
            val result = "Results for \"$it\""
            binding.searchTerm.text = result
        }

        binding.searchBox.setOnEditorActionListener { _, i, _ ->
            when (i) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    val query = binding.searchBox.text.toString()
                    if (query.isEmpty()) {
                        binding.searchBox.error = "Please enter a query"
                    } else {
                        viewModel.searchWord(this@MainActivity, query)
                    }
                    true
                }
                else -> false
            }
        }
    }

    override fun onBackPressed() {
        if (canClose) {
            super.onBackPressed()
        } else {
            canClose = true
            Toasty.info(this, "Press back again to exit").show()
            Handler(Looper.getMainLooper()).postDelayed({ canClose = false }, 2000)
        }
    }
}