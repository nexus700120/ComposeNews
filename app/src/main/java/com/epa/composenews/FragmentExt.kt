package com.epa.composenews

import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@MainThread
inline fun <reified VM : ViewModel> Fragment.viewModel(
    noinline vmCreator: (() -> VM)? = null
): Lazy<VM> = lazy {
    ViewModelProvider(viewModelStore, object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return (vmCreator?.invoke() ?: ViewModelProvider.NewInstanceFactory().create(modelClass)) as T
        }
    }).get(VM::class.java)
}