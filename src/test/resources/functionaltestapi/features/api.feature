Feature: Image resize

#  Scenario: Get simple log
#    Given I set GET request
##    When I set MultipartFormData
#    When I do GET request
#    Then I receive valid HTTP response code 200
#
#  Scenario: Resize an image
#    Given I set resizeImage POST request
#    When I set MultipartFormData with image testimg.jpg, width 100 and height 100
#    And I do resizeImage POST request
#    Then I receive valid HTTP response code 200
#
#  Scenario: Resize an image and verify the output
#    Given I set resizeImage POST request
#    When I set MultipartFormData with image testimg.jpg, width 100 and height 100
#    And I do resizeImage POST request
#    Then I receive a valid resized image with width 100 and height 100

#  Scenario: Resize an image
#    Given I have an image MyImage
#    When I resize MyImage with filename testimg.jpg to width 100 and height 100
#    Then MyImage is resized with width 100 and height 100

  Scenario Outline: get the image resized with valid data
    Given AppMC is a client of the media-converter module
    When AppMC requests to resize an image
      |  originalImage  |  width  |  height  |
      | <originalImage> | <width> | <height> |
    Then the media-converter module returns after AppMC request
      |  resizedImage  |
      | <resizedImage> |
    Examples:
      |  originalImage  |  width  |  height  | resizedImage |
      |   testimg.jpg   |   100   |   100    | resizedImage |
