package altline.recap.ui.history

import altline.androidutil.showSoftInput
import altline.recap.data.Record
import altline.recap.databinding.ItemRecordBinding
import android.graphics.Point
import android.graphics.Rect
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.core.graphics.contains
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RecordAdapter(
    private val onCommitEdit: (Long, String) -> Unit
) : ListAdapter<Record, RecordAdapter.ViewHolder>(Record.DIFF_CALLBACK) {

    var dragHandlePressCallback: ((RecordAdapter.ViewHolder) -> Unit)? = null

    private var recordToFocus: Long? = null

    init {
        setHasStableIds(true)
    }

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

    override fun getItemId(position: Int): Long {
        return getItem(position).id
    }

    fun queueRecordFocus(recordID: Long) {
        recordToFocus = recordID
    }

    fun moveItem(fromPos: Int, toPos: Int) {
        currentList.toMutableList().run {
            val record = removeAt(fromPos)
            add(toPos, record)
            submitList(this)
        }
    }


    inner class ViewHolder(
        private val binding: ItemRecordBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        val item: Record? get() = binding.record

        var isEditMode = false
            private set

        fun bind(item: Record) {
            binding.record = item
            binding.recordTvRecordText.setOnClickListener { toggleEditMode() }
            binding.recordEtRecordText.apply {
                setOnKeyListener { v, keyCode, event ->
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                        v.clearFocus()
                        return@setOnKeyListener true
                    }
                    return@setOnKeyListener false
                }
                setOnFocusChangeListener { _, hasFocus ->
                    if (!hasFocus) toggleEditMode()
                }
            }

            // Show ripple effect properly but still enable pressing the underlying items
            binding.recordOverlay.setOnTouchListener { _, event ->
                val tv = binding.recordTvRecordText
                val et = binding.recordEtRecordText
                val dragHandle = binding.recordDragHandle
                val touchPoint = Point(event.x.toInt(), event.y.toInt())

                val tvBounds = Rect().also {
                    tv.getHitRect(it)
                }
                val etBounds = Rect().also {
                    et.getHitRect(it)
                }
                val dragHandleBounds = Rect().also {
                    dragHandle.getHitRect(it)
                }

                when {
                    tv.isVisible
                            && event.actionMasked == MotionEvent.ACTION_UP
                            && tvBounds.contains(touchPoint) -> {
                        binding.recordTvRecordText.performClick()
                    }

                    tv.isVisible
                            && event.actionMasked == MotionEvent.ACTION_DOWN
                            && dragHandleBounds.contains(touchPoint) -> {
                        dragHandlePressCallback?.invoke(this)
                    }

                    et.isVisible
                            && etBounds.contains(touchPoint) -> {
                        binding.recordEtRecordText.dispatchTouchEvent(event)
                        return@setOnTouchListener true
                    }
                }
                false
            }

            // Focus on newly added record
            if (item.id == recordToFocus) {
                toggleEditMode()

                // Quick hack to show keyboard for newly added records
                GlobalScope.launch {
                    delay(100)
                    showSoftInput(binding.recordEtRecordText, InputMethodManager.SHOW_IMPLICIT)
                }

                recordToFocus = null
            }
        }

        private fun toggleEditMode() {
            binding.recordTextSwitcher.showNext()
            if (isEditMode) {
                isEditMode = false
                commitRecordEdit()
            } else {
                isEditMode = true
                startRecordEdit()
            }
        }

        private fun startRecordEdit() {
            binding.recordEtRecordText.apply {
                if (requestFocus()) {
                    setSelection(length())
                    showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
                }
            }
        }

        fun commitRecordEdit() {
            val recordID = binding.record!!.id
            val recordText = binding.recordEtRecordText.text.toString()
            onCommitEdit(recordID, recordText)
        }

    }
}