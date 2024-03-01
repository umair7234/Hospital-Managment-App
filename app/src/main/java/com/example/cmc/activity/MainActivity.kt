package com.example.cmc.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.widget.DatePicker
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cmc.R
import com.example.cmc.adapters.PreviewsAdapter
import com.example.cmc.databinding.ActivityMainBinding
import com.example.cmc.models.DescriptionsModel
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    //        private var date: String = ""
//        private var name: String = ""
//    private var age: String = ""
//    private var gender: String = ""


    private var descriptionAdapter: PreviewsAdapter? = null
    private var descriptionsList: ArrayList<DescriptionsModel> = ArrayList()

    @SuppressLint("DiscouragedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpListeners()
        setAdapter(ArrayList())

    }

    private fun setUpListeners() {
        binding.gender.setOnClickListener {
            selectGender()
        }
        binding.dateEt.setOnClickListener {
            showDatePicker()
        }
//        binding.dateButton.setOnClickListener {
//            binding.dateEt.setText(getCurrentDateFormatted())
//        }
        binding.nameEt.capitalizeFirstLetterOnInput()
        binding.addDes.setOnClickListener {
            showDialog(this@MainActivity)
        }
        binding.imagePreview.setOnClickListener {
            val gson = Gson().toJson(descriptionsList)
            val name = binding.nameEt.text.toString().trim()
            val age = binding.ageEt.text.toString().trim()
            val gender = binding.gender.text.toString().trim()
            val date = binding.dateEt.text.toString().trim()
            val i = Intent(this@MainActivity, PreviewActivity::class.java).apply {
                putExtra("DescriptionList", gson)
                putExtra("Name", name)
                putExtra("Age", age)
                putExtra("Gender", gender)
                putExtra("Date", date)
            }
            startActivity(i)
        }
    }

    private fun showDialog(context: Context) {
        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL
        val titleEditText = EditText(context)
        titleEditText.hint = "Title"
        val descriptionEditText = EditText(context)
        descriptionEditText.hint = "Description"
        descriptionEditText.inputType =
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
        descriptionEditText.setLines(3)
        layout.addView(titleEditText)
        layout.addView(descriptionEditText)
        val padding = (16 * context.resources.displayMetrics.density).toInt() // 16dp to pixels
        layout.setPadding(padding, padding, padding, padding)
        AlertDialog.Builder(context).apply {
            setTitle("Add Details")
            setView(layout)
            setPositiveButton("Add") { dialog, id ->
                val title = titleEditText.text.toString()
                val description = descriptionEditText.text.toString()
                descriptionsList.add(DescriptionsModel(title, description))
                descriptionAdapter!!.setData(descriptionsList)
            }
            setNegativeButton("Cancel") { dialog, id ->
                dialog.dismiss()
            }
        }.create().show()
    }

    private fun setAdapter(list: ArrayList<DescriptionsModel>) {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = PreviewsAdapter()
            descriptionAdapter = adapter as PreviewsAdapter
            descriptionAdapter!!.setData(list)
        }
    }

    private fun getCurrentDateFormatted(): String {
        val dateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
        return dateFormat.format(Calendar.getInstance().time)
    }

    fun EditText.capitalizeFirstLetterOnInput() {
        this.addTextChangedListener(object : TextWatcher {
            var isEditing = false
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (isEditing) return
                isEditing = true

                s?.let {
                    val words = it.split(" ")
                    val capitalizedWords = words.map { word ->
                        if (word.isNotEmpty()) word.replaceFirstChar { char ->
                            if (char.isLowerCase()) char.titlecase(Locale.getDefault()) else char.toString()
                        } else word
                    }
                    val correctedInput = capitalizedWords.joinToString(" ")
                    if (it.toString() != correctedInput) {
                        val cursorPosition = this@capitalizeFirstLetterOnInput.selectionStart
                        this@capitalizeFirstLetterOnInput.setText(correctedInput)
                        this@capitalizeFirstLetterOnInput.setSelection(
                            cursorPosition.coerceAtMost(
                                correctedInput.length
                            )
                        )
                    }
                }

                isEditing = false
            }
        })
    }

    private fun selectGender() {
        val genders = arrayOf<CharSequence>("Male", "Female")
        var gender = ""
        val alert = AlertDialog.Builder(this)
        val typeface = ResourcesCompat.getFont(this@MainActivity, R.font.outfitsemibold)
        val alertDialog = alert.create()
        alert.setTitle("Select Gender")
        alert.setSingleChoiceItems(
            genders, -1
        ) { _, which ->
            if (genders[which] === "Male") {
                gender = "Male"
            } else if (genders[which] === "Female") {
                gender = "Female"
            } else {
                gender = ""
            }
        }
        val titleId = resources.getIdentifier("alertTitle", "id", "android")
        alertDialog.findViewById<TextView>(titleId)?.typeface = typeface
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)?.typeface = typeface
        alert.setPositiveButton("ok") { _, _ ->
            binding.gender.setText(gender)
        }
        alert.show()
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            this, { view: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                calendar.set(selectedYear, selectedMonth, selectedDay)
                val dateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
                val selectedDate = dateFormat.format(calendar.time)
                binding.dateEt.setText(selectedDate)
            }, year, month, day
        )

        datePickerDialog.show()
    }
}
