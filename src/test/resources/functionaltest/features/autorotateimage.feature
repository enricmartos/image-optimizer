Feature: Image auto-rotate

  @ignore
  @ok
  Scenario Outline: get the image auto-rotated with valid data
    Given AppMC is a client of the image-optimizer module
    When AppMC requests to auto-rotate an image
      |  originalImage  |
      | <originalImage> |
    Then the image-optimizer module returns the image auto-rotated
      |  expectedOrientation  | expectedResponseImage   |
      | <expectedOrientation> | <expectedResponseImage> |
    Examples:
      |  originalImage       |  expectedOrientation |      expectedResponseImage       |
      | monalisaWithEXIF.jpg | AUTOROTATED     | monalisaWithEXIF_autorotated.jpg |
      |   randomImage        |   AUTOROTATED        |                                  |

  @ignore
    @ko
  Scenario Outline: get the image auto-rotated with invalid data
    Given AppMC is a client of the image-optimizer module
    When AppMC requests to auto-rotate an image
      |  originalImage  |
      | <originalImage> |
    Then the request fails with a bad request
    Examples:
      |  originalImage  |
      |   index.html    |


  @ignore
  @ko
  Scenario: get the image auto-rotated with invalid api Key
    Given AppMC is a client of the image-optimizer module
    And AppMC has an invalid api key
    When AppMC requests to auto-rotate an image
      |  originalImage  |
      |  fullHD.jpg     |
    Then the request fails with a bad request

#  @ignore
  @ko
  Scenario: get the image auto-rotated with empty data
    Given AppMC is a client of the image-optimizer module
    When AppMC requests to auto-rotate an image
      |  originalImage  |
      |                 |
    Then the request fails with a bad request

