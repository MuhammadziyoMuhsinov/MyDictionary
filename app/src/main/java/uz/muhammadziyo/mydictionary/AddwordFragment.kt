package uz.muhammadziyo.mydictionary

import android.Manifest
import android.R
import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import uz.muhammadziyo.mydictionary.databinding.ActivityMainBinding
import uz.muhammadziyo.mydictionary.databinding.FragmentAddwordBinding
import uz.muhammadziyo.mydictionary.databinding.ItemDialogPhotoBinding
import uz.muhammadziyo.mydictionary.db.MyDbHelper
import uz.muhammadziyo.mydictionary.entity.MyCategory
import uz.muhammadziyo.mydictionary.entity.Word
import uz.muhammadziyo.mydictionary.utils.Mydata
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AddwordFragment : Fragment() {

    lateinit var binding: FragmentAddwordBinding
    lateinit var activityMain: ActivityMainBinding
    lateinit var myDbHelper: MyDbHelper
    var imagePath: String? = null
    private lateinit var list: ArrayList<MyCategory>
    lateinit var currentImagePath: String
    lateinit var photoUri: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddwordBinding.inflate(layoutInflater)
        activityMain = ActivityMainBinding.inflate(layoutInflater)
        myDbHelper = MyDbHelper(binding.root.context)
        list = ArrayList()
        list.addAll(myDbHelper.getAllCategory())

        val list2 = ArrayList<String>()


        binding.txtSettings.text = Mydata.checkTitle
        list.forEach {
            list2.add(it.name.toString())
        }
        binding.mySpinner.adapter = ArrayAdapter<String>(
            binding.root.context,
            R.layout.simple_expandable_list_item_1,
            list2
        )






        binding.cancel.setOnClickListener {
            findNavController().popBackStack()
        }

        if (Mydata.checkTitle == "So'z tahrirlash") {
            binding.mySpinner.setSelection(list.indexOf(Mydata.word?.category))
            binding.imageView.setImageURI(Uri.parse(Mydata.word?.imagePath))
            binding.word.setText(Mydata.word?.name)
            imagePath = Mydata.word?.imagePath
            binding.translation.setText(Mydata.word?.translation)

            binding.save.setOnClickListener {
                Mydata.word?.name = binding.word.text.toString()
                Mydata.word?.translation = binding.translation.text.toString()
                Mydata.word?.imagePath = imagePath
                Mydata.word?.category = list[binding.mySpinner.selectedItemPosition]
                if (binding.word.text.toString() == "" || binding.translation.text.toString() == "") {
                    Toast.makeText(binding.root.context, "empty", Toast.LENGTH_SHORT).show()
                } else {
                    myDbHelper.updateWord(Mydata.word!!)
                    Toast.makeText(binding.root.context, "Updated", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
            }

        } else {
            binding.save.setOnClickListener {
                val image = imagePath
                val categoryId = list[binding.mySpinner.selectedItemPosition]
                val name = binding.word.text.toString()
                val translation = binding.translation.text.toString()
                if (image == null || name == "" || translation == "") {
                    Toast.makeText(binding.root.context, "empty", Toast.LENGTH_SHORT).show()
                } else {
                    myDbHelper.addWord(Word(name, translation, 0, categoryId, image))
                    Toast.makeText(binding.root.context, "added", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }


            }
        }

        binding.imageView.setOnClickListener {
            val alertDialog = AlertDialog.Builder(binding.root.context).create()
            val itemDialog = ItemDialogPhotoBinding.inflate(layoutInflater)
            alertDialog.setView(itemDialog.root)
            alertDialog.show()
            itemDialog.galereya.setOnClickListener {
                getImageContent.launch("image/*")
                alertDialog.cancel()
            }
            itemDialog.kamera.setOnClickListener {
                val imageFile = createImageFile()
                photoUri = FileProvider.getUriForFile(
                    binding.root.context,
                    BuildConfig.APPLICATION_ID,
                    imageFile
                )
                getImageContent2.launch(photoUri)
                alertDialog.cancel()
            }

        }


        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    private val getImageContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) {
            it ?: return@registerForActivityResult
            binding.imageView.setImageURI(it)
            val inputStream = requireActivity().contentResolver?.openInputStream(it)
            val title = SimpleDateFormat("yyyyMMdd_hhmmss").format(Date())
            val file = File(requireActivity().filesDir, "$title.jpg")
            val fileOutputStream = FileOutputStream(file)
            inputStream?.copyTo(fileOutputStream)
            inputStream?.close()
            fileOutputStream.close()
            imagePath = file.absolutePath


        }

    private fun createImageFile(): File {
        val format = SimpleDateFormat("yyMMdd_HHmmss").format(Date())
        val filesDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_$format", ".jpg", filesDir).apply {
            currentImagePath = absolutePath
        }
    }

    val getImageContent2 = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            binding.imageView.setImageURI(photoUri)
            val date = SimpleDateFormat("yyMMdd_HHmmss").format(Date())
            val file = File(requireActivity().filesDir, "$date.jpg")
            val inputStream = requireActivity().contentResolver?.openInputStream(photoUri)
            val fileOutputStream = FileOutputStream(file)
            inputStream?.copyTo(fileOutputStream)
            inputStream?.close()
            fileOutputStream.close()
            imagePath = file.absolutePath

        }
    }

//    private fun sayPermission(activity: Activity) {
//        if (ActivityCompat.shouldShowRequestPermissionRationale(
//                activity,
//                Manifest.permission.CAMERA
//            )
//        ) {
//            val builder = AlertDialog.Builder(binding.root.context)
//            builder.setMessage("Kamerani ishlatish uchun ruxsat berishingiz kerak!")
//            builder.setTitle("Permissions")
//
//            builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, whick ->
//                ActivityCompat.requestPermissions(
//                    activity,
//                    arrayOf(Manifest.permission.CAMERA),
//                    1
//                )
//            })
//
//            builder.create().show()
//        } else {
//            ActivityCompat.requestPermissions(
//                activity,
//                arrayOf(Manifest.permission.CAMERA),
//                1
//            )
//        }
//
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == 1) {
//            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                val imageFile = createImageFile()
//                photoUri = FileProvider.getUriForFile(
//                    binding.root.context,
//                    BuildConfig.APPLICATION_ID,
//                    imageFile
//                )
//                getImageContent2.launch(photoUri)
//            }
//        }else{
//            val snackbar = Snackbar.make(binding.root, "Kamerani ishlata olmaysiz!", Snackbar.LENGTH_LONG)
//
//            snackbar.show()
//        }
//    }
}