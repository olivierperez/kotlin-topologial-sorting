package fr.o80.klass

import fr.o80.CheckVersionIsUpToDate
import fr.o80.InitCrashlytics
import fr.o80.LoadLocalConfig
import fr.o80.LoadRemoteConfig
import fr.o80.MigrateDatabase
import fr.o80.NoTask
import fr.o80.Task
import fr.o80.buildTasksSequence
import kotlin.reflect.KClass

fun main() {
    val sequence = buildTasksSequence<KClass<out Task>> {
        MigrateDatabase::class dependsOn NoTask
        LoadLocalConfig::class dependsOn NoTask
        LoadRemoteConfig::class dependsOn LoadLocalConfig::class
        InitCrashlytics::class dependsOn LoadLocalConfig::class
        InitCrashlytics::class dependsOn LoadRemoteConfig::class
        CheckVersionIsUpToDate::class dependsOn LoadRemoteConfig::class
    }

    sequence.forEach { group ->
        group.forEach { task -> injectInstanceOf(task).run() }
        println("*then*")
    }
}

fun injectInstanceOf(task: KClass<out Task>): Task = when (task) {
    LoadLocalConfig::class -> LoadLocalConfig()
    LoadRemoteConfig::class -> LoadRemoteConfig()
    InitCrashlytics::class -> InitCrashlytics()
    CheckVersionIsUpToDate::class -> CheckVersionIsUpToDate()
    MigrateDatabase::class -> MigrateDatabase()
    else -> error("Unknown task: $task")
}

