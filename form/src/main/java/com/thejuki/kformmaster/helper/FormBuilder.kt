package com.thejuki.kformmaster.helper

import android.content.Context
import android.support.annotation.IdRes
import android.support.v7.widget.RecyclerView
import android.widget.ArrayAdapter
import com.thejuki.kformmaster.listener.OnFormElementValueChangedListener
import com.thejuki.kformmaster.model.*
import java.io.Serializable
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Form Builder
 *
 * Used for Kotlin DSL to create the FormBuildHelper
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */

/** Type-safe builder method to initialize the form */
fun form(context: Context,
         recyclerView: RecyclerView,
         listener: OnFormElementValueChangedListener? = null,
         cacheForm: Boolean = false,
         init: FormBuildHelper.() -> Unit): FormBuildHelper {
    val form = FormBuildHelper(
            context = context,
            listener = listener,
            recyclerView = recyclerView,
            cacheForm = cacheForm
    )
    form.init()
    form.setItems()
    return form
}

/** FieldBuilder interface */
interface FieldBuilder {
    fun build(): BaseFormElement<*>
}

/** Builder method to add a FormHeader */
class HeaderBuilder(var title: String = "") : FieldBuilder {
    var collapsible: Boolean = false
    override fun build() =
            FormHeader.createInstance(title)
                    .setCollapsible(collapsible)
}

/** FormBuildHelper extension to add a FormHeader */
fun FormBuildHelper.header(init: HeaderBuilder.() -> Unit): FormHeader {
    val element = HeaderBuilder().apply(init).build()
    element.id = ++lastId
    elements.add(element)
    return element
}

/** Builder method to add a BaseFormElement */
abstract class BaseElementBuilder<T : Serializable>(protected val tag: Int = -1, var title: String? = null) : FieldBuilder {
    /**
     * Form Element Value
     */
    var value: T? = null

    /**
     * Form Element Options
     */
    var options: List<T>? = null
        get() = field ?: ArrayList()

    /**
     * Form Element Options Selected
     * NOTE: When using MultiCheckBox, this is the Form Element Value
     */
    var optionsSelected: List<T>? = null
        get() = field ?: ArrayList()

    /**
     * Form Element Hint
     */
    var hint: String? = null

    /**
     * Form Element Error
     */
    var error: String? = null

    /**
     * Form Element Required
     */
    var required: Boolean = false

    /**
     * Form Element Visibility
     */
    var visible: Boolean = true

    /**
     * Form Element Edit Layout
     */
    @IdRes
    var editLayoutId: Int? = null

    /**
     * Form Element Title Layout
     */
    @IdRes
    var titleLayoutId: Int? = null

    /**
     * Form Element Error Layout
     */
    @IdRes
    var errorLayoutId: Int? = null

    /**
     * Form Element Value Observers
     */
    val valueObservers = mutableListOf<(value: T?, element: BaseFormElement<T>) -> Unit>()
}

/** Builder method to add a FormSingleLineEditTextElement */
class SingleLineEditTextBuilder(tag: Int = -1) : BaseElementBuilder<String>(tag) {
    override fun build() =
            FormSingleLineEditTextElement(tag)
                    .setTitle(title.orEmpty())
                    .setValue(value)
                    .setHint(hint)
                    .setError(error)
                    .setRequired(required)
                    .setVisible(visible)
                    .setTitleLayoutId(titleLayoutId)
                    .setErrorLayoutId(errorLayoutId)
                    .setEditLayoutId(editLayoutId)
                    .addAllValueObservers(valueObservers)
                    as FormSingleLineEditTextElement
}

/** FormBuildHelper extension to add a FormSingleLineEditTextElement */
fun FormBuildHelper.text(tag: Int = -1, init: SingleLineEditTextBuilder.() -> Unit): FormSingleLineEditTextElement {
    val element = SingleLineEditTextBuilder(tag).apply(init).build()
    element.id = ++lastId
    elements.add(element)
    return element
}

