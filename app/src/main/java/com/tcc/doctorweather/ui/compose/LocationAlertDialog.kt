package com.tcc.doctorweather.ui.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun LocationAlertDialog(setShowDialog: (Boolean) -> Unit, onValueSet: (String, String) -> Unit) {

    val inputLatitude = remember { mutableStateOf("") }
    val inputLongitude = remember { mutableStateOf("") }

    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.secondaryContainer
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    Text(
                        text = "Set Latitude & Longitude",
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    SetupInputFields(inputLatitude, inputLongitude)

                    Spacer(modifier = Modifier.height(20.dp))

                    // Done button
                    Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                        Button(
                            onClick = {
                                if (inputLatitude.value.isEmpty()) {
                                    return@Button
                                }
                                onValueSet(inputLatitude.value, inputLongitude.value)
                                setShowDialog(false)
                            },
                            shape = RoundedCornerShape(50.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text(text = "Done")
                        }
                    }
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun SetupInputFields(
    inputLatitude: MutableState<String>,
    inputLongitude: MutableState<String>
) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                BorderStroke(
                    width = 2.dp,
                    color = if (inputLatitude.value.isNotEmpty()) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.error
                    }
                ),
                shape = RoundedCornerShape(20)
            ),
        placeholder = { Text(text = "Enter Latitude") },
        value = inputLatitude.value,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        onValueChange = {
            inputLatitude.value = it.take(20).replace("\n", "")
        },
        shape = RoundedCornerShape(20),
        maxLines = 1,
    )

    Spacer(modifier = Modifier.height(10.dp))
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                BorderStroke(
                    width = 2.dp,
                    color = if (inputLongitude.value.isNotEmpty()) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.error
                    }
                ),
                shape = RoundedCornerShape(20)
            ),
        placeholder = { Text(text = "Enter Longitude") },
        value = inputLongitude.value,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        onValueChange = {
            inputLongitude.value = it.take(20).replace("\n", "")
        },
        shape = RoundedCornerShape(20),
        maxLines = 1
    )
}