Feature: Verify tag creation

  @Test
  Scenario: User must be able to create a tag
    User creates a tag using API
    Verify tag is created in database
    Update tag using API
    Verify tag is updated in database
    Delete tag from database using SQL
    Verify tag is deleted using API GET request

    Given base url "https://backend.cashwise.us"
    And user has endpoint "/api/myaccount/tags"
    When user provides valid token
    And user provides request body with "name_tag" and "cap"
    And user provides request body with "description" and "tomato"
    And user hits POST request
    Then verify status code is 201
    Given user set up connection to database
    When user sends the query "select * from tags where tag_id = ?"
    Then verify result set contains id "tag_id"
    Then verify result set contains "name_tag" with "cap"
