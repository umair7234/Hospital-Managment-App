package com.example.cmc.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cmc.R
import com.example.cmc.adapters.PreviewsAdapter
import com.example.cmc.databinding.ActivityMainBinding
import com.example.cmc.databinding.ActivityPreviewBinding
import com.example.cmc.models.DescriptionsModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileOutputStream

class PreviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPreviewBinding
    private var descriptionAdapter: PreviewsAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.shareImage.setOnClickListener {
            val layoutParams = binding.rootView.layoutParams
            layoutParams.width = 700
            layoutParams.height = 1500
            binding.rootView.layoutParams = layoutParams

            shareViewScreenshot(this@PreviewActivity,binding.rootView)

            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            binding.rootView.layoutParams = layoutParams
        }



        val listGson = intent.getStringExtra("DescriptionList")
        val listType = object : TypeToken<ArrayList<DescriptionsModel>>() {}.type
        val list: ArrayList<DescriptionsModel>? = Gson().fromJson(listGson, listType)
        setAdapter(list ?: ArrayList())

//        intent.getStringExtra("Name").toString().also {
//            binding.nameEt.setText(it)
//        }
//        intent.getStringExtra("Age").toString().also {
//            binding.ageEt.setText(it+"Y")
//        }
//        intent.getStringExtra("Gender").toString().also {
//            binding.genderEt.setText(it)
//        }
//        intent.getStringExtra("Date").toString().also {
//            binding.dateEt.setText(it)
//        }
    }

    private fun setAdapter(list: ArrayList<DescriptionsModel>) {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@PreviewActivity)
            adapter = PreviewsAdapter()
            descriptionAdapter = adapter as PreviewsAdapter
            descriptionAdapter!!.setData(list)
        }
    }

    private fun shareViewScreenshot(context: Context, view: View) {
        // Capture View
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)

        // Save Bitmap to Cache
        val file = File(context.cacheDir, "screenshot.png").apply {
            FileOutputStream(this).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                out.flush()
            }
        }

        // Share Intent
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(Intent.createChooser(shareIntent, "Share via"))
    }
}