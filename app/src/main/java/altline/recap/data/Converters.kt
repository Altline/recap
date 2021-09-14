package altline.recap.data

import androidx.room.TypeConverter
import java.time.LocalDate

class Converters {
    @TypeConverter
    fun dateToEpochDay(date: LocalDate): Long {
        return date.toEpochDay()
    }

    @TypeConverter
    fun epochDayToDate(epochDay: Long): LocalDate {
        return LocalDate.ofEpochDay(epochDay)
    }
}