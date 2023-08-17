# TaskSprint

A simple todo app with date pagination backed by firebase. It is built in compose
and follows MVVM architecture, UDF pattern and Single Activity architecture. It uses
firebase phone authentication.

## Feature

1. Firebase Phone Authentication in a clean architecture.
2. Add/Update/Delete Task using firebase firestore.
3. Pagination: Load task according to month.

## Screenshots

|                                                                                                                 |                                                                                                                 |                                                                                                                      |
|-----------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------|
| ![ss_screen_auth](https://github.com/sDevPrem/TaskSprint/assets/130966261/89d39783-cc5b-4243-adb5-43e8f1f0b5f5) | ![ss_screen_home](https://github.com/sDevPrem/TaskSprint/assets/130966261/6ddbf188-2a80-4ca6-ae4e-af7e6efe2d1c) | ![ss_screen_edit_task](https://github.com/sDevPrem/TaskSprint/assets/130966261/47483d40-f023-48dc-8b5f-ef3a70875965) |

## Build with

[Kotlin](https://kotlinlang.org/): As the programming language.  
[Jetpack Compose](https://developer.android.com/jetpack/compose): To build UI.  
[Jetpack Navigation](https://developer.android.com/guide/navigation): To navigate between screens.  
[Firebase Firestore](https://firebase.google.com/docs/firestore): As the database.  
[Firebase Phone Auth](https://firebase.google.com/docs/auth/android/phone-auth): To authenticate
user using phone number.  
[Timber](https://github.com/JakeWharton/timber): For Logging.  
[maxkeppeler sheets](https://github.com/maxkeppeler/sheets) : To create date picker dialog.

## Using instruction

1. Enter your phone number. It will open browser for captcha verification. After that,
   a verification will be sent to you phone number. Enter it and log in.
2. Add new task using + button and start using the app.

## Installation

1. Create a firebase project.
2. Extract SHA1 and SHA256 and add it in the firebase project SHA certificate fingerprints.
3. Clone and open this project in AndroidStudio.
4. Connect this project with the firebase project that was created in step 1.
5. Create index for fields `userId` by Ascending order and `dueDate` by Descending order in
   tasks collection in order to enable filters.

## Architecture

This app follows MVVM architecture, Unidirectional data flow (UDF) pattern and Single Activity
architecture.

## Folder Structure

- `core`: Handles the backend of the app like authentication and database related work.
- `ui`: Handles the user interface components.
- `common`: Includes utility classes and helper functions used across the app.
- `di` : Contains hilt modules.