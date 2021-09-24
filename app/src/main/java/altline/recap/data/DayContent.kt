package altline.recap.data

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Embedded
import androidx.room.Relation

data class DayContent(
    @Embedded val day: Day,
    @Relation(
        parentColumn = "id",
        entityColumn = "dayID"
    ) val records: List<Record>
) {
    val orderedRecords get() = records.sortedBy { it.order }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DayContent>() {
            override fun areItemsTheSame(a: DayContent, b: DayContent) = a.day.id == b.day.id
            override fun areContentsTheSame(a: DayContent, b: DayContent) = a == b
        }
    }
}
