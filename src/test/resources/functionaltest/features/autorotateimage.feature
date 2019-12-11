Feature: Image auto-rotate

#  @ignore
  @ok
  Scenario Outline: get the image auto-rotated with valid data
    Given AppMC is a client of the image-optimizer module
    When AppMC requests to auto-rotate an image
      |  originalImage  |
      | <originalImage> |
    Then the image-optimizer module returns the image auto-rotated
      |  responseImage  |  expectedOrientation  | expectedResponseImage   |
      | <responseImage> | <expectedOrientation> | <expectedResponseImage> |
    Examples:
      |  originalImage       |  responseImage | expectedOrientation |      expectedResponseImage       |
      | monalisaWithEXIF.jpg |  responseImage |     AUTOROTATED     | monalisaWithEXIF_autorotated.jpg |
      |                      |  responseImage |     AUTOROTATED     |                                  |


