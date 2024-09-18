@ignore
Feature: Password Change

  Rule: As a user
  I want to be able to change my password within the guidelines of the password policy
  So that the integrity of my account security requirements is maintained.

  Scenario: Change password successfully
    Given the user is logged in with username "user1" and password "currentPass123"
    When the user navigates to the change password page
    And the user enters the current password "currentPass123"
    And the user enters a new password "NewPass1"
    And the user confirms the new password "NewPass1"
    And the user submits the password change form
    Then the password should be changed successfully

  Scenario: Password does not meet length requirement
    Given the user is logged in with username "user2" and password "currentPass123"
    When the user navigates to the change password page
    And the user enters the current password "currentPass123"
    And the user enters a new password "N1"  # Less than 3 characters
    And the user confirms the new password "N1"
    And the user submits the password change form
    Then the password change should fail
    And an error message "Password must contain at least 3 characters" should be displayed

  Scenario: Password does not contain a digit
    Given the user is logged in with username "user3" and password "currentPass123"
    When the user navigates to the change password page
    And the user enters the current password "currentPass123"
    And the user enters a new password "NewPassword"  # No digit
    And the user confirms the new password "NewPassword"
    And the user submits the password change form
    Then the password change should fail
    And an error message "Password must contain at least 1 digit" should be displayed

  Scenario: New password and confirmation do not match
    Given the user is logged in with username "user4" and password "currentPass123"
    When the user navigates to the change password page
    And the user enters the current password "currentPass123"
    And the user enters a new password "NewPass123"
    And the user confirms the new password "DifferentPass123"
    And the user submits the password change form
    Then the password change should fail
    And an error message "Passwords do not match" should be displayed
