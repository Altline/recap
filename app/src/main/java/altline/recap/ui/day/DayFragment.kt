package altline.recap.ui.day

import altline.androidutil.observeOnce
import altline.recap.MainActivity
import altline.recap.R
import altline.recap.data.Record
import altline.recap.databinding.FragmentDayBinding
import altline.recap.ui.history.RecordAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DayFragment : Fragment() {

    private val viewModel: DayViewModel by viewModels()
    private val args: DayFragmentArgs by navArgs()

    private var binding: FragmentDayBinding? = null
    private val recordAdapter get() = binding!!.rvRecords.adapter as RecordAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.loadDayContent(args.dayID)

        binding = FragmentDayBinding.inflate(inflater, container, false)

        val b = binding!!
        b.lifecycleOwner = viewLifecycleOwner
        b.viewModel = viewModel

        setupRecordList()

        b.btnAddRecord.setOnClickListener {
            createNewRecord()
        }

        return b.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupRecordList() {
        val adapter = RecordAdapter { editedRecordID, newText ->
            viewModel.editRecord(editedRecordID, newText)
        }
        val rvRecords = binding!!.rvRecords
        rvRecords.adapter = adapter

        viewModel.dayContent.observe(viewLifecycleOwner) { dayContent ->
            adapter.submitList(dayContent.orderedRecords)
            // Calling this because the RecyclerView was not updating otherwise (suspected bug)
            // https://stackoverflow.com/q/49726385/6640693
            adapter.notifyDataSetChanged()
        }

        val recordTouchHelper = ItemTouchHelper(RecordTouchCallback())
        recordTouchHelper.attachToRecyclerView(rvRecords)
        adapter.dragHandlePressCallback = {
            recordTouchHelper.startDrag(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (args.autoNewRecord) createNewRecord()
    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as MainActivity).fabVisible = false
    }

    override fun onStop() {
        super.onStop()
        val dayContent = viewModel.dayContent.value!!
        if (dayContent.records.isEmpty()) {
            viewModel.deleteCurrentDay()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun createNewRecord() {
        viewModel.createNewRecord().observeOnce(viewLifecycleOwner) {
            recordAdapter.queueRecordFocus(it)
        }
    }

    private fun deleteRecord(record: Record) {
        viewModel.deleteRecord(record).invokeOnCompletion {
            Snackbar.make(
                binding!!.rvRecords,
                R.string.snack_recordDeleted,
                Snackbar.LENGTH_LONG
            ).setAction(R.string.undo) {
                viewModel.undoDeleteRecord()
            }.show()
        }
    }


    private inner class RecordTouchCallback
        : ItemTouchHelper.SimpleCallback(UP or DOWN, LEFT or RIGHT) {

        private var finalDrag: Triple<Long, Long, Long>? = null
        private val posToID = HashMap<Int, Long>()

        override fun getDragDirs(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            viewHolder as RecordAdapter.ViewHolder
            if (viewHolder.isEditMode) return 0
            return super.getDragDirs(recyclerView, viewHolder)
        }

        override fun getSwipeDirs(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            viewHolder as RecordAdapter.ViewHolder
            if (viewHolder.isEditMode) return 0
            return super.getSwipeDirs(recyclerView, viewHolder)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val dayID = (viewHolder as RecordAdapter.ViewHolder).item!!.dayID
            val fromPos = viewHolder.absoluteAdapterPosition
            val fromID = viewHolder.itemId
            val toPos = target.absoluteAdapterPosition

            if (posToID.isEmpty()) {
                recordAdapter.currentList.associateTo(posToID) {
                    Pair(it.order, it.id)
                }
            }

            recordAdapter.moveItem(fromPos, toPos)
            finalDrag = Triple(dayID, fromID, posToID[toPos]!!)

            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            viewHolder as RecordAdapter.ViewHolder
            val record = viewHolder.item!!
            deleteRecord(record)
        }

        override fun clearView(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ) {
            super.clearView(recyclerView, viewHolder)
            finalDrag?.let {
                viewModel.reorderRecords(it.first, it.second, it.third)
                finalDrag = null
            }
            posToID.clear()
        }
    }
}
