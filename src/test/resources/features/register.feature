Feature: register a user
    Scenario Outline: Test User Registration
      Given I navigate to "https://membership.basketballengland.co.uk/NewSupporterAccount" on "<browser>"
      When I enter a valid dob "<dob>"
      And I enter a valid firstname "<firstname>"
      And I enter a valid lastname "<lastname>"
      And I enter a valid email "<email>"
      And I enter a valid confirmEmail "<confirmEmail>"
      And I enter a valid password "<password>"
      And I enter a valid confirmPassword "<confirmPassword>"
      And I selected terms "<terms>"
      And I selected ageAccept "<ageAccept>"
      And I selected codeOfEthics "<codeOfEthics>"
      And I click button
      Then I should see a "<expectedMessage>" message
      Examples:
        | browser | dob        | firstname | lastname | email                | confirmEmail         | password  | confirmPassword | terms | ageAccept | codeOfEthics | expectedMessage                                                           |
        | chrome  | 01/02/1989 | test      |          | testuser89@gmail.com | testuser89@gmail.com | qwerty123 | qwerty123       | true  | true      | true         | Last Name is required                                                     |
        | chrome  | 01/02/1989 | test      | user     | testuser89@gmail.com | testuser89@gmail.com | qwerty123 | qwerty          | true  | true      | true         | Password did not match                                                    |
        | firefox | 01/02/1989 | test      | user     | testuser89@gmail.com | testuser89@gmail.com | qwerty123 | qwerty123       | false | true      | true         | You must confirm that you have read and accepted our Terms and Conditions |
        | chrome  | 01/02/1989 | test      | user     | testuser89@gmail.com | testuser89@gmail.com | qwerty123 | qwerty123       | true  | true      | true         | THANK YOU FOR CREATING AN ACCOUNT WITH BASKETBALL ENGLAND                 |