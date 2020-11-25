package com.lawlett.planner.ui.main

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat.checkSelfPermission
import com.lawlett.planner.R
import com.lawlett.planner.base.BaseFragment
import com.lawlett.planner.extensions.invisible
import com.lawlett.planner.extensions.loadImage
import com.lawlett.planner.extensions.toastShow
import com.lawlett.planner.extensions.visible
import kotlinx.android.synthetic.main.fragment_create_idea.*

class CreateIdeaFragment : BaseFragment(R.layout.fragment_create_idea) {
    private val IMAGE_CAPTURE_CODE = 1001
    var image_uri: Uri? = null

    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.rotate_open_anim
        )
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.rotate_close_anim
        )
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.from_bottom_anim
        )
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.to_bottom_anim
        )
    }
    private var isClicked = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fabClickers()
        getImageFromGallery()
        getImageFromCamera()
    }
    private fun getImageFromCamera() {
        fab_camera.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(requireContext(),Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(requireContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED){
                    //permission was not enabled
                    val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    //show popup to request permission
                    requestPermissions(permission, PERMISSION_CODE)
                }
                else{
                    //permission already granted
                    openCamera()
                }
            }
            else{
                //system os is < marshmallow
                openCamera()
            }
        }
    }
    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        image_uri = requireContext().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }
    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }
    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000;
        //Permission code
        private val PERMISSION_CODE = 1001;
        //Camera code
        private val PERMISSION_CAMERA = 1002;
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        //called when user presses ALLOW or DENY from Permission Request Popup
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup was granted
                    openCamera()
                }
                else{
                    //permission from popup was denied
requireContext().toastShow("Permission denied")
                }
            }
        }
    }
    private fun getImageFromGallery() {
        fab_image.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED){
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE);
                }
                else{
                    //permission already granted
                    pickImageFromGallery();
                }
            }
            else{
                //system OS is < Marshmallow
                pickImageFromGallery();
            }
        }
        }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            image_create_idea.setImageURI(data?.data)
        }
        else if(resultCode == Activity.RESULT_OK){
            image_create_idea.loadImage(image_uri)
        }
    }
    private fun fabClickers() {
        fab_create.setOnClickListener {
            onAddButtonClicked();
        }
    }
    private fun onAddButtonClicked() {
        setVisibility(isClicked)
        setAnimation(isClicked)
        isClicked = !isClicked
    }
    private fun setAnimation(isClicked: Boolean) {
        if (!isClicked) {
            fab2_container_create.visible();
            fab3_container_create.visible()
        }
        fab2_container_create.invisible();
        fab3_container_create.invisible()
    }
    private fun setVisibility(isClicked: Boolean) {
        if (!isClicked) {
            fab2_container_create.startAnimation(fromBottom)
            fab3_container_create.startAnimation(fromBottom)
            fab_create.startAnimation(rotateOpen)
        } else {
            fab2_container_create.startAnimation(toBottom)
            fab3_container_create.startAnimation(toBottom)
            fab_create.startAnimation(rotateClose)
        }
    }
}
