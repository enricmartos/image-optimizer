Feature: API endpoint
  As a user
  I want to test an API

  Scenario: Get simple log
    Given I set GET request
#    When I set MultipartFormData
    And I do GET request
    Then I receive valid HTTP response code 200