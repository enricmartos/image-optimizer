Feature: Image resize

#  @ignore
  @ok
  Scenario Outline: get the image resized with valid data
    Given AppMC is a client of the media-converter module
    When AppMC requests to resize an image
      |  originalImage  |  width  |  height  |
      | <originalImage> | <width> | <height> |
    Then the media-converter module returns
      |  resizedImage  |  expectedWidth  |  expectedHeight  |  expectedResizedImage  |
      | <resizedImage> | <expectedWidth> | <expectedHeight> | <expectedResizedImage> |
    Examples:
      |  originalImage  |  width  |  height  | resizedImage | expectedWidth | expectedHeight | expectedResizedImage |
      |   fullHD.jpg    |   100   |   100    | resizedImage |      100      |      100       |  fullHD_100x100.jpg  |
#      |   fullHD.png    |   50   |   25    | resizedImage |      50      |      25       |  fullHD_50x25.jpg  |
      |                 |   100   |   100    | resizedImage |      100      |      100       |                      |

     # new scenario
     # compare with ground truth images pixel by pixel

     # if empty example -> java randomize (weather:
#  @ignore
  @ko
  Scenario Outline: get the image resized with invalid data
    Given AppMC is a client of the media-converter module
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
    Given AppMC is a client of the media-converter module
    And AppMC has an invalid api key
    When AppMC requests to resize an image
      |  originalImage  |  width  |  height |
      |  fullHD.jpg     |   100   |   100   |
    Then the request fails with a bad request
