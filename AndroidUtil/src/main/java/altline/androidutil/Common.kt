package altline.androidutil

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun showSoftInput(view: View, flags: Int) {
    val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(view, flags)
}
