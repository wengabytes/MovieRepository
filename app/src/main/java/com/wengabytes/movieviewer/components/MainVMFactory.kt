package com.wengabytes.movieviewer.components

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wengabytes.movieviewer.components.details.DetailsFragmentVM
import com.wengabytes.movieviewer.components.seatmap.SeatmapFragmentVM
import com.wengabytes.movieviewer.services.MovieRepository
import org.koin.dsl.module

val vmFactoryModule = module {
    factory { MainVMFactory(get(), get()) }
}

class MainVMFactory constructor(
    private val context: Context,
    private val repo: MovieRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            (modelClass.isAssignableFrom(DetailsFragmentVM::class.java)) ->
                DetailsFragmentVM(context, repo) as T

            (modelClass.isAssignableFrom(SeatmapFragmentVM::class.java)) ->
                SeatmapFragmentVM(context, repo) as T

            else ->
                throw IllegalArgumentException("Could not create $modelClass Unknown ViewModel Class. Creation not defined in $this")
        }
    }
}