/** Builder method to add a FormMultiLineEditTextElement */
class MultiLineEditTextBuilder(tag: Int = -1) : BaseElementBuilder<String>(tag) {
    override fun build() =
            FormMultiLineEditTextElement(tag)
                    .setTitle(title.orEmpty())
                    .setValue(value)
                    .setHint(hint)
                    .setError(error)
                    .setRequired(required)
                    .setVisible(visible)
                    .setTitleLayoutId(titleLayoutId)
                    .setErrorLayoutId(errorLayoutId)
                    .setEditLayoutId(editLayoutId)
                    .addAllValueObservers(valueObservers)
                    as FormMultiLineEditTextElement
}

/** FormBuildHelper extension to add a FormMultiLineEditTextElement */
fun FormBuildHelper.textArea(tag: Int = -1, init: MultiLineEditTextBuilder.() -> Unit): FormMultiLineEditTextElement {
    val element = MultiLineEditTextBuilder(tag).apply(init).build()
    element.id = ++lastId
    elements.add(element)
    return element
}

/** Builder method to add a FormNumberEditTextElement */
class NumberEditTextBuilder(tag: Int = -1) : BaseElementBuilder<String>(tag) {
    var numbersOnly: Boolean = false
    override fun build() =
            (FormNumberEditTextElement(tag)
                    .setTitle(title.orEmpty())
                    .setValue(value)
                    .setHint(hint)
                    .setError(error)
                    .setRequired(required)
                    .setVisible(visible)
                    .setTitleLayoutId(titleLayoutId)
                    .setErrorLayoutId(errorLayoutId)
                    .setEditLayoutId(editLayoutId)
                    .addAllValueObservers(valueObservers)
                    as FormNumberEditTextElement)
                    .setNumbersOnly(numbersOnly)
}

/** FormBuildHelper extension to add a FormNumberEditTextElement */
fun FormBuildHelper.number(tag: Int = -1, init: NumberEditTextBuilder.() -> Unit): FormNumberEditTextElement {
    val element = NumberEditTextBuilder(tag).apply(init).build()
    element.id = ++lastId
    elements.add(element)
    return element
}

/** Builder method to add a FormEmailEditTextElement */
class EmailEditTextBuilder(tag: Int = -1) : BaseElementBuilder<String>(tag) {
    override fun build() =
            FormEmailEditTextElement(tag)
                    .setTitle(title.orEmpty())
                    .setValue(value)
                    .setHint(hint)
                    .setError(error)
                    .setRequired(required)
                    .setVisible(visible)
                    .setTitleLayoutId(titleLayoutId)
                    .setErrorLayoutId(errorLayoutId)
                    .setEditLayoutId(editLayoutId)
                    .addAllValueObservers(valueObservers)
                    as FormEmailEditTextElement
}

/** FormBuildHelper extension to add a FormEmailEditTextElement */
fun FormBuildHelper.email(tag: Int = -1, init: EmailEditTextBuilder.() -> Unit): FormEmailEditTextElement {
    val element = EmailEditTextBuilder(tag).apply(init).build()
    element.id = ++lastId
    elements.add(element)
    return element
}

/** Builder method to add a FormPhoneEditTextElement */
class PasswordEditTextBuilder(tag: Int = -1) : BaseElementBuilder<String>(tag) {
    override fun build() =
            FormPasswordEditTextElement(tag)
                    .setTitle(title.orEmpty())
                    .setValue(value)
                    .setHint(hint)
                    .setError(error)
                    .setRequired(required)
                    .setVisible(visible)
                    .setTitleLayoutId(titleLayoutId)
                    .setErrorLayoutId(errorLayoutId)
                    .setEditLayoutId(editLayoutId)
                    .addAllValueObservers(valueObservers)
                    as FormPasswordEditTextElement
}

/** FormBuildHelper extension to add a PasswordEditTextBuilder */
fun FormBuildHelper.password(tag: Int = -1, init: PasswordEditTextBuilder.() -> Unit): FormPasswordEditTextElement {
    val element = PasswordEditTextBuilder(tag).apply(init).build()
    element.id = ++lastId
    elements.add(element)
    return element
}

