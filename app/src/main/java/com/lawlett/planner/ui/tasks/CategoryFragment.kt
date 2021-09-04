package com.lawlett.planner.ui.tasks

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.CategoryModel
import com.lawlett.planner.data.room.viewmodels.CategoryViewModel
import com.lawlett.planner.databinding.FragmentCategoryBinding
import com.lawlett.planner.extensions.explosionView
import com.lawlett.planner.ui.adapter.CategoryAdapter
import com.lawlett.planner.ui.base.BaseAdapter
import com.lawlett.planner.ui.base.BaseFragment
import com.lawlett.planner.ui.dialog.fragment.CreateCategoryBottomSheetDialog
import com.lawlett.planner.utils.SwipeHelper
import org.koin.android.ext.android.inject

class CategoryFragment : BaseFragment<FragmentCategoryBinding>(FragmentCategoryBinding::inflate),
    BaseAdapter.IBaseAdapterClickListener<CategoryModel> {
    private val viewModel by inject<CategoryViewModel>()
    private val adapter = CategoryAdapter()
    var listModel: List<CategoryModel>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickers()
        initAdapter()
        swipeItem()
    }

    private fun initClickers() {
        binding.addCategoryFab.setOnClickListener { initBottomSheet() }
        backClick()
    }

    private fun getDataFromDataBase() {
        viewModel.getCategoryLiveData().observe(viewLifecycleOwner, { category ->
            if (category.isNotEmpty()) {
                adapter.setData(category)
                listModel = category
            }
        })
    }

    private fun deleteButton(position: Int): SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(
            requireContext(),
            getString(R.string.to_delete),
            14.0f,
            android.R.color.holo_red_light,
            object : SwipeHelper.UnderlayButtonClickListener {
                override fun onClick() {
                    binding.categoryRecycler.findViewHolderForAdapterPosition(
                        position
                    )?.itemView?.explosionView(explosionField)

                    listModel?.get(position)?.let { viewModel.delete(it) }
                    if (position == 0) {
                        findNavController().navigate(R.id.category_fragment)
                    } else {
                        adapter.notifyItemRemoved(position)
                    }
                }
            })
    }

    private fun markAsUnreadButton(position: Int): SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(
            requireContext(),
            getString(R.string.edit),
            14.0f,
            android.R.color.holo_green_light,
            object : SwipeHelper.UnderlayButtonClickListener {
                override fun onClick() {
                    val bundle = Bundle()
                    bundle.putSerializable("model", listModel?.get(position))
                    val bottomDialog = CreateCategoryBottomSheetDialog()
                    bottomDialog.arguments = bundle
                    bottomDialog.show(requireActivity().supportFragmentManager, "TAG")
                }
            })
    }

    private fun swipeItem() {
        val itemTouchHelper = ItemTouchHelper(object : SwipeHelper(binding.categoryRecycler) {
            override fun instantiateUnderlayButton(position: Int): List<UnderlayButton> {
                val buttons: List<UnderlayButton>
                val deleteButton = deleteButton(position)
                val markAsUnreadButton = markAsUnreadButton(position)
                buttons = listOf(deleteButton, markAsUnreadButton)
                return buttons
            }
        })

        itemTouchHelper.attachToRecyclerView(binding.categoryRecycler)
    }

    private fun initBottomSheet() {
        val bottomDialog = CreateCategoryBottomSheetDialog()
        bottomDialog.show(requireActivity().supportFragmentManager, "TAG")
    }

    private fun initAdapter() {
        binding.categoryRecycler.adapter = adapter
        adapter.listener = this
        getDataFromDataBase()
    }

    override fun onClick(model: CategoryModel) {
        openCategory(model)
    }

    private fun openCategory(model: CategoryModel) {
        val pAction: CategoryFragmentDirections.ActionCategoryFragmentToCreateTasksFragment =
            CategoryFragmentDirections.actionCategoryFragmentToCreateTasksFragment(model)
        findNavController().navigate(pAction)
    }
}
