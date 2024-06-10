package com.example.mystory.ui.view.main

import android.graphics.drawable.ColorDrawable
import android.icu.text.SimpleDateFormat
import android.net.ParseException
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.mystory.R
import com.example.mystory.data.api.response.ListStoryItem
import com.example.mystory.databinding.ActivityDetailBinding
import java.util.Locale

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    this,
                    R.color.peach
                )
            )
        )

        val story = intent.getParcelableExtra<ListStoryItem>("story")

        binding.userTextView.text = story?.name
        binding.descriptionTextView.text = story?.description

        Glide.with(this)
            .load(story?.photoUrl)
            .into(binding.storyImageView)

        val oldFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.UK)
        val newFormat = SimpleDateFormat("dd MMMM yyyy", Locale.UK)
        try {
            val date = oldFormat.parse(story?.createdAt)
            val formattedDate = newFormat.format(date)
            binding.dateTextView.text = formattedDate
        } catch (e: ParseException) {
            e.printStackTrace()
            binding.dateTextView.text = story?.createdAt
        }
    }
}