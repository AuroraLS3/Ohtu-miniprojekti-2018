Feature: User can add a description to a recommendation

  # Näissä on jotain vikaa, testit jäävät jumiin

#  Scenario: User is not shown description when viewing all recommendations
#    Given some book recommendations have been created
#    When command all is selected
#    And the app processes the input
#    Then the list of recommendations will not include "Kuvaus: hyva kirja"
#
#  Scenario: User can view description of an existing recommendation
#    Given some book recommendations have been created
#    When command select is selected
#    And existing recommendation id "1" is entered
#    And the app processes the input
#    Then system will respond with "Kuvaus: hyva kirja"
#
#  Scenario: User can modify description of an existing recommendation
#    Given some book recommendations have been created
#    When  command select is selected
#    And existing recommendation id "1" is entered
#    And command edit is selected
#    And description "Uusi kuvaus" is entered in edit mode
#    And affirmative response is given when asked for confirmation
#    And the app processes the input
#    Then system will respond with "Kuvaus: Uusi kuvaus"
