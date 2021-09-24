package altline.recap.data

import androidx.recyclerview.widget.DiffUtil
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(
    foreignKeys = [ForeignKey(
        entity = Day::class,
        parentColumns = ["id"],
        childColumns = ["dayID"]
    )],
    indices = [Index("dayID")]
)
data class Record(
    var text: String,
    val dayID: Long,
    var order: Int = -1,

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
) {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Record>() {
            override fun areItemsTheSame(a: Record, b: Record) = a.id == b.id
            override fun areContentsTheSame(a: Record, b: Record) = a == b
        }
    }
}

@Dao
interface RecordDao {
    @Insert
    suspend fun insert(record: Record): Long

    @Update
    suspend fun update(vararg records: Record)

    @Delete
    suspend fun delete(record: Record)

    @Query("SELECT * FROM record WHERE id = :recordID")
    fun getByID(recordID: Long): Flow<Record>

    @Query("SELECT * FROM record WHERE dayID = :dayID ORDER BY `order`")
    fun getAllByDay(dayID: Long): Flow<List<Record>>

    @Transaction
    suspend fun runInTransaction(block: suspend () -> Unit) = block()
}
