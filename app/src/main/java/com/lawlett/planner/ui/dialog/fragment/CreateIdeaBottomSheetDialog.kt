package com.lawlett.planner.ui.dialog.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.IdeaModel
import com.lawlett.planner.data.room.viewmodels.IdeaViewModel
import com.lawlett.planner.databinding.CreateIdeaBottomSheetBinding
import com.lawlett.planner.extensions.loadImage
import com.lawlett.planner.ui.adapter.IdeaAdapter
import com.lawlett.planner.ui.base.BaseBottomSheetDialog
import com.lawlett.planner.utils.Const
import org.koin.android.ext.android.inject
import java.util.*

class CreateIdeaBottomSheetDialog :
    BaseBottomSheetDialog<CreateIdeaBottomSheetBinding>(CreateIdeaBottomSheetBinding::inflate) {
    private var imageUri: Uri? = null
    private val viewModel by inject<IdeaViewModel>()
    val adapter = IdeaAdapter()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickers()
    }

    private fun initClickers() {
        binding.chooseImageButton.setOnClickListener { getImageFromGallery() }
        binding.applyButton.setOnClickListener {
            insertDataToDataBase()
        }
    }

    private fun insertDataToDataBase() {
        val rnd = Random()
        val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        val title = binding.titleEditText.text.toString()
        when {
            title.isEmpty() -> {
                binding.titleEditText.error = getString(R.string.fill_field)
            }
            imageUri == null -> {
                binding.chooseImageButton.error = getString(R.string.choose_image)
            }
            else -> {
                val idea = IdeaModel(title = title, image = imageUri.toString(), color = color)
                viewModel.addIdea(idea)
                adapter.notifyDataSetChanged()
                dismiss()
            }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(
            intent,
            Const.Constants.IMAGE_PICK_CODE
        )
    }

    private fun getImageFromGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) ==
                PackageManager.PERMISSION_DENIED
            ) {
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                requestPermissions(
                    permissions,
                    Const.Constants.PERMISSION_CODE
                );
            } else {
                pickImageFromGallery();
            }
        } else {
            pickImageFromGallery();
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == Const.Constants.IMAGE_PICK_CODE) {
            imageUri = data?.data
            binding.image.setImageURI(data?.data)
        } else if (resultCode == Activity.RESULT_OK) {
            binding.image.loadImage(imageUri)
        }
    }
}