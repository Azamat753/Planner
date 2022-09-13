package com.lawlett.planner.ui.fragment.standup

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.navigation.fragment.findNavController
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.StandUpModel
import com.lawlett.planner.data.room.viewmodels.StandUpViewModel
import com.lawlett.planner.databinding.FragmentStandUpBinding
import com.lawlett.planner.extensions.*
import com.lawlett.planner.ui.adapter.StandUpAdapter
import com.lawlett.planner.ui.base.BaseAdapter
import com.lawlett.planner.ui.base.BaseFragment
import com.lawlett.planner.utils.BooleanPreference
import com.lawlett.planner.utils.Constants
import com.takusemba.spotlight.Target
import org.koin.android.ext.android.inject
import java.util.*


class StandUpFragment : BaseFragment<FragmentStandUpBinding>(FragmentStandUpBinding::inflate),
    BaseAdapter.IBaseAdapterLongClickListenerWithModel<StandUpModel> {
    private val viewModel by inject<StandUpViewModel>()
    val adapter = StandUpAdapter()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickers()
        initAdapter()
        backToProgress()
        addFalseDataForExample()
        showSpotlight()
    }

    private fun showSpotlight() {
//        if (BooleanPreference.getInstance(requireContext())
//                ?.getBooleanData(Constants.STANDUP_INSTRUCTION) == false
//        ) {
//        val targets = ArrayList<Target>()
//        val root = FrameLayout(requireContext())
//        val first = layoutInflater.inflate(R.layout.layout_target, root)
//        val view = View(requireContext())
//        Handler().postDelayed({
//            val firstSpot = setSpotLightTarget(
//                view,
//                first,
//                getString(R.string.stand_up)+" \n\n\n "+getString(R.string.list_standup)+"\n "+ getString(R.string.team_work)+" \n"+getString(
//                                    R.string.plan_and_problem_share)
//            )
//            val secondSpot = setSpotLightTarget(
//                view,
//                first,
//                getString(R.string.good_system)+" \n"+getString(R.string.good_friend_standup)
//            )
//            val thirdSpot = setSpotLightTarget(
//                view,
//                first,
//               getString(R.string.hold_card)+"\n"+getString(R.string.share_fun_send)
//            )
//            val fourSpot = setSpotLightTarget(
//                binding.addStandUpButton,
//                first,
//                getString(R.string.insert_button) + " \n" + getString(R.string.create_new_standup)
//            )
//            targets.add(firstSpot)
//            targets.add(secondSpot)
//            targets.add(thirdSpot)
//            targets.add(fourSpot)
//            setSpotLightBuilder(requireActivity(), targets, first)
//        }, 100)
//        BooleanPreference.getInstance(requireContext())
//            ?.saveBooleanData(Constants.STANDUP_INSTRUCTION, true)
//    }
    }

    private fun addFalseDataForExample() {
        if (BooleanPreference.getInstance(requireContext())
                ?.getBooleanData(Constants.STANDUP_EXAMPLE_DATA) == false
        ) {


            val model = StandUpModel(
                whatDone = getString(R.string.rewrite_code)+"\n" +
                        getString(R.string.watch_stream)+"\n" +
                        getString(R.string.write_screen_se_q)+"\n" +
                        getString(R.string.create_h_st_dao)+"\n" +
                        getString(R.string.in_dao_delete),
                whatPlan = getString(R.string.reread_code_q)+"\n" +
                        getString(R.string.impl_f_fi),
                problems = getString(R.string.hard_fa_da),
                dateCreated = getTodayDate(requireContext())
            )
            val model2 = StandUpModel(
                whatDone = getString(R.string.im_sh_pr)+"\n" +
                        getString(R.string.im_sh_ca)+"\n" +
                        getString(R.string.im_ba_f_a)+"\n" +
                        getString(R.string.im_b_an)+"\n" +
                        getString(R.string.one_sec_pag)+"\n",
                whatPlan = getString(R.string.st_sc_qr) +
                        "\n " +
                        getString(R.string.im_wi_fi),
                problems = getString(R.string.re_q_l_ans),
                dateCreated = getTodayDate(requireContext())
            )
            viewModel.insertData(model)
            viewModel.insertData(model2)
            BooleanPreference.getInstance(requireContext())
                ?.saveBooleanData(Constants.STANDUP_EXAMPLE_DATA, true)
        }
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
        adapter.longListenerWithModel = this
        getDataFromDataBase()
    }

    private fun initClickers() {
        binding.addStandUpButton.setOnClickListener {
            val bundle = Bundle()
            val model = StandUpModel(
                whatDone = "",
                whatPlan = "",
                problems = "",
                information = "",
                dateCreated = ""
            )
            bundle.putSerializable(Constants.UPDATE_MODEL, model)
            findNavController().navigate(
                R.id.mainCreateStandUpFragment, bundle
            )
        }
    }

    private fun shareStandUp(model: StandUpModel) {
        val standUp: String = if (model.information != null) {
            getString(R.string.what_done_yesterday) + "\n" + model.whatDone + "\n" +
                    getString(R.string.what_plan_today) + "\n" + model.whatPlan + "\n" +
                    getString(R.string.problems) + "\n" + model.problems + "\n" +
                    getString(R.string.important_info) + "\n" + model.information
        } else {
            getString(R.string.what_done_yesterday) + "\n" + model.whatDone + "\n" +
                    getString(R.string.what_plan_today) + "\n" + model.whatPlan + "\n" +
                    getString(R.string.problems) + "\n" + model.problems
        }

        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, standUp)
        startActivity(Intent.createChooser(intent, "Share"))
    }

    override fun onLongClick(model: StandUpModel, itemView: View, position: Int) {
        val dialog = requireContext().getDialog(R.layout.long_click_dialog)
        val delete = dialog.findViewById<Button>(R.id.delete_button)
        val edit: Button = dialog.findViewById(R.id.edit_button)
        val share: Button = dialog.findViewById(R.id.third_button)
        share.visible()
        share.text = getString(R.string.share)

        delete.setOnClickListener {
            deleteModel(itemView, model, dialog)
            dialog.dismiss()
        }

        edit.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(Constants.UPDATE_MODEL, model)
            findNavController().navigate(R.id.mainCreateStandUpFragment, bundle)
            dialog.dismiss()
        }

        share.setOnClickListener {
            shareStandUp(model)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun deleteModel(
        itemView: View,
        model: StandUpModel,
        dialog: Dialog
    ) {
        explosionField.explode(itemView)
        viewModel.delete(model)
        dialog.dismiss()
    }
}