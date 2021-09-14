package altline.recap.data

import androidx.recyclerview.widget.DiffUtil
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Entity
data class Record(
    var date: LocalDate,
    var text: String,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
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
    fun insert(vararg records: Record)

    @Delete
    fun delete(vararg records: Record)

    @Query("SELECT * FROM record")
    fun getAll(): Flow<List<Record>>
}
