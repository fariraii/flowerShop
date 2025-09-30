🌸 FlowerShop

An Android app built with Kotlin and Jetpack Compose, showcasing a simple flower shop. The app demonstrates MVVM architecture, state management with ViewModel, and clean separation of data, domain, and UI layers.

✨ Features

📋 Browse flowers (sample data in FlowerRepository)

🎨 Modern UI with Jetpack Compose

🧩 MVVM pattern with ViewModel

🔄 Reactive state updates using MutableState

📱 Material Design theming

🛠️ Tech stack

Language: Kotlin

UI: Jetpack Compose, Material Design 3

Architecture: MVVM (ViewModel, Repository, Domain model)

Build system: Gradle (KTS)

Testing: JUnit, AndroidX Test

🚀 Getting started

Clone this repository:

git clone https://github.com/<your-username>/flowerShop.git


Open the project in Android Studio Iguana+.

Let Gradle sync.

Run the app on an emulator or device.

📂 Project structure
app/
 └── src/main/java/com/example/flowershop/
     ├── data/           # FlowerRepository (sample data)
     ├── domain/         # Flower model
     ├── ui/theme/       # Compose theme setup
     ├── ui/             # FlowerViewModel + UI screens
     └── MainActivity.kt # App entry point
