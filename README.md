<p align="center">
  <img src="/Preview/ComposeLockBanner.png" width="100%"  />
</p>

## Cloned from https://github.com/therewasluna/compose-lock

This is not a fork as I don't plan to make PRs, but to maintain my own version

# Compose Lock

Compose lock is a pattern lock library based on jetpack compose framework

## Features

- custom dimension (size of the pattern)
- custom sensitivity
- custom colors
- custom animation duration
- supporting any size (>= 2)

## Download

![Maven Central Version](https://img.shields.io/maven-central/v/io.github.elnix90.lock/compose-lock)

### Version Catalog

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

## Setup

Use ComposeLock function in @Composable scope

```kotlin
ComposeLock(
    modifier = Modifier.fillMaxWidth(),
    dimension = 4,
    sensitivity = 100f,
    dotsColor = Color.Black,
    dotsSize = 20f,
    linesColor = Color.Black,
    linesStroke = 30f,
    animationDuration = 200,
    animationDelay = 100,
    callback = object : ComposeLockCallback {
        override fun onStart(dot: Dot) {
            Toast.makeText(context, "start on dot with id : ${dot.id}", Toast.LENGTH_SHORT).show()
        }

        override fun onDotConnected(dot: Dot) {
            Toast.makeText(context, "dot connected with id : ${dot.id}", Toast.LENGTH_SHORT).show()
        }

        override fun onResult(result: List<Dot>) {
            var connectedDots = ""
            for (dot in result) connectedDots += "${dot.id}  "
            Toast.makeText(context, "result : $connectedDots", Toast.LENGTH_SHORT).show()
        }
    }
)
```

## Preview

<p float="left">
  <img src="/Preview/preview1.gif" width="32%" />
  <img src="/Preview/preview2.gif" width="32%" />
  <img src="/Preview/preview3.gif" width="32%" />
</p>

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

5. you can now publish to maven by using `./gradlew publish`