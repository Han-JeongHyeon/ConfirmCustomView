package com.kotlin.confirmcustomview

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.text.Editable
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import java.lang.Exception

/*
* 에딧 배경, 포커스 막대기 커스텀 가능하게
* 지우기 한번에 가능하게
* 내가 입력한 값 가져오기
* */

@RequiresApi(Build.VERSION_CODES.Q)
@SuppressLint("ClickableViewAccessibility")
class ConfirmCustomView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    init {
        View.inflate(context, R.layout.customview_confirm, this)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ConfirmCustomView)

        val number: ArrayList<Int> = arrayListOf()

        val cursor = ContextCompat.getDrawable(
            context,
            typedArray.getResourceId(R.styleable.ConfirmCustomView_Cursor, R.drawable.edit_cursor))

        val background = ContextCompat.getDrawable(
            context,
            typedArray.getResourceId(R.styleable.ConfirmCustomView_Background, R.drawable.edit_default))

        for (id in 1..6) {
            var editTextId = getIdentifier(id)

            editTextId.textCursorDrawable = cursor
            editTextId.background = background
            editTextId.setSelectAllOnFocus(true)

            editTextId.addTextChangedListener { text: Editable? ->
                if (text?.length == 1) {
                    number.add(text.toString().toInt())
                    if (id != 6) {
                        editTextId = getIdentifier(id + 1)
                        editTextId.requestFocus()
                    } else {
                        clear()
                        Log.d("TAG", number.toString())
                    }
                }
            }

            editTextId.setOnKeyListener { view, i, keyEvent ->
                if (keyEvent.keyCode == KeyEvent.KEYCODE_DEL &&
                    keyEvent.action == MotionEvent.ACTION_DOWN )
                {
                    try {
                        number.removeAt(id-1)
                    } catch (e: Exception) { }

                    if (id != 1) {
                        editTextId = getIdentifier(id-1)
                        editTextId.requestFocus()
                    }
                }

                false
            }

        }

    }

    fun clear(){
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow( this.windowToken, 0)
        clearFocus()
    }

    private fun getIdentifier(id: Int): EditText {
        return findViewById(
            resources.getIdentifier("edit$id","id","com.kotlin.confirmcustomview")
        )
    }

}