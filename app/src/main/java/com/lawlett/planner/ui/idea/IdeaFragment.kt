package com.lawlett.planner.ui.idea

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.checkSelfPermission
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.IdeaModel
import com.lawlett.planner.data.room.viewmodels.IdeaViewModel
import com.lawlett.planner.databinding.FragmentIdeaBinding
import com.lawlett.planner.extensions.loadImage
import com.lawlett.planner.ui.adapter.IdeaAdapter
import com.lawlett.planner.ui.base.BaseFragment
import com.lawlett.planner.utils.Const.Constants.IMAGE_PICK_CODE
import com.lawlett.planner.utils.Const.Constants.PERMISSION_CODE
import org.koin.android.ext.android.inject
import java.util.*

class IdeaFragment : BaseFragment<FragmentIdeaBinding>(FragmentIdeaBinding::inflate) {
    private val viewModel by inject<IdeaViewModel>()
    private var imageUri: Uri? = null
    val adapter = IdeaAdapter()

    private lateinit var imageViewIdea: ImageView
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

    private fun insertDataToDataBase(title: String, image: String) {
        val rnd = Random()
        val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        val idea = IdeaModel(title = title, image = image, color = color)
        viewModel.addIdea(idea)
        adapter.notifyDataSetChanged()
    }

    private fun showCreateIdeaDialog() {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.create_idea_custom_diolog)
        val titleEditText: EditText = dialog.findViewById(R.id.editText_create_idea)
        val apply = dialog.findViewById<TextView>(R.id.apply_btn)
        imageViewIdea = dialog.findViewById(R.id.idea_image)
        imageViewIdea.setOnClickListener { getImageFromGallery() }
        apply.setOnClickListener {
            insertDataToDataBase(title = titleEditText.text.toString(), imageUri.toString())
            dialog.cancel()
        }
        dialog.show()
    }

    private fun initClickers(view: FragmentIdeaBinding) {
        view.addQuickBtn.setOnClickListener {
            showCreateIdeaDialog()
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(
            intent,
            IMAGE_PICK_CODE
        )
    }

    private fun getImageFromGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) ==
                PackageManager.PERMISSION_DENIED
            ) {
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                requestPermissions(
                    permissions,
                    PERMISSION_CODE
                );
            } else {
                pickImageFromGallery();
            }
        } else {
            pickImageFromGallery();
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            imageUri = data?.data
            imageViewIdea.setImageURI(data?.data)
        } else if (resultCode == Activity.RESULT_OK) {
            imageViewIdea.loadImage(imageUri)
        }
    }
}