# Topological sorting

Sometimes I needed to execute a bunch of tasks with some dependencies between them.
Each time I had to update the order of execution it was painful because:
- you have to know if a task can be executed before another one?
- you have to know if a task should be executed before another one?
- basically you have to understand the full picture everytime you need to update the sequence

So the idea here is to **describe the dependencies** between tasks instead of calling them in a given order,
and let the system choose the order.

## Proposals

I've made two almost similar approach, [one based on classes](src/main/kotlin/fr/o80/klass/Main.kt) and [one based on instances](src/main/kotlin/fr/o80/instance/Main.kt),
I guess your choice will depend on which Dependency Injection you use. You may also be interested in [algorithm](src/main/kotlin/fr/o80/SequenceBuilder.kt).

```kotlin
val tasksSequence = buildTasksSequence<Task> {
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
```
Result:
```
[
    [MigrateDatabase, LoadLocalConfig] <-- start by executine this ones (no matter the order, can be done in parallel)
    [LoadRemoteConfig] <-- then this one
    [InitCrashlytics, CheckVersionIsUpToDate] <-- then these ones (no matter the order, can be done in parallel)
]
```

### Based on KClass

```kotlin
buildTasksSequence<KClass<out Task>> {
    MigrateDatabase::class dependsOn NoTask
    LoadLocalConfig::class dependsOn NoTask
    LoadRemoteConfig::class dependsOn LoadLocalConfig::class
    InitCrashlytics::class dependsOn LoadLocalConfig::class
    InitCrashlytics::class dependsOn LoadRemoteConfig::class
    CheckVersionIsUpToDate::class dependsOn LoadRemoteConfig::class
}
```

## Nota bene

This code is far away to be production ready, there are many edge cases that are not handled like
Cyclic dependencies for instance.