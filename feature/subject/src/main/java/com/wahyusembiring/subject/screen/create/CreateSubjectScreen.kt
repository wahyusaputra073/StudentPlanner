package com.wahyusembiring.subject.screen.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.wahyusembiring.common.navigation.Screen
import com.wahyusembiring.subject.R
import com.wahyusembiring.ui.component.button.ChooseColorButton
import com.wahyusembiring.ui.component.dropdown.Dropdown
import com.wahyusembiring.ui.component.popup.alertdialog.confirmation.ConfirmationAlertDialog
import com.wahyusembiring.ui.component.popup.alertdialog.error.ErrorAlertDialog
import com.wahyusembiring.ui.component.popup.alertdialog.information.InformationAlertDialog
import com.wahyusembiring.ui.component.popup.alertdialog.loading.LoadingAlertDialog
import com.wahyusembiring.ui.component.popup.picker.colorpicker.ColorPicker
import com.wahyusembiring.ui.theme.spacing
import com.wahyusembiring.ui.util.UIText

@Composable
fun CreateSubjectScreen(
    viewModel: CreateSubjectViewModel,
    navController: NavHostController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CreateSubjectScreen(
        modifier = Modifier.fillMaxSize(),
        state = state,
        onUIEvent = viewModel::onUIEvent,
        onNavigateUp = {
            navController.navigateUp()
        },
        onNavigateToCreateLecturer = {
            navController.navigate(Screen.AddLecturer())
        }
    )
}

