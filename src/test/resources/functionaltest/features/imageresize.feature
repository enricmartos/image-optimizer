Feature: Image resize

#  @ignore
  @ok
  Scenario Outline: get the image resized with valid data
    Given AppMC is a client of the image-optimizer module
    When AppMC requests to resize an image
      |  originalImage  |  width  |  height  |
      | <originalImage> | <width> | <height> |
    Then the image-optimizer module returns the image resized
      |  responseImage  |  expectedWidth  |  expectedHeight  |  expectedResponseImage  |
      | <responseImage> | <expectedWidth> | <expectedHeight> | <expectedResponseImage> |
    Examples:
      |  originalImage  |  width  |  height  | responseImage | expectedWidth | expectedHeight | expectedResponseImage |
#      |   fullHD.jpg    |   100   |   100    | responseImage |      100      |      100       |  fullHD_100x100.jpg  |
      |                 |   100   |   100    | resizedImage |      100      |      100       |                      |


#  @ignore
  @ko
  Scenario Outline: get the image resized with invalid data
    Given AppMC is a client of the image-optimizer module
    When AppMC requests to resize an image
      |  originalImage  |  width  |  height  |
      | <originalImage> | <width> | <height> |
    Then the request fails with a bad request
    Examples:
      |  originalImage  |  width  |  height  |
      |   fullHD.jpg    |    -1   |   100    |
      |   fullHD.jpg    |    100  |   -1     |
      |   fullHD.jpg    |   7680  |   100    |
      |   fullHD.jpg    |    100  |   4320   |
      |   fullHD.jpg    |    -1   |   4320   |

#  @ignore
  @ko
  Scenario: get the image resized with invalid api Key
    Given AppMC is a client of the image-optimizer module
    And AppMC has an invalid api key
    When AppMC requests to resize an image
      |  originalImage  |  width  |  height |
      |  fullHD.jpg     |   100   |   100   |
    Then the request fails with a bad request
