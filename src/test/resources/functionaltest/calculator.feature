Feature: Calculator
  As a user
  I want to use a calculator
  So that I don't need to calculate myself

  Scenario Outline: Add two numbers
    Given MyCalculator a calculator
    When MyCalculator adds two values
      | arg1  | arg2    |   sum    |
      |  <arg1>     | <arg2>   | <sum> |
    Then The result of MyCalculator should be the sum of these 2 values
      | arg1  | arg2    |   sum    |
      |  <arg1>     | <arg2>   | <sum> |
  Examples:
      |  arg1  | arg2 | sum |
      |  2 | 3      |   5   |
      |  1 | 10     |  11   |
      | -2 | 3      |   1   |
