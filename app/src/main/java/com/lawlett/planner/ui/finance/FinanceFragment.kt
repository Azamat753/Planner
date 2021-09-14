package com.lawlett.planner.ui.finance

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.FinanceModel
import com.lawlett.planner.data.room.viewmodels.FinanceViewModel
import com.lawlett.planner.databinding.FragmentFinanceBinding
import com.lawlett.planner.extensions.explosionView
import com.lawlett.planner.extensions.getDialog
import com.lawlett.planner.ui.adapter.FinanceAdapter
import com.lawlett.planner.ui.adapter.FinancePatternAdapter
import com.lawlett.planner.ui.base.BaseAdapter
import com.lawlett.planner.ui.base.BaseFragment
import com.lawlett.planner.ui.dialog.fragment.FinanceBottomSheetDialog
import com.lawlett.planner.utils.AdvicePreference
import com.lawlett.planner.utils.AdviceText
import com.lawlett.planner.utils.Constants
import org.koin.android.ext.android.inject

class FinanceFragment : BaseFragment<FragmentFinanceBinding>(FragmentFinanceBinding::inflate),
    BaseAdapter.IBaseAdapterLongClickListener, BaseAdapter.IBaseAdapterClickListener<FinanceModel> {
    val adapter = FinanceAdapter()
    private val adapterPattern = FinancePatternAdapter()
    val viewModel by inject<FinanceViewModel>()
    var listModel: List<FinanceModel>? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickers()
        getHistory()
        setPattern()
    }

    private fun setPattern() {
        binding.patternRecycler.adapter = adapterPattern
        adapterPattern.longListener = this
        adapterPattern.listener = this
        viewModel.getCategory(Constants.PATTERN_CATEGORY)
            .observe(viewLifecycleOwner, { list ->
                if (list.isNotEmpty()) {
                    adapterPattern.setData(list)
                    listModel = list
                }
            })
        adapter.notifyDataSetChanged()
    }

    private fun getHistory() {
        viewModel.getCategory(Constants.HISTORY_CATEGORY)
            .observe(viewLifecycleOwner, { list ->
                if (list.isNotEmpty()) {
                    adapter.setData(list)
                    listModel = list
                }
            })
        adapter.notifyDataSetChanged()
    }

    private fun initClickers() {
        binding.incomeBtn.setOnClickListener {
            openSheetDialog(Constants.HISTORY_CATEGORY, true)
        }
        binding.expensesBtn.setOnClickListener {
            openSheetDialog(Constants.HISTORY_CATEGORY, false)
        }
        binding.historyCard.setOnClickListener { showHistoryDialog() }
        binding.patternBtn.setOnClickListener { openSheetDialog(Constants.PATTERN_CATEGORY, false) }
        binding.adviceCard.setOnClickListener { showAdviceDialog() }
    }

    private fun showAdviceDialog() {
        val dialog = requireContext().getDialog(R.layout.advice_dialog)
        val titleCard :View= dialog.findViewById(R.id.title_card)
        val title = titleCard.findViewById<TextView>(R.id.title)
        title.text = getString(R.string.advices)
        setAdvice(dialog)
        dialog.show()
    }

    private fun setAdvice(dialog: Dialog) {
        val advicePreference = AdvicePreference(requireContext())
        val advicePosition = advicePreference.getAdvice()
        val adviceTitle = dialog.findViewById<TextView>(R.id.advice_title)
        val adviceSub = dialog.findViewById<TextView>(R.id.advice_sub)
        adviceTitle.text = AdviceText().getTitleAdvice(advicePosition, requireContext())
        adviceSub.text = AdviceText().getDescAdvice(advicePosition, requireContext())
    }

    private fun showHistoryDialog() {
        val dialog = requireContext().getDialog(R.layout.history_dialog)
        val recyclerView: RecyclerView = dialog.findViewById(R.id.history_recycler)
        val title: TextView = dialog.findViewById(R.id.title)
        title.text = "История"
        recyclerView.adapter = adapter
        adapter.longListener = this
        dialog.show()
    }

    private fun openSheetDialog(category: String, isIncome: Boolean, model: FinanceModel? = null) {
        val bottomDialog = FinanceBottomSheetDialog()
        val bundle = Bundle()
        bundle.putBoolean(Constants.IS_INCOME, isIncome)
        bundle.putSerializable(Constants.WORK_WITH_PATTERN, model)
        bottomDialog.arguments = bundle
        bottomDialog.show(requireActivity().supportFragmentManager, category)
    }

    override fun onLongClick(position: Int) {
        binding.patternRecycler.findViewHolderForAdapterPosition(
            position
        )?.itemView?.explosionView(explosionField)
        viewModel.delete(listModel!![position])
        if (position == 0) {
            findNavController().navigate(R.id.financeFragment)
        } else {
            adapter.notifyItemRemoved(position)
        }
    }

    override fun onClick(model: FinanceModel,position:Int) {
        openSheetDialog(Constants.WORK_WITH_PATTERN, false, model)
    }
}