/** Builder method to add a FormEmailEditTextElement */
class PhoneEditTextBuilder(tag: Int = -1) : BaseElementBuilder<String>(tag) {
    override fun build() =
            FormPhoneEditTextElement(tag)
                    .setTitle(title.orEmpty())
                    .setValue(value)
                    .setHint(hint)
                    .setError(error)
                    .setRequired(required)
                    .setVisible(visible)
                    .setTitleLayoutId(titleLayoutId)
                    .setErrorLayoutId(errorLayoutId)
                    .setEditLayoutId(editLayoutId)
                    .addAllValueObservers(valueObservers)
                    as FormPhoneEditTextElement
}

/** FormBuildHelper extension to add a FormPhoneEditTextElement */
fun FormBuildHelper.phone(tag: Int = -1, init: PhoneEditTextBuilder.() -> Unit): FormPhoneEditTextElement {
    val element = PhoneEditTextBuilder(tag).apply(init).build()
    element.id = ++lastId
    elements.add(element)
    return element
}

/** Builder method to add a FormAutoCompleteElement */
class AutoCompleteBuilder<T : Serializable>(tag: Int = -1) : BaseElementBuilder<T>(tag) {
    var arrayAdapter: ArrayAdapter<*>? = null
    var dropdownWidth: Int? = null
    override fun build() =
            (FormAutoCompleteElement<T>(tag)
                    .setTitle(title.orEmpty())
                    .setValue(value)
                    .setHint(hint)
                    .setError(error)
                    .setRequired(required)
                    .setVisible(visible)
                    .setTitleLayoutId(titleLayoutId)
                    .setErrorLayoutId(errorLayoutId)
                    .setEditLayoutId(editLayoutId)
                    .addAllValueObservers(valueObservers)
                    as FormAutoCompleteElement<T>)
                    .setArrayAdapter(arrayAdapter)
                    .setDropdownWidth(dropdownWidth)
}

/** FormBuildHelper extension to add a FormAutoCompleteElement */
fun <T : Serializable> FormBuildHelper.autoComplete(tag: Int = -1, init: AutoCompleteBuilder<T>.() -> Unit): FormAutoCompleteElement<T> {
    val element = AutoCompleteBuilder<T>(tag).apply(init).build()
    element.id = ++lastId
    elements.add(element)
    return element
}

/** Builder method to add a FormTokenAutoCompleteElement */
class AutoCompleteTokenBuilder<T : Serializable>(tag: Int = -1) : BaseElementBuilder<T>(tag) {
    var arrayAdapter: ArrayAdapter<*>? = null
    var dropdownWidth: Int? = null
    override fun build() =
            (FormTokenAutoCompleteElement<T>(tag)
                    .setTitle(title.orEmpty())
                    .setValue(value)
                    .setHint(hint)
                    .setError(error)
                    .setRequired(required)
                    .setVisible(visible)
                    .setTitleLayoutId(titleLayoutId)
                    .setErrorLayoutId(errorLayoutId)
                    .setEditLayoutId(editLayoutId)
                    .addAllValueObservers(valueObservers)
                    as FormTokenAutoCompleteElement<T>)
                    .setArrayAdapter(arrayAdapter)
                    .setDropdownWidth(dropdownWidth)
}

/** FormBuildHelper extension to add a FormTokenAutoCompleteElement */
fun <T : Serializable> FormBuildHelper.autoCompleteToken(tag: Int = -1, init: AutoCompleteTokenBuilder<T>.() -> Unit): FormTokenAutoCompleteElement<T> {
    val element = AutoCompleteTokenBuilder<T>(tag).apply(init).build()
    element.id = ++lastId
    elements.add(element)
    return element
}

