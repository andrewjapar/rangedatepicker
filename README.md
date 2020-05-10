# CPicker (Calendar Picker)
📆 A simple vertical date picker for Android, written in kotlin  🇮🇩

[![Android Arsenal]( https://img.shields.io/badge/Android%20Arsenal-CPicker%20(CalendarPicker)-green.svg?style=flat )]( https://android-arsenal.com/details/1/8025 )
[![Download](https://api.bintray.com/packages/andrewjapar/Android/CPicker/images/download.svg)](https://bintray.com/andrewjapar/Android/CPicker/_latestVersion)

## Screenshoot
<img src="https://github.com/andrewjapar/rangedatepicker/blob/master/screenshoot/rangepicker_screenshoot_1.png" width="250">&nbsp;&nbsp;<img src="https://github.com/andrewjapar/rangedatepicker/blob/master/screenshoot/rangepicker_ux.gif" width="250">

## Installation
### Gradle
```kotlin
repositories {
    jcenter()
}

dependencies {
    implementation 'com.andrewjapar.rangedatepicker:rangedatepicker:0.2.4'
}
```

## Usage
### 1. Add CalendarPicker to your XML file
```xml
<com.andrewjapar.rangedatepicker.CalendarPicker
      android:id="@+id/calendar_view"
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      android:clipToPadding="false"
      android:scrollbarStyle="outsideOverlay"
      app:picker_type="range" />
```
### 2. You can customize the picker range (by default current date + 1 year)
```kotlin
val startDate = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault())
val endDate = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault())
endDate.add(Calendar.MONTH, 6) // Add 6 months ahead from current date
 
calendar_view.apply {
  showDayOfWeekTitle(false) // If you want to disable day of the week title, just make it false
  setMode(SelectionMode.RANGE) // You can set it via XML
  setRangeDate(startDate.time, endDate.time)
  setSelectionDate(startDate.time, endDate.time)
}
```
### 3. Don't forget to set the listener to get range selection date
```kotlin
// Set listener first before set the selection to ensure we can track changed date range
// This will be called when range is selected
calendar_view.setOnRangeSelectedListener { startDate, endDate, startLabel, endLabel ->
    departure_date.text = startLabel
    return_date.text = endLabel
}

// This will be called when only single day is selected for both SINGLE and RANGE type
calendar_view.setOnStartSelectedListener { startDate, label ->
    departure_date.text = label
    return_date.text = "-"
}
```
### 4. But wait, there is more! If you want to custom text color and background, just override it on your `colors.xml` file
```xml
<!--Background color-->
<color name="calendar_picker_bg">#00FFFFFF</color>

<color name="calendar_day_unselected_bg">#00000000</color>
<color name="calendar_day_selected_bg">#E91E63</color>
<color name="calendar_day_range_selected_bg">#8CE91E63</color>

<!--Font color-->
<color name="calendar_day_disabled_font">#8CE91E63</color>
<color name="calendar_day_weekend_font">#ff0000</color>
<color name="calendar_day_selected_font">#FFFFFF</color>
<color name="calendar_day_normal_font">#333333</color>

<color name="calendar_month_font">#000000</color>
<color name="calendar_week_font">#333333</color>
```
### 5. You also can override the value of `dimens.xml`
```xml
<dimen name="calendar_day_height">36dp</dimen>
<dimen name="calendar_day_gap">8dp</dimen>
<dimen name="calendar_day_textsize">12sp</dimen>

<dimen name="calendar_month_padding">16dp</dimen>
<dimen name="calendar_month_textsize">16sp</dimen>

<dimen name="calendar_week_textsize">12sp</dimen>
```

## Future Plan
1. Support Horizontal View like Traveloka, Expedia, Airbnb, etc
2. Change font color when the day is weekend
3. Add a marker on some dates (e.g for public holidays)
4. Render optimization
5. Please let me know, if you have any other ideas

## Contributing
Currently, CPicker is used only on my personal project but if you have any idea to make it more powerfull, just make a pull request, You are in!

## License
```
    MIT License
    
    Copyright (c) 2020 andrewjapar
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publiAsh, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
