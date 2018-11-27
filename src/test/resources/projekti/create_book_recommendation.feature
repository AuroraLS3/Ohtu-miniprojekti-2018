Feature: Users can add book recommendations

  Scenario: user can add a new book recommendation
    Given command new is selected
    When  author "Pekka" title "Hieno Kirja" and ISBN "123456789" are entered
    And   no description is entered
    And   the app processes the input
    Then  system will respond with "new book recommendation added"

