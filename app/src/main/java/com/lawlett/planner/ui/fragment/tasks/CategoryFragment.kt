package com.lawlett.planner.ui.fragment.tasks

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.CategoryModel
import com.lawlett.planner.data.room.models.EventModel
import com.lawlett.planner.data.room.models.HabitModel
import com.lawlett.planner.data.room.viewmodels.AchievementViewModel
import com.lawlett.planner.data.room.viewmodels.CategoryViewModel
import com.lawlett.planner.data.room.viewmodels.EventViewModel
import com.lawlett.planner.data.room.viewmodels.HabitViewModel
import com.lawlett.planner.databinding.FragmentCategoryBinding
import com.lawlett.planner.extensions.*
import com.lawlett.planner.ui.adapter.CategoryAdapter
import com.lawlett.planner.ui.base.BaseAdapter
import com.lawlett.planner.ui.base.BaseFragment
import com.lawlett.planner.ui.dialog.ChooseThemeBottomSheetDialog
import com.lawlett.planner.ui.dialog.CreateCategoryBottomSheetDialog
import com.lawlett.planner.ui.dialog.SetPasswordBottomSheetDialog
import com.lawlett.planner.utils.BooleanPreference
import com.lawlett.planner.utils.Constants
import com.lawlett.planner.utils.StringPreference
import com.takusemba.spotlight.Target
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.util.*

