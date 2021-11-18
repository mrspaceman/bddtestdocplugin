# Created by andy aspell-clark at 10/11/2021

Feature: the version can be retrieved

  Scenario: client makes call to GET /version
    When the client calls /version
    Then the client receives status code of 200
    And the client receives server version {"name":"ELibrary Server","version": "0.0.1-SNAPSHOT"}
