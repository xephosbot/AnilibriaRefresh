package com.xbot.preference.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.xbot.designsystem.icons.AnilibriaIcons
import com.xbot.designsystem.icons.AnilibriaLogo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProfileDetailScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
) {
    var state by remember { mutableStateOf<ProfileScreenState>(ProfileScreenState.LoggedOut) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { /*TODO*/ },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = AnilibriaIcons.Outlined.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            when (state) {
                is ProfileScreenState.LoggedIn -> {

                }
                ProfileScreenState.LoggedOut -> {
                    ProfileLogin(
                        onLogin = { login, password ->
                            state = ProfileScreenState.LoggedIn(null)
                        }
                    )
                }
            }

        }
    }
}

@Composable
private fun ProfileLogin(
    modifier: Modifier = Modifier,
    onLogin: (String, String) -> Unit,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceBright)
            .padding(16.dp)
            .widthIn(max = 450.dp)
    ) {
        Icon(
            modifier = Modifier.size(30.dp),
            imageVector = AnilibriaIcons.Filled.AnilibriaLogo,
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Sign in",
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = "",
            onValueChange = {},
            label = {
                Text(text = "Login")
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = "",
            onValueChange = {},
            label = {
                Text(text = "Password")
            }
        )
        TextButton(onClick = { /*TODO*/ }) {
            Text(text = "Forgot password?")
        }
        Row {
            Spacer(Modifier.weight(1f))
            TextButton(onClick = { /*TODO*/ }) {
                Text(text = "Create account")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { onLogin("", "") }) {
                Text(text = "Next")
            }
        }
    }
}

@Composable
private fun ProfileContent(modifier: Modifier = Modifier) {

}

sealed interface ProfileScreenState {
    data object LoggedOut : ProfileScreenState
    data class LoggedIn(val user: String?) : ProfileScreenState
}