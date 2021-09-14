package altline.recap.data

import javax.inject.Inject

class RecordRepository @Inject constructor(
    private val recordDao: RecordDao
) {
    fun getAllRecords() = recordDao.getAll()
}