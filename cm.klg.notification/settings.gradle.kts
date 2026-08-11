rootProject.name = "cm.klg.notification"

pluginManagement {
    repositories {
        maven {
            credentials {
                username = "dev"
                password = "dev"
            }
            url = uri("http://localhost:8095/repository/klg-snapshots/")
            isAllowInsecureProtocol = true
        }
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files(settingsDir.resolve("../submodule/cm.klg.common.build/gradle/libs.versions.toml")))
        }
    }
}
