package com.lawlett.planner.ui.fragment.standup

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.StandUpModel
import com.lawlett.planner.data.room.viewmodels.StandUpViewModel
import com.lawlett.planner.databinding.FragmentStandUpBinding
import com.lawlett.planner.extensions.getDialog
import com.lawlett.planner.extensions.getTodayDate
import com.lawlett.planner.ui.adapter.StandUpAdapter
import com.lawlett.planner.ui.base.BaseAdapter
import com.lawlett.planner.ui.base.BaseFragment
import com.lawlett.planner.utils.BooleanPreference
import com.lawlett.planner.utils.Constants
import org.koin.android.ext.android.inject


class StandUpFragment : BaseFragment<FragmentStandUpBinding>(FragmentStandUpBinding::inflate),
    BaseAdapter.IBaseAdapterLongClickListenerWithModel<StandUpModel> {
    private val viewModel by inject<StandUpViewModel>()
    val adapter = StandUpAdapter()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("onViewCreated", "onViewCreated: StandUpFragment")
        initClickers()
        initAdapter()
        backToProgress()
        addFalseDataForExample()
    }

    private fun addFalseDataForExample() {
        if (BooleanPreference.getInstance(requireContext())?.getBooleanData(Constants.STANDUP_EXAMPLE_DATA)==false) {


            val model = StandUpModel(
                whatDone = "1.Переписал и перечитал код с урока\n" +
                        "2.Просмотрел стрим\n" +
                        "3.Написал верстку для Settings и для Quiz\n" +
                        "4.в HistoryStorage создал методы для HistoryDao\n" +
                        "5.в HistoryDao написал метод deleteAll под анотацией @Delete",
                whatPlan = "1.Перечитаю код QuizApp для большего понимания \n" +
                        "2.Перейду к реализации пунктов 4 и 5",
                problems = "Были сложности с заполнением History фейковыми данными",
                dateCreated = getTodayDate()
            )
            val model2 = StandUpModel(
                whatDone = "-Реализовал отображение прогресса\n" +
                        "-Реализовал отображение категории\n" +
                        "-Реализовал отработку кнопки back если первая страница закрывает Activity\n" +
                        "-Реализовал отработку кнопок на ответ\n" +
                        "-Поставил таймер на onAnswerClick чтоб при нажатии на ответ ждал 1 секунду прежде чем перелистнуть на следующий вопрос\n",
                whatPlan = "1.Начну верстку QuizResult \n 2.Реализую фунции определения наличия интернета или WI-FI",
                problems = "С перемешиванием вопросов и вложением в  лист answer",
                dateCreated = getTodayDate()
            )
            viewModel.insertData(model)
            viewModel.insertData(model2)
            BooleanPreference.getInstance(requireContext())
                ?.saveBooleanData(Constants.STANDUP_EXAMPLE_DATA, true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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