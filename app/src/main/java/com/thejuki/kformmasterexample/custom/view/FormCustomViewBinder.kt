package com.thejuki.kformmasterexample.custom.view

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.AppCompatTextView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.github.vivchar.rendererrecyclerviewadapter.ViewHolder
import com.github.vivchar.rendererrecyclerviewadapter.ViewState
import com.github.vivchar.rendererrecyclerviewadapter.ViewStateProvider
import com.github.vivchar.rendererrecyclerviewadapter.binder.ViewBinder
import com.thejuki.kformmaster.helper.FormBuildHelper
import com.thejuki.kformmaster.view.BaseFormViewBinder
import com.thejuki.kformmasterexample.R
import com.thejuki.kformmasterexample.custom.model.FormCustomElement
import com.thejuki.kformmasterexample.custom.state.FormCustomViewState

/**
 * Form Custom ViewBinder
 *
 * View Binder for [FormCustomElement]
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
class CustomViewBinder(private val context: Context, private val formBuilder: FormBuildHelper) : BaseFormViewBinder() {
    var viewBinder = ViewBinder(R.layout.form_element_custom, FormCustomElement::class.java, { model, finder, _ ->
        val textViewTitle = finder.find(model.titleLayoutId
                ?: R.id.customFormElementTitle) as AppCompatTextView
        val textViewError = finder.find(model.errorLayoutId
                ?: R.id.customFormElementError) as AppCompatTextView
        val itemView = finder.getRootView() as View
        baseSetup(model, textViewTitle, textViewError, itemView)

        val editTextValue = finder.find(model.editLayoutId
                ?: R.id.customFormElementValue) as AppCompatEditText

        editTextValue.setText(model.valueAsString)
        editTextValue.hint = model.hint ?: ""

        setEditTextFocusEnabled(editTextValue, itemView)

        editTextValue.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                textViewTitle.setTextColor(ContextCompat.getColor(context,
                        R.color.colorFormMasterElementFocusedTitle))
            } else {
                textViewTitle.setTextColor(ContextCompat.getColor(context,
                        R.color.colorFormMasterElementTextTitle))
            }
        }

        editTextValue.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {

                // get current form element, existing value and new value
                val currentValue = model.valueAsString
                val newValue = charSequence.toString()

                // trigger event only if the value is changed
                if (currentValue != newValue) {
                    model.setValue(newValue)
                    model.setError(null)
                    setError(textViewError, null)
                    formBuilder.onValueChanged(model)
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })
    }, object : ViewStateProvider<FormCustomElement, ViewHolder> {
        override fun createViewStateID(model: FormCustomElement): Int {
            return model.id
        }

        override fun createViewState(holder: ViewHolder): ViewState<ViewHolder> {
            return FormCustomViewState(holder)
        }
    })

    private fun setEditTextFocusEnabled(editTextValue: AppCompatEditText, itemView: View) {
        itemView.setOnClickListener {
            editTextValue.requestFocus()
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            editTextValue.setSelection(editTextValue.text.length)
            imm.showSoftInput(editTextValue, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}
