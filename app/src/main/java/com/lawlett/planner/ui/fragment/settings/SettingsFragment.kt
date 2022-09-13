package com.lawlett.planner.ui.fragment.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.LanguageModel
import com.lawlett.planner.databinding.FragmentSettingsBinding
import com.lawlett.planner.extensions.changeLanguage
import com.lawlett.planner.extensions.getDialog
import com.lawlett.planner.extensions.getLanguageList
import com.lawlett.planner.ui.adapter.LanguageAdapter
import com.lawlett.planner.ui.base.BaseAdapter
import com.lawlett.planner.ui.base.BaseFragment
import com.lawlett.planner.ui.dialog.ChooseThemeBottomSheetDialog
import com.lawlett.planner.utils.Constants


class SettingsFragment : BaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate),
    BaseAdapter.IBaseAdapterClickListener<LanguageModel> {
    lateinit var reviewInfo: ReviewInfo
    lateinit var manager: ReviewManager
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backToProgress()
        changeTheme()
        showLanguageDialog()
        initClickers()
        activateReview()
    }

    private fun initClickers() {
        binding.shareCard.setOnClickListener { shareApp() }
        binding.rateCard.setOnClickListener { rateApp() }
        binding.instructionCard.setOnClickListener { findNavController().navigate(R.id.action_settingsFragment_to_instructionFragment) }
    }

    private fun languageAdapter() {
        val adapter = LanguageAdapter()
        adapter.listener = this
        adapter.setData(getLanguageList())
        val dialog = requireContext().getDialog(R.layout.language_dialog)
        val recyclerView = dialog.findViewById<RecyclerView>(R.id.history_recycler)
        recyclerView.adapter = adapter
        dialog.show()
    }

    fun showLanguageDialog() {
        binding.changeLanguageLayout.setOnClickListener { languageAdapter() }
    }

    private fun changeTheme() {
        binding.changeThemeButton.setOnClickListener { initBottomSheet() }
    }

    private fun initBottomSheet() {
        val bottomDialog = ChooseThemeBottomSheetDialog()
        bottomDialog.show(requireActivity().supportFragmentManager, "TAG")
    }

    private fun shareApp() {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Planner+")
            var shareMessage = "\nPlanner+\n"
            shareMessage += Constants.APP_LINK
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent.createChooser(shareIntent, getString(R.string.choose_app)))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun rateApp() {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(Constants.APP_LINK)
        startActivity(i)
//        startReviewFlow()
    }

    override fun onClick(model: LanguageModel, position: Int) {
        requireActivity().changeLanguage(position)
    }

    fun startReviewFlow(){
        val flow = manager.launchReviewFlow(requireActivity(),reviewInfo)
        flow.addOnCompleteListener { task->
            Log.e("ololo", "startReviewFlow: rate complete", )
        }
    }

    fun activateReview(){
        manager = ReviewManagerFactory.create(requireContext())
        val managerInfoTask = manager.requestReviewFlow()
        managerInfoTask.addOnCompleteListener {task->
            if (task.isSuccessful){
                reviewInfo = task.result
            }else{
                Log.e("ololo", "activateReview: review failed", )
            }
        }
    }
}