/** Builder method to add a FormButtonElement */
class ButtonBuilder(val tag: Int = -1) : FieldBuilder {
    var value: String? = null
    var visible: Boolean = true
    val valueObservers = mutableListOf<(value: String?, element: BaseFormElement<String>) -> Unit>()
    @IdRes
    var editLayoutId: Int? = null
    override fun build() =
            (FormButtonElement(tag)
                    .setValue(value)
                    .setVisible(visible)
                    .setEditLayoutId(editLayoutId)
                    .addAllValueObservers(valueObservers)
                    as FormButtonElement)
}

/** FormBuildHelper extension to add a FormButtonElement */
fun FormBuildHelper.button(tag: Int = -1, init: ButtonBuilder.() -> Unit): FormButtonElement {
    val element = ButtonBuilder(tag).apply(init).build()
    element.id = ++lastId
    elements.add(element)
    return element
}

/** Builder method to add a FormPickerDateElement */
class DateBuilder(tag: Int = -1) : BaseElementBuilder<FormPickerDateElement.DateHolder>(tag) {
    var dateFormat: DateFormat = SimpleDateFormat.getDateInstance()
    var dateValue: Date? = null
    override fun build() =
            (FormPickerDateElement(tag)
                    .setTitle(title.orEmpty())
                    .setValue(value?: FormPickerDateElement.DateHolder(dateValue, dateFormat))
                    .setHint(hint)
                    .setError(error)
                    .setRequired(required)
                    .setVisible(visible)
                    .setTitleLayoutId(titleLayoutId)
                    .setErrorLayoutId(errorLayoutId)
                    .setEditLayoutId(editLayoutId)
                    .addAllValueObservers(valueObservers)
                    as FormPickerDateElement)
}

/** FormBuildHelper extension to add a FormPickerDateElement */
fun FormBuildHelper.date(tag: Int = -1, init: DateBuilder.() -> Unit): FormPickerDateElement {
    val element = DateBuilder(tag).apply(init).build()
    element.id = ++lastId
    elements.add(element)
    return element
}

/** Builder method to add a FormPickerTimeElement */
class TimeBuilder(tag: Int = -1) : BaseElementBuilder<FormPickerTimeElement.TimeHolder>(tag) {
    var dateFormat: DateFormat = SimpleDateFormat.getDateInstance()
    var dateValue: Date? = null
    override fun build() =
            (FormPickerTimeElement(tag)
                    .setTitle(title.orEmpty())
                    .setValue(value?: FormPickerTimeElement.TimeHolder(dateValue, dateFormat))
                    .setHint(hint)
                    .setError(error)
                    .setRequired(required)
                    .setVisible(visible)
                    .setTitleLayoutId(titleLayoutId)
                    .setErrorLayoutId(errorLayoutId)
                    .setEditLayoutId(editLayoutId)
                    .addAllValueObservers(valueObservers)
                    as FormPickerTimeElement)
}

/** FormBuildHelper extension to add a FormPickerTimeElement */
fun FormBuildHelper.time(tag: Int = -1, init: TimeBuilder.() -> Unit): FormPickerTimeElement {
    val element = TimeBuilder(tag).apply(init).build()
    element.id = ++lastId
    elements.add(element)
    return element
}

/** Builder method to add a FormButtonElement */
class DateTimeBuilder(tag: Int = -1) : BaseElementBuilder<FormPickerDateTimeElement.DateTimeHolder>(tag) {
    var dateFormat: DateFormat = SimpleDateFormat.getDateInstance()
    var dateValue: Date? = null
    override fun build() =
            (FormPickerDateTimeElement(tag)
                    .setTitle(title.orEmpty())
                    .setValue(value?: FormPickerDateTimeElement.DateTimeHolder(dateValue, dateFormat))
                    .setHint(hint)
                    .setError(error)
                    .setRequired(required)
                    .setVisible(visible)
                    .setTitleLayoutId(titleLayoutId)
                    .setErrorLayoutId(errorLayoutId)
                    .setEditLayoutId(editLayoutId)
                    .addAllValueObservers(valueObservers)
                    as FormPickerDateTimeElement)
}

