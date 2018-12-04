Feature: Users can delete recommendations

    Scenario: user can delete an existing book recommendation
       Given some book recommendations have been created
       And   command delete is selected
       And   recommendation type "book" is selected
       When  existing recommendation id "2" is entered
       And   affirmative response is given when asked for confirmation
       And   the app processes the input
       Then  system will respond with "recommendation successfully deleted"

    Scenario: user can delete an existing blog recommendation
       Given some blog recommendations have been created
       And   command delete is selected
       And   recommendation type "blog" is selected
       When  existing recommendation id "2" is entered
       And   affirmative response is given when asked for confirmation
       And   the app processes the input
       Then  system will respond with "recommendation successfully deleted"

    Scenario: user can delete an existing other recommendation
       Given some other recommendations have been created
       And   command delete is selected
       And   recommendation type "other" is selected
       When  existing recommendation id "2" is entered
       And   affirmative response is given when asked for confirmation
       And   the app processes the input
       Then  system will respond with "recommendation successfully deleted"
    Scenario: user gets a warning message when trying to delete a nonexisting recommendation
       Given some book recommendations have been created
       And   command delete is selected
       And   recommendation type "book" is selected
       When  nonexisting recommendation id "23" is entered
       And   the app processes the input
       Then  system will respond with "No recommendation found"

    Scenario: user gets a confirmation message before actual deletion and can decide to revoke deletion
       Given some book recommendations have been created
       And   command delete is selected
       And   recommendation type "book" is selected
       When  existing recommendation id "3" is entered
       And   negative response is given when asked for confirmation
       And   the app processes the input
       Then  system will respond with "recommendation deletion canceled"

    Scenario: user gets a warning message when entering an invalid confirmation response
       Given some book recommendations have been created
       And   command delete is selected
       And   recommendation type "book" is selected
       When  existing recommendation id "3" is entered
       And   other response is given when asked for confirmation
       And   negative response is given when asked for confirmation
       And   the app processes the input
       Then  system will respond with "Invalid input"

    Scenario: user gets a warning message when entering a non-integer ID
       Given some book recommendations have been created
       And   command delete is selected
       And   recommendation type "book" is selected
       When  invalid recommendation id "strings_are_not_valid_IDs" is entered
       And   the app processes the input
       Then  system will respond with "Not a valid ID. Has to be a number."
