@file:Suppress("DEPRECATION")

plugins {
    alias(libs.plugins.android.application) // Apply Android app plugin
}

android {
    namespace = "com.marsIT.marsSyntaxApp" // Package ID
    compileSdk = 36 // API level to compile against

    defaultConfig {
        applicationId = "com.marsIT.marsSyntaxApp"    // Unique app ID
        minSdk = 21                             // Minimum Android version
        targetSdk = 36                          // Target API level
        versionCode = 1                         // App version (for updates)
        versionName = "6.25.1"                  // Displayed version

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner" // UI test runner
    }

//    lint {
//        // Disable specific lint warnings for compatibility with older or future SDKs
//        disable.addAll(
//            listOf(
//                "ExpiredTargetSdkVersion",
//                "ObsoleteSdkInt",
//                "TargetSdkTooLow"
//            )
//        )
//
//        // Don't abort release builds due to lint errors
//        checkReleaseBuilds = false
//
//        // enable lint reports
//        xmlReport = true
//        htmlReport = true
//        htmlOutput = file("$buildDir/reports/lint/lint-report.html")
//        xmlOutput = file("$buildDir/reports/lint/lint-results.xml")
//    }

    buildTypes {
        release {
            isMinifyEnabled = false // Disable code shrinking
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    // Show deprecation warnings
    tasks.withType<JavaCompile>().configureEach {
        options.compilerArgs.add("-Xlint:deprecation")
    }

    buildFeatures {
        viewBinding = true // Enable ViewBinding
    }
}

dependencies {
    implementation(libs.appcompat)                  // AppCompat: Provides backward compatibility for Android components
    implementation(libs.material)                   // Material Design Components: Provides Material UI components (buttons, dialogs, etc.)
    implementation(libs.activity)                   // AndroidX Activity: Helps manage Android activities efficiently
    implementation(libs.constraintlayout)           // ConstraintLayout: A flexible layout for better UI design
    implementation(libs.recyclerview)               // RecyclerView: An optimized view for displaying lists and grids
    implementation(libs.navigation.fragment)        // Navigation Component: Manages fragment navigation
    implementation(libs.navigation.ui)              // Navigation UI: Simplifies top-level navigation with UI elements (e.g., BottomNavigationView)
    implementation(libs.gridlayout)                 // GridLayout: Allows designing UI with a grid-like structure
    implementation(libs.play.services.location)     // Google Play Services Location: Provides location services for GPS tracking

    // Testing dependencies
    testImplementation(libs.junit)                  // JUnit: A framework for running unit tests in JVM
    androidTestImplementation(libs.ext.junit)       // JUnit for Android: Runs tests on Android devices/emulators
    androidTestImplementation(libs.espresso.core)   // Espresso: UI testing framework for automated interaction testing

    // WorkManager for background tasks
    implementation(libs.work.runtime)               // WorkManager: Schedules and manages background tasks

    // Glide for image loading
    implementation(libs.glide)                      // Glide: Efficient image loading and caching library
    annotationProcessor(libs.glide.compiler)        // Glide Compiler: Generates optimized code for image handling

    // Lottie for animations
    implementation(libs.lottie)                     // Lottie: Loads and plays JSON-based animations
}


