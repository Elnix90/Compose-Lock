# Compose Lock

| Pattern                                                                                         | Pin                                                                                     |
|-------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------|
| ![Pattern](https://raw.githubusercontent.com/Elnix90/Compose-Lock/main/assets/pattern_demo.gif) | ![Pin](https://raw.githubusercontent.com/Elnix90/Compose-Lock/main/assets/pin_demo.gif) |

Compose lock offers a **pattern lock library** and a **pin lock library**, for jetpack compose framework

## Features

### Pattern
- Custom dimension (size of the pattern)
- Custom sensitivity
- Custom colors
- Custom animation duration
- Supporting any size (>= 2) (dont be dumb lol)

### Pin
- Inspired by Pixel Phones lock screen
- Custom size
- Custom colors
- Hightly responsive with material 3 expressive theming

## Download

![Maven Central Version](https://img.shields.io/maven-central/v/io.github.elnix90.lock/compose-lock?style=for-the-badge)

### Setup - Version Catalog

Configure the dependency by adding it to your `libs.versions.toml` file as follows:

```toml
[versions]
#...
compose-lock = "1.1.0"

[libraries]
#...
compose-lock = { group = "io.github.elnix90.compose", name = "lock", version.ref = "compose-lock" }
```

```kotlin

// Module build file
dependencies {
    // ...
    implementation(libs.compose.lock)
}
```

## Usage

Use ComposeLock functions in `@Composable` scope

```kotlin
PatternLock(
    modifier = Modifier.padding(bottom = 80.dp),
    patternLockOptions = PatternLockOptions.defaultPatternLockOptions.copy(
        dimension = patternSize,
        sensitivity = patternSensitivity,
        showSensibility = showSensitivity
    ),
    onFinished = { patternString ->

    },
)


PinLock(
    pinLockOptions = PinLockOptions.defaultPinLockOptions,
    modifier = Modifier.padding(bottom = 80.dp),
    onValidate = { pinString ->

    }
)
```


# Test the library

I use this library in [Dragon Launcher](https://github.com/Elnix90/Dragon-Launcher)
If you want to test it, download the app and follow the instructions below:

| Go to settings                                                                                           | Go to Behavior                                                                                           | Enable lock method                                                                                               |
|----------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------|
| ![Go To Settings](https://raw.githubusercontent.com/Elnix90/Compose-Lock/main/assets/go_to_settings.jpg) | ![Go To Behavior](https://raw.githubusercontent.com/Elnix90/Compose-Lock/main/assets/go_to_behavior.jpg) | ![Enable lock method](https://raw.githubusercontent.com/Elnix90/Compose-Lock/main/assets/enable_lock_method.jpg) | 


## How to clone and add credentials

1. Generate the key

```bash
gpg --full-generate-key
```

- Select **RSA** and then 4096
- fill in the next prompts with what you need

2. Export the key to the `creds/` folder

```bash
gpg --armor --export-secret-key YOUR_KEY_ID > creds/private.asc
```

This will create or overwrite the file `creds/private.asc` in the repo (gitignored)

3. Export password to `creds/private.txt`

```bash
echo YOUR_PASSWORD > creds/passwd.txt
```

4. Generate Maven tokens

- Go to https://central.sonatype.com/usertoken and create a new token.
- Paste the username given to `creds/maven_username.txt`
- Paste the password given to `creds/maven_password.txt`

5. Export public keys 

```bash
gpg --keyserver keyserver.ubuntu.com --send-keys YOUR_KEY_ID
```

6. Verify export

```bash
gpg --keyserver keyserver.ubuntu.com --recv-keys YOUR_KEY_ID
```


7. you can now publish to maven by using `./gradlew publish` or triggering the workflow