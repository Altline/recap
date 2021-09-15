package altline.recap.data

import javax.inject.Inject

class Repository @Inject constructor(
    private val dayDao: DayDao,
    private val recordDao: RecordDao
) {
    fun getAllDaysWithContent() = dayDao.getAllWithContent()

    /*init {
        thread {
            dayDao.insert(Day(LocalDate.now()))
            dayDao.insert(Day(LocalDate.now().minusDays(2)))
            recordDao.insert(Record("heh", 1))
            recordDao.insert(Record("heh2", 1))
            recordDao.insert(Record("hoch", 2))
        }
    }*/
}