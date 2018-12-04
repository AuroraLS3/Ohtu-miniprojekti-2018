Feature: Users can see a list of existing recommendations

    Scenario: user can see a list of existing book recommendations
       Given some book recommendations have been created
       When  command all is selected
       And   the app processes the input
       Then  system will show a list of existing book recommendations
