package fr.o80


interface Task {
    fun run()
}

class LoadLocalConfig : Task {
    override fun run() {
        println("LoadLocalConfig")
    }
}

class LoadRemoteConfig : Task {
    override fun run() {
        println("LoadRemoteConfig")
    }
}

class InitCrashlytics : Task {
    override fun run() {
        println("InitCrashlytics")
    }
}

class CheckVersionIsUpToDate : Task {
    override fun run() {
        println("CheckVersionIsUpToDate")
    }
}

class MigrateDatabase : Task {
    override fun run() {
        println("MigrateDatabase")
    }
}
