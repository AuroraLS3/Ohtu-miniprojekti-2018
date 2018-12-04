Feature: Users can select a specific recommendation

  Scenario: user can select a recommendation to see its description
    Given    some book recommendations have been created
    And      command select is selected
    And      recommendation type "book" is selected
    When     existing recommendation id "2" is entered
    And      command return is entered
    And      the app processes the input
    Then     system will respond with "2. Reetta: Great Book, ISBN: 111122222"
    Then     system will respond with "Kuvaus: hyva kirja"

  Scenario: user gets a warning message when trying to select a nonexisting recommendation
    Given   some book recommendations have been created
    And     command select is selected
    And     recommendation type "book" is selected
    When    nonexisting recommendation id "43" is entered
    And     command return is entered
    And     the app processes the input
    Then    system will respond with "No recommendation found"

  Scenario: user can edit a selected recommendation
    Given   some book recommendations have been created
    And     command select is selected
    And     recommendation type "book" is selected
    When    existing recommendation id "2" is entered
    And     command edit is entered
    And     new author "New Author" is entered
    And     new title "Greatest Book" is entered
    And     new ISBN "77777711133" is entered
    And     new url "http://www.faketestfaketestfaketesturl.com" is entered
    And     new description "new description" is entered
    And     affirmative response is given when asked for confirmation
    And     command return is entered
    And     the app processes the input
    Then    system will respond with "2. New Author: Greatest Book, ISBN: 77777711133"
    Then     system will respond with "Kuvaus: new description"

  Scenario: user can delete selected recommendation
    Given some book recommendations have been created
    And   command select is selected
    And   recommendation type "book" is selected
    When  existing recommendation id "2" is entered
    And   command delete is selected
    And   affirmative response is given when asked for confirmation
    And   the app processes the input
    Then  system will respond with "recommendation successfully deleted"
