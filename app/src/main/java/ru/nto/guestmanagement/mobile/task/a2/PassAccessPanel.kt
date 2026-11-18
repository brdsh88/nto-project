package ru.nto.guestmanagement.mobile.task.a2

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.nto.guestmanagement.mobile.R
import ru.nto.guestmanagement.mobile.data.TimeProvider
import ru.nto.guestmanagement.mobile.model.PassData
import ru.nto.guestmanagement.mobile.model.PassStatus
import ru.nto.guestmanagement.mobile.ui.PassQr
import ru.nto.guestmanagement.mobile.ui.TestIds
import ru.nto.guestmanagement.mobile.ui.statusColor

/** A2. Метка статуса и QR-код (или заглушка «QR-код недоступен»). */
@Composable
fun PassAccessPanel(data: PassData, timeProvider: TimeProvider, modifier: Modifier = Modifier) {
    val now = timeProvider.now()
    val status = calculatePassStatus(data.pass, data.scans, now)
    val color = statusColorFor(status)

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = stringResource(statusText(status)),
            color = color,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(TestIds.PASS_STATUS)
                .statusColor(color),
        )

        if (status == PassStatus.ACTIVE) {
            PassQr(content = data.pass.qrCode)
            Text(
                text = stringResource(R.string.qr_hint),
                style = MaterialTheme.typography.bodySmall,
            )
        } else {
            Text(
                text = stringResource(R.string.qr_unavailable),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(TestIds.PASS_QR_HIDDEN),
            )
        }
    }
}

private fun statusText(status: PassStatus): Int = when (status) {
    PassStatus.ACTIVE -> R.string.pass_status_active
    PassStatus.NOT_STARTED -> R.string.pass_status_not_started
    PassStatus.EXPIRED -> R.string.pass_status_expired
    PassStatus.EXHAUSTED -> R.string.pass_status_exhausted
    PassStatus.BLOCKED -> R.string.pass_status_blocked
}

@Composable
private fun statusColorFor(status: PassStatus): Color = colorResource(
    when (status) {
        PassStatus.ACTIVE -> R.color.status_active
        PassStatus.NOT_STARTED -> R.color.status_not_started
        PassStatus.EXPIRED -> R.color.status_expired
        PassStatus.EXHAUSTED -> R.color.status_exhausted
        PassStatus.BLOCKED -> R.color.status_blocked
    },
)
