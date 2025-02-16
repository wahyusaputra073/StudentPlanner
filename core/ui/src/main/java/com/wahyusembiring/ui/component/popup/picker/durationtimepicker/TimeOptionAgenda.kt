package com.wahyusembiring.ui.component.popup.picker.durationtimepicker


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
import com.wahyusembiring.data.model.SpanTime
import com.wahyusembiring.ui.R
import com.wahyusembiring.ui.theme.spacing

@Composable
fun TimePickerOptionAgenda(
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.when_do_you_want_to_be_reminded),
    spanTime: SpanTime,
    onTimeSelected: (time: Time) -> Unit,
    onDismissRequest: () -> Unit,
) {
    // Options for reminder time based on start time from SpanTime
    val timeOptions = listOf(
        "5 minutes before start" to Time(
            hour = spanTime.startTime.hour,
            minute = spanTime.startTime.minute - 5
        ),
        "10 minutes before start" to Time(
            hour = spanTime.startTime.hour,
            minute = spanTime.startTime.minute - 10
        ),
        "15 minutes before start" to Time(
            hour = spanTime.startTime.hour,
            minute = spanTime.startTime.minute - 15
        ),
        "30 minutes before start" to Time(
            hour = spanTime.startTime.hour,
            minute = spanTime.startTime.minute - 30
        ),
        "1 hour before start" to Time(
            hour = spanTime.startTime.hour - 1,
            minute = spanTime.startTime.minute
        ),
        "2 hour before start" to Time(
            hour = spanTime.startTime.hour - 2,
            minute = spanTime.startTime.minute
        )
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