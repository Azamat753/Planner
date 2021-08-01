package com.lawlett.planner.ui.standup

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.StandUpModel
import com.lawlett.planner.data.room.viewmodels.StandUpViewModel
import com.lawlett.planner.databinding.FragmentStandUpBinding
import com.lawlett.planner.ui.adapter.StandUpAdapter
import com.lawlett.planner.ui.base.BaseAdapter
import com.lawlett.planner.ui.base.BaseFragment
import org.koin.android.ext.android.inject


class StandUpFragment : BaseFragment<FragmentStandUpBinding>(FragmentStandUpBinding::inflate),
    BaseAdapter.IBaseAdapterClickListener<StandUpModel> {
    private val viewModel by inject<StandUpViewModel>()
    val adapter = StandUpAdapter()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickers()
        initAdapter()
        backClick()
    }

    private fun getDataFromDataBase() {
        viewModel.getData()
            .observe(viewLifecycleOwner, { standUp ->
                if (standUp.isNotEmpty()) {
                    adapter.setData(standUp)
                }
            })
    }

    private fun initAdapter() {
        binding.standUpRecycler.adapter = adapter
        adapter.listener = this
        getDataFromDataBase()
    }

    private fun initClickers() {
        binding.addStandUpButton.setOnClickListener {
            findNavController().navigate(
                R.id.mainCreateStandUpFragment
            )
        }
    }

    override fun onClick(model: StandUpModel) {
        shareStandUp(model)
    }

    private fun shareStandUp(model: StandUpModel) {
        val standUp: String = if (model.information != null) {
            getString(R.string.what_done_yesterday) + "\n" + model.whatDone + "\n" +
                    getString(R.string.what_plan_today) + "\n" + model.whatPlan +"\n" +
                    getString(R.string.problems) + "\n" + model.problems +"\n" +
                    getString(R.string.important_info) + "\n" + model.information
        } else {
            getString(R.string.what_done_yesterday) + "\n" + model.whatDone + "\n" +
                    getString(R.string.what_plan_today) + "\n" + model.whatPlan + "\n" +
                    getString(R.string.problems) + "\n" + model.problems
        }

        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.type = "text/plain";
        intent.putExtra(Intent.EXTRA_TEXT, standUp);
        startActivity(Intent.createChooser(intent, "Share"));
    }
}