/** FormBuildHelper extension to add a FormPickerDateTimeElement */
fun FormBuildHelper.dateTime(tag: Int = -1, init: DateTimeBuilder.() -> Unit): FormPickerDateTimeElement {
    val element = DateTimeBuilder(tag).apply(init).build()
    element.id = ++lastId
    elements.add(element)
    return element
}

/** Builder method to add a FormPickerDropDownElement */
class DropDownBuilder<T : Serializable>(tag: Int = -1) : BaseElementBuilder<T>(tag) {
    var dialogTitle: String? = null
    var arrayAdapter: ArrayAdapter<*>? = null
    override fun build() =
            (FormPickerDropDownElement<T>(tag)
                    .setTitle(title.orEmpty())
                    .setValue(value)
                    .setHint(hint)
                    .setError(error)
                    .setRequired(required)
                    .setVisible(visible)
                    .setOptions(options?: ArrayList())
                    .setTitleLayoutId(titleLayoutId)
                    .setErrorLayoutId(errorLayoutId)
                    .setEditLayoutId(editLayoutId)
                    .addAllValueObservers(valueObservers)
                    as FormPickerDropDownElement<T>)
                    .setDialogTitle(dialogTitle)
                    .setArrayAdapter(arrayAdapter)
}

/** FormBuildHelper extension to add a FormPickerDropDownElement */
fun <T : Serializable> FormBuildHelper.dropDown(tag: Int = -1, init: DropDownBuilder<T>.() -> Unit): FormPickerDropDownElement<T> {
    val element = DropDownBuilder<T>(tag).apply(init).build()
    element.id = ++lastId
    elements.add(element)
    return element
}

/** Builder method to add a FormPickerMultiCheckBoxElement */
class MultiCheckBoxBuilder<T : Serializable>(tag: Int = -1) : BaseElementBuilder<T>(tag) {
    var dialogTitle: String? = null
    override fun build() =
            (FormPickerMultiCheckBoxElement<T>(tag)
                    .setTitle(title.orEmpty())
                    .setValue(value)
                    .setHint(hint)
                    .setError(error)
                    .setRequired(required)
                    .setVisible(visible)
                    .setOptions(options?: ArrayList())
                    .setOptionsSelected(optionsSelected?: ArrayList())
                    .setTitleLayoutId(titleLayoutId)
                    .setErrorLayoutId(errorLayoutId)
                    .setEditLayoutId(editLayoutId)
                    .addAllValueObservers(valueObservers)
                    as FormPickerMultiCheckBoxElement<T>)
                    .setDialogTitle(dialogTitle)
}

/** FormBuildHelper extension to add a FormPickerMultiCheckBoxElement */
fun <T : Serializable> FormBuildHelper.multiCheckBox(tag: Int = -1, init: MultiCheckBoxBuilder<T>.() -> Unit): FormPickerMultiCheckBoxElement<T> {
    val element = MultiCheckBoxBuilder<T>(tag).apply(init).build()
    element.id = ++lastId
    elements.add(element)
    return element
}

/** Builder method to add a FormSwitchElement */
class SwitchBuilder<T : Serializable>(tag: Int = -1) : BaseElementBuilder<T>(tag) {
    var onValue: T? = null
    var offValue: T? = null
    override fun build() =
            (FormSwitchElement<T>(tag)
                    .setTitle(title.orEmpty())
                    .setValue(value)
                    .setHint(hint)
                    .setError(error)
                    .setRequired(required)
                    .setVisible(visible)
                    .setTitleLayoutId(titleLayoutId)
                    .setErrorLayoutId(errorLayoutId)
                    .setEditLayoutId(editLayoutId)
                    .addAllValueObservers(valueObservers)
                    as FormSwitchElement<T>)
                    .setOnValue(onValue)
                    .setOffValue(offValue)
}

/** FormBuildHelper extension to add a FormSwitchElement */
fun <T : Serializable> FormBuildHelper.switch(tag: Int = -1, init: SwitchBuilder<T>.() -> Unit): FormSwitchElement<T> {
    val element = SwitchBuilder<T>(tag).apply(init).build()
    element.id = ++lastId
    elements.add(element)
    return element
}

