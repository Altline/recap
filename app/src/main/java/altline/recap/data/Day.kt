package altline.recap.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Entity
data class Day(
    val date: LocalDate,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)

@Dao
interface DayDao {
    @Insert
    fun insert(vararg days: Day)

    @Delete
    fun delete(vararg days: Day)

    @Transaction
    @Query("SELECT * FROM day")
    fun getAllWithContent(): Flow<List<DayContent>>
}
