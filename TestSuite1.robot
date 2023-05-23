*** Settings ***
Library           SeleniumLibrary
Library           Collections

*** Variables ***
${Sleep Time}     5
${TodoText}       Todo
${TodoDescription}    description

*** Test Cases ***
AddNewTODO
    [Setup]    Open Browser    file:///D:/FCAI-CU/Level4/secondterm/Testing/Assignments/3/TODOWebsite/todo.html    Chrome
    Maximize Browser Window
    Wait Until Page Contains Element    css=h1.header
    Wait Until Element Is Visible    css=h1.header
    Sleep    ${Sleep Time}
    Input Text    id=todo    ${TodoText}
    Input Text    id=desc    ${TodoDescription}
    Click Button    xpath=//button[contains(@onclick, 'addTodoFormHandler')]
    @{elements}=    Get WebElements    xpath=//tbody[@id='todo-table']/tr
    ${counter}=    Set Variable    0 
    ${text}  Set Variable   ${EMPTY}
    ${description}  Set Variable   ${EMPTY}
    FOR    ${element}    IN    @{elements}
        ${counter}=    Evaluate    ${counter} + 1
        Sleep    ${Sleep Time}
        ${description}=    Get Text    xpath=/html/body/div/table/tbody/tr[${counter}]/td[3]
        ${text}=    Get Text    xpath=/html/body/div/table/tbody/tr[${counter}]/td[2]
        Log To Console    ${description}
        Log To Console    ${text}
        Log To Console     element: ${element}
    END  
    Sleep    ${Sleep Time}
    Run Keyword If    '${text}' == '${TodoText}' and '${description}' == '${TodoDescription}'    Log To Console    Last element contains the description and text.
    [Teardown]    Close Browser
    
DeleteTodo
    [Setup]    Open Browser    file:///D:/FCAI-CU/Level4/secondterm/Testing/Assignments/3/TODOWebsite/todo.html    Chrome
    Maximize Browser Window
    Wait Until Page Contains Element    xpath=//h1[contains(@class, 'header')]
    @{elements}=    Get WebElements   xpath=//tbody[@id='todo-table']/tr
    Sleep    ${Sleep Time}
    IF    ${elements}
        Click Button    xpath=//table[contains(@class,'table')]/tbody/tr[last()]//td//button[contains(@class,'btn-danger')]
        Log To Console    deleted Successfully.
    ELSE
        Log To Console    The list is empty.
    END
    [Teardown]    Close Browser

GetAllTODOs
    [Setup]    Open Browser    file:///D:/FCAI-CU/Level4/secondterm/Testing/Assignments/3/TODOWebsite/todo.html    Chrome
    Maximize Browser Window
    Wait Until Page Contains Element    xpath=//h1[contains(@class, 'header')]
    @{elements}=    Get WebElements   xpath=//tbody[@id='todo-table']/tr
    ${original_rows}=    Create List
    ${counter}=    Set Variable    0 
    FOR    ${element}    IN    @{elements}
        ${counter}=    Evaluate    ${counter} + 1
        ${number}=    Get Text    xpath=/html/body/div/table/tbody/tr[${counter}]/td[1]
        append to list    ${original_rows}    ${number}
    END
    Sleep    ${Sleep Time}
    ${copied_numbers}=    Copy List    ${original_rows}
    Sort List    ${copied_numbers}
    Lists Should Be Equal  ${copied_numbers}   ${original_rows}
    Log List    ${copied_numbers}
    Log List    ${original_rows}
    Sleep    ${Sleep Time}
    [Teardown]    Close Browser
