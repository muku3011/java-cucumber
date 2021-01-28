Feature: Basic web ui testing in browser

  #Background:
    #Given Setup driver path

  Rule: Open and Search

    Scenario: Open browser and search
      Given Browser driver is configured
      When Open 'https://www.gsmarena.com/'
      Then Search for element "Apple" and verify it's latest phone is listed
      Then Close browser

    Scenario Outline: Open browser and search
      Given Browser driver is configured
      When Open 'https://www.gsmarena.com/'
      Then Search for element '<Company>' and verify it's latest phone is listed
      Then Close browser
      Examples:
        | Company |
        | Apple   |
        | OnePlus |
        | Samsung |
        | Pixel   |