# EazyPGminiTask

App starts with Login screen and has a link to go to Registration screen. Login is done using email and password while Firebase authentication provides the backend and necessary validation.

![Login Screen](screenshots/1.png?raw=true "Login Screen")

Registraion screen has different fields as shown and if registering as tenant, you have to enter owner id.

![Registration Screen](screenshots/2.png?raw=true "Registration Screen")
![Registration Screen](screenshots/3.png?raw=true "Registration Screen")

This is the Tenant Dashboard (UI is pretty basic)

![Dashboard Tenant](screenshots/4.png?raw=true "Dashboard Tenant")

By clicking on Pay Bill or Pay Rent, you are shown this dialog box to pay. On clicking on pay it updates data on firebase.

![Bill Pay Screen](screenshots/5.png?raw=true "Bill Pay Screen")
![Rent Pay Screen](screenshots/6.png?raw=true "Rent Pay Screen")

On clicking on Pay, A Toast is generated as follows.

![Rent Pay Toast Screen](screenshots/7.png?raw=true "Rent Pay Toast Screen")

On clicking on Raise Complaint, you are shown this screen where all your past complaints are shown and you can add a new issue by clicking th Log New Complaint button at the bottom.

![Complaint Screen](screenshots/8.png?raw=true "Complaint Screen")
![Complaint Dialog Screen](screenshots/9.png?raw=true "Complaint Dialog Screen")
![Complaint Screen](screenshots/10.png?raw=true "Complaint Screen")

Finally at end is button to open Chat Bot. I have used Google's Dialog Flow API to build this bot. It can continue with small talk but also has some basic commands built into it as asked in assignment.

![Chat Bot Screen](screenshots/11.png?raw=true "Chat Bot Screen")
![Chat Bot Screen](screenshots/12.png?raw=true "Chat Bot Screen")

This is the Owner Dashboard.

![Dashboard Owner](screenshots/13.png?raw=true "Dashboard Owner")

Upon clicking on Issues button it shows a list of registered tenants and upon clicking on one of them it shows list of all complaints registered by them.

![Complaint Screen](screenshots/14.png?raw=true "Complaint Screen")
![Complaint Screen](screenshots/15.png?raw=true "Complaint Screen")

Upon clicking on individual complaints, Owner can mark it as received or completed (as asked in mini task to do)

![Complaint Dialog Screen](screenshots/16.png?raw=true "Complaint Dialog Screen")
![Complaint Dialog Screen](screenshots/17.png?raw=true "Complaint Dialog Screen")
![Complaint Dialog Screen](screenshots/18.png?raw=true "Complaint Dialog Screen")

Firebase database and authentication was used in building this app and database looks as follows

![Firebase Database](screenshots/19.png?raw=true "Firebase Database")
![Firebase Authentication](screenshots/20.png?raw=true "Firebase Authentication")