@Composable
private fun CreateSubjectScreen(
    modifier: Modifier = Modifier,
    state: CreateSubjectScreenUIState,
    onUIEvent: (CreateSubjectScreenUIEvent) -> Unit,
    onNavigateUp: () -> Unit,
    onNavigateToCreateLecturer: () -> Unit,
) {
    Scaffold { paddingValues ->
        Column(
            modifier = modifier.padding(paddingValues)
        ) {
            BackAndSaveHeader(
                onBackButtonClicked = onNavigateUp,
                onSaveButtonClicked = {
                    onUIEvent(CreateSubjectScreenUIEvent.OnSaveButtonClicked)
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(MaterialTheme.spacing.Medium)
            ) {
                // Subject Name Field
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = stringResource(R.string.subject_name)) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = com.wahyusembiring.ui.R.drawable.ic_title),
                            contentDescription = stringResource(R.string.subject_name),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    singleLine = true,
                    value = state.name,
                    onValueChange = { onUIEvent(CreateSubjectScreenUIEvent.OnSubjectNameChanged(it)) },
                )

                // Room Field
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_location),
                            contentDescription = stringResource(R.string.room),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    placeholder = { Text(text = stringResource(R.string.room)) },
                    singleLine = true,
                    value = state.room,
                    onValueChange = { onUIEvent(CreateSubjectScreenUIEvent.OnRoomChanged(it)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    )
                )

                // Color Picker Button
                ChooseColorButton(
                    color = state.color,
                    onClick = { onUIEvent(CreateSubjectScreenUIEvent.OnPickColorButtonClicked) }
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.Medium))

                // Primary Lecturer Dropdown
                Box(modifier = Modifier.fillMaxWidth()) {
                    Dropdown(
                        items = state.lecturers,
                        title = {
                            if (it?.name != null) {
                                UIText.DynamicString(it.name)
                            } else {
                                UIText.StringResource(R.string.select_primary_lecturer)
                            }
                        },
                        selected = state.primaryLecturer,
                        onItemClick = { onUIEvent(CreateSubjectScreenUIEvent.OnPrimaryLecturerSelected(it)) },
                        emptyContent = {
                            LecturerEmptyContent(onNavigateToCreateLecturer)
                        }
                    )

                    if (state.primaryLecturer != null) {
                        IconButton(
                            modifier = Modifier.align(Alignment.CenterEnd),
                            onClick = { onUIEvent(CreateSubjectScreenUIEvent.OnLecturerRemoved(false)) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = stringResource(R.string.remove_lecturer),
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.Medium))


                // Secondary Lecturer Dropdown
                Box(modifier = Modifier.fillMaxWidth()) {
                    Dropdown(
                        items = state.lecturers,
                        title = {
                            if (it?.name != null) {
                                UIText.DynamicString(it.name)
                            } else {
                                UIText.StringResource(R.string.select_secondary_lecturer)
                            }
                        },
                        selected = state.secondaryLecturer,
                        onItemClick = { onUIEvent(CreateSubjectScreenUIEvent.OnSecondaryLecturerSelected(it)) },
                        emptyContent = {
                            LecturerEmptyContent(onNavigateToCreateLecturer)
                        }
                    )

                    if (state.secondaryLecturer != null) {
                        IconButton(
                            modifier = Modifier.align(Alignment.CenterEnd),
                            onClick = { onUIEvent(CreateSubjectScreenUIEvent.OnLecturerRemoved(true)) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = stringResource(R.string.remove_lecturer),
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }

    // Color Picker Dialog
    if (state.showColorPicker) {
        ColorPicker(
            initialColor = state.color,
            onDismissRequest = { onUIEvent(CreateSubjectScreenUIEvent.OnColorPickerDismiss) },
            onColorConfirmed = { onUIEvent(CreateSubjectScreenUIEvent.OnColorPicked(it)) }
        )
    }

    // Loading Dialog
    if (state.showSavingLoading) {
        LoadingAlertDialog(message = stringResource(R.string.saving))
    }

    // Save Confirmation Dialog
    if (state.showSaveConfirmationDialog) {
        ConfirmationAlertDialog(
            title = stringResource(R.string.save_subject),
            message = stringResource(R.string.are_you_sure_you_want_to_save_this_subject),
            positiveButtonText = stringResource(R.string.save),
            onPositiveButtonClick = {
                onUIEvent(CreateSubjectScreenUIEvent.OnSaveConfirmationDialogConfirm)
            },
            negativeButtonText = stringResource(R.string.cancel),
            onNegativeButtonClick = {
                onUIEvent(CreateSubjectScreenUIEvent.OnSaveConfirmationDialogDismiss)
            },
            onDismissRequest = {
                onUIEvent(CreateSubjectScreenUIEvent.OnSaveConfirmationDialogDismiss)
            },
        )
    }

    // Success Dialog
    if (state.showSubjectSavedDialog) {
        InformationAlertDialog(
            title = stringResource(R.string.success),
            message = stringResource(R.string.subject_saved),
            buttonText = stringResource(R.string.ok),
            onButtonClicked = {
                onUIEvent(CreateSubjectScreenUIEvent.OnSubjectSavedDialogDismiss)
                onNavigateUp()
            },
            onDismissRequest = {
                onUIEvent(CreateSubjectScreenUIEvent.OnSubjectSavedDialogDismiss)
                onNavigateUp()
            },
        )
    }

    if (state.errorMessage != null) {
        ErrorAlertDialog(
            message = state.errorMessage.asString(),
            buttonText = stringResource(R.string.ok),
            onButtonClicked = {
                onUIEvent(CreateSubjectScreenUIEvent.OnErrorDialogDismiss)
            },
            onDismissRequest = {
                onUIEvent(CreateSubjectScreenUIEvent.OnErrorDialogDismiss)
            }
        )
    }
}

@Composable
private fun BackAndSaveHeader(
    onBackButtonClicked: () -> Unit,
    onSaveButtonClicked: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackButtonClicked
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_back_arrow),
                contentDescription = stringResource(R.string.back)
            )
        }
        Button(
            modifier = Modifier.padding(end = MaterialTheme.spacing.Medium),
            onClick = onSaveButtonClicked
        ) {
            Text(text = stringResource(id = R.string.save))
        }
    }
}

@Composable
private fun LecturerEmptyContent(onNavigateToCreateLecturer: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.Medium),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(R.string.there_are_no_lecturer_avaliable))
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.Small))
        Button(onClick = onNavigateToCreateLecturer) {
            Text(text = stringResource(R.string.add_new_lecturer))
        }
    }
}