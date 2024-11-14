1.The project focuses on creating a pill reminder system that allows users to set reminders with customizable start and end dates.Reminders can be scheduled multiple times per day with options for daily or weekly frequency. When a reminder time is reached, a toast notification will appear.

2.Feature:
--The style can adapt to screen size.
2.1 Pill Reminders Page
--Language Selection: Users can change the language of the page, enabling multilingual support.
--Medication Search: Users can search for a specific medication brand name. This search will fetch real-time medication information from the openFDA API.
--Customizable Day Selection: Users can filter to view reminders on specific days, rather than all at once.
--Sorted Reminders: Reminders are listed in chronological order based on the time of day.
--Mark as Done: Users can mark a reminder as "done" once they've taken the medication.
--Timed Notifications: When the time of a scheduled reminder is approaching, a toast notification will appear, reminding the user to take their medication.
--Reminder Detail View: By clicking on a single reminder item, a detailed pop-up will display, showing all relevant information about the reminder.
2.2 New Pill Reminder Page
--Medication Search: Users can search for a specific medication brand name here, as well, with results fetched from openFDA.
--Create New Reminder:
---Date Selection: Users can specify a start date and an end date for the reminder.
---Frequency Options: Reminders can be set to recur daily, weekly, or at multiple times within a day.
---Validation:
If a reminder with the same name already exists, a warning will appear when user clicks on 'Add' button.
If the end date is earlier than the start date, a warning will display when user clicks on 'Add' button.
If any required input fields are left blank, a warning will appear when user clicks on 'Add' button.
--Confirmation Pop-Up: Once a new reminder is created, a detailed pop-up appears, confirming the reminder settings.
2.3 Medication Search Page
--Search and Navigation:
Users can enter keywords in a search bar to look up medication names.
Pressing ENTER triggers a search using openFDA.
The app then navigates to a results page displaying all brand names that match the search keywords.

3.How to run it: 
3.1 Please install:
npm install react-bootstrap bootstrap
npm install react-i18next i18next
npm install @mui/material @mui/x-date-pickers
npm install dayjs
npm install react-toastify
npm install react-router-dom
3.2 Test Features:
3.2.1 Pill Reminder Page:
--Language Button: Check that the language selection button functions correctly.
--Search Function: Test the search feature by entering real brand names like “Long,” “Advil,” “Herzuma,” or any other known brands. Also, input a fake name like “wwrera” to verify that the system provides appropriate feedback for unrecognized items.
3.2.2 New Pill Page:
--Form Validation: Fill out the form and test validation by omitting a field or setting an end date earlier than the start date. Check that a warning appears when clicking “Add Reminder.”
--Reminder Timing: Set a reminder time close to the current time (e.g., within 5 minutes) and verify that a notification pop-up appears at the scheduled time.
--Return Button: Test the “<” (back) button to verify it navigates back to the previous page.
--Duplicate Reminder Check: Attempt to create another reminder with the same name. Verify that a warning appears when you click “Add Reminder” for a duplicate entry.
3.2.3 Pill Reminder Page:
--Order Verification: Check that all reminder items are listed in chronological order.
--Toast Notification: Upon returning to this page, verify that reminders set for earlier times today trigger toast notifications immediately, while reminders for later today trigger notifications only at their scheduled time. Each reminder should trigger a toast notification only once.
--Frequency Check: Select a different date to verify that reminders set for “daily” or “weekly” frequency function correctly.
--Mark as Complete: Mark reminder items as completed and verify that they do not trigger notifications even if their scheduled time is reached.



