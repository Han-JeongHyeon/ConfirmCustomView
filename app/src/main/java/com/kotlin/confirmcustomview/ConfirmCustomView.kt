package com.kotlin.confirmcustomview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
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
import androidx.core.graphics.drawable.toDrawable
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

    private val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    private var default: Drawable? = null
    private var custom: Drawable? = null

    init {
        View.inflate(context, R.layout.customview_confirm, this)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ConfirmCustomView)

        default = ContextCompat.getDrawable(
            context,
            typedArray.getResourceId(
                R.styleable.ConfirmCustomView_Background,
                R.drawable.edit_default
            )
        )

        custom = ContextCompat.getDrawable(
            context,
            typedArray.getResourceId(
                R.styleable.ConfirmCustomView_Background,
                R.drawable.edit_custom
            )
        )

        val width = typedArray.getDimension(R.styleable.ConfirmCustomView_Width, 0F)

        val height = typedArray.getDimension(R.styleable.ConfirmCustomView_Height, 0F)

        val fontSize = typedArray.getDimension(R.styleable.ConfirmCustomView_FontSize, 16F)

        val editText = findViewById<EditText>(R.id.edit)

        var textViewId: TextView

        editText.setOnKeyListener { view, i, keyEvent ->
            if (keyEvent.keyCode == KeyEvent.KEYCODE_DEL &&
                keyEvent.action == MotionEvent.ACTION_DOWN
            ) {
                try {
                    textViewId = getIdentifier(editText.length())
                    textViewId.text = ""
                    val num = editText.length() - 1
                    backgroundChange(if (num in 1..6) num else 1)
                } catch (e:Exception) { }

            } else if (keyEvent.keyCode - 7 in 0..9 &&
                keyEvent.action == MotionEvent.ACTION_UP)
            {
                if (editText.length() == 6) Log.d("TAG", "${editText.text}")

                textViewId = getIdentifier(editText.length())
                textViewId.text = editText.text.substring(editText.length() - 1, editText.length())
                backgroundChange(editText.length() + 1)
            }

            false
        }

        for (id in 1..6) {
            textViewId = getIdentifier(id)

            textViewId.background = default

            textViewId.width = width.toInt()
            textViewId.height = height.toInt()

            textViewId.textSize = fontSize

            textViewId.setOnClickListener {
                editText.requestFocus()
                inputMethodManager.showSoftInput(editText, 0)

                val num = editText.length() + 1

                backgroundChange( if (num in 1..6) num else 6)
            }
        }
    }

    fun clear() {
        backgroundChange(0)
        inputMethodManager.hideSoftInputFromWindow(this.windowToken, 0)
        clearFocus()
    }

    private fun backgroundChange(id: Int) {
        for (i in 1..6) {
            val textViewId = getIdentifier(i)

            if (i == id) {
                textViewId.background = custom
            } else {
                textViewId.background = default
            }
        }

    }

    private fun getIdentifier(id: Int): TextView {
        return findViewById(
            resources.getIdentifier("text$id", "id", context.packageName)
        )
    }

}