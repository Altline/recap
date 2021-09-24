package altline.recap.data

import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DayRepository @Inject constructor(
    private val dayDao: DayDao
) {
    suspend fun addDay(day: Day) = dayDao.insert(day)
    suspend fun deleteDay(day: Day) = dayDao.delete(day)
    fun getDayByDate(date: LocalDate) = dayDao.getByDate(date)
    fun getAllDaysWithContent() = dayDao.getAllWithContent()
    fun getDayContentByID(dayID: Long) = dayDao.getByIdWithContent(dayID)

}