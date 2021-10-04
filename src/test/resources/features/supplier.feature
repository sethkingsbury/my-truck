Feature: Supplier Database

  Scenario: Adding a supplier to the database
    Given There is a supplier "Josh" with contact number "0800838383"
    When I add "Josh" to the supplier database
    Then The supplier database contains "Josh"

  Scenario: Removing a supplier from the database
    Given There is a supplier "Josh" with contact number "0800838383"
    When I add "Josh" to the supplier database
    And The supplier database contains "Josh"
    And I remove "Josh" from the suppliers database
    Then The supplier database does not contain "Josh"

  Scenario: Changing a suppliers contact number
    Given There is a supplier "Josh" with contact number "0800838383"
    When I add "Josh" to the supplier database
    And The supplier database contains "Josh"
    And I change the contact number of "Josh" to "0800304050"
    Then There is a supplier "Josh" with contact number "0800304050"
