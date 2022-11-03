package com.kotlin.confirmcustomview

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import org.w3c.dom.Text
import java.lang.Exception
import kotlin.math.log

/*
* 에딧 배경, 커서 커스텀 가능하게
* 지우기 한번에
* 내가 입력한 값 가져오기
* 글자 사이즈, 에딧 크기 변경
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

        val width = typedArray.getDimension(R.styleable.ConfirmCustomView_Width,0F)

        val height = typedArray.getDimension(R.styleable.ConfirmCustomView_Height,0F)

        val fontSize = typedArray.getDimension(R.styleable.ConfirmCustomView_FontSize,16F)

        for (id in 1..6) {
            var editTextId = getIdentifier(id)

            editTextId.textCursorDrawable = cursor
            editTextId.background = background
            editTextId.setSelectAllOnFocus(true)
            editTextId.width = width.toInt()
            editTextId.height = height.toInt()
            editTextId.textSize = fontSize

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
                else if (keyEvent.action == MotionEvent.ACTION_DOWN) {
//                    editTextId.setText("1")
//                    editTextId.addTextChangedListener { text: Editable? ->
//                        if (text?.length == 1) {
//                            number.add(text.toString().toInt())
//
//                            if (id == 6) {
//                                clear()
//                                Log.d("TAG", number.toString())
//                            }

                            try {
                                editTextId = getIdentifier(id + 1)
                                editTextId.requestFocus()
                            } catch (e: Exception) { }

//                        }
//                    }
                }

                true
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
            resources.getIdentifier("edit$id","id",context.packageName)
        )
    }

}