CSCI 310
Group members: Ang Li, Kevin Nguyen, Tri Nguyen, Andre Takhmazyan, Utsav Thapa

Sanity: an Android app for managing budgets

Click the $ icon to start the app
Pressing the back button inside the app will not logout the user, but closing the app logs out the user for security.

Run on Pixel API 25 on Android Studio 3.0. To run in Android studio, clean and then build. The APK is included for use on phones.
To install the apk please make sure that Unknown sources is enabled in settings. 

**** Login Screen:
First time users should press Sign up

Returning users should enter their login information

If the user has forgotten their password, they should enter their username and then press "Forgot Password"
**** End Login screen


**** Signup Screen
User should select an unoccupied username, a password of 5 or more characters, one of the 3 security questions and an answer to the security question.

After this is entered, the user should press create account.

If all the information is correctly added, the user will be taken directly to the main menu.
****  End Signup Screen


**** Reset Password Screen
User should pick a new password of 5 or more characters.

User must answer the security question correctly to reset their password.
**** End Rest Password Screen


**** Main Menu screen
The overall budget, along with each budget sub-category are displayed. Sub-categories are sorted in alphabetical order.

Progess bars are color coded according to what percent of the budget limit has been spent.

Pressing on the progress bars of the sub-categories will display all the transactions in the category sorte by date.

The gear icon changes the settings of the budget categories, such as the notification threshold or budget period.

The + icon adds transactions to the categories or new categories to the overall budget.

To delete a category, click and hold on the progress bar until a red border appears. The analytics button should now say "Delete." 
Select more categories or de-select by clicking and holding. After you have selected all the ones to delete, press delete and then confirm.

The analytics button shows a pie chart of the overall budget, showing what percent of the spending came from each category.

The user can logout and the user will be taken to the login screen.
**** End Main menu




