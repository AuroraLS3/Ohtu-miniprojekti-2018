Feature: Users can add recommendations

  Scenario: user can add a new book recommendation
    Given command new is selected
    And   recommendation type "book" is selected
    When  author "Pekka" title "Hieno Kirja" and ISBN "123456789" are entered
    And   no description is entered
    And   the app processes the input
    Then  system will respond with "new book recommendation added"

  Scenario: user can add a new blog recommendation
    Given command new is selected
    And   recommendation type "blog" is selected
    When  new title "Hieno Blogi" is entered
    And   new url "http://www.faketestfaketestfaketesturl.com" is entered
    And   no description is entered
    And   the app processes the input
    Then  system will respond with "new blog recommendation added"

  Scenario: user cannot add a new blog with an invalid URL
    Given command new is selected
    And   recommendation type "blog" is selected
    When  new title "Hieno Blogi" is entered
    And   new url "faketestfaketestfaketesturl" is entered
    And   new url "w.faketestfaketestfaketesturl.c" is entered
    And   new url "http://www.faketestfaketestfaketesturl.com" is entered
    And   no description is entered
    And   the app processes the input
    Then  system will respond with "new blog recommendation added"
  
  Scenario: user can add a new other recommendation
    Given command new is selected
    And   recommendation type "other" is selected
    When  new title "Hieno Sivusto" is entered
    And   new url "http://www.faketestfaketestfaketesturl.com" is entered
    And   no description is entered
    And   the app processes the input
    Then  system will respond with "new other recommendation added"
