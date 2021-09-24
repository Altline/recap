package altline.recap.data

import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecordRepository @Inject constructor(
    private val recordDao: RecordDao
) {

    fun getRecordByID(recordID: Long) = recordDao.getByID(recordID)
    fun getRecordsByDay(dayID: Long) = recordDao.getAllByDay(dayID)

    private suspend fun getRecordsByDayOnce(dayID: Long) = recordDao.getAllByDay(dayID).first()

    suspend fun addRecord(record: Record): Long {
        val dayRecords = getRecordsByDayOnce(record.dayID)
        record.order = dayRecords.size
        return recordDao.insert(record)
    }

    suspend fun addRecordRespectOrder(record: Record) {
        val order = record.order
        val dayRecords = getRecordsByDayOnce(record.dayID)

        for (i in order until dayRecords.size) {
            dayRecords[i].order++
        }

        recordDao.runInTransaction {
            recordDao.update(*dayRecords.subList(order, dayRecords.size).toTypedArray())
            recordDao.insert(record)
        }
    }

    suspend fun updateRecord(record: Record) = recordDao.update(record)

    suspend fun deleteRecord(record: Record) {
        recordDao.runInTransaction {
            recordDao.delete(record)
            val dayRecords = getRecordsByDayOnce(record.dayID)
            for (i in record.order until dayRecords.size) {
                dayRecords[i].order--
            }
            recordDao.update(*dayRecords.subList(record.order, dayRecords.size).toTypedArray())
        }
    }

    suspend fun reorderRecords(dayID: Long, fromID: Long, toID: Long) {
        val dayRecords = getRecordsByDayOnce(dayID)
        val from = dayRecords.find { it.id == fromID }!!
        val fromOrder = from.order
        val to = dayRecords.find { it.id == toID }!!
        val toOrder = to.order
        val recordsToUpdate: List<Record>?

        from.order = to.order

        if (fromOrder < toOrder) {
            recordsToUpdate = dayRecords.subList(fromOrder, toOrder + 1)
            for (i in fromOrder + 1..toOrder) {
                dayRecords[i].order--
            }
        } else {
            recordsToUpdate = dayRecords.subList(toOrder, fromOrder + 1)
            for (i in fromOrder - 1 downTo toOrder) {
                dayRecords[i].order++
            }
        }

        recordDao.update(*recordsToUpdate.toTypedArray())
    }
}