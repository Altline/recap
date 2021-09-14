package altline.recap.ui.records

import altline.recap.data.Record
import altline.recap.databinding.FragmentRecordsBinding
import altline.recap.databinding.ItemRecordBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class RecordAdapter : ListAdapter<Record, RecordAdapter.ViewHolder>(Record.DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRecordBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemRecordBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Record) {
            binding.record = item
        }
    }

}