package com.example.android.medmanagerapplication;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class UserProfileActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void userProfileActivityTest() {
        pressBack();

        ViewInteraction pl = onView(
                allOf(withText("Sign in"),
                        childAtPosition(
                                allOf(withId(R.id.sign_in_button),
                                        childAtPosition(
                                                withId(R.id.login_form),
                                                6)),
                                0)));
        pl.perform(scrollTo(), click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.firstname),
                        childAtPosition(
                                allOf(withId(R.id.login_form),
                                        childAtPosition(
                                                withClassName(is("android.support.constraint.ConstraintLayout")),
                                                1)),
                                0)));
        appCompatEditText.perform(scrollTo(), click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.firstname),
                        childAtPosition(
                                allOf(withId(R.id.login_form),
                                        childAtPosition(
                                                withClassName(is("android.support.constraint.ConstraintLayout")),
                                                1)),
                                0)));
        appCompatEditText2.perform(scrollTo(), replaceText("George"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.lastname),
                        childAtPosition(
                                allOf(withId(R.id.login_form),
                                        childAtPosition(
                                                withClassName(is("android.support.constraint.ConstraintLayout")),
                                                1)),
                                1)));
        appCompatEditText3.perform(scrollTo(), replaceText("Udosen"), closeSoftKeyboard());

        ViewInteraction appCompatAutoCompleteTextView = onView(
                allOf(withId(R.id.email),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.design.widget.TextInputLayout")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatAutoCompleteTextView.perform(replaceText("date@gmail.com"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.password),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.design.widget.TextInputLayout")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("hello"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.emailSignIn_btn), withText("Sign in or register"),
                        childAtPosition(
                                allOf(withId(R.id.login_form),
                                        childAtPosition(
                                                withClassName(is("android.support.constraint.ConstraintLayout")),
                                                1)),
                                4)));
        appCompatButton.perform(scrollTo(), click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(android.R.id.button1), withText("Yes"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.buttonPanel),
                                        0),
                                3)));
        appCompatButton2.perform(scrollTo(), click());

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.title), withText("User Profile"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.v7.view.menu.ListMenuItemView")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction editText = onView(
                allOf(withId(R.id.userProfileFirstName_EditText), withText("George"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.include),
                                        0),
                                2),
                        isDisplayed()));
        editText.check(matches(withText("George")));

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.userProfileFirstName_EditText), withText("George"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.include),
                                        0),
                                2),
                        isDisplayed()));
        editText2.check(matches(isDisplayed()));

        ViewInteraction editText3 = onView(
                allOf(withId(R.id.userProfileLastName_EditText), withText("Udosen"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.include),
                                        0),
                                4),
                        isDisplayed()));
        editText3.check(matches(withText("Udosen")));

        ViewInteraction editText4 = onView(
                allOf(withId(R.id.userProfileLastName_EditText), withText("Udosen"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.include),
                                        0),
                                4),
                        isDisplayed()));
        editText4.check(matches(isDisplayed()));

        ViewInteraction textView = onView(
                allOf(withId(R.id.textView6), withText("User Profile"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.include),
                                        0),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("User Profile")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.textView6), withText("User Profile"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.include),
                                        0),
                                0),
                        isDisplayed()));
        textView2.check(matches(isDisplayed()));

        ViewInteraction textView3 = onView(
                allOf(withText("User Profile"),
                        childAtPosition(
                                allOf(withId(R.id.action_bar),
                                        childAtPosition(
                                                withId(R.id.action_bar_container),
                                                0)),
                                0),
                        isDisplayed()));
        textView3.check(matches(withText("User Profile")));

        ViewInteraction textView4 = onView(
                allOf(withText("User Profile"),
                        childAtPosition(
                                allOf(withId(R.id.action_bar),
                                        childAtPosition(
                                                withId(R.id.action_bar_container),
                                                0)),
                                0),
                        isDisplayed()));
        textView4.check(matches(isDisplayed()));

        ViewInteraction imageView = onView(
                allOf(withContentDescription("More options"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.action_bar),
                                        1),
                                0),
                        isDisplayed()));
        imageView.check(matches(isDisplayed()));

        ViewInteraction imageView2 = onView(
                allOf(withContentDescription("More options"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.action_bar),
                                        1),
                                0),
                        isDisplayed()));
        imageView2.check(matches(isDisplayed()));

        pressBack();

        ViewInteraction editText5 = onView(
                allOf(withId(R.id.userProfilePassword_EditText),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.textInputLayout),
                                        0),
                                0),
                        isDisplayed()));
        editText5.check(matches(isDisplayed()));

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.textView5), withText("Password:"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.include),
                                        0),
                                7),
                        isDisplayed()));
        textView5.check(matches(withText("Password:")));

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.textView5), withText("Password:"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.include),
                                        0),
                                7),
                        isDisplayed()));
        textView6.check(matches(isDisplayed()));

        ViewInteraction textView7 = onView(
                allOf(withId(R.id.emailLabelTextView), withText("Email Address:"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.include),
                                        0),
                                5),
                        isDisplayed()));
        textView7.check(matches(withText("Email Address:")));

        ViewInteraction textView8 = onView(
                allOf(withId(R.id.emailLabelTextView), withText("Email Address:"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.include),
                                        0),
                                5),
                        isDisplayed()));
        textView8.check(matches(isDisplayed()));

        ViewInteraction textView9 = onView(
                allOf(withId(R.id.profileLastNameLabelextView), withText("Last Name:"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.include),
                                        0),
                                3),
                        isDisplayed()));
        textView9.check(matches(withText("Last Name:")));

        ViewInteraction textView10 = onView(
                allOf(withId(R.id.profileLastNameLabelextView), withText("Last Name:"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.include),
                                        0),
                                3),
                        isDisplayed()));
        textView10.check(matches(isDisplayed()));

        ViewInteraction textView11 = onView(
                allOf(withId(R.id.profileLabelTextView), withText("First Name:"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.include),
                                        0),
                                1),
                        isDisplayed()));
        textView11.check(matches(withText("First Name:")));

        ViewInteraction textView12 = onView(
                allOf(withId(R.id.profileLabelTextView), withText("First Name:"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.include),
                                        0),
                                1),
                        isDisplayed()));
        textView12.check(matches(isDisplayed()));

        ViewInteraction imageButton = onView(
                allOf(withId(R.id.profile_fab_left),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.include),
                                        0),
                                9),
                        isDisplayed()));
        imageButton.check(matches(isDisplayed()));

        ViewInteraction imageButton2 = onView(
                allOf(withId(R.id.profile_fab_right),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.include),
                                        0),
                                10),
                        isDisplayed()));
        imageButton2.check(matches(isDisplayed()));

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
