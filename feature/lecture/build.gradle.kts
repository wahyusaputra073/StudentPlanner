plugins {
    alias(libs.plugins.habit.android.feature)
}

android {
    namespace = "com.wahyusembiring.lecture"

}

dependencies {
//   Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.svg)
}