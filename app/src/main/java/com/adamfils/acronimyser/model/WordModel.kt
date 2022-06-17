package com.adamfils.acronimyser.model

class WordModel {
    var sf = ""
    var lfs = ArrayList<LFWords>()
}

class LFWords {
    var lf = String()
    var freq = 0
    var since = 0
    var vars = ArrayList<LFWords>()
}

enum class WordErrorType {
    NO_RESULTS,
    NETWORK_ERROR,
}