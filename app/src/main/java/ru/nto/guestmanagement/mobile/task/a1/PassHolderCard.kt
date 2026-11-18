package ru.nto.guestmanagement.mobile.task.a1

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.nto.guestmanagement.mobile.R
import ru.nto.guestmanagement.mobile.model.PassDetails
import ru.nto.guestmanagement.mobile.ui.TestIds
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val DATE_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
private val TIME_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
private val DATE_TIME_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

/**
 * A1. Карточка владельца: ФИО, организация, срок действия, лимит проходов.
 * Подробности и теги — в README.md и в техническом задании.
 */
@Composable
fun PassHolderCard(pass: PassDetails, modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = pass.visitor.fullName,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(TestIds.PASS_HOLDER),
            )

            val organization = pass.visitor.organization
            if (organization != null && organization.isNotBlank()) {
                Text(
                    text = organization,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(TestIds.PASS_ORGANIZATION),
                )
            }

            Text(
                text = validPeriodText(pass.validFrom, pass.validTo),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(TestIds.PASS_VALID_PERIOD),
            )

            Text(
                text = entriesLimitText(pass.maxEntries),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(TestIds.PASS_ENTRIES_LIMIT),
            )
        }
    }
}

@Composable
private fun validPeriodText(validFrom: LocalDateTime, validTo: LocalDateTime): String {
    val fromDate = validFrom.toLocalDate()
    val toDate: LocalDate = validTo.toLocalDate()
    return if (fromDate == toDate) {
        stringResource(
            R.string.pass_valid_same_day,
            DATE_FORMAT.format(fromDate),
            TIME_FORMAT.format(validFrom),
            TIME_FORMAT.format(validTo),
        )
    } else {
        stringResource(
            R.string.pass_valid_range,
            DATE_TIME_FORMAT.format(validFrom),
            DATE_TIME_FORMAT.format(validTo),
        )
    }
}

@Composable
private fun entriesLimitText(maxEntries: Int): String = when {
    maxEntries == 1 -> stringResource(R.string.pass_single_entry)
    maxEntries % 100 in 11..14 -> stringResource(R.string.pass_entries_limit_many, maxEntries)
    maxEntries % 10 == 1 -> stringResource(R.string.pass_entries_limit_one, maxEntries)
    maxEntries % 10 in 2..4 -> stringResource(R.string.pass_entries_limit_few, maxEntries)
    else -> stringResource(R.string.pass_entries_limit_many, maxEntries)
}
