
# Clash of Words

Clash of Words is a word-based multiplayer game where players compete to answer questions and score points. This Android application offers a fun and interactive gaming experience, complete with user profiles, leaderboards, and in-game rewards.

## Features

-   **Multiplayer Gameplay:** Compete against friends or bots in real-time word battles. For example, you can challenge your friends to answer a series of questions within a time limit, with scores updated live.
-   **User Profiles:** Customize your profile with unique avatars, track your progress, and view your leaderboard rankings.
-   **In-Game Shop:** Purchase power-ups, avatar enhancements, or additional game content using earned or purchased in-game currency.
-   **Interactive UI:** Enjoy a visually appealing interface with intuitive navigation, animated transitions, and responsive design for seamless gameplay.
-   **Firebase Integration:** Secure user authentication, real-time game state synchronization, and cloud-based data storage ensure a smooth and reliable experience.

## Technologies Used

-   **Java:** Core programming language for the application.
-   **XML:** UI design and layout.
-   **Firebase:** Backend services for authentication, database, and cloud functions.
-   **Glide:** Image loading and caching.
-   **Android Architecture Components:** ViewModel, LiveData, and Navigation.
-   **Material Design Components:** For modern and responsive UI components.
-   **Crashlytics:** Firebase service for monitoring and fixing crashes.

## Installation

1.  Clone the repository:
    
    ```bash
    git clone https://github.com/furblood0/clash-of-words.git
    
    ```
    
2.  Open the project in Android Studio.
3.  Add your `google-services.json` file to the `app` directory.
4.  Sync the project with Gradle files.
5.  Build and run the project on an emulator or physical device.

## Project Structure

-   **Activities:**
    
    -   **MainActivity:** Serves as the entry point of the application, managing user authentication and navigation setup.
    -   **HomeActivity:** Provides the main hub where users can access different features like the shop, profile, and gameplay.
-   **Fragments:**
    
    -   **GamePlayFragment:** Handles the main gameplay mechanics, including displaying questions, managing timers, and processing answers.
    -   **QuestionStartFragment:** Displays pre-game information or instructions before starting a gameplay session.
    -   **ResultFragment:** Shows the results of the game, including scores and rankings.
    -   **FriendsFragment:** Displays a list of friends, allowing users to invite or compete with them.
    -   **ProfileFragment:** Allows users to view and edit their profile details, such as username and avatar.
    -   **ProfilePictureFragment:** Enables users to select and update their profile pictures.
    -   **ShopFragment:** Provides access to in-game items or power-ups that users can purchase with in-game currency.
    -   **SettingsFragment:** Allows users to configure application settings, such as sound, notifications, and privacy options.
-   **Adapters:**
    
    -   **AnswerAdapter:** Manages the display of answer options in the gameplay screen.
    -   **FriendsAdapter:** Manages the display of friends in the FriendsFragment.
    -   **ProfilePictureAdapter:** Handles the selection and display of profile pictures.
    -   **ShopAdapter:** Manages the in-game shop's items and their display.
-   **Helpers:**
    
    -   **ResourceBarHelper:** Provides utility functions for managing resource bars, such as progress or score indicators.
    -   **BotManager:** Handles bot interactions during gameplay, ensuring challenging yet balanced AI opponents.
-   **XML Layouts:**
    
    -   **activity_main.xml:** Defines the layout for the MainActivity.
    -   **activity_home.xml:** Layout for the main hub interface.
    -   **fragment_game_play.xml:** Design for the gameplay screen, including question and answer display.
    -   **fragment_profile.xml:** Layout for the user profile screen.
    -   **fragment_settings.xml:** Design for the settings screen.

## Advanced Features

-   **Real-Time Updates:** Firebase's Firestore enables real-time data synchronization, ensuring that all players see updated scores and game states immediately.
-   **Enhanced Security:** Firebase Authentication secures user data with features like email/password authentication and Google Sign-In.
-   **In-Game Economy:** The shop system is designed with virtual currency, allowing both earned and purchased credits to be used for upgrades.

## Contribution

Contributions are welcome! Please follow these steps:

1.  Fork the repository.
2.  Create a new branch for your feature or bug fix.
3.  Commit your changes and push to your branch.
4.  Submit a pull request.
