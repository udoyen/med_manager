package com.example.android.medmanagerapplication;


import android.support.test.espresso.DataInteraction;
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
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SettingsActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void settingsActivityTest() {


        ViewInteraction pl = onView(
                allOf(withText("Sign in"),
                        childAtPosition(
                                allOf(withId(R.id.sign_in_button),
                                        childAtPosition(
                                                withId(R.id.login_form),
                                                6)),
                                0)));

        ViewInteraction appCompatAutoCompleteTextView = onView(
                allOf(withId(R.id.email),
                        childAtPosition(
                                childAtPosition(
                                          withClassName(is("android.support.design.widget.TextInputLayout")),
                                      0),
                                0),
                        isDisplayed()));

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.password),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.design.widget.TextInputLayout")),
                                        0),
                                0),
                        isDisplayed()));

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.emailSignIn_btn), withText("Sign in or register"),
                        childAtPosition(
                                allOf(withId(R.id.login_form),
                                        childAtPosition(
                                                withClassName(is("android.support.constraint.ConstraintLayout")),
                                                1)),
                                4)));

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.firstname),
                        childAtPosition(
                                allOf(withId(R.id.login_form),
                                        childAtPosition(
                                                withClassName(is("android.support.constraint.ConstraintLayout")),
                                                1)),
                                0)));

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.lastname),
                        childAtPosition(
                                allOf(withId(R.id.login_form),
                                        childAtPosition(
                                                withClassName(is("android.support.constraint.ConstraintLayout")),
                                                1)),
                                1)));

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.emailSignIn_btn), withText("Sign in or register"),
                        childAtPosition(
                                allOf(withId(R.id.login_form),
                                        childAtPosition(
                                                withClassName(is("android.support.constraint.ConstraintLayout")),
                                                1)),
                                4)));

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(android.R.id.button1), withText("Yes"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.buttonPanel),
                                        0),
                                3)));

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.update_drug_detail),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.nameOfDrug),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                1)));

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.drug_description),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                2)));

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.startDate_editText),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                5)));

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                allOf(withClassName(is("android.widget.LinearLayout")),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                3)),
                                3),
                        isDisplayed()));

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.endDate_editText),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                0)));

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                allOf(withClassName(is("android.widget.LinearLayout")),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                3)),
                                3),
                        isDisplayed()));

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(R.id.increment), withText("+"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.detail_Number_picker),
                                        0),
                                2)));

        ViewInteraction appCompatButton7 = onView(
                allOf(withId(R.id.decrement), withText("—"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.detail_Number_picker),
                                        0),
                                0)));

        ViewInteraction floatingActionButton2 = onView(
                allOf(withId(R.id.update_drug_detail),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                8)));

        ViewInteraction floatingActionButton3 = onView(
                allOf(withId(R.id.fab_backBtn),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                9)));

        ViewInteraction view = onView(
                allOf(withId(R.id.contrain_box),
                        childAtPosition(
                                allOf(withId(R.id.rv_drugs),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.RelativeLayout.class),
                                                0)),
                                0),
                        isDisplayed()));

        ViewInteraction textView = onView(
                allOf(withId(R.id.drugName_textView), withText("Ckae"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.drugList_Container),
                                        0),
                                0),
                        isDisplayed()));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.interval_textView), withText("Medication Interval 1"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.drugList_Container),
                                        0),
                                1),
                        isDisplayed()));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.starDate_textView), withText("Starts: 19/04/2018"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.drugList_Container),
                                        1),
                                0),
                        isDisplayed()));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.duration_textView), withText("Duration 11 days"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.drugList_Container),
                                        1),
                                1),
                        isDisplayed()));

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.duration_textView), withText("Duration 11 days"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.drugList_Container),
                                        1),
                                1),
                        isDisplayed()));

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.endDate_textView), withText("Ends: 30/04/2018"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.drugList_Container),
                                        1),
                                2),
                        isDisplayed()));

        ViewInteraction linearLayout = onView(
                allOf(withId(R.id.drugList_Container),
                        childAtPosition(
                                allOf(withId(R.id.contrain_box),
                                        childAtPosition(
                                                withId(R.id.rv_drugs),
                                                0)),
                                0),
                        isDisplayed()));

        ViewInteraction linearLayout2 = onView(
                allOf(withId(R.id.drugList_Container),
                        childAtPosition(
                                allOf(withId(R.id.contrain_box),
                                        childAtPosition(
                                                withId(R.id.rv_drugs),
                                                0)),
                                0),
                        isDisplayed()));

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.title), withText("User Settings"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.v7.view.menu.ListMenuItemView")),
                                        0),
                                0),
                        isDisplayed()));

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.list),
                        childAtPosition(
                                withClassName(is("android.widget.FrameLayout")),
                                0)));

        DataInteraction appCompatCheckedTextView = onData(anything())
                .inAdapterView(allOf(withId(R.id.select_dialog_listview),
                        childAtPosition(
                                withId(R.id.contentPanel),
                                1)))
                .atPosition(0);



        ViewInteraction appCompatTextView2 = onView(
                allOf(withId(R.id.title), withText("User Settings"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.v7.view.menu.ListMenuItemView")),
                                        0),
                                0),
                        isDisplayed()));

        ViewInteraction relativeLayout = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.list),
                                0),
                        0),
                        isDisplayed()));

        ViewInteraction linearLayout3 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.list),
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class),
                                        0)),
                        0),
                        isDisplayed()));

        ViewInteraction textView7 = onView(
                allOf(withText("Signin Method"),
                        childAtPosition(
                                allOf(withId(R.id.list),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class),
                                                0)),
                                1),
                        isDisplayed()));

        ViewInteraction textView8 = onView(
                allOf(withText("Signin Method"),
                        childAtPosition(
                                allOf(withId(R.id.list),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class),
                                                0)),
                                1),
                        isDisplayed()));

        ViewInteraction relativeLayout2 = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.list),
                                2),
                        0),
                        isDisplayed()));

        ViewInteraction textView9 = onView(
                allOf(withId(android.R.id.title), withText("SignIn Path"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        0),
                                0),
                        isDisplayed()));

        ViewInteraction textView10 = onView(
                allOf(withId(android.R.id.summary), withText("Google Account"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        0),
                                1),
                        isDisplayed()));

        ViewInteraction checkBox = onView(
                allOf(withId(android.R.id.checkbox),
                        childAtPosition(
                                allOf(withId(android.R.id.widget_frame),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                1)),
                                0),
                        isDisplayed()));

        ViewInteraction textView11 = onView(
                allOf(withText("User Settings"),
                        childAtPosition(
                                allOf(withId(R.id.action_bar),
                                        childAtPosition(
                                                withId(R.id.action_bar_container),
                                                0)),
                                1),
                        isDisplayed()));

        ViewInteraction textView12 = onView(
                allOf(withText("User Settings"),
                        childAtPosition(
                                allOf(withId(R.id.action_bar),
                                        childAtPosition(
                                                withId(R.id.action_bar_container),
                                                0)),
                                1),
                        isDisplayed()));

        ViewInteraction imageButton = onView(
                allOf(withContentDescription("Navigate up"),
                        childAtPosition(
                                allOf(withId(R.id.action_bar),
                                        childAtPosition(
                                                withId(R.id.action_bar_container),
                                                0)),
                                0),
                        isDisplayed()));


        ViewInteraction imageButton2 = onView(
                allOf(withContentDescription("Navigate up"),
                        childAtPosition(
                                allOf(withId(R.id.action_bar),
                                        childAtPosition(
                                                withId(R.id.action_bar_container),
                                                0)),
                                0),
                        isDisplayed()));

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
