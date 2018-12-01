Feature: Users can select specific recommendation

  Scenario: user can select a recommendation to see its description
    Given    some book recommendations have been created
    And      command select is selected
    When     existing recommendation id "2" is entered
    And      command return is entered
    And      the app processes the input
    Then     system will respond with "2. Reetta: Great Book, ISBN: 111122222"
    Then     system will respond with "Kuvaus: hyva kirja"

  Scenario: user gets a warning message when trying to select a nonexisting recommendation
    Given   some book recommendations have been created
    And     command select is selected
    When    nonexisting recommendation id "43" is entered
    And     command return is entered
    And     the app processes the input
    Then    system will respond with "No book found"

  Scenario: user can edit a selected recommendation
    Given   some book recommendations have been created
    And     command select is selected
    When    existing recommendation id "2" is entered
    And     command edit is entered
    And     new author "" is entered
    And     new title "Greatest Book" is entered
    And     new ISBN "" is entered
    And     new description "" is entered
    And     affirmative response is given when asked for confirmation
    And     command return is entered
    And     the app processes the input
    Then    system will respond with "2. Reetta: Greatest Book, ISBN: 111122222"
    Then     system will respond with "Kuvaus: hyva kirja"

  Scenario: user can delete selected recommendation
    Given some book recommendations have been created
    And   command select is selected
    When  existing recommendation id "2" is entered
    And command delete is selected
    And   affirmative response is given when asked for confirmation
    And   the app processes the input
    Then  system will respond with "recommendation successfully deleted"
    