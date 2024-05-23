# Weather Project

An Android application that provides weather updates using the OpenWeather API and location services with Google Map Places API. The project features a modern user interface for an enhanced user experience.

## Table of Contents

1. [Features](#features)
2. [Screenshots](#screenshots)
3. [Installation](#installation)
4. [Usage](#usage)
5. [Contributing](#contributing)
6. [License](#license)
7. [Contact](#contact)

## Features

- Fetch current weather data from the OpenWeather API
- Search for locations using Google Map Places API
- Display weather data in a modern and user-friendly UI
- Save favorite locations for quick access

## Screenshots

![Home Screen]([path/to/home_screen.png](https://firebasestorage.googleapis.com/v0/b/live-scores-schedule.appspot.com/o/WhatsApp%20Image%202024-05-23%20at%203.36.37%20PM%20(1).jpeg?alt=media&token=4c54672c-6858-434b-b48d-e8634d41efef)
![Search Screen]([path/to/search_screen.png](https://firebasestorage.googleapis.com/v0/b/live-scores-schedule.appspot.com/o/WhatsApp%20Image%202024-05-23%20at%203.36.37%20PM.jpeg?alt=media&token=165f02be-bd01-4fef-8ebc-f8574214dfe9)

## Installation

### Prerequisites

- Android Studio
- Java knowledge
- An OpenWeather API key
- A Google Cloud project with Places API enabled

### Setup

1. **Clone the repository:**

    ```sh
    git clone https://github.com/yourusername/weather-project.git
    cd weather-project
    ```

2. **Open the project in Android Studio:**

    - Launch Android Studio.
    - Select "Open an existing project" and navigate to the project directory.

3. **Configure API keys:**

    - Add your OpenWeather API key in `local.properties`:

      ```properties
      openweather.api.key=YOUR_OPENWEATHER_API_KEY
      ```

    - Add your Google Places API key in `local.properties`:

      ```properties
      google.api.key=YOUR_GOOGLE_PLACES_API_KEY
      ```

4. **Sync the project:**

    - Sync the project with Gradle files.

5. **Run the app:**

    - Connect your Android device or start an emulator.
    - Click the "Run" button in Android Studio.

## Usage

1. **Getting Weather Data:**

    - Launch the app.
    - The home screen displays the current weather for your location.

2. **Searching for Locations:**

    - Use the search bar to find weather information for different locations.
    - Select a location from the search results to view its weather details.

3. **Saving Favorite Locations:**

    - Tap the "Save" button to save a location to your favorites.
    - Access saved locations from the "Favorites" section.

## Contributing

We welcome contributions! Please follow these steps to contribute:

1. Fork the repository.
2. Create a new branch (`git checkout -b feature-branch`).
3. Make your changes.
4. Commit your changes (`git commit -m 'Add some feature'`).
5. Push to the branch (`git push origin feature-branch`).
6. Open a pull request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact

For any questions or feedback, please reach out:

- Email: itwerse@gmail.com
