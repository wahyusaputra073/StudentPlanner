import com.wahyusembiring.habit.androidApplicationExtension
import com.wahyusembiring.habit.configureAndroidKotlin
import com.wahyusembiring.habit.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidApplicationConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {  // Mengakses objek target (Project)
            with(pluginManager) {  // Mengakses plugin manager untuk menerapkan plugin
                apply("com.android.application")  // Menerapkan plugin Android untuk aplikasi
                apply("org.jetbrains.kotlin.android")  // Menerapkan plugin Kotlin untuk Android
            }

            androidApplicationExtension {  // Mengonfigurasi ekstensi Android aplikasi
                configureAndroidKotlin(this)  // Memanggil fungsi untuk mengonfigurasi Kotlin di proyek Android
            }

            dependencies {  // Menambahkan dependensi
                add("implementation", libs.findLibrary("androidx-core-ktx").get())  // Menambahkan dependensi androidx-core-ktx
                add("implementation", libs.findLibrary("kotlinx-coroutine").get())  // Menambahkan dependensi kotlinx-coroutine
            }
        }
    }
}
