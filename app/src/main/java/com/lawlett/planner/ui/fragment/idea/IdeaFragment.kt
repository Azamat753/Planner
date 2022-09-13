package com.lawlett.planner.ui.fragment.idea

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import com.lawlett.planner.R
import com.lawlett.planner.callback.CheckListEvent
import com.lawlett.planner.data.room.models.IdeaModel
import com.lawlett.planner.data.room.viewmodels.AchievementViewModel
import com.lawlett.planner.data.room.viewmodels.IdeaViewModel
import com.lawlett.planner.databinding.FragmentIdeaBinding
import com.lawlett.planner.extensions.*
import com.lawlett.planner.ui.adapter.IdeaAdapter
import com.lawlett.planner.ui.base.BaseAdapter
import com.lawlett.planner.ui.base.BaseFragment
import com.lawlett.planner.ui.dialog.CreateIdeaBottomSheetDialog
import com.lawlett.planner.utils.BooleanPreference
import com.lawlett.planner.utils.Constants
import com.takusemba.spotlight.Target
import org.koin.android.ext.android.inject
import java.util.*


class IdeaFragment : BaseFragment<FragmentIdeaBinding>(FragmentIdeaBinding::inflate),
    BaseAdapter.IBaseAdapterLongClickListenerWithModel<IdeaModel>, CheckListEvent,
    BaseAdapter.IBaseAdapterClickListener<IdeaModel> {
    private var listSize: Int = 0
    private val viewModel by inject<IdeaViewModel>()
    private val achievementViewModel by inject<AchievementViewModel>()
    val adapter = IdeaAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickers(binding)
        initAdapter()
        addFalseDataForExample()
        showSpotlight()
    }

    private fun showSpotlight() {
//        if (BooleanPreference.getInstance(requireContext())
//                ?.getBooleanData(Constants.IDEA_INSTRUCTION) == false
//        ) {
//            val targets = ArrayList<Target>()
//            val root = FrameLayout(requireContext())
//            val first = layoutInflater.inflate(R.layout.layout_target, root)
//            val view: View = View(requireContext())
//            Handler().postDelayed({
//                val firstSpot = setSpotLightTarget(
//                    view,
//                    first,
//                    getString(R.string.ideas) + " \n\n\n" +
//                            getString(R.string.list_ideas) +
//                            "\n " +
//                            getString(R.string.vis_im_idea) +
//                            " \n " +
//                            getString(R.string.by_cl_im_sh) +
//                            "\n " +
//                            getString(R.string.hold_card)
//                )
//                val secondSpot = setSpotLightTarget(
//                    binding.addIdeaBtn,
//                    first,
//                    getString(R.string.insert_button) + " \n " +
//                            getString(R.string.cl_ca_re_id)
//                )
//                targets.add(firstSpot)
//                targets.add(secondSpot)
//                setSpotLightBuilder(requireActivity(), targets, first)
//            }, 100)
//            BooleanPreference.getInstance(requireContext())
//                ?.saveBooleanData(Constants.IDEA_INSTRUCTION, true)
//        }
    }

    private fun addFalseDataForExample() {
        if (BooleanPreference.getInstance(requireContext())
                ?.getBooleanData(Constants.IDEA_EXAMPLE_DATA) == false
        ) {
            val model = IdeaModel(
                image = getURLForResource(R.drawable.new_theme),
                title = getString(R.string.for_next_lesson),
                color = R.color.yellow_theme
            )
            val model2 = IdeaModel(
                image = getURLForResource(R.drawable.implement_fun),
                title = getString(R.string.implement_new_fun),
                color = R.color.red_theme
            )
            viewModel.addIdea(model)
            viewModel.addIdea(model2)
            BooleanPreference.getInstance(requireContext())
                ?.saveBooleanData(Constants.IDEA_EXAMPLE_DATA, true)
        }
    }

    private fun getURLForResource(resourceId: Int): String {
        return Uri.parse(
            "android.resource://" + R::class.java.getPackage().name + "/" + resourceId
        ).toString()
    }

    private fun initAdapter() {
        binding.ideaRecycler.adapter = adapter
        adapter.longListenerWithModel = this
        adapter.listener = this
        getDataFromDataBase()
    }

    private fun getDataFromDataBase() {
        viewModel.getIdeasLiveData()
            .observe(viewLifecycleOwner, { ideas ->
                if (ideas.isNotEmpty()) {
                    listSize = ideas.size
                    adapter.setData(ideas)
                }
            })
    }

    private fun initClickers(view: FragmentIdeaBinding) {
        view.addIdeaBtn.setOnClickListener {
            initBottomSheetDialog()
        }
        backToProgress()
    }

    private fun initBottomSheetDialog() {
        val bottomDialog = CreateIdeaBottomSheetDialog(this)
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
            val bottomDialog = CreateIdeaBottomSheetDialog(this)
            bundle.putSerializable(Constants.UPDATE_MODEL, model)
            bottomDialog.arguments = bundle
            bottomDialog.show(requireActivity().supportFragmentManager, Constants.UPDATE_MODEL)
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onClick(model: IdeaModel, position: Int) {
        val dialog = requireContext().getDialog(R.layout.image_dialog)
        val image: ImageView = dialog.findViewById(R.id.image_idea)
        image.loadImage(model.image)
        image.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    override fun onStop() {
        super.onStop()
        clearAnimations(achievementView = binding.achievementView)
    }

    override fun check() {
        adapter.notifyDataSetChanged()
        rewardAnAchievement(listSize,requireActivity(),achievementViewModel,binding.achievementView)
    }
}