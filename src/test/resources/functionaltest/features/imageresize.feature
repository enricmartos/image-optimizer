Feature: Image resize

  @ignore
  @ok
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
      |   fullHD.jpg   |   100   |   100    | resizedImage |
      |   fullHD.png   |   100   |   100    | resizedImage |

  @ignore
  @ko
  Scenario Outline: get the image resized with invalid data
    Given AppMC is a client of the media-converter module
    When AppMC requests to resize an image
      |  originalImage  |  width  |  height  |
      | <originalImage> | <width> | <height> |
    Then the media-converter module returns a bad request after AppMC request
    Examples:
      |  originalImage  |  width  |  height  |
      |   fullHD.jpg    |    -1   |   100    |
      |   fullHD.jpg    |    100  |   -1     |
      |   fullHD.jpg    |   7680  |   100    |
      |   fullHD.jpg    |    100  |   4320   |
      |   fullHD.jpg    |    -1   |   4320   |

  @ko
  Scenario: get the image resized with invalid api Key
    Given AppMC is a client of the media-converter module
    And AppMC has an invalid api key
    When AppMC requests to resize an image
      |  originalImage  |  width  |  height |
      |  fullHD.jpg     |   100   |   100   |
    Then the media-converter module returns a bad request after AppMC request
