package com.epa.composenews.list

import com.epa.composenews.common.News

sealed class NewsListItem {
    class Item(val news: News) : NewsListItem()
    object Progress : NewsListItem()
    object Error : NewsListItem()
}