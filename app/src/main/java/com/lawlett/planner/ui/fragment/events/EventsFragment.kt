package com.lawlett.planner.ui.fragment.events

import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.EventModel
import com.lawlett.planner.data.room.viewmodels.EventViewModel
import com.lawlett.planner.databinding.FragmentEventsBinding
import com.lawlett.planner.extensions.getDialog
import com.lawlett.planner.extensions.setSpotLightBuilder
import com.lawlett.planner.extensions.setSpotLightTarget
import com.lawlett.planner.ui.adapter.EventAdapter
import com.lawlett.planner.ui.base.BaseAdapter
import com.lawlett.planner.ui.base.BaseFragment
import com.lawlett.planner.ui.dialog.CreateEventBottomSheetDialog
import com.lawlett.planner.utils.BooleanPreference
import com.lawlett.planner.utils.Constants
import com.takusemba.spotlight.Target
import org.koin.android.ext.android.inject
import java.util.*

class EventsFragment : BaseFragment<FragmentEventsBinding>(FragmentEventsBinding::inflate),
    BaseAdapter.IBaseAdapterLongClickListenerWithModel<EventModel> {
    val adapter = EventAdapter()
    val viewModel by inject<EventViewModel>()
    val calendar = Calendar.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initListeners()
        showSpotlight()
    }

    private fun showSpotlight() {
        if (BooleanPreference.getInstance(requireContext())
                ?.getBooleanData(Constants.EVENTS_INSTRUCTION) == false
        ) {
            val targets = ArrayList<Target>()
            val root = FrameLayout(requireContext())
            val first = layoutInflater.inflate(R.layout.layout_target, root)
            Handler().postDelayed({

                val firstSpot = setSpotLightTarget(
                    binding.eventRecycler,
                    first,
                    "\n\n\n\n "+ getString(R.string.events)+"\n\n\n "+ getString(R.string.planning_events) +"\n "+getString(R.string.set_remind_by_wish) +" \n "+getString(R.string.hold_card)
                )
                val secondSpot = setSpotLightTarget(
                    binding.addEventButton,
                    first,
                    getString(R.string.insert_button)+" \n "+getString(R.string.create_ev_date)
                )
                targets.add(firstSpot)
                targets.add(secondSpot)
                setSpotLightBuilder(requireActivity(), targets, first)
            }, 100)
            BooleanPreference.getInstance(requireContext())
                ?.saveBooleanData(Constants.EVENTS_INSTRUCTION, true)
        }
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

    private fun pickTime(model: EventModel) {
        if (requestPermission()) {
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            val timePicker =
                TimePickerDialog(
                    requireContext(),
                    { _, selectedHour, selectedMinute ->
//                        setNotification(model,selectedHour,selectedMinute)
                    },
                    hour,
                    minute,
                    true
                )
            timePicker.show()
        }
    }
    private fun requestPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(requireContext())) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + requireActivity().packageName)
                )
                startActivityForResult(intent, 1)
                return false
            }
        }
        return true
    }
}