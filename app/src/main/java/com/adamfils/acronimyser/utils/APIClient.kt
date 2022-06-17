package com.adamfils.acronimyser.utils

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.adamfils.acronimyser.model.WordErrorType
import com.adamfils.acronimyser.model.WordModel
import com.adamfils.acronimyser.utils.SharedPrefUtils.QUERY_KEY
import com.adamfils.acronimyser.utils.SharedPrefUtils.RESULT_KEY
import com.adamfils.acronimyser.utils.SharedPrefUtils.setPrefString
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.google.gson.Gson
import kotlinx.coroutines.suspendCancellableCoroutine
import org.json.JSONArray
import kotlin.coroutines.resume

object APIClient {

    suspend fun Context.searchWord(
        word: String,
        error: MutableLiveData<WordErrorType>,
        query: MutableLiveData<String>
    ): ArrayList<WordModel> =
        suspendCancellableCoroutine { continuation ->
            val baseUrl = "http://www.nactem.ac.uk/software/acromine/dictionary.py"
            AndroidNetworking.get(baseUrl)
                .addQueryParameter("sf", word)
                .build()
                .getAsJSONArray(object : JSONArrayRequestListener {
                    override fun onResponse(response: JSONArray) {
                        setPrefString(RESULT_KEY, response.toString())
                        setPrefString(QUERY_KEY, word)
                        query.postValue(word)
                        val data =
                            Gson().fromJson(
                                response.toString(),
                                Array<WordModel>::class.java
                            ).toCollection(ArrayList())
                        if (data.isEmpty()) {
                            error.value = WordErrorType.NO_RESULTS
                        }
                        continuation.resume(data)
                    }

                    override fun onError(anError: ANError) {
                        continuation.resume(ArrayList())
                        anError.printStackTrace()
                        //anError.message?.log()
                        error.postValue(WordErrorType.NETWORK_ERROR)
                    }
                })
        }

    /*fun Any.log() {
        Log.e("ADAMX", this.toString())
    }*/
}