/** Builder method to add a FormCheckBoxElement */
class CheckBoxBuilder<T : Serializable>(tag: Int = -1) : BaseElementBuilder<T>(tag) {
    var checkedValue: T? = null
    var unCheckedValue: T? = null
    override fun build() =
            (FormCheckBoxElement<T>(tag)
                    .setTitle(title.orEmpty())
                    .setValue(value)
                    .setHint(hint)
                    .setError(error)
                    .setRequired(required)
                    .setVisible(visible)
                    .setTitleLayoutId(titleLayoutId)
                    .setErrorLayoutId(errorLayoutId)
                    .setEditLayoutId(editLayoutId)
                    .addAllValueObservers(valueObservers)
                    as FormCheckBoxElement<T>)
                    .setCheckedValue(checkedValue)
                    .setUnCheckedValue(unCheckedValue)
}

/** FormBuildHelper extension to add a FormCheckBoxElement */
fun <T : Serializable> FormBuildHelper.checkBox(tag: Int = -1, init: CheckBoxBuilder<T>.() -> Unit): FormCheckBoxElement<T> {
    val element = CheckBoxBuilder<T>(tag).apply(init).build()
    element.id = ++lastId
    elements.add(element)
    return element
}

/** Builder method to add a FormSliderElement */
class SliderBuilder(tag: Int = -1) : BaseElementBuilder<Int>(tag) {
    /**
     * Maximum progress of the slider
     */
    var max: Int = 100

    /**
     * Minimum progress of the slider
     */
    var min: Int = 0

    /**
     * Steps of the slider
     * Ex. 20 steps with max of 100 would step 0, 5, 10, 15, 20, 25,... to 100
     */
    var steps: Int = 1

    /**
     * Form Element Progress Number Layout
     */
    @IdRes
    var progressLayoutId: Int? = null

    override fun build() =
            (FormSliderElement(tag)
                    .setTitle(title.orEmpty())
                    .setValue(value)
                    .setHint(hint)
                    .setError(error)
                    .setRequired(required)
                    .setVisible(visible)
                    .setTitleLayoutId(titleLayoutId)
                    .setErrorLayoutId(errorLayoutId)
                    .setEditLayoutId(editLayoutId)
                    .addAllValueObservers(valueObservers)
                    as FormSliderElement)
                    .setMax(max)
                    .setMin(min)
                    .setSteps(steps)
                    .setProgressLayoutId(progressLayoutId)
}

/** FormBuildHelper extension to add a FormSliderElement */
fun FormBuildHelper.slider(tag: Int = -1, init: SliderBuilder.() -> Unit): FormSliderElement {
    val element = SliderBuilder(tag).apply(init).build()
    element.id = ++lastId
    elements.add(element)
    return element
}

class TextViewBuilder(val tag: Int = -1) : FieldBuilder {
    var title: String? = null
    var value: String? = null
    var visible: Boolean = true
    @IdRes
    var editLayoutId: Int? = null
    @IdRes
    var titleLayoutId: Int? = null
    @IdRes
    var errorLayoutId: Int? = null
    val valueObservers = mutableListOf<(value: String?, element: BaseFormElement<String>) -> Unit>()
    override fun build() =
            (FormTextViewElement(tag)
                    .setTitle(title.orEmpty())
                    .setValue(value)
                    .setVisible(visible)
                    .setTitleLayoutId(titleLayoutId)
                    .setErrorLayoutId(errorLayoutId)
                    .setEditLayoutId(editLayoutId)
                    .addAllValueObservers(valueObservers)
                    as FormTextViewElement)
}

/** FormBuildHelper extension to add a FormTextViewElement */
fun FormBuildHelper.textView(tag: Int = -1, init: TextViewBuilder.() -> Unit): FormTextViewElement {
    val element = TextViewBuilder(tag).apply(init).build()
    element.id = ++lastId
    elements.add(element)
    return element
}


