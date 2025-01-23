plugins {
    alias(libs.plugins.habit.android.application)
    alias(libs.plugins.habit.android.compose)
    alias(libs.plugins.habit.testing)
    alias(libs.plugins.habit.dagger.hilt)
    alias(libs.plugins.habit.android.room)
    alias(libs.plugins.habit.android.lifecycle)
    alias(libs.plugins.habit.android.navigation)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.google.services)
//    id("com.android.application")
//    id("org.jetbrains.kotlin.android")
//    id("com.google.devtools.ksp") // Tambahkan ini jika menggunakan KSP
}

android {
    namespace = "com.wahyusembiring.habit"

    defaultConfig {
        applicationId = "com.wahyusembiring.habit"
        versionCode = 1
        versionName = "1.0"

    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }

}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    implementation(project(":core:data"))

    implementation(libs.androidx.activity.compose)

    implementation(project(":feature:settings"))
    implementation(project(":feature:task"))
    implementation(project(":feature:subject"))
    implementation(project(":feature:overview"))
    implementation(project(":feature:exam"))
    implementation(project(":feature:agenda"))
    implementation(project(":feature:calendar"))
    implementation(project(":feature:onboarding"))
    implementation(project(":feature:lecturer"))
    implementation(project(":feature:thesisplanner"))
    implementation(project(":feature:auth"))

    val roomVersion = "2.5.0" // Sesuaikan dengan versi Room Anda
    implementation("androidx.room:room-runtime:$roomVersion")
    annotationProcessor("androidx.room:room-compiler:$roomVersion") // Hanya untuk Java
    ksp("androidx.room:room-compiler:$roomVersion") // Untuk Kotlin

//    Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)

//   Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.svg)

    coreLibraryDesugaring(libs.desugar.jdk.libs)

    implementation(libs.androidx.core.splashscreen)

    implementation(libs.facebook.android.sdk)

}