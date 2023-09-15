package com.amcwustl.dailytarot;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class DailyReadingTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void dailyReadingTest() {
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.LoginActivityLoginEditTextTextEmailAddress),
                        childAtPosition(
                                allOf(withId(R.id.contentContainer),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                1)),
                                2),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("amclarkwustl+demo2@gmail.com"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.LoginActivityLoginEditTextTextEmailAddress), withText("amclarkwustl+demo2@gmail.com"),
                        childAtPosition(
                                allOf(withId(R.id.contentContainer),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                1)),
                                2),
                        isDisplayed()));
        appCompatEditText2.perform(pressImeActionButton());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.LoginActivityPasswordEditTextTextPassword),
                        childAtPosition(
                                allOf(withId(R.id.contentContainer),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                1)),
                                3),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("test123!"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.LoginActivityLoginButton), withText("Login!"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.contentContainer),
                                        4),
                                0),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open"),
                        childAtPosition(
                                allOf(withId(com.amplifyframework.core.R.id.action_bar),
                                        childAtPosition(
                                                withId(com.amplifyframework.core.R.id.action_bar_container),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction navigationMenuItemView = onView(
                allOf(withId(R.id.nav_reading),
                        childAtPosition(
                                allOf(withId(com.amplifyframework.core.R.id.design_navigation_view),
                                        childAtPosition(
                                                withId(R.id.MainActivityNavigationView),
                                                0)),
                                3),
                        isDisplayed()));
        navigationMenuItemView.perform(click());

        ViewInteraction button = onView(
                allOf(withId(R.id.ReadingActivityRewardAdButton), withText("Watch Ad To Get More Readings"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction imageView = onView(
                allOf(withId(R.id.ReadingActivityDrawnCardOne), withContentDescription("First Drawn Card"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        imageView.check(matches(isDisplayed()));

        ViewInteraction imageView2 = onView(
                allOf(withId(R.id.ReadingActivityDrawnCardTwo), withContentDescription("Second Drawn Card"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        imageView2.check(matches(isDisplayed()));

        ViewInteraction imageView3 = onView(
                allOf(withId(R.id.ReadingActivityDrawnCardThree), withContentDescription("Third Drawn Card"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        imageView3.check(matches(isDisplayed()));

        ViewInteraction imageView4 = onView(
                allOf(withId(R.id.ReadingActivityDeckImage), withContentDescription("Deck of Cards"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        imageView4.check(matches(isDisplayed()));

        ViewInteraction imageView5 = onView(
                allOf(withId(R.id.ReadingActivityDeckImage), withContentDescription("Deck of Cards"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        imageView5.check(matches(isDisplayed()));

        ViewInteraction imageView6 = onView(
                allOf(withId(R.id.ReadingActivityDeckImage), withContentDescription("Deck of Cards"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        imageView6.check(matches(isDisplayed()));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
