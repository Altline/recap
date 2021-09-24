package altline.recap.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Entity
data class Day(
    val date: LocalDate,

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)

@Dao
interface DayDao {
    @Insert
    suspend fun insert(day: Day): Long

    @Delete
    suspend fun delete(day: Day)

    @Query("SELECT * FROM day WHERE date = :date")
    fun getByDate(date: LocalDate): Flow<Day?>

    @Transaction
    @Query("SELECT * FROM day ORDER BY date DESC")
    fun getAllWithContent(): Flow<List<DayContent>>

    @Transaction
    @Query("SELECT * FROM day WHERE id = :dayID")
    fun getByIdWithContent(dayID: Long): Flow<DayContent>
}
