package com.lifelog.feature.today

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.lifelog.app.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class TodayScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun todayScreen_displaysCorrectly() {
        // For this test to pass, you need to ensure you're on the Today screen.
        // A more robust test would use navigation to get to the correct screen.
        
        composeTestRule.onNodeWithText("How are you feeling?").assertIsDisplayed()
        composeTestRule.onNodeWithText("My mood").assertIsDisplayed()
        composeTestRule.onNodeWithText("Energy").assertIsDisplayed()
        composeTestRule.onNodeWithText("Stress").assertIsDisplayed()
        composeTestRule.onNodeWithText("Save Entry").assertIsDisplayed()
    }
}
