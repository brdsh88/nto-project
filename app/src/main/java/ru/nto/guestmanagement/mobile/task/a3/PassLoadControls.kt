package ru.nto.guestmanagement.mobile.task.a3

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.nto.guestmanagement.mobile.R
import ru.nto.guestmanagement.mobile.model.PassScreenState
import ru.nto.guestmanagement.mobile.ui.TestIds

/** A3. Индикатор загрузки, ошибка с кнопкой «Повторить», кнопка «Обновить», надпись «Нет связи». */
@Composable
fun PassLoadControls(
    state: PassScreenState,
    onRefresh: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        when (state) {
            PassScreenState.InitialLoading -> LoadingIndicator()

            is PassScreenState.InitialError -> {
                Text(
                    text = state.message,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(TestIds.PASS_ERROR),
                )
                Button(
                    onClick = onRetry,
                    modifier = Modifier.testTag(TestIds.PASS_RETRY),
                ) {
                    Text(stringResource(R.string.action_retry))
                }
            }

            is PassScreenState.Content -> {
                if (state.isRefreshing) {
                    LoadingIndicator()
                }
                Button(
                    onClick = onRefresh,
                    enabled = !state.isRefreshing,
                    modifier = Modifier.testTag(TestIds.PASS_REFRESH),
                ) {
                    Text(stringResource(R.string.action_refresh))
                }
                if (!state.isRefreshing && state.isOffline) {
                    Text(
                        text = stringResource(R.string.offline),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag(TestIds.PASS_OFFLINE),
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingIndicator() {
    CircularProgressIndicator(modifier = Modifier.testTag(TestIds.PASS_LOADING))
}
