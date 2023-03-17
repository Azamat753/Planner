package com.lawlett.planner.ui.fragment.standup

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.StandUpModel
import com.lawlett.planner.data.room.viewmodels.StandUpViewModel
import com.lawlett.planner.databinding.FragmentCreateStandupBinding
import com.lawlett.planner.extensions.visible
import com.lawlett.planner.ui.base.BaseFragment
import com.lawlett.planner.utils.Constants
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*


class CreateStandUpFragment :
    BaseFragment<FragmentCreateStandupBinding>(FragmentCreateStandupBinding::inflate) {
    private val viewModel by inject<StandUpViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews()
        onBackPressOverride(R.id.standUpFragment)
    }

    private fun isUpdate(): Boolean {
        return MainCreateStandUpFragment.instance.getModel().whatDone.isNotEmpty()
    }

    private fun getModel(): StandUpModel {
        return MainCreateStandUpFragment.instance.getModel()
    }

    private fun fillWhatDone() {
        if (isUpdate()) {
            binding.description.setText(getModel().whatDone)
        }

        binding.applyButton.setOnClickListener {
            if (binding.description.text.toString().trim().isEmpty()) {
                setErrorOnField()
            } else {
                changePage(1)
                val whatDone = binding.description.text.toString().trim()
                saveToPreferences(Constants.WHAT_DONE, whatDone)
            }
        }
    }

    private fun fillWhatPlan() {
        if (isUpdate()) {
            binding.description.setText(getModel().whatPlan)
        }
        binding.applyButton.setOnClickListener {
            if (binding.description.text.toString().trim().isEmpty()) {
                setErrorOnField()
            } else {
                changePage(2)
                val whatPlan = binding.description.text.toString().trim()
                saveToPreferences(Constants.WHAT_PLAN, whatPlan)
            }
        }
    }

    private fun fillProblems() {
        if (isUpdate()) {
            binding.description.setText(getModel().problems)
        }
        binding.applyButton.setOnClickListener {
            if (binding.description.text.toString().trim().isEmpty()) {
                setErrorOnField()
            } else {
                changePage(3)
                val problems = binding.description.text.toString().trim()
                saveToPreferences(Constants.PROBLEMS, problems)
            }
        }
    }

    private fun fillInfo() {
        if (isUpdate()) {
            if (getModel().information != null) {
                binding.description.setText(getModel().information)
            }
        }
        if (binding.description.text.toString().trim().isNotEmpty()) {
            val importantInfo = binding.description.text.toString().trim()
            saveToPreferences(Constants.INFO, importantInfo)
        }
    }

    private fun saveToPreferences(key: String, value: String) {
        val sharedPref =
            requireContext().getSharedPreferences(Constants.STAND_UP, Context.MODE_PRIVATE)
        sharedPref?.edit()?.putString(key, value)?.apply()
    }

    private fun getFromPreferences(key: String): String? {
        val sharedPref =
            requireContext().getSharedPreferences(Constants.STAND_UP, Context.MODE_PRIVATE)
        return sharedPref.getString(key, "")
    }

    private fun clearPreferences() {
        val sharedPref =
            requireContext().getSharedPreferences(Constants.STAND_UP, Context.MODE_PRIVATE)
        sharedPref.edit().clear().apply()
    }


    private fun setErrorOnField() {
        binding.description.error = getString(R.string.fill_field)
    }

    @SuppressLint("SimpleDateFormat")
    private fun checkFieldAndFillModel() {
        binding.applyButton.setOnClickListener {
            fillInfo()
            val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm ")
            val currentDate = sdf.format(Date()).toString()
            val whatDone = getFromPreferences(Constants.WHAT_DONE).toString()
            val whatPlan = getFromPreferences(Constants.WHAT_PLAN).toString()
            val problems = getFromPreferences(Constants.PROBLEMS).toString()
            val importantInfo = getFromPreferences(Constants.INFO).toString()
            when {
                whatDone.isEmpty() -> {
                    changePage(0)
                    setErrorOnField()
                }
                whatPlan.isEmpty() -> {
                    changePage(1)
                    setErrorOnField()
                }
                problems.isEmpty() -> {
                    changePage(2)
                    setErrorOnField()
                }
                else -> {
                    if (importantInfo.isNotEmpty()) {
                        val model = StandUpModel(
                            whatPlan = whatPlan,
                            whatDone = whatDone,
                            problems = problems,
                            dateCreated = currentDate,
                            information = importantInfo
                        )
                        viewModel.insertData(model)
                    } else {
                        if (isUpdate()) {
                            val model = StandUpModel(
                                id = getModel().id,
                                whatPlan = whatPlan,
                                whatDone = whatDone,
                                problems = problems,
                                dateCreated = currentDate
                            )
                            viewModel.update(model)
                        } else {
                            val model = StandUpModel(
                                whatPlan = whatPlan,
                                whatDone = whatDone,
                                problems = problems,
                                dateCreated = currentDate
                            )
                            viewModel.insertData(model)
                        }
                    }
                    clearPreferences()
                    findNavController().popBackStack(R.id.mainCreateStandUpFragment, true)
                }
            }
        }
    }

    fun changePage(page: Int) {
        MainCreateStandUpFragment.instance.binding.pagerStandUp.currentItem = page
    }

    @SuppressLint("SetTextI18n")
    private fun setViews() {
        when (arguments?.getInt(Constants.STAND_UP_POS)) {
            0 -> {
                binding.title.text = getString(R.string.what_done_yesterday)
                fillWhatDone()
            }
            1 -> {
                binding.title.text = getString(R.string.what_plan_today)
                fillWhatPlan()
                binding.standUpAnimation.setAnimation("planning.json")
            }
            2 -> {
                binding.title.text = getString(R.string.problems)
                fillProblems()
                binding.standUpAnimation.setAnimation("solving.json")
            }
            3 -> {
                binding.applyButton.visible()
                binding.title.text = getString(R.string.important_info) +"\n" +getString(R.string.not_necessary)
                binding.applyButton.text = getString(R.string.done)
                fillInfo()
                checkFieldAndFillModel()
                binding.standUpAnimation.setAnimation("data.json")
            }
        }
    }
}
