Feature: Basket
  Scenario: Adding a single item to an empty order
    Given An item burger is priced at $5
    And The basket is empty
    When I add 1 burger to the basket
    Then The basket contains 1 burger
    And The total price is $5

  Scenario: Adding a single item to an order already containing an item
    Given An item burger is priced at $5
    And The basket contains 1 hotdog priced at $3
    When I add 1 burger to the basket
    Then The order contains 1 burger and 1 hotdog
    And The total price is $8

  Scenario: Removing an item from the empty basket
    Given The basket contains 1 burger priced at $5
    When I remove 1 burger from the basket
    Then The basket contains 0 items
    And The total price is $0

  Scenario: Removing an item from the basket that contains an item
    Given The basket contains 1 burger priced at $5
    And The basket contains 1 hotdog priced at $3
    When I remove 1 burger from the basket
    Then The basket contains 1 hotdog
    And The total price is $3

  Scenario: Modifying items in the basket
    Given The basket contains 1 burger priced at $5
    When I add bacon to the burger
    Then The burger contains bacon

  Scenario: Order confirmation
    Given The basket contains 1 burger priced at $5
    When I confirm the order
    Then The sales screen resets