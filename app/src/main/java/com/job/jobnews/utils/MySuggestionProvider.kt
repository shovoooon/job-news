package com.job.jobnews.utils

import android.content.SearchRecentSuggestionsProvider

class MySuggestionProvider : SearchRecentSuggestionsProvider() {
    init {
        setupSuggestions(AUTHORITY, MODE)
    }

    companion object {
        const val AUTHORITY = "com.job.jobnews.utils.MySuggestionProvider"
        const val MODE: Int = DATABASE_MODE_QUERIES
    }
}