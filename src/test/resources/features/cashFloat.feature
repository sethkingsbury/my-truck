Feature: Cash Float
  Scenario: Receiving 10c coin
    Given There are 100 10c coins in the Cash Float
    When I receive 2 10c coins
    Then There is 102 10c coins in the Cash Float

  Scenario: Returning $10 Note
    Given There are 100 $10 Notes in the Cash Float
    When I give a 1 $10 Note in change
    Then There is 99 $10 Notes in the Cash Float
