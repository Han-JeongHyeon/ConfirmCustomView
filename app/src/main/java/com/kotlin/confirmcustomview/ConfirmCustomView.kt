package com.kotlin.confirmcustomview

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.addTextChangedListener
import com.kotlin.confirmcustomview.databinding.CustomviewConfirmBinding
import kotlin.math.log

@SuppressLint("ClickableViewAccessibility")
class ConfirmCustomView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    init {
        View.inflate(context, R.layout.customview_confirm, this)

        for (id in 1..6) {
            var editTextId = getIdentifier(id)

            editTextId.addTextChangedListener { text: Editable? ->
                if (id != 6 && text?.length == 1) {
                    editTextId = getIdentifier(id+1)
                    editTextId.requestFocus()
                }
            }

            editTextId.setOnKeyListener { view, i, keyEvent ->
                if (id != 1 && keyEvent.keyCode == KeyEvent.KEYCODE_DEL) {
                    editTextId = getIdentifier(id-1)
                    editTextId.requestFocus()
                }
                false
            }

        }

    }

    fun clear(){
        clearFocus()
    }


    private fun getIdentifier(id: Int): EditText {
        return findViewById(
            resources.getIdentifier("edit$id","id","com.kotlin.confirmcustomview")
        )
    }

}