package com.epa.composenews.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.epa.composenews.NewsRepository
import com.epa.composenews.ui.theme.ComposeNewsTheme
import com.epa.composenews.viewModel

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
                    NewsScreen(viewModel)
                }
            }
        }
    }
}