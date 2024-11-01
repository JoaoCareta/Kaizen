# 🏆 Kaizen App 📱

## 📌 Table of Contents
- [Overview](#-overview)
- [Features](#-features)
- [Main Screen States](#-main-screen-states)
- [Category Details](#-category-details)
- [Project Structure](#-project-structure)
- [Installation](#-installation)
- [Usage](#-usage)
- [Contributing](#-contributing)
- [License](#-license)

## 🌟 Overview

Welcome to the Kaizen App! This Android application simulates a platform that fetches and displays sports events from an API. 🏅

## 🚀 Features

- Fetch sports events from API
- Display events categorized by sports
- Favorite/unfavorite events
- Filter events by favorites
- Countdown timer for upcoming events

## 🖥 Main Screen States

The main screen of the app can be in one of three states:

1. **Loading** ⏳
   - Displayed when fetching results from the API
   - Shows a loading animation

2. **Events List** 📋
   - Displays a list of sports categories
   - Each category shows:
     - Category name
     - Switch button to filter favorite events
     - Option to expand/collapse the category

3. **Empty State** 🏜
   - Shown when the API returns no events
   - Displays a message indicating no events were found

## 📊 Category Details

When a category is expanded, it shows:

- ⏱ Countdown timer for each event
- ❤️ Favorite icon (can be toggled on/off)
- 🆚 Information about the teams competing

## 📁 Project Structure

The project follows a clean and organized structure:

```
kaizen (root project name)
│
├── data
│   ├── datasources
│   ├── local (DAOs)
│   ├── remote (API calls)
│   └── repositories
│
├── di (Dependency Injection modules)
│
├── domain
│   └── models (Data models and mappers)
│
├── navigation (App navigation)
│
├── screens
│   └── [screen_name]
│       └── components
│
├── ui (Default Android Studio UI folder)
│
└── utils (Constants, dimensions, time functions)
```

### 📁 Structure Rationale:

- **data**: Contains all data-related components, separating local and remote data sources for clarity.
- **di**: Houses all dependency injection modules for easy management.
- **domain**: Includes business logic and data models, keeping the core functionality separate.
- **navigation**: Centralizes navigation logic for better organization.
- **screens**: Each screen has its folder with components, promoting modularity.
- **ui**: Default Android Studio folder, maintained for consistency.
- **utils**: Holds utility functions and constants across the project.


## 🎮 Usage

1. Launch the app
2. Wait for the events to load
3. Browse through different sports categories
4. Expand categories to see event details
5. Favorite events you're interested in
6. Use the switch to filter favorite events in each category

