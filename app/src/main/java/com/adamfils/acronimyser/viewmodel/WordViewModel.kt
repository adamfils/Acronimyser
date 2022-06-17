package com.adamfils.acronimyser.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adamfils.acronimyser.model.WordErrorType
import com.adamfils.acronimyser.model.WordModel
import com.adamfils.acronimyser.utils.APIClient.searchWord
import com.adamfils.acronimyser.utils.SharedPrefUtils.QUERY_KEY
import com.adamfils.acronimyser.utils.SharedPrefUtils.RESULT_KEY
import com.adamfils.acronimyser.utils.SharedPrefUtils.getPrefString
import com.google.gson.Gson
import kotlinx.coroutines.launch

class WordViewModel(context: Context) : ViewModel() {
    private val wordList = MutableLiveData<ArrayList<WordModel>>()
    private val error = MutableLiveData<WordErrorType>()
    private val cacheQuery = MutableLiveData<String>()

    init {
        val result = context.getPrefString(RESULT_KEY)
        if (result.isNotEmpty()) {
            val data =
                Gson().fromJson(result, Array<WordModel>::class.java).toCollection(ArrayList())
            wordList.value = data
        }
        val query = context.getPrefString(QUERY_KEY)
        if (query.isNotEmpty()) {
            cacheQuery.value = query
        }
    }

    fun searchWord(context: Context, word: String) {
        wordList.value?.clear()
        viewModelScope.launch {
            val list = context.searchWord(word, error, cacheQuery)
            wordList.postValue(list)
        }
    }

    fun getWordList(): LiveData<ArrayList<WordModel>> {
        return wordList
    }

    fun getQuery(): LiveData<String> {
        return cacheQuery
    }

    fun getError(): LiveData<WordErrorType> {
        return error
    }
}