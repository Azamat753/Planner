package com.lawlett.planner.ui.dialog

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.IdeaModel
import com.lawlett.planner.data.room.viewmodels.IdeaViewModel
import com.lawlett.planner.databinding.CreateIdeaBottomSheetBinding
import com.lawlett.planner.extensions.loadImage
import com.lawlett.planner.ui.adapter.IdeaAdapter
import com.lawlett.planner.ui.base.BaseBottomSheetDialog
import com.lawlett.planner.utils.Constants
import com.lawlett.planner.utils.StringPreference
import org.koin.android.ext.android.inject
import java.util.*

class CreateIdeaBottomSheetDialog :
    BaseBottomSheetDialog<CreateIdeaBottomSheetBinding>(CreateIdeaBottomSheetBinding::inflate) {
    private var imageUri: String? = null
    private val viewModel by inject<IdeaViewModel>()
    val adapter = IdeaAdapter()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickers()
        fillFields()
    }

    private fun initClickers() {
        binding.chooseImageButton.setOnClickListener { getImageFromGallery() }
        binding.applyButton.setOnClickListener {
            insertDataToDataBase()
        }
    }

    private fun fillFields() {
        if (isUpdate()) {
            val model: IdeaModel = arguments?.getSerializable(Constants.UPDATE_MODEL) as IdeaModel
            binding.image.loadImage(model.image)
            binding.titleEditText.setText(model.title)
            imageUri= model.image
        }
    }

    private fun isUpdate(): Boolean {
        return tag.toString() == Constants.UPDATE_MODEL
    }

    private fun insertDataToDataBase() {
        val rnd = Random()
        val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        val title = binding.titleEditText.text.toString().trim()
        when {
            title.isEmpty() -> {
                binding.titleEditText.error = getString(R.string.fill_field)
            }
            imageUri == null -> {
                binding.chooseImageButton.error = getString(R.string.choose_image)
            }
            else -> {
                if (isUpdate()) {
                    val model: IdeaModel =
                        arguments?.getSerializable(Constants.UPDATE_MODEL) as IdeaModel

                    val updateModel = IdeaModel(
                        title = title,
                        image = imageUri!!,
                        id = model.id,
                        color = model.color
                    )
                    viewModel.update(updateModel)
                } else {
                    val idea = IdeaModel(title = title, image = imageUri.toString(), color = color)
                    viewModel.addIdea(idea)
                }
                adapter.notifyDataSetChanged()
                dismiss()
            }
        }
    }


    private fun getImageFromGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) ==
                PackageManager.PERMISSION_DENIED
            ) {
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(
                    permissions,
                    Constants.PERMISSION_CODE
                )
            } else {
                pickImageFromGallery()
            }
        } else {
            pickImageFromGallery()
        }
    }

    private fun pickImageFromGallery() {
        getContent.launch("image/*")
    }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri.toString()
            binding.image.loadImage(imageUri.toString())
        }
}