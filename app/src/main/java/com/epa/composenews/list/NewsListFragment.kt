package com.epa.composenews.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.epa.composenews.NewsRepository
import com.epa.composenews.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.epa.composenews.ui.theme.ComposeNewsTheme

class NewsListFragment : Fragment() {

    private val viewModel by viewModel {
        NewsListViewModel(
            repository = NewsRepository()
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                ComposeNewsTheme() {
                    val context = LocalContext.current
                    NewsScreen(viewModel)
                }
            }
        }
    }
}