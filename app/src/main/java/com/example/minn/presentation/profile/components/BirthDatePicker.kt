package com.example.minn.presentation.profile.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.minn.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun BirthDatePicker(
    birthDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit
){

    var showDialog by rememberSaveable{ mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(0.9f)
    ){
        Text(
            text = stringResource(R.string.birthdate),
            modifier = Modifier
                .padding(top = 15.dp),
            style = MaterialTheme.typography.labelMedium
        )
        Text(
            text = birthDate?.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) ?: "",
            modifier = Modifier
                .padding(start = 10.dp, top = 8.dp, bottom = 8.dp)
                .fillMaxWidth(0.9f)
                .clickable { showDialog = true },
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )

    }
    if (showDialog){
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = birthDate?.toEpochDay()?.let {
            java.time.Instant.from(LocalDate.ofEpochDay(it).atStartOfDay(java.time.ZoneId.systemDefault()))
                .toEpochMilli()
        })
        DatePickerDialog(
            onDismissRequest = {showDialog = false},
            confirmButton = {
                TextButton(onClick = {
                    val selectedMillis = datePickerState.selectedDateMillis
                    if (selectedMillis != null) {
                        val selectedDate = java.time.Instant.ofEpochMilli(selectedMillis)
                            .atZone(java.time.ZoneId.systemDefault())
                            .toLocalDate()
                        onDateSelected(selectedDate)
                    }
                    showDialog = false
                }) {
                    Text(
                        text = stringResource(R.string.ok))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog = false }) {
                    Text(
                        text = stringResource(R.string.cancellation)
                    )
                }
            },
        ){
            DatePicker(state = datePickerState)
        }
    }

}