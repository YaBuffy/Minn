package com.example.minn.presentation.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.minn.R
import com.example.minn.domain.model.Gender

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderDropDown(
    selectedGender: Gender,
    onGenderSelected: (Gender) -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth(0.9f)
    ) {
        Text(
            text = stringResource(R.string.gender),
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        Row(
            Modifier
                .padding(top = 5.dp)
                .fillMaxWidth()
                .clip(shape = MaterialTheme.shapes.extraLarge)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = MaterialTheme.shapes.extraLarge
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                BasicTextField(
                    value = stringResource(selectedGender.labelRes),
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .menuAnchor()
                        .padding(start = 10.dp, top = 8.dp, bottom = 8.dp)
                        .fillMaxWidth(0.8f),
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.onPrimaryContainer)
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = MaterialTheme.shapes.medium
                        )
                ){
                    Gender.entries.forEachIndexed { index, gender ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = stringResource(gender.labelRes),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = if (gender == selectedGender)
                                        MaterialTheme.colorScheme.onPrimaryContainer
                                    else
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            leadingIcon = {
                                if (gender == selectedGender) {
                                    Icon(
                                        painter = painterResource(R.drawable.outline_check_24),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            },
                            onClick = {
                                onGenderSelected(gender)
                                expanded = false
                            },
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.medium)
                                .background(
                                    if (gender == selectedGender)
                                        MaterialTheme.colorScheme.primaryContainer
                                    else
                                        Color.Transparent
                                )
                        )

                        if (index < Gender.entries.lastIndex) {
                            Divider(
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                thickness = 0.5.dp,
                                modifier = Modifier.padding(horizontal = 10.dp)
                            )
                        }
                    }
                }
            }
            Icon(
                painter = painterResource(R.drawable.outline_wc_24),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(end = 10.dp, top = 8.dp, bottom = 8.dp)
            )
        }

    }

}

@Preview
@Composable
fun prevGenderDropDown(){
    var gender by rememberSaveable { mutableStateOf<Gender>(Gender.NOT_SPECIFIED) }
    GenderDropDown(selectedGender = gender, onGenderSelected = { gender = it})

}