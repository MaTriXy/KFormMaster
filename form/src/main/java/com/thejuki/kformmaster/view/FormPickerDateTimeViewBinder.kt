package com.thejuki.kformmaster.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.AppCompatTextView
import android.text.InputType
import android.view.View
import com.github.vivchar.rendererrecyclerviewadapter.ViewHolder
import com.github.vivchar.rendererrecyclerviewadapter.ViewState
import com.github.vivchar.rendererrecyclerviewadapter.ViewStateProvider
import com.github.vivchar.rendererrecyclerviewadapter.binder.ViewBinder
import com.thejuki.kformmaster.R
import com.thejuki.kformmaster.helper.FormBuildHelper
import com.thejuki.kformmaster.model.FormPickerDateTimeElement
import com.thejuki.kformmaster.state.FormEditTextViewState

/**
 * Form Picker DateTime ViewBinder
 *
 * View Binder for [FormPickerDateTimeElement]
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
class FormPickerDateTimeViewBinder(private val context: Context, private val formBuilder: FormBuildHelper) : BaseFormViewBinder() {
    var viewBinder = ViewBinder(R.layout.form_element, FormPickerDateTimeElement::class.java, { model, finder, _ ->
        val textViewTitle = finder.find(model.titleLayoutId
                ?: R.id.formElementTitle) as AppCompatTextView
        val textViewError = finder.find(model.errorLayoutId
                ?: R.id.formElementError) as AppCompatTextView
        val itemView = finder.getRootView() as View
        baseSetup(model, textViewTitle, textViewError, itemView)

        val editTextValue = finder.find(model.editLayoutId
                ?: R.id.formElementValue) as AppCompatEditText

        editTextValue.setText(model.valueAsString)
        editTextValue.hint = model.hint ?: ""

        model.editView = editTextValue

        editTextValue.setRawInputType(InputType.TYPE_NULL)

        // If no value is set by the user, create a new instance of DateTimeHolder
        with(model.value)
        {
            if (this == null) {
                model.setValue(FormPickerDateTimeElement.DateTimeHolder())
            }
            this?.validOrCurrentDate()
        }

        val datePickerDialog = DatePickerDialog(context,
                dateDialogListener(model, editTextValue, textViewError),
                model.value?.year!!,
                model.value?.month!! - 1,
                model.value?.dayOfMonth!!)

        setOnClickListener(editTextValue, itemView, datePickerDialog)
    }, object : ViewStateProvider<FormPickerDateTimeElement, ViewHolder> {
        override fun createViewStateID(model: FormPickerDateTimeElement): Int {
            return model.id
        }

        override fun createViewState(holder: ViewHolder): ViewState<ViewHolder> {
            return FormEditTextViewState(holder)
        }
    })

    private fun dateDialogListener(model: FormPickerDateTimeElement,
                                   editTextValue: AppCompatEditText,
                                   textViewError: AppCompatTextView): DatePickerDialog.OnDateSetListener {
        return DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            // get current form element, existing value and new value

            with(model.value)
            {
                this?.year = year
                this?.month = monthOfYear + 1
                this?.dayOfMonth = dayOfMonth
            }

            // Now show time picker
            TimePickerDialog(context, timeDialogListener(model, editTextValue, textViewError),
                    model.value!!.hourOfDay!!,
                    model.value!!.minute!!,
                    false).show()
        }
    }

    private fun timeDialogListener(model: FormPickerDateTimeElement,
                                   editTextValue: AppCompatEditText,
                                   textViewError: AppCompatTextView): TimePickerDialog.OnTimeSetListener {
        return TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            with(model.value)
            {
                this?.hourOfDay = hourOfDay
                this?.minute = minute
                this?.isEmptyDateTime = false
            }
            model.setError(null) // Reset after value change
            formBuilder.onValueChanged(model)
            model.valueObservers.forEach { it(model.value, model) }
            editTextValue.setText(model.valueAsString)
            setError(textViewError, null)
        }
    }
}
