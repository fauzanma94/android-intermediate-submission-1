package com.example.aplikasistoryapp.ui.addstory

import android.app.Activity.RESULT_OK
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.aplikasistoryapp.R
import com.example.aplikasistoryapp.data.ViewModelFactory
import com.example.aplikasistoryapp.data.api.ApiConfig
import com.example.aplikasistoryapp.data.local.store.Preference
import com.example.aplikasistoryapp.data.response.addstory.AddStoryResponse
import com.example.aplikasistoryapp.databinding.FragmentAddStoryBinding
import com.example.aplikasistoryapp.ui.login.LoginViewModel
import com.example.aplikasistoryapp.ui.story.UserViewModel
import com.example.aplikasistoryapp.utils.createCustomTempFile
import com.example.aplikasistoryapp.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddStoryFragment : Fragment() {
    private lateinit var userViewModel: UserViewModel
    private lateinit var binding: FragmentAddStoryBinding
    private var getFile: File? = null
    private lateinit var currentPhotoPath: String
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!allPermissionsGranted()){
            requestPermission()
        }

        configurationViewModel(requireContext())

        binding.galleryButton.setOnClickListener{startGallery()}
        binding.cameraButton.setOnClickListener{startCamera()}
        binding.uploadButton.setOnClickListener{
            uploadImage()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddStoryBinding.inflate(layoutInflater,container,false)
        return binding.root

    }



    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ result ->
        if(result.resultCode == RESULT_OK){
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, requireContext())
                getFile = myFile
                binding.storyImage.setImageURI(uri)

            }
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
//            val imageBitmap = it.data?.extras?.get("data") as Bitmap
//            binding.storyImage.setImageBitmap(imageBitmap)
            val myFile = File(currentPhotoPath)
            myFile.let{file ->
                getFile = file
                binding.storyImage.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }



    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent,"Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun startCamera(){
        val context = requireContext()
        val packageManager: PackageManager = context.packageManager
        val application: Application = context.applicationContext as Application
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                requireContext(),
                "com.example.aplikasistoryapp",
                it
            )

            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun requestPermission(){
        val permission = REQUIRED_PERMISSIONS
        permission.any{
            ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),it)
        }

        val requestPermission = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permission ->
            val granted = permission.all { it.value }
            if(granted){
                Toast.makeText(requireContext(),"Mendapat izin",Toast.LENGTH_SHORT).show()
            } else{
                if(!permission.entries.all {
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        it.key
                    )
                    }
                )  {
                    Toast.makeText(requireContext(),"Tidak mendapatkan izin",Toast.LENGTH_SHORT).show()
                } else{
                    Toast.makeText(requireContext(),"Tidak mendapatkan izin",Toast.LENGTH_SHORT).show()
                }
            }
        }

        requestPermission.launch(permission)


    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun uploadImage(){
        if(getFile != null){
            val file = getFile as File

            val description = binding.description.text.toString().toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            userViewModel.getUser().observe(viewLifecycleOwner){ user ->
                if(user._isLogin){
                    val client = ApiConfig.getApiService().addNewStory(
                        "Bearer ${user.token}",
                        description,
                        imageMultipart
                    )
                    client.enqueue(object : Callback<AddStoryResponse>{
                        override fun onResponse(
                            call: Call<AddStoryResponse>,
                            response: Response<AddStoryResponse>
                        ) {
                            if(response.isSuccessful){
                                val responseBody = response.body()
                                if(responseBody != null && !responseBody.error){
                                    findNavController().navigate(R.id.action_addStoryFragment_to_storyFragment)
                                    Toast.makeText(requireContext(),responseBody.message,Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(requireContext(), response.message(), Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<AddStoryResponse>, t: Throwable) {
                            Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
                        }

                    })
                }
            }
        } else{
            Toast.makeText(requireContext(), "Upload Gambar terlebih dahulu", Toast.LENGTH_SHORT).show()
        }
    }

    private fun configurationViewModel(context: Context){
        val dataStore: DataStore<Preferences> = context.dataStore
        userViewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                Preference
                    .getInstance(dataStore))
        )[UserViewModel::class.java]

    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )

    }
}