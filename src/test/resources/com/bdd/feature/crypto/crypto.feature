# language: en
#A # language: header on the first line of a feature file tells Cucumber what spoken language to use - for example '# language: fr' for French.
# If you omit this header, Cucumber will default to English (en).

# The purpose of the Feature keyword is to provide a high-level description of a software feature, and to group related scenarios.
Feature: AES GCM crypto features
  It should include all encrypt/decrypt possible operations
  # Free-form descriptions can also be placed underneath Example/Scenario, Background, Scenario Outline and Rule.

  # A Background allows you to add some context to the scenarios that follow it.
  # It can contain one or more Given steps, which are run before each scenario, but after any Before hooks.
  # A Background is placed before the first Scenario/Example, at the same level of indentation.
  Background:
    Given Steps to be performed before each scenario

  # The purpose of the Rule keyword is to represent one business rule that should be implemented.
  # It provides additional information for a feature.
  # A Rule is used to group together several scenarios that belong to this business rule.
  # A Rule should contain one or more scenarios that illustrate the particular rule.
  Rule: Encrypt Scenarios

    # Background is also supported at the Rule level.
    # Whilst usage of Background within Rule is currently supported, it’s not recommended, and may be removed in future versions of Gherkin.
    Background:
      Given Steps to be performed before each scenario

    # This is a concrete example that illustrates a business rule. It consists of a list of steps.
    # The keyword 'Example' is a synonym of the keyword 'Scenario'.
    Scenario: Encrypt with AES GCM
      # Each step starts with 'Given', 'When', 'Then', 'And', or 'But'.
      Given Nothing is given
      When Encrypt 'Hello world' with password 'PASSWORD'
      Then Validate text encoded and not equal to 'Hello world'

    # The Scenario Outline keyword can be used to run the same Scenario multiple times, with different combinations of values.
    # The keyword Scenario Template is a synonym of the keyword Scenario Outline
    Scenario Outline: Multiple-Encrypt with AES GCM
      # Each step starts with 'Given', 'When', 'Then', 'And', or 'But'.
      Given Nothing is given
      When Encrypt '<PlainText>' with password '<Password>'
      Then Validate text encoded and not equal to '<PlainText>'

      # A Scenario Outline must contain an Examples (or Scenarios) section.
      Scenarios:
        | PlainText | Password |
        | Mukesh    | PASSWORD |
        | Joshi     | PASSWORD |
        | Barcelona | PASSWORD |

  Rule: Encrypt and Decrypt Scenarios

    Scenario: Encrypt with AES GCM
      When Encrypt 'Hello world' with password 'PASSWORD'
      Then Validate text encoded and not equal to 'Hello world'
      Then Decrypt with password 'PASSWORD' and verify plain text equal to 'Hello world'

    Scenario Outline: Multiple-Encrypt with AES GCM
      When Encrypt '<PlainText>' with password '<Password>'
      Then Validate text encoded and not equal to '<PlainText>'
      Then Decrypt with password '<Password>' and verify plain text equal to '<PlainText>'

      Scenarios:
        | PlainText | Password  |
        | Mukesh    | PASSWORD1 |
        | Joshi     | PASSWORD2 |
        | Barcelona | PASSWORD3 |