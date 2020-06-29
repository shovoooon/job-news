package com.job.news.utils

import android.content.SearchRecentSuggestionsProvider

class MySuggestionProvider : SearchRecentSuggestionsProvider() {
    init {
        setupSuggestions(AUTHORITY, MODE)
    }

    companion object {
        const val AUTHORITY = "com.example.jobnewspaper.utils.MySuggestionProvider"
        const val MODE: Int = DATABASE_MODE_QUERIES
    }
}