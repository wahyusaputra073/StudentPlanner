package com.wahyusembiring.ui.component.popup.picker.timepicker


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import com.wahyusembiring.data.model.Time
import com.wahyusembiring.ui.R
import com.wahyusembiring.ui.theme.spacing

@Composable
fun TimePickerOption(
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.when_do_you_want_to_be_reminded),
    onTimeSelected: (time: Time) -> Unit,
    onDismissRequest: () -> Unit,
) {
    // Options for reminder time (negative values indicate time before deadline)
    val timeOptions = listOf(
        "5 minutes before" to Time(hour = 0, minute = -5),
        "10 minutes before" to Time(hour = 0, minute = -10),
        "15 minutes before" to Time(hour = 0, minute = -15),
        "30 minutes before" to Time(hour = 0, minute = -30),
        "1 hour before" to Time(hour = -1, minute = 0),
        "2 hours before" to Time(hour = -2, minute = 0)
    )

    var selectedOption by remember { mutableStateOf<Time?>(null) }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = modifier,
            color = AlertDialogDefaults.containerColor,
            shape = AlertDialogDefaults.shape,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(
                modifier = Modifier.padding(vertical = MaterialTheme.spacing.Large),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = MaterialTheme.spacing.Large)
                        .padding(bottom = MaterialTheme.spacing.Medium),
                    color = MaterialTheme.colorScheme.primary,
                    text = title
                )

                Column(
                    modifier = Modifier.padding(horizontal = MaterialTheme.spacing.Large)
                ) {
                    timeOptions.forEach { (text, time) ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = time == selectedOption,
                                    onClick = { selectedOption = time }
                                )
                                .padding(vertical = MaterialTheme.spacing.Small),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = time == selectedOption,
                                onClick = { selectedOption = time }
                            )
                            Text(
                                text = text,
                                modifier = Modifier.padding(start = MaterialTheme.spacing.Small)
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.spacing.Large),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                    TextButton(
                        onClick = {
                            selectedOption?.let { time ->
                                onTimeSelected(time)
                                onDismissRequest()
                            }
                        }
                    ) {
                        Text(text = stringResource(id = R.string.confirm))
                    }
                }
            }
        }
    }
}