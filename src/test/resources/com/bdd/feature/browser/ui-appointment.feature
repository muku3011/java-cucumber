Feature: Basic web ui testing in browser

  Background:
    Given Setup driver path
    Given Setup driver configuration

  Scenario Outline: Open browser and search for appointment
    When Open 'https://india.blsspainvisa.com/book_appointment.php'
    Then Fill form using provided phone number '<Phone Number>', emailId '<EmailId>'
    Then Access gmail with id '<EmailId>', and password '<EmailPassword>' to fetch OTP url and open it in a new tab
    Then Close browser
    Examples:
      | Phone Number | EmailId        | EmailPassword  |
      | 123456789    | demo@gmail.com | password-value |

