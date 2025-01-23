package com.wahyusembiring.ui.component.topappbar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.material3.TopAppBar
import com.wahyusembiring.ui.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
   title: String,
   onMenuClick: () -> Unit,
   actions: @Composable RowScope.() -> Unit = {}
) {
   TopAppBar(
      title = {
         Text(text = title)
      },
      navigationIcon = {
         IconButton(onClick = onMenuClick) {
            Icon(
               painter = painterResource(id = R.drawable.ic_hamburger_menu),
               contentDescription = stringResource(R.string.menu)
            )
         }
      },
      actions = actions
   )
}