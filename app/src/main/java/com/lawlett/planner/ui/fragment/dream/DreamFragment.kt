package com.lawlett.planner.ui.fragment.dream

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.lawlett.planner.R
import com.lawlett.planner.callback.CheckListEvent
import com.lawlett.planner.data.room.models.DreamModel
import com.lawlett.planner.data.room.viewmodels.AchievementViewModel
import com.lawlett.planner.data.room.viewmodels.DreamViewModel
import com.lawlett.planner.databinding.FragmentDreamBinding
import com.lawlett.planner.extensions.*
import com.lawlett.planner.ui.adapter.DreamAdapter
import com.lawlett.planner.ui.base.BaseAdapter
import com.lawlett.planner.ui.base.BaseFragment
import com.lawlett.planner.ui.dialog.CreateDreamBottomSheetDialog
import com.lawlett.planner.utils.BooleanPreference
import com.lawlett.planner.utils.Constants
import com.takusemba.spotlight.Target
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.util.*

class DreamFragment : BaseFragment<FragmentDreamBinding>(FragmentDreamBinding::inflate),
    BaseAdapter.IBaseAdapterLongClickListenerWithModel<DreamModel>,
    BaseAdapter.IBaseAdapterClickListener<DreamModel>, CheckListEvent {
    private var listSize: Int = 0
    private val viewModel by inject<DreamViewModel>()
    private val achievementViewModel by inject<AchievementViewModel>()
    val adapter = DreamAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initClickers()
        showSpotlight()
        addFalseDataForExample()
    }

    private fun addFalseDataForExample() {
        if (BooleanPreference.getInstance(requireContext())
                ?.getBooleanData(Constants.DREAM_EXAMPLE_DATA) == false
        ) {
            val model = DreamModel(
                wokeUp = "23 : 00",
                fellAsleep = "05 : 00",
                sleptHour = "06:00",
                dream = getString(R.string.flight_over_city),
                dateCreated = getTodayDate(requireContext()),
                color = R.color.yellow_theme
            )
            val model2 =
                DreamModel(
                    wokeUp = "22 : 40",
                    fellAsleep = "05 : 00",
                    sleptHour = "06:20",
                    dream = getString(R.string.my_dream_in_dream),
                    dateCreated = getTodayDate(requireContext()),
                    color = R.color.red_theme
                )
            viewModel.insertData(model)
            viewModel.insertData(model2)
            BooleanPreference.getInstance(requireContext())
                ?.saveBooleanData(Constants.DREAM_EXAMPLE_DATA, true)
        }
    }


    private fun showSpotlight() {
        if (BooleanPreference.getInstance(requireContext())
                ?.getBooleanData(Constants.DREAM_INSTRUCTION) == false
        ) {
            val view = View(requireContext())
            lifecycleScope.launch {
                delay(1000)
                requireActivity().showSpotlight(
                    lifecycleScope,
                    mapOf(
                        view to getString(R.string.dream) + "\n\n\n " + getString(R.string.list_dream) + "\n " + getString(
                            R.string.up_slept_hour
                        ) + " \n " + getString(R.string.next_time_date) + " \n " + getString(
                            R.string.course_record_dream
                        )
                    ),
                    mapOf(
                        view to getString(R.string.hold_card)
                    ),
                )
            }
            BooleanPreference.getInstance(requireContext())
                ?.saveBooleanData(Constants.DREAM_INSTRUCTION, true)
        }
    }

    private fun initClickers() {
        binding.addDreamFab.setOnClickListener { showSheetDialog() }
    }

    private fun showSheetDialog() {
        val bottomDialog = CreateDreamBottomSheetDialog(this)
        bottomDialog.show(requireActivity().supportFragmentManager, "TAG")
    }

    private fun initAdapter() {
        binding.categoryRecycler.adapter = adapter
        adapter.listener = this
        adapter.longListenerWithModel = this
        getDataFromDataBase()
    }

    private fun getDataFromDataBase() {
        viewModel.getData().observe(viewLifecycleOwner, { dream ->
            if (dream.isNotEmpty()) {
                listSize = dream.size
                adapter.setData(dream)
            }
        })
    }

    override fun onLongClick(model: DreamModel, itemView: View, position: Int) {
        val dialog = requireContext().getDialog(R.layout.long_click_dialog)
        val delete: Button = dialog.findViewById(R.id.delete_button)
        val edit: Button = dialog.findViewById(R.id.edit_button)
        edit.setOnClickListener {
            showDialogForUpdate(model)
            dialog.dismiss()
        }
        delete.setOnClickListener {
            itemView.explosionView(explosionField)
            viewModel.delete(model)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showDialogForUpdate(model: DreamModel) {
        val bottomDialog = CreateDreamBottomSheetDialog(this)
        val bundle = Bundle()
        bundle.putSerializable(Constants.UPDATE_MODEL, model)
        bottomDialog.arguments = bundle
        bottomDialog.show(requireActivity().supportFragmentManager, "TAG")
    }

    override fun onClick(model: DreamModel, position: Int) {
        val dialog = requireContext().getDialog(R.layout.dream_dialog)
        val title = dialog.findViewById<TextView>(R.id.title)
        val dream = dialog.findViewById<TextView>(R.id.dream_tv)
        if (model.dream?.isEmpty() == true) {
            title.text = getString(R.string.no_records)
        } else {
            title.text = getString(R.string.dream)
            dream.text = model.dream
        }
        dialog.show()
    }

    override fun onStop() {
        super.onStop()
        clearAnimations(achievementView = binding.achievementView)
    }

    override fun check() {
        rewardAnAchievement(
            listSize,
            requireActivity(),
            achievementViewModel,
            binding.achievementView
        )
    }
}