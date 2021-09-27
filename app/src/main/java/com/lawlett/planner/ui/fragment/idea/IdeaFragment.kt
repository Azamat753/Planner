package com.lawlett.planner.ui.fragment.idea

import android.os.Bundle
import android.view.View
import android.widget.Button
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.IdeaModel
import com.lawlett.planner.data.room.viewmodels.IdeaViewModel
import com.lawlett.planner.databinding.FragmentIdeaBinding
import com.lawlett.planner.extensions.explosionView
import com.lawlett.planner.extensions.getDialog
import com.lawlett.planner.ui.adapter.IdeaAdapter
import com.lawlett.planner.ui.base.BaseAdapter
import com.lawlett.planner.ui.base.BaseFragment
import com.lawlett.planner.ui.dialog.CreateIdeaBottomSheetDialog
import com.lawlett.planner.utils.Constants
import org.koin.android.ext.android.inject

class IdeaFragment : BaseFragment<FragmentIdeaBinding>(FragmentIdeaBinding::inflate),
    BaseAdapter.IBaseAdapterLongClickListenerWithModel<IdeaModel> {
    private val viewModel by inject<IdeaViewModel>()
    val adapter = IdeaAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickers(binding)
        initAdapter()
    }

    private fun initAdapter() {
        binding.ideaRecycler.adapter = adapter
        adapter.longListenerWithModel = this
        getDataFromDataBase()
    }

    private fun getDataFromDataBase() {
        viewModel.getIdeasLiveData()
            .observe(viewLifecycleOwner, { ideas ->
                if (ideas.isNotEmpty()) {
                    adapter.setData(ideas)
                }
            })
    }

    private fun initClickers(view: FragmentIdeaBinding) {
        view.addQuickBtn.setOnClickListener {
            initBottomSheetDialog()
        }
        backToProgress()
    }

    private fun initBottomSheetDialog() {
        val bottomDialog = CreateIdeaBottomSheetDialog()
        bottomDialog.show(requireActivity().supportFragmentManager, "TAG")
    }

    override fun onLongClick(model: IdeaModel, itemView: View, position: Int) {
        val dialog = requireContext().getDialog(R.layout.long_click_dialog)
        val delete: Button = dialog.findViewById(R.id.delete_button)
        val edit: Button = dialog.findViewById(R.id.edit_button)
        delete.setOnClickListener {
            itemView.explosionView(explosionField)
            viewModel.delete(model)
            dialog.dismiss()
        }
        edit.setOnClickListener {
            val bundle = Bundle()
            val bottomDialog = CreateIdeaBottomSheetDialog()
            bundle.putSerializable(Constants.UPDATE_MODEL,model)
            bottomDialog.arguments = bundle
            bottomDialog.show(requireActivity().supportFragmentManager, Constants.UPDATE_MODEL)
            dialog.dismiss()
        }
        dialog.show()
    }
}