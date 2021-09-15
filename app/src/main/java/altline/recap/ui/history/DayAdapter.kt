package altline.recap.ui.history

import altline.recap.R
import altline.recap.data.DayContent
import altline.recap.databinding.ItemDayBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.size
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class DayAdapter : ListAdapter<DayContent, DayAdapter.ViewHolder>(DayContent.DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemDayBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemDayBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DayContent) {
            binding.dayContent = item

            itemView as LinearLayout
            itemView.removeViews(1, itemView.size - 1)

            item.records.forEach { record ->
                itemView.addView(TextView(itemView.context).apply {
                    text = resources.getString(R.string.template_record_text, record.text)
                })
            }

        }
    }

}