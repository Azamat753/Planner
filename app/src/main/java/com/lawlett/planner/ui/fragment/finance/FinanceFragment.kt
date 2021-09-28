package com.lawlett.planner.ui.fragment.finance

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
import com.lawlett.planner.ui.dialog.FinanceBottomSheetDialog
import com.lawlett.planner.utils.*
import org.koin.android.ext.android.inject

class FinanceFragment : BaseFragment<FragmentFinanceBinding>(FragmentFinanceBinding::inflate),
    BaseAdapter.IBaseAdapterLongClickListener, BaseAdapter.IBaseAdapterClickListener<FinanceModel>,
    FinanceBottomSheetDialog.UpdateBalance {
    val adapter = FinanceAdapter()
    private val adapterPattern = FinancePatternAdapter()
    val viewModel by inject<FinanceViewModel>()
    var listModel: List<FinanceModel>? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickers()
        getHistory()
        setPattern()
        showAdvice()
        setBalance()
        addFalsePatternDataForExample()
    }

    private fun addFalsePatternDataForExample() {
        if (BooleanPreference.getInstance(requireContext())
                ?.getBooleanData(Constants.FINANCE_EXAMPLE_DATA) == false
        ) {
            val model = FinanceModel(
                description = "Транпорт",
                category = Constants.PATTERN_CATEGORY,
                amount = 770
            )
            val model2 = FinanceModel(
                description = "Продукты",
                category = Constants.PATTERN_CATEGORY,
                amount = 1320
            )
            viewModel.addModel(model)
            viewModel.addModel(model2)
            BooleanPreference.getInstance(requireContext())
                ?.saveBooleanData(Constants.FINANCE_EXAMPLE_DATA, true)
        }
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

    fun setBalance() {
        val income: Int = IntPreference.getInstance(requireContext())?.getInt(Constants.INCOME) ?: 0
        val expensive: Int =
            IntPreference.getInstance(requireContext())?.getInt(Constants.EXPENSIVE) ?: 0
        val balance: Int = income - expensive
        binding.balanceAmountTv.text = balance.toString()
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
    }

    private fun showAdvice() {
        val advicePreference = AdvicePreference(requireContext())
        val advicePosition = advicePreference.getAdvice()
        binding.adviceTitle.text = AdviceText().getTitleAdvice(advicePosition, requireContext())
        binding.adviceSub.text = AdviceText().getDescAdvice(advicePosition, requireContext())
    }

    private fun showHistoryDialog() {
        val dialog = requireContext().getDialog(R.layout.history_dialog)
        val recyclerView: RecyclerView = dialog.findViewById(R.id.history_recycler)
        val title: TextView = dialog.findViewById(R.id.title)
        title.text = getString(R.string.history)
        recyclerView.adapter = adapter
        adapter.longListener = this
        dialog.show()
    }

    private fun openSheetDialog(category: String, isIncome: Boolean, model: FinanceModel? = null) {
        val bottomDialog = FinanceBottomSheetDialog(this)
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

    override fun onClick(model: FinanceModel, position: Int) {
        openSheetDialog(Constants.WORK_WITH_PATTERN, false, model)
    }

    override fun needUpdate() {
        setBalance()
    }
}