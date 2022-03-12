package nz.co.chrisdrake.transit.ui.departures

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.systemBarsPadding
import nz.co.chrisdrake.transit.R
import nz.co.chrisdrake.transit.ui.common.MainTheme
import nz.co.chrisdrake.transit.ui.common.textPrimary

@Composable
fun DeparturesView(
    title: String,
    progressVisible: Boolean,
    nextJourneyEnabled: Boolean,
    departures: List<DepartureListItem>,
    onClickNextJourney: () -> Unit,
) {
    MainTheme {
        ProvideWindowInsets {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colors.surface)
                    .animateContentSize(tween(durationMillis = 500))
                    .systemBarsPadding(start = false, top = false, end = false, bottom = true)
            ) {
                DeparturesHeading(
                    title = title,
                    progressVisible = progressVisible,
                    nextJourneyEnabled = nextJourneyEnabled,
                    onClickNextJourney = onClickNextJourney,
                )

                Spacer(modifier = Modifier.height(8.dp))

                departures.forEach {
                    key(it.id) {
                        Departure(it)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun DeparturesHeading(
    title: String,
    progressVisible: Boolean,
    nextJourneyEnabled: Boolean,
    onClickNextJourney: () -> Unit,
) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colors.primary)
            .requiredHeight(48.dp)
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.onPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
        )

        if (progressVisible) {
            Spacer(modifier = Modifier.width(8.dp))

            CircularProgressIndicator(
                modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.CenterVertically),
                strokeWidth = 2.dp,
                color = MaterialTheme.colors.secondaryVariant,
            )

            Spacer(modifier = Modifier.width(8.dp))
        } else {
            Spacer(modifier = Modifier.width(36.dp))
        }

        Button(
            modifier = Modifier.align(Alignment.CenterVertically),
            colors = buttonColors(
                disabledBackgroundColor = Color.Transparent,
                disabledContentColor = Color.White.copy(alpha = ContentAlpha.disabled),
            ),
            onClick = onClickNextJourney,
            enabled = nextJourneyEnabled
        ) {
            Text(stringResource(id = R.string.home_journey_next).uppercase())
        }
    }
}

@Composable
fun Departure(departure: DepartureListItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                enabled = departure.onClick != null,
                onClick = { departure.onClick?.invoke() })
            .padding(horizontal = 16.dp)
    ) {
        Image(
            modifier = Modifier
                .width(32.dp)
                .align(Alignment.CenterVertically),
            painter = painterResource(id = departure.icon),
            contentDescription = null,
            alignment = Alignment.CenterStart,
            contentScale = ContentScale.FillWidth,
            colorFilter = ColorFilter.tint(MaterialTheme.colors.primary.copy(alpha = 0.87f))
        )

        Spacer(modifier = Modifier.width(24.dp))

        Column {
            Row {
                Text(
                    text = departure.expectedDepartureTime,
                    modifier = Modifier.paddingFromBaseline(top = 32.dp),
                    style = MaterialTheme.typography.subtitle1,
                    color = MaterialTheme.colors.textPrimary,
                )

                if (departure.occupancyIcon != null) {
                    Spacer(modifier = Modifier.width(8.dp))

                    Image(
                        modifier = Modifier
                            .width(32.dp)
                            .align(Alignment.Bottom),
                        painter = painterResource(id = departure.occupancyIcon),
                        contentDescription = null,
                        alignment = Alignment.BottomCenter,
                        contentScale = ContentScale.FillWidth,
                    )
                }
            }

            Text(
                text = stringResource(id = departure.status),
                modifier = Modifier.paddingFromBaseline(top = 20.dp, bottom = 20.dp),
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.textPrimary,
            )
        }
    }
}

@Preview
@Composable
private fun DeparturesViewPreview() {
    val defaultDeparture = DepartureListItem(
        id = "123",
        expectedDepartureTime = "In 0 minutes (NX1)",
        status = R.string.home_departure_status_scheduled,
        position = null,
        icon = R.drawable.ic_departure,
        occupancyIcon = null,
        onClick = {}
    )

    DeparturesView(
        title = "NX1",
        progressVisible = true,
        nextJourneyEnabled = true,
        departures = listOf(
            defaultDeparture,
            defaultDeparture.copy(icon =  R.drawable.ic_departure_in_transit),
            defaultDeparture.copy(icon =  R.drawable.ic_departure_alert),
            defaultDeparture.copy(occupancyIcon = R.drawable.ic_occupancy_few),
            defaultDeparture.copy(occupancyIcon = R.drawable.ic_occupancy_full),
        ),
        onClickNextJourney = {}
    )
}