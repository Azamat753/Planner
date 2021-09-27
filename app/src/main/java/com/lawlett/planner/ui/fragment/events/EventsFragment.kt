package com.lawlett.planner.ui.fragment.events

import android.os.Bundle
import android.view.View
import android.widget.Button
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.EventModel
import com.lawlett.planner.data.room.viewmodels.EventViewModel
import com.lawlett.planner.databinding.FragmentEventsBinding
import com.lawlett.planner.extensions.getDialog
import com.lawlett.planner.ui.adapter.EventAdapter
import com.lawlett.planner.ui.base.BaseAdapter
import com.lawlett.planner.ui.base.BaseFragment
import com.lawlett.planner.ui.dialog.CreateEventBottomSheetDialog
import com.lawlett.planner.utils.Constants
import org.koin.android.ext.android.inject

class EventsFragment : BaseFragment<FragmentEventsBinding>(FragmentEventsBinding::inflate),
    BaseAdapter.IBaseAdapterLongClickListenerWithModel<EventModel> {
    val adapter = EventAdapter()
    val viewModel by inject<EventViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initListeners()
    }

    private fun initAdapter() {
        binding.eventRecycler.adapter = adapter
        adapter.longListenerWithModel = this
        viewModel.getData().observe(viewLifecycleOwner, { events ->
            adapter.setData(events)
        })
    }

    private fun initBottomSheetDialog() {
        val bottomDialog = CreateEventBottomSheetDialog()
        bottomDialog.show(requireActivity().supportFragmentManager, "TAG")
    }

    private fun openSheetDialogForEdit(model: EventModel) {
        val bottomDialog = CreateEventBottomSheetDialog()
        val bundle = Bundle()
        bundle.putSerializable(Constants.UPDATE_MODEL, model)
        bottomDialog.arguments = bundle
        bottomDialog.show(requireActivity().supportFragmentManager, Constants.UPDATE_MODEL)
    }

    private fun initListeners() {
        binding.addEventButton.setOnClickListener {
            initBottomSheetDialog()
        }
        backToProgress()
    }

    override fun onLongClick(model: EventModel, itemView: View, position: Int) {
        val dialog = requireContext().getDialog(R.layout.long_click_dialog)
        val delete: Button = dialog.findViewById(R.id.delete_button)
        val edit: Button = dialog.findViewById(R.id.edit_button)
        delete.setOnClickListener {
            deleteModel(itemView, model)
            dialog.dismiss()
        }
        edit.setOnClickListener {
            openSheetDialogForEdit(model)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun deleteModel(
        itemView: View,
        model: EventModel
    ) {
        explosionField.explode(itemView)
        viewModel.delete(model)
    }


}