package com.mobile.diary

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import android.os.IBinder
import android.view.WindowManager
import androidx.test.espresso.Root
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ToastMatcher : TypeSafeMatcher<Root?>() {

    override fun describeTo(description: Description) {
        description.appendText("is toast")
    }

    override fun matchesSafely(root: Root?): Boolean {
        val type: Int = root?.windowLayoutParams?.get()?.type ?: return false
        if (type == WindowManager.LayoutParams.TYPE_TOAST) {
            val windowToken: IBinder = root.decorView.windowToken
            val appToken: IBinder = root.decorView.applicationWindowToken
            return windowToken == appToken
        }
        return false
    }

}
class WriteDiaryActivityTest {

    @Before
    fun setUp() {
        ActivityScenario.launch(WriteDiaryActivity::class.java)
    }

    @Test
    fun testUIComponents() {
        // Check if the required UI components are displayed
        Espresso.onView(ViewMatchers.withId(R.id.date)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.location)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.iv)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.content)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.takephoto)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.selectAlbums)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.clear)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.shared)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.save)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }


    @Test
    fun testPickDateButton_Click() {
        // Perform click on the pick date button
        Espresso.onView(ViewMatchers.withId(R.id.date)).perform(ViewActions.click())
    }

    @Test
    fun testCameraButton_Click() {
        // Perform click on the camera button
        Espresso.onView(ViewMatchers.withId(R.id.takephoto)).perform(ViewActions.click())
    }

    @Test
    fun testClearButton_Click() {
        // Perform click on the clear button
        Espresso.onView(ViewMatchers.withId(R.id.clear)).perform(ViewActions.click())

    }


}
