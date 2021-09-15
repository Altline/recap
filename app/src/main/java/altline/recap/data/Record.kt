package altline.recap.data

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
    val text: String,
    val dayID: Int,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)

@Dao
interface RecordDao {
    @Insert
    fun insert(vararg records: Record)

    @Delete
    fun delete(vararg records: Record)
}
