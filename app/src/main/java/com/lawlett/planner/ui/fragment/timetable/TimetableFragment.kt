package com.lawlett.planner.ui.fragment.timetable

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.Button
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.lawlett.planner.R
import com.lawlett.planner.callback.CheckListTimeTable
import com.lawlett.planner.data.room.models.TimetableModel
import com.lawlett.planner.data.room.viewmodels.AchievementViewModel
import com.lawlett.planner.data.room.viewmodels.TimetableViewModel
import com.lawlett.planner.databinding.FragmentTimetableBinding
import com.lawlett.planner.extensions.*
import com.lawlett.planner.ui.adapter.TimetableAdapter
import com.lawlett.planner.ui.base.BaseAdapter.IBaseAdapterLongClickListenerWithModel
import com.lawlett.planner.ui.base.BaseFragment
import com.lawlett.planner.ui.dialog.CreateTimetableBottomSheetDialog
import com.lawlett.planner.utils.BooleanPreference
import com.lawlett.planner.utils.Constants
import com.takusemba.spotlight.Target
import org.koin.android.ext.android.inject

class TimetableFragment :
    BaseFragment<FragmentTimetableBinding>(FragmentTimetableBinding::inflate),
    CreateTimetableBottomSheetDialog.UpdateRecycler, CheckListTimeTable {
    private val viewModel by inject<TimetableViewModel>()
    private val achievementViewModel by inject<AchievementViewModel>()
    private val adapter = TimetableAdapter()
    private lateinit var listMonday: List<TimetableModel>
    private lateinit var listTuesday: List<TimetableModel>
    private lateinit var listWednesday: List<TimetableModel>
    private lateinit var listThursday: List<TimetableModel>
    private lateinit var listFriday: List<TimetableModel>
    private lateinit var listSaturday: List<TimetableModel>
    private lateinit var listSunday: List<TimetableModel>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle()
        backToProgress()
        initListeners()
        initMondayAdapter()
        initTuesdayAdapter()
        initWednesdayAdapter()
        initThursdayAdapter()
        initFridayAdapter()
        initSaturdayAdapter()
        initSundayAdapter()
        showSpotlight()
    }

    private fun showSpotlight() {
        if (BooleanPreference.getInstance(requireContext())
                ?.getBooleanData(Constants.TIMETABLE_INSTRUCTION) == false
        ) {

            val targets = ArrayList<Target>()
            val root = FrameLayout(requireContext())
            val first = layoutInflater.inflate(R.layout.layout_target, root)

            Handler().postDelayed({
                val firstSpot = setSpotLightTarget(
                    binding.monday.titleCard,
                    first,
                    getString(R.string.timetable) + " \n\n\n " + getString(R.string.under_everyday) + " \n " + getString(
                        R.string.cause_see
                    ) + " \n " + getString(R.string.hold_task_action)
                )
                val secondSpot = setSpotLightTarget(
                    binding.addTimetableButton,
                    first,
                    getString(R.string.insert_button) + "\n " + getString(R.string.click_here_button)
                )
                targets.add(firstSpot)
                targets.add(secondSpot)
                setSpotLightBuilder(requireActivity(), targets, first)
            }, 100)
            BooleanPreference.getInstance(requireContext())
                ?.saveBooleanData(Constants.TIMETABLE_INSTRUCTION, true)
        }
    }

    private fun initSundayAdapter() {
        val adapter = TimetableAdapter()
        binding.sundayRecycler.adapter = adapter
        viewModel.getDataOfDayOfWeek(getString(R.string.sunday))
            .observe(viewLifecycleOwner, { sunday ->
                adapter.setData(sunday)
                listSunday = sunday
            })
        adapter.longListenerWithModel =
            object : IBaseAdapterLongClickListenerWithModel<TimetableModel> {
                override fun onLongClick(model: TimetableModel, itemView: View, position: Int) {
                    showActionDialog(binding.sundayRecycler, position, listSunday, 6)
                }
            }
    }

    private fun initSaturdayAdapter() {
        val adapter = TimetableAdapter()
        binding.saturdayRecycler.adapter = adapter
        viewModel.getDataOfDayOfWeek(getString(R.string.saturday))
            .observe(viewLifecycleOwner, { saturday ->
                adapter.setData(saturday)
                listSaturday = saturday
            })
        adapter.longListenerWithModel =
            object : IBaseAdapterLongClickListenerWithModel<TimetableModel> {
                override fun onLongClick(model: TimetableModel, itemView: View, position: Int) {
                    showActionDialog(binding.saturdayRecycler, position, listSaturday, 5)
                }
            }
    }

    private fun initFridayAdapter() {
        val adapter = TimetableAdapter()
        binding.fridayRecycler.adapter = adapter
        viewModel.getDataOfDayOfWeek(getString(R.string.friday))
            .observe(viewLifecycleOwner, { friday ->
                adapter.setData(friday)
                listFriday = friday
            })
        adapter.longListenerWithModel =
            object : IBaseAdapterLongClickListenerWithModel<TimetableModel> {
                override fun onLongClick(model: TimetableModel, itemView: View, position: Int) {
                    showActionDialog(binding.fridayRecycler, position, listFriday, 4)
                }
            }
    }

    private fun initThursdayAdapter() {
        val adapter = TimetableAdapter()
        binding.thursdayRecycler.adapter = adapter
        viewModel.getDataOfDayOfWeek(getString(R.string.thursday))
            .observe(viewLifecycleOwner, { thursday ->
                adapter.setData(thursday)
                listThursday = thursday
            })
        adapter.longListenerWithModel =
            object : IBaseAdapterLongClickListenerWithModel<TimetableModel> {
                override fun onLongClick(model: TimetableModel, itemView: View, position: Int) {
                    showActionDialog(binding.thursdayRecycler, position, listThursday, 3)
                }
            }
    }

    private fun initWednesdayAdapter() {
        val adapter = TimetableAdapter()
        binding.wednesdayRecycler.adapter = adapter
        viewModel.getDataOfDayOfWeek(getString(R.string.wednesday))
            .observe(viewLifecycleOwner, { wednesday ->
                adapter.setData(wednesday)
                listWednesday = wednesday
            })

        adapter.longListenerWithModel =
            object : IBaseAdapterLongClickListenerWithModel<TimetableModel> {
                override fun onLongClick(model: TimetableModel, itemView: View, position: Int) {
                    showActionDialog(binding.wednesdayRecycler, position, listWednesday, 2)
                }
            }
    }

    private fun initTuesdayAdapter() {
        val adapter = TimetableAdapter()
        binding.tuesdayRecycler.adapter = adapter
        viewModel.getDataOfDayOfWeek(getString(R.string.tuesday))
            .observe(viewLifecycleOwner, { tuesday ->
                adapter.setData(tuesday)
                listTuesday = tuesday
            })
        adapter.longListenerWithModel =
            object : IBaseAdapterLongClickListenerWithModel<TimetableModel> {
                override fun onLongClick(model: TimetableModel, itemView: View, position: Int) {
                    showActionDialog(binding.tuesdayRecycler, position, listTuesday, 1)
                }
            }
    }

    private fun initMondayAdapter() {
        binding.mondayRecycler.adapter = adapter
        viewModel.getDataOfDayOfWeek(getString(R.string.monday))
            .observe(viewLifecycleOwner, { monday ->
                adapter.setData(monday)
                listMonday = monday
            })
        adapter.longListenerWithModel =
            object : IBaseAdapterLongClickListenerWithModel<TimetableModel> {
                override fun onLongClick(model: TimetableModel, itemView: View, position: Int) {
                    showActionDialog(binding.mondayRecycler, position, listMonday, 0)
                }
            }
    }

    fun showActionDialog(
        recyclerView: RecyclerView,
        position: Int,
        list: List<TimetableModel>,
        dayIndex: Int
    ) {
        val dialog = requireContext().getDialog(R.layout.long_click_dialog)
        val delete = dialog.findViewById<Button>(R.id.delete_button)
        val edit = dialog.findViewById<Button>(R.id.edit_button)
        edit.text = getString(R.string.edit)
        delete.setOnClickListener {
            deleteItem(recyclerView, position, list)
            dialog.dismiss()
        }
        edit.setOnClickListener {
            openSheetDialogForEdit(list[position], dayIndex)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun deleteItem(
        recyclerView: RecyclerView,
        position: Int,
        list: List<TimetableModel>
    ) {
        recyclerView.findViewHolderForAdapterPosition(
            position
        )?.itemView?.explosionView(explosionField)

        list[position].let { viewModel.delete(it) }
        if (position == 0) {
            findNavController().navigate(R.id.timetableFragment)
        } else {
            adapter.notifyItemRemoved(position)
        }
    }

    private fun initListeners() {
        binding.addTimetableButton.setOnClickListener {
            openSheetDialog(-5)
        }
        binding.monday.titleCard.setOnClickListener {
            setVisibility(binding.mondayRecycler)
        }
        binding.tuesday.titleCard.setOnClickListener { setVisibility(binding.tuesdayRecycler) }
        binding.wednesday.titleCard.setOnClickListener { setVisibility(binding.wednesdayRecycler) }
        binding.thursday.titleCard.setOnClickListener { setVisibility(binding.thursdayRecycler) }
        binding.friday.titleCard.setOnClickListener { setVisibility(binding.fridayRecycler) }
        binding.saturday.titleCard.setOnClickListener { setVisibility(binding.saturdayRecycler) }
        binding.sunday.titleCard.setOnClickListener { setVisibility(binding.sundayRecycler) }
    }

    private fun setVisibility(recyclerView: RecyclerView) {
        if (recyclerView.isVisible) {
            recyclerView.gone()
        } else {
            val lac = LayoutAnimationController(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.item_anim
                )
            )
            lac.delay = 0.20f
            lac.order = LayoutAnimationController.ORDER_NORMAL
            recyclerView.layoutAnimation = lac
            recyclerView.startLayoutAnimation()
            recyclerView.visible()
        }
    }

    private fun openSheetDialogForEdit(model: TimetableModel, dayIndex: Int) {
        val bottomDialog = CreateTimetableBottomSheetDialog(this, this)
        val bundle = Bundle()
        bundle.putSerializable(Constants.UPDATE_MODEL, model)
        bottomDialog.arguments = bundle
        bottomDialog.show(requireActivity().supportFragmentManager, dayIndex.toString())
    }

    private fun openSheetDialog(day: Int) {
        val bottomDialog = CreateTimetableBottomSheetDialog(this, this)
        bottomDialog.show(requireActivity().supportFragmentManager, day.toString())
    }

    private fun setTitle() {
        binding.monday.title.text = getString(R.string.monday)
        binding.tuesday.title.text = getString(R.string.tuesday)
        binding.wednesday.title.text = getString(R.string.wednesday)
        binding.thursday.title.text = getString(R.string.thursday)
        binding.friday.title.text = getString(R.string.friday)
        binding.saturday.title.text = getString(R.string.saturday)
        binding.sunday.title.text = getString(R.string.sunday)
    }

    override fun needUpdate(dayOfWeek: Int) {
        updateAdapter(dayOfWeek)
    }

    private fun updateAdapter(dayOfWeek: Int) {
        when (dayOfWeek) {
            0 -> {
                initMondayAdapter()
            }
            1 -> {
                initTuesdayAdapter()
            }
            2 -> {
                initWednesdayAdapter()
            }
            3 -> {
                initThursdayAdapter()
            }
            4 -> {
                initFridayAdapter()
            }
            5 -> {
                initSaturdayAdapter()
            }
            6 -> {
                initSundayAdapter()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        clearAnimations(achievementView = binding.achievementView)
    }

    override fun check(dayOfWeek: Int) {
        updateAdapter(dayOfWeek)

        when (dayOfWeek) {
            0 -> {
                rewardAnAchievement(listMonday.size,requireActivity(),achievementViewModel,binding.achievementView)
            }
            1 -> {
                rewardAnAchievement(listTuesday.size,requireActivity(),achievementViewModel,binding.achievementView)
            }
            2 -> {
                rewardAnAchievement(listWednesday.size,requireActivity(),achievementViewModel,binding.achievementView)
            }
            3 -> {
                rewardAnAchievement(listThursday.size,requireActivity(),achievementViewModel,binding.achievementView)
            }
            4 -> {
                rewardAnAchievement(listFriday.size,requireActivity(),achievementViewModel,binding.achievementView)
            }
            5 -> {
                rewardAnAchievement(listSaturday.size,requireActivity(),achievementViewModel,binding.achievementView)
            }
            6 -> {
                rewardAnAchievement(listSunday.size,requireActivity(),achievementViewModel,binding.achievementView)
            }
        }
    }
}