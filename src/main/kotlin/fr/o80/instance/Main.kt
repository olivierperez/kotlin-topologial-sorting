package fr.o80.instance

import fr.o80.CheckVersionIsUpToDate
import fr.o80.InitCrashlytics
import fr.o80.LoadLocalConfig
import fr.o80.LoadRemoteConfig
import fr.o80.MigrateDatabase
import fr.o80.NoTask
import fr.o80.Task
import fr.o80.buildTasksSequence

fun main() {
    val sequence = buildTasksSequence<Task> {
        val loadLocalConfig = LoadLocalConfig()
        val loadRemoteConfig = LoadRemoteConfig()
        val initCrashlytics = InitCrashlytics()
        val checkVersionIsUpToDate = CheckVersionIsUpToDate()
        val migrateDatabase = MigrateDatabase()

        migrateDatabase dependsOn NoTask
        loadLocalConfig dependsOn NoTask
        loadRemoteConfig dependsOn loadLocalConfig
        initCrashlytics dependsOn loadLocalConfig
        initCrashlytics dependsOn loadRemoteConfig
        checkVersionIsUpToDate dependsOn loadRemoteConfig
    }

    sequence.forEach { group ->
        group.forEach { task -> task.run() }
        println("*then*")
    }
}

