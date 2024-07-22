package com.example.outdoorromagna.ui.composables

import android.util.Log
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.outdoorromagna.ui.OutdoorRomagnaRoute
import com.example.outdoorromagna.ui.screens.tracks.TracksActions

enum class FilterOption(val title: String) {
    ALL_TRACKS("Tutti i percorsi"),
    YOUR_TRACKS("I tuoi percorsi"),
    FAVOURITES("Preferiti"),
}
@Composable
fun FilterBar(
    actions: TracksActions,
    filterOption: Int,
) {
    Log.d("TAG", "Dentro filter bar")
    val scrollState = rememberScrollState()
    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(filterOption)
    }
    Log.d("selected item",selectedItemIndex.toString())
    //var currentFilter by remember { mutableStateOf(FilterOption.ALL_TRACKS) }
    NavigationBar(
        Modifier
            .fillMaxWidth()
            .height(56.dp)
            .horizontalScroll(scrollState)
            .padding(vertical = 1.dp),
        containerColor = MaterialTheme.colorScheme.primaryContainer,
    ) {
        FilterOption.entries.forEachIndexed { index, _ ->
            NavigationBarItem(
                modifier = Modifier.padding(horizontal = 2.dp),
                onClick = {
                    selectedItemIndex = index
                    when (FilterOption.entries[selectedItemIndex]) {
                        FilterOption.YOUR_TRACKS -> actions.setFilter(FilterOption.YOUR_TRACKS)
                        FilterOption.ALL_TRACKS -> actions.setFilter(FilterOption.ALL_TRACKS)
                        FilterOption.FAVOURITES -> actions.setFilter(FilterOption.FAVOURITES)
                    }
                },
                icon = {  },
                selected = index == selectedItemIndex,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    indicatorColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                label = { Text(text = FilterOption.entries[index].title) }
            )
        }
    }
}