package com.lawlett.planner.ui.fragment.finance

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.lawlett.planner.R
import com.lawlett.planner.callback.CheckListEvent
import com.lawlett.planner.data.room.models.FinanceModel
import com.lawlett.planner.data.room.viewmodels.AchievementViewModel
import com.lawlett.planner.data.room.viewmodels.FinanceViewModel
import com.lawlett.planner.databinding.FragmentFinanceBinding
import com.lawlett.planner.extensions.*
import com.lawlett.planner.ui.adapter.FinanceAdapter
import com.lawlett.planner.ui.adapter.FinancePatternAdapter
import com.lawlett.planner.ui.base.BaseAdapter
import com.lawlett.planner.ui.base.BaseFragment
import com.lawlett.planner.ui.dialog.FinanceBottomSheetDialog
import com.lawlett.planner.utils.*
import com.takusemba.spotlight.Target
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.util.*

class FinanceFragment : BaseFragment<FragmentFinanceBinding>(FragmentFinanceBinding::inflate),
    BaseAdapter.IBaseAdapterClickListener<FinanceModel>,
    FinanceBottomSheetDialog.UpdateBalance, CheckListEvent {
    private var listSize: Int = 0
    val adapter = FinanceAdapter()
    private val adapterPattern = FinancePatternAdapter()
    val viewModel by inject<FinanceViewModel>()
    val achievementViewModel by inject<AchievementViewModel>()
    var listModel: List<FinanceModel>? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickers()
        getHistory()
        setPattern()
        showAdvice()
        setBalance()
        addFalsePatternDataForExample()
        showSpotlight()
    }

    private fun showSpotlight() {
        if (BooleanPreference.getInstance(requireContext())
                ?.getBooleanData(Constants.FINANCE_INSTRUCTION) == false
        ) {

            val view = View(requireContext())
            lifecycleScope.launch {
                delay(1000)
                requireActivity().showSpotlight(
                    lifecycleScope,
                    mapOf(
                        view to getString(R.string.history) + " \n\n\n " + getString(R.string.folow_finance_window) + " \n" + getString(
                            R.string.new_appear_open
                        )
                    ),
                    mapOf(
                        view to getString(R.string.main_fun_in_ex) + " \n " + getString(R.string.template_work)
                    ),
                    mapOf(
                        view to getString(R.string.history_button_ex_te)
                    ),
                    mapOf(
                        view to getString(R.string.hold_card)
                    ),
                )
            }

            BooleanPreference.getInstance(requireContext())
                ?.saveBooleanData(Constants.FINANCE_INSTRUCTION, true)
        }
    }

    private fun addFalsePatternDataForExample() {
        if (BooleanPreference.getInstance(requireContext())
                ?.getBooleanData(Constants.FINANCE_EXAMPLE_DATA) == false
        ) {
            val model = FinanceModel(
                description = getString(R.string.transport),
                category = Constants.PATTERN_CATEGORY,
                amount = 0
            )
            val model2 = FinanceModel(
                description = getString(R.string.products),
                category = Constants.PATTERN_CATEGORY,
                amount = 0
            )
            viewModel.addModel(model)
            viewModel.addModel(model2)
            BooleanPreference.getInstance(requireContext())
                ?.saveBooleanData(Constants.FINANCE_EXAMPLE_DATA, true)
        }
    }

    private fun setPattern() {
        binding.patternRecycler.adapter = adapterPattern
        adapterPattern.listener = this
        adapterPattern.longListenerWithModel = object :
            BaseAdapter.IBaseAdapterLongClickListenerWithModel<FinanceModel> {
            override fun onLongClick(model: FinanceModel, itemView: View, position: Int) {
                showAlertDialogForDelete(itemView, model, position)
            }
        }
        viewModel.getCategory(Constants.PATTERN_CATEGORY)
            .observe(viewLifecycleOwner, { list ->
                if (list.isNotEmpty()) {
                    listSize = list.size
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

    private fun setBalance() {
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
        binding.balanceCard.setOnClickListener { openSheetDialog(Constants.HISTORY_CATEGORY, true) }
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
        adapter.longListenerWithModel =
            object : BaseAdapter.IBaseAdapterLongClickListenerWithModel<FinanceModel> {
                override fun onLongClick(model: FinanceModel, itemView: View, position: Int) {
                    showAlertDialogForDelete(itemView, model, position)
                }
            }
        dialog.show()
    }

    private fun showAlertDialogForDelete(
        itemView: View,
        model: FinanceModel,
        position: Int
    ) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setMessage(getString(R.string.are_you_sure))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                itemView.explosionView(explosionField)
                viewModel.delete(model)
                if (position == 0) {
                    findNavController().navigate(R.id.financeFragment)
                } else {
                    adapter.notifyItemRemoved(position)
                }
            }
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.cancel()
            }
        val alert = dialogBuilder.create()
        alert.setTitle(getString(R.string.attention_alert))
        alert.show()
    }

    private fun openSheetDialog(category: String, isIncome: Boolean, model: FinanceModel? = null) {
        val bottomDialog = FinanceBottomSheetDialog(this, this)
        val bundle = Bundle()
        bundle.putBoolean(Constants.IS_INCOME, isIncome)
        bundle.putSerializable(Constants.WORK_WITH_PATTERN, model)
        bottomDialog.arguments = bundle
        bottomDialog.show(requireActivity().supportFragmentManager, category)
    }

    override fun onClick(model: FinanceModel, position: Int) {
        openSheetDialog(Constants.WORK_WITH_PATTERN, false, model)
    }

    override fun needUpdate() {
        setBalance()
    }

    override fun onStop() {
        super.onStop()
        clearAnimations(achievementView = binding.achievementView)
    }

    override fun check() {
        adapterPattern.notifyDataSetChanged()
        rewardAnAchievement(
            listSize,
            requireActivity(),
            achievementViewModel,
            binding.achievementView
        )
    }
}