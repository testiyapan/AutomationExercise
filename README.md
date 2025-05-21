# 🧪 AutomationExercise

This is a web automation framework built using Java, Selenium, TestNG, and Maven. It features a user-friendly GUI interface that allows users to dynamically generate and execute TestNG XML files.

## 📁 Project Structure

```
AutomationExercise
│
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── pages/               # Page classes (Page Object Model)
│   │   │   ├── locators/            # Page element locator classes
│   │   │   ├── expecteds/           # Expected result definitions
│   │   │   ├── interfaces/          # Page interface definitions
│   │   │   ├── utils/               # Utility classes and TestNG XML generator
│   │   │   │   └── factory/         # Browser driver factories
│   │   │   └── gui/                 # Swing-based GUI interface
│   │
│   └── test
│       └── java
│           ├── base/               # BaseTest class
│           ├── sample/             # Sample test classes
│           └── HomePageTest.java   # Main test class
│
├── pom.xml                        # Maven configuration file
└── .gitignore
```

## 🧰 Technologies Used

- Java 17+
- Selenium WebDriver
- TestNG
- Maven
- Swing (GUI)
- ExtentReport
- Allure

## 🚀 Setup and Execution

### 1. Clone the Project

```bash
git clone <repo-url>
cd AutomationExercise
```

### 2. Download Maven Dependencies

```bash
mvn clean install
```

### 3. Run Tests via GUI Interface

```bash
java -cp target/classes gui.TestGuiLauncher
```

> The GUI automatically detects available test classes and browser options.

## 🧩 Key Components

### ✔️ Page Object Model

The `pages/` directory includes the page classes (e.g., `HomePage.java`, `PageObject.java`) that handle user interactions.

### 🔍 Locators

The `locators/` directory contains the XPath definitions for page elements.

### 🧠 Dynamic TestNG XML Generation

The `TestNGXmlGenerator.java` class dynamically generates a testng.xml file based on GUI selections.

### 🧪 Test Execution

You can execute tests in two ways:

#### 1. Using GUI:
- Select the `TestNGXmlGenerator.java`
- Click the `Run` button.
- Select driver/s, test/s or test folder/s, therad
- Run in Fx GUI

#### 2. Manual Execution:

```bash
mvn test -DsuiteXmlFile=testng.xml
```

## 🌐 Browser Support

- Chrome
- Firefox
- Edge

Each browser is abstracted via the factory pattern in the `factory/` package.

## 📌 Notes

- The project is designed to be modular, making it easy to extend with new pages or test scenarios.
- Support for thread count and parallel test execution can be added via the GUI in future updates.
- Configuration settings are externalized using the `ConfigReader` utility class.

