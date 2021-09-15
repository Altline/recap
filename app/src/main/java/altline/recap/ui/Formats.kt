package altline.recap.ui

import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

// TODO make available in settings
var dateTimeLocale: Locale = Locale.forLanguageTag("hr")

val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
    .withLocale(dateTimeLocale)