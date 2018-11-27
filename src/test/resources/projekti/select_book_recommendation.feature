Feature: Users can select specific recommendation

    Scenario: user can select a recommendation to see its description
       Given some book recommendations have been created
       And command select is selected
       When existing recommendation id "2" is entered
       And command return is entered
       And   the app processes the input
       Then  system will respond with "Description: hyva kirja"
