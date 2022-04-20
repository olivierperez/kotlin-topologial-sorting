package fr.o80

fun <T> buildTasksSequence(sequenceBuilder: SequenceBuilder<T>.() -> Unit): List<List<T>> {
    return SequenceBuilder<T>().apply(sequenceBuilder).create()
}

data class Dependency<T>(
    val inbound: T,
    val outbound: T,
)

object NoTask

class SequenceBuilder<T> {

    private val allTasks = mutableSetOf<T>()
    private val dependencies = mutableSetOf<Dependency<T>>()

    infix fun T.dependsOn(task: T) {
        allTasks.add(this)
        allTasks.add(task)
        dependencies.add(Dependency(inbound = task, outbound = this))
    }

    infix fun T.dependsOn(noTask: NoTask) {
        allTasks.add(this)
    }

    fun create(): List<List<T>> {
        val result = mutableListOf<List<T>>()
        var remainingTasks = allTasks.toList()
        var remainingDependencies = dependencies.toList()

        while (remainingTasks.isNotEmpty()) {
            val (firstTasks, nextTasks) = remainingTasks.partition { task ->
                task !in remainingDependencies.map(
                    Dependency<T>::outbound
                )
            }
            remainingTasks = nextTasks
            remainingDependencies = remainingDependencies.filterNot { it.inbound in firstTasks }

            result.add(firstTasks)
        }
        return result
    }
}