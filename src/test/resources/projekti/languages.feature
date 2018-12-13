Feature: Users can choose a language

    Scenario: user can choose English
       Given the user chooses "English" when the app starts
       And   the app processes the input
       Then  the user will receive a prompt in English

    Scenario: user can choose Finnish
       Given the user chooses "Suomi" when the app starts
       And   the app processes the input
       Then  the user will receive a prompt in Finnish
