package com.example.minn.presentation.chatList.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.minn.R


@Composable
fun UserSearchBar(
    searchQuery: String,
    onQueryChange: (String)-> Unit,
    onBack: ()->Unit,
    onSearchClear: ()-> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .systemBarsPadding()
            .padding(top = 5.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBack
        ) {
            Icon(
                painter = painterResource(R.drawable.outline_arrow_back_24),
                contentDescription = null
            )
        }
        BasicTextField(
            value = searchQuery,
            onValueChange = onQueryChange,
            maxLines = 1,
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
            decorationBox = { innerTextField ->
                if (searchQuery.isEmpty()) {
                    Text(
                        text = stringResource(R.string.search),
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.4f)
                    )
                }
                innerTextField()
            },
            modifier = Modifier
                .weight(1f)
        )
        IconButton(
            onClick = onSearchClear
        ) {
            Icon(
                painterResource(R.drawable.outline_clear_24),
                contentDescription = null
            )
        }
    }
}

//@Composable
//@Preview
//fun PrevUserSearchBar(){
//    UserSearchBar(
//        searchQuery = "",
//        onQueryChange = { },
//        onBack = {},
//        onSearchClear = {},
//    )
//}