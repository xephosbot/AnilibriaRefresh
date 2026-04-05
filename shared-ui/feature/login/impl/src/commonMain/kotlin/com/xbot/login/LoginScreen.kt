package com.xbot.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.designsystem.icons.AnilibriaLogo
import com.xbot.designsystem.icons.Favorite
import com.xbot.designsystem.utils.AnilibriaPreview
import com.xbot.designsystem.utils.SnackbarManager
import com.xbot.designsystem.utils.union
import com.xbot.localization.UiText
import com.xbot.resources.Res
import com.xbot.resources.login_create_account
import com.xbot.resources.login_description
import com.xbot.resources.login_email_label
import com.xbot.resources.login_forgot_password
import com.xbot.resources.login_password_label
import com.xbot.resources.login_sign_in
import com.xbot.resources.login_success_message
import com.xbot.resources.login_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    onRegistrationClick: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val username by viewModel.username.collectAsStateWithLifecycle()
    val password by viewModel.password.collectAsStateWithLifecycle()
    val snackbarManager = koinInject<SnackbarManager>()

    LaunchedEffect(viewModel.effects) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is LoginScreenEffect.NavigateBack -> onBackClick()
                is LoginScreenEffect.LoginSuccess -> snackbarManager.showMessage(
                    title = UiText.Text(Res.string.login_success_message)
                )
                is LoginScreenEffect.ShowError -> snackbarManager.showMessage(
                    title = UiText.String(effect.message)
                )
            }
        }
    }

    LoginScreenContent(
        modifier = modifier,
        state = state,
        username = username,
        password = password,
        onAction = viewModel::onAction,
        onRegistrationClick = onRegistrationClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun LoginScreenContent(
    modifier: Modifier = Modifier,
    state: LoginScreenState,
    username: String,
    password: String,
    onAction: (LoginScreenAction) -> Unit,
    onRegistrationClick: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    val contentPadding = PaddingValues()
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding.union(PaddingValues(16.dp))),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .widthIn(max = 480.dp)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceBright
            ),
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                Icon(
                    imageVector = AnilibriaIcons.AnilibriaLogo,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(Res.string.login_title),
                    style = MaterialTheme.typography.displaySmall
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(Res.string.login_description),
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = username,
                    onValueChange = { onAction(LoginScreenAction.UsernameChanged(it)) },
                    label = { Text(stringResource(Res.string.login_email_label)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .semantics {
                            contentType = ContentType.EmailAddress
                        },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    singleLine = true,
                )

                Spacer(modifier = Modifier.height(16.dp))

                var isPasswordVisible by remember { mutableStateOf(false) }

                OutlinedTextField(
                    value = password,
                    onValueChange = { onAction(LoginScreenAction.PasswordChanged(it)) },
                    label = { Text(stringResource(Res.string.login_password_label)) },
                    trailingIcon = {
                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(
                                imageVector = if (isPasswordVisible) AnilibriaIcons.Outlined.Favorite else AnilibriaIcons.Filled.Favorite,
                                contentDescription = null
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        autoCorrectEnabled = false,
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    singleLine = true,
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = {
                        //TODO: Handle forgot password
                        onAction(LoginScreenAction.Logout)
                    },
                ) {
                    Text(stringResource(Res.string.login_forgot_password))
                }

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilledTonalButton(onClick = { onRegistrationClick() }) {
                        Text(stringResource(Res.string.login_create_account))
                    }

                    Button(
                        onClick = { onAction(LoginScreenAction.Login) },
                        enabled = !state.isLoading
                    ) {
                        Text(stringResource(Res.string.login_sign_in))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview(
    @PreviewParameter(LoginScreenStateProvider::class) state: LoginScreenState
) {
    AnilibriaPreview {
        LoginScreenContent(
            state = state,
            username = "",
            password = "",
            onAction = {},
            onRegistrationClick = {}
        )
    }
}

private class LoginScreenStateProvider : PreviewParameterProvider<LoginScreenState> {
    override val values = sequenceOf(
        LoginScreenState(isLoading = false),
        LoginScreenState(isLoading = true)
    )
}
