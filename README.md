# 🛠️ Hytale Plugin Template

Welcome to the **Hytale Plugin Template**! This project is a pre-configured foundation for building **Java Plugins**. It streamlines the development process by handling classpath setup, server execution, and asset bundling.

> **⚠️ Early Access Warning**
> Hytale is currently in Early Access. Features, APIs, and this template are subject to frequent changes. Please ensure you are using the latest version of the template for the best experience.

---

## 📋 Prerequisites

Before you begin, ensure your environment is ready:

* **Hytale Launcher**: Installed and updated.
* **Java 25 SDK**: Required for modern Hytale development.
* **IntelliJ IDEA**: (Community or Ultimate) Recommended for full feature support.

---

## 🚀 Quick Start Installation

### 1. Initial Setup (Before Importing)

To avoid IDE caching issues, configure these files **before** you open the project in IntelliJ:

* **`settings.gradle`**: Set your unique project name.
```gradle
rootProject.name = 'MyAwesomePlugin'

```


* **`gradle.properties`**: Set your `maven_group` (e.g., `com.yourname`) and starting version.
* **`src/main/resources/manifest.json`**: Update your plugin metadata.
* **CRITICAL:** Ensure the `"Main"` property points exactly to your entry-point class.



### 2. Importing the Project

1. Open IntelliJ IDEA and select **Open**.
2. Navigate to the template folder and click **OK**.
3. Wait for the Gradle sync to finish. This will automatically download dependencies, create a `./run` folder, and generate the **HytaleServer** run configuration.

### 3. Authenticating your Test Server

You **must** authenticate your local server to connect to it:

1. Launch the **HytaleServer** configuration in IDEA.
2. In the terminal, run: `auth login device`.
3. Follow the printed URL to log in via your Hytale account.
4. Once verified, run: `auth persistence Encrypted`.

---

## 🎮 Developing & Testing

### Running the Server

If you do not see the **HytaleServer** run configuration in the top-right dropdown, click "Edit Configurations..." to unhide it. Press the **Green Play Button** to start, or the **Bug Icon** to start in Debug Mode to enable breakpoints.

### Verifying the Setup

1. Launch your standard Hytale Client.
2. Connect to `Local Server` (127.0.0.1).
3. Type `/test` in-game. If it returns your plugin version, everything is working!

### Bundling Assets

You can include models and textures by placing them in `src/main/resources/Common/` or `src/main/resources/Server/`. These are editable in real-time using the in-game **Asset Editor**.

---

## 📦 Building your Plugin

To create a shareable `.jar` file for distribution:

1. Open the **Gradle Tab** on the right side of IDEA.
2. Navigate to `Tasks` -> `build` -> `build`.
3. Your compiled plugin will be in: `build/libs/your-plugin-name-1.0.0.jar`.

To install it manually, drop the JAR into `%appdata%/Hytale/UserData/Mods/`.

---

## 📚 Advanced Documentation

For detailed guides on commands, event listeners, and professional patterns, visit our full documentation:
👉 **[Hytale Modding Documentation](https://britakee-studios.gitbook.io/hytale-modding-documentation)**

---

## 🆘 Troubleshooting

* **Sync Fails**: Check that your Project SDK is set to **Java 25** via `File > Project Structure`.
* **Cannot Connect**: Ensure you ran the `auth` commands in the server console.
* **Plugin Not Loading**: Double-check your `manifest.json` for typos in the `"Main"` class path.

---

**Need Help?** Visit our full guide here: **[Hytale Modding Documentation](https://britakee-studios.gitbook.io/hytale-modding-documentation)**