package com.lawlett.planner.ui.idea

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.lawlett.planner.R
import com.lawlett.planner.databinding.FragmentIdeaBinding
import com.lawlett.planner.extensions.loadImage
import com.lawlett.planner.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_create_idea.*

class IdeaFragment : BaseFragment<FragmentIdeaBinding>(FragmentIdeaBinding::inflate) {
    private var imageUri:Uri?=null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickers(binding)
        showCreateIdeaDialog()
    }

    private fun showCreateIdeaDialog() {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.create_idea_custom_diolog)
        val apply = dialog.findViewById(R.id.apply_btn) as TextView
        apply.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun initClickers(view: FragmentIdeaBinding) {
        view.addQuickBtn.setOnClickListener { findNavController().navigate(R.id.action_idea_fragment_to_recordIdeaFragment) }
    }
    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent,
            CreateIdeaFragment.IMAGE_PICK_CODE
        )
    }
    private fun getImageFromGallery() {
        fab_image.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) ==
                    PackageManager.PERMISSION_DENIED
                ) {
                        val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    requestPermissions(permissions,
                        CreateIdeaFragment.PERMISSION_CODE
                    );
                } else {
                    pickImageFromGallery();
                }
            } else {
                pickImageFromGallery();
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == CreateIdeaFragment.IMAGE_PICK_CODE) {
            image_create_idea.setImageURI(data?.data)
        } else if (resultCode == Activity.RESULT_OK) {
            image_create_idea.loadImage(imageUri)
        }
    }

}