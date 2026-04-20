



# Booking.com UI Automation Framework

##  Overview
A resilient and high-performance UI automation framework designed for the **Booking.com** platform. Built with **Selenium WebDriver** and **TestNG**, it specializes in handling dynamic web elements, multi-window navigation, and complex travel booking flows.

##  Core Features
* **Defensive Automation**: Implements fallback selectors and **JavascriptExecutor** to bypass UI flakiness, overlays, and sticky headers.
* **Page Object Model (POM)**: Decouples UI interactions from test logic for maximum maintainability and readability.
* **Dynamic Date Logic**: Utilizes Java 8 `LocalDate` to generate future-proof test data, preventing test failures due to expired dates.
* **Multi-Window Management**: Expertly handles switching focus between search results and property detail tabs.

##  Technical Highlights
* **Relative XPaths**: Uses contextual searching (`.//`) within property cards for precise data extraction (Name, Price, Rating).
* **AJAX & Lazy Loading**: Manages asynchronous content loading via JavaScript `scrollIntoView` and explicit wait strategies.
* **Evidence Collection**: Integrated screenshot utility for visual debugging and Jenkins reporting.

##  Project Structure
* `pages`: Contains Page Object classes (`HomePage`, `SearchResultsPage`, `HotelDetailsPage`).
* `tests`: End-to-end test scenarios (e.g., `BookingTest`).
* `base`: WebDriver lifecycle management and browser configuration.
* `utils`: Shared utilities for waits and screenshots.

##  How to Run
1. Ensure a compatible Chrome browser is installed.
2. Execute via Maven:
   ```bash
   mvn clean test
