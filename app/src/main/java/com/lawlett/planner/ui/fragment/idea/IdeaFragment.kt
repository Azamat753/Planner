package com.lawlett.planner.ui.fragment.idea

import android.os.Bundle
import android.view.View
import com.lawlett.planner.data.room.viewmodels.IdeaViewModel
import com.lawlett.planner.databinding.FragmentIdeaBinding
import com.lawlett.planner.ui.adapter.IdeaAdapter
import com.lawlett.planner.ui.base.BaseFragment
import com.lawlett.planner.ui.dialog.CreateIdeaBottomSheetDialog
import org.koin.android.ext.android.inject

class IdeaFragment : BaseFragment<FragmentIdeaBinding>(FragmentIdeaBinding::inflate) {
    private val viewModel by inject<IdeaViewModel>()
    val adapter = IdeaAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickers(binding)
        initAdapter()
    }

    private fun initAdapter() {
        binding.ideaRecycler.adapter = adapter
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
}