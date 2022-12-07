package com.example.tubespbp


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun loginActivityTest() {
        val textInputEditText = onView(
            allOf(
                childAtPosition(
                    childAtPosition(
                        withId(R.id.inputLayoutUsername),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText.perform(click())

        val textInputEditText2 = onView(
            allOf(
                childAtPosition(
                    childAtPosition(
                        withId(R.id.inputLayoutUsername),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText2.perform(click())

        val textInputEditText3 = onView(
            allOf(
                childAtPosition(
                    childAtPosition(
                        withId(R.id.inputLayoutUsername),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText3.perform(replaceText("admin"), closeSoftKeyboard())

        val textInputEditText4 = onView(
            allOf(
                childAtPosition(
                    childAtPosition(
                        withId(R.id.inputLayoutPassword),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText4.perform(replaceText("admin123"), closeSoftKeyboard())

        val checkableImageButton = onView(
            allOf(
                withId(com.google.android.material.R.id.text_input_end_icon),
                withContentDescription("Show password"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        checkableImageButton.perform(click())

        val materialButton = onView(
            allOf(
                withId(R.id.btnLogin), withText("Login"),
                childAtPosition(
                    allOf(
                        withId(R.id.linearLayout2),
                        childAtPosition(
                            withId(R.id.loginactivity),
                            4
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }

    fun waitFor(delay: Long): ViewAction? {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isRoot()
            }

            override fun getDescription(): String {
                return "Wait for $delay milliseconds."
            }

            override fun perform(uiController: UiController?, view: View?) {
                uiController?.loopMainThreadForAtLeast(delay)
            }
        }
    }
}
