Feature: Ingredient Database

  Scenario: Adding an ingredient to the database
    Given There is an ingredient "pepper"
    When I add "pepper" to the ingredient database
    Then The ingredient database contains "pepper"

  Scenario: Removing an ingredient from the inventory
    Given There is an ingredient "pork"
    And I add "pork" to the ingredient database
    And The ingredient database contains "pork"
    When I remove "pork" from the ingredient database
    Then The ingredient database no longer contains "pork"

  Scenario: Increasing stock level of an ingredient
    Given There is an ingredient "beef" with 100 units
    And I add "beef" to the ingredient database
    And The ingredient database contains "beef"
    When I add 10 units of "beef"
    Then There is 110 units of "beef"

  Scenario: Decreasing stock level of an ingredient
    Given There is an ingredient "bellpepper" with 100 units
    And I add "bellpepper" to the ingredient database
    And The ingredient database contains "bellpepper"
    When I remove 10 units of "bellpepper"
    Then There is 90 units of "bellpepper"