class CategoryFragment : BaseFragment<FragmentCategoryBinding>(FragmentCategoryBinding::inflate),
    BaseAdapter.IBaseAdapterClickListener<CategoryModel>,
    BaseAdapter.IBaseAdapterLongClickListenerWithModel<CategoryModel> {
    private val viewModel by inject<CategoryViewModel>()
    private val adapter = CategoryAdapter()
    private var listModel: List<CategoryModel>? = null
    private val habitViewModel by inject<HabitViewModel>()
    private val categoryViewModel by inject<CategoryViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickers()
        initAdapter()
        spotlightOrThemeDialog()
        addFalseDataForExample()
    }

    private fun showPassDialog() {
        BooleanPreference.getInstance(requireContext())
            ?.saveBooleanData(Constants.PASS_INSTRUCTION_SHOWED, true)
        val dialog = requireContext().getDialog(R.layout.pass_instruction_dialog)
        val title = dialog.findViewById<TextView>(R.id.title)
        title.text = getString(R.string.pass_instruction)
        val continueBtn = dialog.findViewById<MaterialButton>(R.id.continue_button)
        val passBtn = dialog.findViewById<MaterialButton>(R.id.pass_button)
        continueBtn.text = getString(R.string.yes)
        continueBtn.setOnClickListener {
            showSpotlight()
            dialog.dismiss()
        }
        passBtn.setOnClickListener {
            BooleanPreference.getInstance(requireContext())
                ?.saveBooleanData(Constants.PASS_INSTRUCTION_SKIP, true)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun spotlightOrThemeDialog() {
        if (BooleanPreference.getInstance(requireContext())
                ?.getBooleanData(Constants.THEME_SELECTED) == false
        ) {
            BooleanPreference.getInstance(requireContext())
                ?.saveBooleanData(Constants.THEME_SELECTED, true)
            openThemeDialog()
        } else {
            if (BooleanPreference.getInstance(requireContext())
                    ?.getBooleanData(Constants.PASS_INSTRUCTION_SHOWED) == false
            ) {
                showPassDialog()
            }
        }
    }

    private fun openThemeDialog() {
        val bottomDialog = ChooseThemeBottomSheetDialog()
        bottomDialog.show(requireActivity().supportFragmentManager, "TAG")
    }

    private fun addFalseDataForExample() {
        if (BooleanPreference.getInstance(requireContext())
                ?.getBooleanData(Constants.PROGRESS_EXAMPLE_DATA) == false
        ) {
            val habitModel = HabitModel(
                title = getString(R.string.wake_up_morning),
                currentDay = 0,
                allDays = "21",
                icon = "\uD83D\uDE06"
            )
            val habitModel2 =
                HabitModel(
                    title = getString(R.string.read_by_minutes),
                    currentDay = 0,
                    allDays = "30",
                    icon = "⏳"
                )

            val categoryModel = CategoryModel(
                categoryName = getString(R.string.health),
                categoryIcon = "\uD83C\uDF4E",
                taskAmount = 0,
                doneTaskAmount = 0
            )
            val categoryModel2 = CategoryModel(
                categoryName = getString(R.string.targets),
                categoryIcon = "\uD83C\uDFAF",
                taskAmount = 0,
                doneTaskAmount = 0
            )

            habitViewModel.insertData(habitModel)
            habitViewModel.insertData(habitModel2)
            categoryViewModel.addCategory(categoryModel)
            categoryViewModel.addCategory(categoryModel2)
            BooleanPreference.getInstance(requireContext())
                ?.saveBooleanData(Constants.PROGRESS_EXAMPLE_DATA, true)
        }
    }

    private fun showSpotlight() {
        if (BooleanPreference.getInstance(requireContext())
                ?.getBooleanData(Constants.CATEGORY_INSTRUCTION) == false
        ) {
            val view = View(requireContext())
            lifecycleScope.launch {
                delay(1000)
                requireActivity().showSpotlight(
                    lifecycleScope,
                    mapOf(
                        view to getString(R.string.before_start) + "\n " + getString(
                            R.string.two_tools
                        ) + "\n " + getString(
                            R.string.bottom_and_side
                        ) + " \n " + getString(R.string.side_open_by_click) + "\n " + getString(
                            R.string.after_instruction
                        ) + "\n " + getString(
                            R.string.go
                        )
                    ),
                    mapOf(
                        view to "\n\n\n " + getString(R.string.categories) + "\n\n\n " + getString(
                            R.string.inside_cards_record
                        ) + " \n " + getString(
                            R.string.hold_card
                        )
                    ),
                    mapOf(
                        view to getString(R.string.show_profile) + " \n " + getString(
                            R.string.avater_name
                        ) + " \n " + getString(
                            R.string.lvlup_some_action
                        )
                    ),
                    mapOf(
                        binding.addCategoryFab to getString(R.string.insert_button) + " \n " + getString(
                            R.string.create_new_category_click
                        )
                    )
                )
            }
            BooleanPreference.getInstance(requireContext())
                ?.saveBooleanData(Constants.CATEGORY_INSTRUCTION, true)
        }
    }

    private fun initClickers() {
        binding.addCategoryFab.setOnClickListener { initBottomSheet() }
        backToProgress()
    }

    private fun getDataFromDataBase() {
        viewModel.getCategoryLiveData().observe(viewLifecycleOwner) { category ->
            if (category.isNotEmpty()) {
                adapter.setData(category)
                listModel = category
            }
            val list = adapter.getData()
            if (list.isNotEmpty()) {
                binding.textNooruz.visibility = View.GONE
                binding.imageNooruz.visibility = View.GONE
            } else {
                binding.textNooruz.visibility = View.VISIBLE
                binding.imageNooruz.visibility = View.VISIBLE
            }
        }
    }

    private fun initBottomSheet() {
        val bottomDialog = CreateCategoryBottomSheetDialog()
        bottomDialog.show(requireActivity().supportFragmentManager, "TAG")
    }

    private fun initAdapter() {
        binding.categoryRecycler.adapter = adapter
        adapter.listener = this
        adapter.longListenerWithModel = this
        getDataFromDataBase()
    }

    override fun onClick(model: CategoryModel, position: Int) {
        if (model.isBlock) {
            passPassword(Constants.OPEN_CATEGORY, model)
        } else {
            openCategory(model)
        }
    }

    private fun passPassword(action: String, model: CategoryModel) {
        val dialog = requireContext().getDialog(R.layout.password_layout)
        val editText = dialog.findViewById<TextInputEditText>(R.id.editText_password)
        val wordEditText = dialog.findViewById<TextInputEditText>(R.id.editText_word)
        val wordWrapper = dialog.findViewById<TextInputLayout>(R.id.word_wrapper)
        val apply: Button = dialog.findViewById(R.id.apply_btn)
        val forgotPassword: Button = dialog.findViewById(R.id.forgot_password)

        val secretWord =
            StringPreference.getInstance(requireContext())?.getStringData(Constants.SECRET_WORD)

        val password = StringPreference.getInstance(requireContext())
            ?.getStringData(Constants.CATEGORY_PASSWORD)

        forgotPassword.setOnClickListener {
            wordWrapper.visible()
        }

        apply.setOnClickListener {
            val passwordInputUser = editText.text.toString().trim()
            val secretWordInputUser = wordEditText.text.toString().trim()

            if (wordWrapper.isVisible) {
                if (secretWordInputUser == secretWord) {
                    doByAction(action, model)
                    dialog.dismiss()
                } else {
                    wordEditText.error = getString(R.string.not_correct)
                }
            } else {
                if (passwordInputUser == password) {
                    doByAction(action, model)
                    dialog.dismiss()
                } else {
                    editText.error = getString(R.string.wrong_password)
                }
            }
        }
        dialog.show()
    }

    private fun doByAction(
        action: String,
        model: CategoryModel
    ) {
        when (action) {
            Constants.OPEN_CATEGORY -> {
                openCategory(model)
            }
            Constants.UNBLOCK_CATEGORY -> {
                setCategoryUnblock(model)
            }
            Constants.CHANGE_PASSWORD_CATEGORY -> {
                openPasswordSheetDialog(model)
            }
        }
    }

    private fun setCategoryUnblock(model: CategoryModel) {
        val updateModel = CategoryModel(
            id = model.id,
            categoryIcon = model.categoryIcon,
            categoryName = model.categoryName,
            taskAmount = model.taskAmount,
            isBlock = false
        )
        viewModel.update(updateModel)
    }

    private fun openCategory(model: CategoryModel) {
        val p =
            CategoryFragmentDirections.actionCategoryFragmentToCreateTasksFragment(model, false)
        findNavController().navigate(p)
    }

    override fun onLongClick(model: CategoryModel, itemView: View, position: Int) {
        showActionDialog(model, position)
    }

    private fun showActionDialog(
        model: CategoryModel,
        position: Int
    ) {
        val dialog = requireContext().getDialog(R.layout.long_click_dialog)
        val delete = dialog.findViewById<TextView>(R.id.delete_button)
        val edit = dialog.findViewById<TextView>(R.id.edit_button)
        val block = dialog.findViewById<Button>(R.id.third_button)
        val changePassword = dialog.findViewById<Button>(R.id.four_button)
        val unBlock = dialog.findViewById<Button>(R.id.five_button)
        val password = StringPreference.getInstance(requireContext())
            ?.getStringData(Constants.CATEGORY_PASSWORD)
        block.text = getString(R.string.block)

        if (model.isBlock) {
            changePassword.visible()
            unBlock.visible()
        } else {
            block.visible()
        }

        unBlock.setOnClickListener {
            passPassword(Constants.UNBLOCK_CATEGORY, model)
            dialog.dismiss()
        }


        changePassword.setOnClickListener {
            passPassword(Constants.CHANGE_PASSWORD_CATEGORY, model)
            dialog.dismiss()
        }

        delete.setOnClickListener {
            deleteCategory(position, model)
            dialog.dismiss()
        }

        edit.setOnClickListener {
            editCategory(position)
            dialog.dismiss()
        }

        block.setOnClickListener {
            blockCategory(password, model, dialog)
        }
        dialog.show()
    }

    private fun blockCategory(
        password: String?,
        model: CategoryModel,
        dialog: Dialog
    ) {
        if (TextUtils.isEmpty(password)) {
            openPasswordSheetDialog(model)
        } else {
            val updateModel = CategoryModel(
                id = model.id,
                categoryIcon = model.categoryIcon,
                categoryName = model.categoryName,
                taskAmount = model.taskAmount,
                isBlock = true
            )
            viewModel.update(updateModel)
        }
        dialog.dismiss()
    }

    private fun openPasswordSheetDialog(model: CategoryModel) {
        val bottomDialog = SetPasswordBottomSheetDialog()
        val bundle = Bundle()
        bundle.putSerializable(Constants.UPDATE_MODEL, model)
        bottomDialog.arguments = bundle
        bottomDialog.show(requireActivity().supportFragmentManager, "TAG")
    }

    private fun deleteCategory(
        position: Int,
        model: CategoryModel
    ) {
        binding.categoryRecycler.findViewHolderForAdapterPosition(
            position
        )?.itemView?.explosionView(explosionField)
        viewModel.delete(model)
        if (position == 0) {
            findNavController().navigate(R.id.category_fragment)
        } else {
            adapter.notifyItemRemoved(position)
        }
    }

    private fun editCategory(position: Int) {
        val bundle = Bundle()
        bundle.putSerializable(Constants.CATEGORY_MODEL, listModel?.get(position))
        val bottomDialog = CreateCategoryBottomSheetDialog()
        bottomDialog.arguments = bundle
        bottomDialog.show(
            requireActivity().supportFragmentManager,
            Constants.UPDATE_MODEL
        )
    }
}
