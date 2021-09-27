package com.lawlett.planner.ui.fragment.habit

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.navigation.fragment.findNavController
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.HabitModel
import com.lawlett.planner.data.room.viewmodels.AchievementViewModel
import com.lawlett.planner.data.room.viewmodels.HabitViewModel
import com.lawlett.planner.databinding.FragmentHabitBinding
import com.lawlett.planner.extensions.*
import com.lawlett.planner.service.MessageService
import com.lawlett.planner.ui.adapter.HabitAdapter
import com.lawlett.planner.ui.base.BaseAdapter
import com.lawlett.planner.ui.base.BaseFragment
import com.lawlett.planner.ui.dialog.CreateHabitBottomSheetDialog
import com.lawlett.planner.utils.Constants
import com.takusemba.spotlight.Target
import org.koin.android.ext.android.inject
import java.util.*

class HabitFragment : BaseFragment<FragmentHabitBinding>(FragmentHabitBinding::inflate),
    BaseAdapter.IBaseAdapterClickListener<HabitModel>,
    BaseAdapter.IBaseAdapterLongClickListenerWithModel<HabitModel> {
    private val viewModel by inject<HabitViewModel>()
    private val achievementViewModel by inject<AchievementViewModel>()
    var listModel: List<HabitModel>? = null
    private val calendar = Calendar.getInstance()

    val adapter = HabitAdapter()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickers()
        initAdapter()

//    val targets = ArrayList<Target>()
//
//    val root = FrameLayout(requireContext())
//    val first = layoutInflater.inflate(R.layout.layout_target, root)
//
//    setSpotLightTarget(binding.addHabitFab, first, targets, "test1")
//    setSpotLightBuilder(requireActivity(), targets, first)
    }

    private fun editHabit(position: Int, model: HabitModel) {
        val bundle = Bundle()
        bundle.putSerializable(Constants.HABIT_MODEL, model)
        val bottomDialog = CreateHabitBottomSheetDialog()
        bottomDialog.arguments = bundle
        bottomDialog.show(
            requireActivity().supportFragmentManager,
            Constants.UPDATE_MODEL
        )
        adapter.notifyItemChanged(position)
    }

    private fun initClickers() {
        binding.addHabitFab.setOnClickListener { initBottomSheet() }
        backToProgress()
    }

    private fun initBottomSheet() {
        val bottomDialog = CreateHabitBottomSheetDialog()
        bottomDialog.show(requireActivity().supportFragmentManager, "TAG")
    }

    private fun initAdapter() {
        val adapter = HabitAdapter()
        binding.habitRecycler.adapter = adapter
        adapter.listener = this
        adapter.longListenerWithModel = this
        getDataFromDataBase(adapter)
    }

    override fun onStop() {
        super.onStop()
        clearAnimations(achievementView = binding.achievementView)
    }



    override fun onClick(model: HabitModel, position: Int) {
//        showHabitDayUpDialog(model, position)
        com.lawlett.planner.extensions.showHabitDayUpDialog(
            model,
            position,
            requireContext(),
            adapter,
            viewModel,
            requireActivity(),
            achievementViewModel,
            binding.achievementView
        )

    }
    private fun getDataFromDataBase(adapter: HabitAdapter) {
        viewModel.getHabitsLiveData()
            .observe(viewLifecycleOwner, { habits ->
                if (habits.isNotEmpty()) {
                    adapter.setData(habits)
                    listModel = habits
                }
            })
    }

    override fun onLongClick(model: HabitModel, itemView: View, position: Int) {
        val dialog = requireContext().getDialog(R.layout.long_click_dialog)
        val delete = dialog.findViewById<Button>(R.id.delete_button)
        val edit = dialog.findViewById<Button>(R.id.edit_button)

        delete.setOnClickListener {
            deleteHabit(position, model, itemView)
            dialog.dismiss()
        }
        edit.setOnClickListener {
            editHabit(position, model)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun pickTime(habitModel: HabitModel) {
        if (requestPermission()) {
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            val timePicker =
                TimePickerDialog(
                    requireContext(),
                    { _, selectedHour, selectedMinute ->
                        calendar.set(
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DATE),
                            selectedHour,
                            selectedMinute
                        )
                        val time = calendar.timeInMillis
                        setNotification(time, habitModel, selectedHour, selectedMinute)
                    },
                    hour,
                    minute,
                    true
                )
            timePicker.show()
        }
    }

    private fun setNotification(
        time: Long,
        habitModel: HabitModel,
        selectedHour: Int,
        selectedMinute: Int
    ) {
        val i = Intent(requireContext(), MessageService::class.java)
        i.putExtra("displayText", "sample text")
        i.putExtra(Constants.TITLE, "Planner")
        i.putExtra(
            Constants.TEXT,
            getString(R.string.you_forgot_habbit) + " " + habitModel.title + " ?"
        )
        val pi = PendingIntent.getBroadcast(
            requireContext(),
            habitModel.id as Int,
            i,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val mAlarm = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        mAlarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, time, AlarmManager.INTERVAL_DAY, pi)
        requireContext().showToast(
            getString(R.string.set_notification_on) + " " + habitModel.title + " на " + "$selectedHour : $selectedMinute"
        )
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

    fun deleteHabit(position: Int, model: HabitModel, view: View) {
        view.explosionView(explosionField)
        viewModel.delete(model)
        if (position == 0) {
            findNavController().navigate(R.id.habitFragment)
        } else {
            adapter.notifyItemRemoved(position)
        }
    }
}