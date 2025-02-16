package com.wahyusembiring.ui.component.button

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.wahyusembiring.common.util.withZeroPadding
import com.wahyusembiring.data.model.Time
import com.wahyusembiring.ui.R

@Composable
fun AddAgendaReminderButton(
    modifier: Modifier = Modifier,
    time: Time?,
    startTime: Time, // Tambahkan parameter startTime untuk menghitung selisih waktu
    onClicked: (() -> Unit)? = null,
    permissionCheck: (() -> Unit)? = null
) {
    ListItem(
        modifier = modifier
            .then(
                if (onClicked != null) {
                    Modifier.clickable {
                        permissionCheck?.invoke() ?: onClicked()
                    }
                } else {
                    Modifier
                }
            ),
        leadingContent = {
            Icon(
                painter = painterResource(id = R.drawable.ic_reminder),
                contentDescription = stringResource(R.string.add_reminder),
                tint = MaterialTheme.colorScheme.primary
            )
        },
        headlineContent = {
            if (time != null) {
                // Hitung selisih waktu antara waktu pengingat dan waktu mulai
                val hourDiff = startTime.hour - time.hour
                val minuteDiff = startTime.minute - time.minute

                val reminderText = when {
                    hourDiff == 1 && minuteDiff == 0 -> "${stringResource(R.string.remind_me)} 1 hour before "
                    hourDiff == 2 && minuteDiff == 0 -> "${stringResource(R.string.remind_me)} 2 hour before "
                    minuteDiff == 5 -> "${stringResource(R.string.remind_me)} 5 minutes before"
                    minuteDiff == 10 -> "${stringResource(R.string.remind_me)} 10 minutes before"
                    minuteDiff == 15 -> "${stringResource(R.string.remind_me)} 15 minutes before"
                    minuteDiff == 30 -> "${stringResource(R.string.remind_me)} 30 minutes before"
                    else -> "${stringResource(R.string.remind_me)} ${time.hour.withZeroPadding()}:${time.minute.withZeroPadding()}"
                }

                Column {
                    Text(text = reminderText)
                }
            } else {
                Text(
                    color = TextFieldDefaults.colors().disabledTextColor,
                    text = stringResource(id = R.string.add_reminder),
                )
            }
        }
    )
}