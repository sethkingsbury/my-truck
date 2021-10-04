Feature: MenuItems

  Scenario: Adding a menu item
    Given There is a MenuItem "Burger" with code "burg" of category "Main Course" that costs $6
    When I add "Burger" to the MenuItem database
    Then The MenuItem database contains "Burger"

  Scenario: Removing a MenuItem
    Given There is a MenuItem "Burger" with code "burg" of category "Main Course" that costs $6
    When I add "Burger" to the MenuItem database
    And I remove "Burger" from the MenuItem database
    Then The MenuItem database no longer contains "Burger"

  Scenario: Editing the price of an item
    Given There is a MenuItem "Burger" with code "burg" of category "Main Course" that costs $6
    When I add "Burger" to the MenuItem database
    And The MenuItem database contains "Burger"
    And I change the price of "Burger" to $5
    Then There is a MenuItem "Burger" with code "burg" of type "Main Course" that costs $5

  Scenario: Editing the category of an item
    Given There is a MenuItem "Burger" with code "burg" of category "Main Course" that costs $6
    When I add "Burger" to the MenuItem database
    And The MenuItem database contains "Burger"
    And I change the category of "Burger" to "Snack"
    Then There is a MenuItem "Burger" with code "burg" of category "Snack" that costs $6

  Scenario: Editing the name of an item
    Given There is a MenuItem "Burger" with code "burg" of category "Main Course" that costs $6
    When I add "Burger" to the MenuItem database
    And The MenuItem database contains "Burger"
    And I change the name of "Burger" to "Beef Burger"
    Then There is a MenuItem "Beef Burger" with code "burg" of type "Main Course" that costs $6