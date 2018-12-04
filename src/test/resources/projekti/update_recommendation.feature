Feature: Users can update existing recommendations

    Scenario: user can update an existing book recommendation and receives a message upon successful update
       Given some book recommendations have been created
       And   command update is selected
       And   recommendation type "book" is selected
       When  existing recommendation id "2" is entered
       And   new author "Reetta" is entered
       And   new title "Jolly Book" is entered
       And   new ISBN "987655555" is entered
       And   new url "http://www.faketestfaketestfaketesturl.com" is entered
       And   new description "" is entered
       And   affirmative response is given when asked for confirmation
       And   command all is selected
       And   the app processes the input
       Then  system will respond with "update successful"
       And   the list of recommendations will include "2. Reetta: Jolly Book, ISBN: 987655555, URL: http://www.faketestfaketestfaketesturl.com"

    Scenario: user can update an existing blog recommendation and receives a message upon succesful update
       Given some blog recommendations have been created
       And   command update is selected
       And   recommendation type "blog" is selected
       When  existing recommendation id "2" is entered
       And   new title "Jolliest Blog" is entered
       And   new url "http://www.faketestfaketestfaketesturl.com" is entered
       And   new description "" is entered
       And   affirmative response is given when asked for confirmation
       And   command all is selected
       And   the app processes the input
       Then  system will respond with "update successful"
       And   the list of recommendations will include "2. Jolliest Blog, URL: http://www.faketestfaketestfaketesturl.com"

    Scenario: user can update an existing other recommendation and receives a message upon succesful update
       Given some other recommendations have been created
       And   command update is selected
       And   recommendation type "other" is selected
       When  existing recommendation id "2" is entered
       And   new title "Jolliest Website" is entered
       And   new url "http://www.faketestfaketestfaketesturl.com" is entered
       And   new description "" is entered
       And   affirmative response is given when asked for confirmation
       And   command all is selected
       And   the app processes the input
       Then  system will respond with "update successful"
       And   the list of recommendations will include "2. Jolliest Website, URL: http://www.faketestfaketestfaketesturl.com"


    Scenario: user gets a warning message when trying to update a nonexisting recommendation
       Given some book recommendations have been created
       And   command update is selected
       And   recommendation type "book" is selected
       When  nonexisting recommendation id "42" is entered
       And   the app processes the input
       Then  system will respond with "No recommendation found"

    Scenario: user gets a confirmation message before actual update and can decide to revoke update
       Given some book recommendations have been created
       And   command update is selected
       And   recommendation type "book" is selected
       When  existing recommendation id "3" is entered
       And   new author "Minna" is entered
       And   new title "Jolly Book" is entered
       And   new ISBN "987655555" is entered
       And   new url "http://www.faketestfaketestfaketesturl.com" is entered
       And   new description "a jolly old book" is entered
       And   negative response is given when asked for confirmation
       And   command all is selected
       And   the app processes the input
       Then  system will respond with "recommendation update canceled"
       And   the list of recommendations will not include "3. Minna: Jolly Book, ISBN: 987655555"
       And   the list of recommendations will include "3. Heli: Kirjojen Kirja, ISBN: 777777333, URL: -"

    Scenario: user gets a warning message when entering a non-integer ID
       Given some book recommendations have been created
       And   command update is selected
       And   recommendation type "book" is selected
       When  invalid recommendation id "strings_are_not_valid_IDs" is entered
       And   the app processes the input
       Then  system will respond with "Not a valid ID. Has to be a number."
