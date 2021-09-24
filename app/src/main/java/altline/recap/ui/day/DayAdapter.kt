package altline.recap.ui.day

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

class DayAdapter(
    private val onItemClick: (Long) -> Unit
) : ListAdapter<DayContent, DayAdapter.ViewHolder>(DayContent.DIFF_CALLBACK) {

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
        holder.bind(getItem(position), onItemClick)
    }

    inner class ViewHolder(
        private val binding: ItemDayBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        val item: DayContent? get() = binding.dayContent

        fun bind(item: DayContent, onItemClick: (Long) -> Unit) {
            binding.dayContent = item
            binding.onItemClick = onItemClick

            itemView as LinearLayout
            itemView.removeViews(1, itemView.size - 1)

            item.orderedRecords.forEach { record ->
                itemView.addView(TextView(itemView.context).apply {
                    text = resources.getString(R.string.template_record_text, record.text)
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        topMargin = resources.getDimension(R.dimen.record_text_margin).toInt()
                    }
                })
            }

        }
    }

}