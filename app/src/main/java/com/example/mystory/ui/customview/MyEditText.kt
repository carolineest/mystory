package com.example.mystory.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.View
import androidx.appcompat.widget.AppCompatEditText

class MyEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    private var isEmailType: Boolean = false

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (isEmailType) {
                    if (!Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                        setError("Email tidak valid", null)
                    } else {
                        error = null
                    }
                } else {
                    if (s.toString().length < 8) {
                        setError("Password tidak boleh kurang dari 8 karakter", null)
                    } else {
                        error = null
                    }
                }
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
    }

    fun setInputTypeEmail() {
        isEmailType = true
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
    }

    fun setInputTypePassword() {
        isEmailType = false
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }
}