object Day20 {
    enum class ModuleType {
        BROADCASTER,
        CONJUNCTION,
        FLIP_FLOP
    }

    enum class PulseType {
        HIGH,
        LOW
    }

    data class Pulse(val type: PulseType, val sender: String)

    data class Module(
        val id: String,
        val type: ModuleType,
        val destinations: List<String>,
        var on: Boolean,
        val inputs: MutableSet<String> = mutableSetOf(),
        val lastPulses: MutableMap<String, PulseType> = mutableMapOf()
    ) {

        fun process(pulse: Pulse): List<Pair<String, Pulse>> {
            lastPulses[pulse.sender] = pulse.type
            return when (type) {
                ModuleType.BROADCASTER -> destinations.map { it to pulse }
                ModuleType.CONJUNCTION -> {
                    val typeToSend = if (inputs.map { lastPulses[it] ?: PulseType.LOW }.all { it == PulseType.HIGH }) {
                        PulseType.LOW
                    } else {
                        PulseType.HIGH
                    }
                    destinations.map { it to Pulse(typeToSend, id) }
                }

                ModuleType.FLIP_FLOP -> {
                    if (pulse.type == PulseType.HIGH) {
                        emptyList()
                    } else {
                        val typeToSend = if (on) {
                            PulseType.LOW
                        } else {
                            PulseType.HIGH
                        }
                        on = !on
                        destinations.map { it to Pulse(typeToSend, id) }
                    }
                }
            }
        }

        companion object {
            fun parse(input: String): Module {
                val (moduleIdAndType, destinations) = input.split(" -> ")
                if (moduleIdAndType == "broadcaster") {
                    return Module(
                        "broadcaster",
                        ModuleType.BROADCASTER,
                        destinations.split(", "),
                        true
                    )
                }
                val type = moduleIdAndType.take(1)
                val id = moduleIdAndType.drop(1)
                return Module(
                    id,
                    when (type) {
                        "%" -> ModuleType.FLIP_FLOP
                        "&" -> ModuleType.CONJUNCTION
                        else -> throw IllegalArgumentException("Unknown module type: $type")
                    },
                    destinations.split(", "),
                    type == "&"
                )
            }
        }
    }

    fun part1(input: List<String>): String {
        val modules = input.map { Module.parse(it) }.associateBy { it.id }
        modules.forEach { key, value ->
            value.destinations.forEach { modules[it]?.inputs?.add(key) }
        }
        val presses = (1..1000).map { pushButton(modules) }.flatMap { it.values }.reduce { acc, map ->
            (acc.toList() + map.toList())
                .groupBy { it.first }
                .mapValues { it.value.map { it.second }.sum() }
        }
        return presses.values.reduce(Long::times).toString()
    }

    private fun pushButton(modules: Map<String, Module>): Map<String, Map<PulseType, Long>> {
        val initialModule = modules["broadcaster"]!!
        val initialPulse = Pulse(PulseType.LOW, "broadcaster")
        val queue = ArrayDeque<Pair<Module?, Pulse>>(listOf(initialModule to initialPulse))
        val pulseTypeMap = mutableMapOf<String, MutableMap<PulseType, Long>>()
        pulseTypeMap["broadcaster"] = mutableMapOf(PulseType.LOW to 1L)
        while (queue.isNotEmpty()) {
            val (module, pulse) = queue.removeFirst()
            val destinations = module?.process(pulse) ?: emptyList()
            destinations.forEach { (destination, pulse) ->
                val moduleMap = pulseTypeMap.getOrPut(destination) { mutableMapOf() }
                moduleMap[pulse.type] = moduleMap.getOrDefault(pulse.type, 0) + 1
            }
            queue.addAll(destinations.map { (destination, pulse) -> modules[destination] to pulse })
        }
        return pulseTypeMap
    }

    private fun numberOfButtonPushesUntilLowState(modules: Map<String, Module>, key: String): Long {
        val initialModule = modules["broadcaster"]!!
        val initialPulse = Pulse(PulseType.LOW, "broadcaster")
        val queue = ArrayDeque<Pair<Module?, Pulse>>(listOf(initialModule to initialPulse))
        var count = 1L
        // We know the input to the target is a cond, so we can just find when the inputs to the cond are all high
        val targetInput =
            modules.values.find { it.destinations.contains(key) } ?: throw IllegalStateException("Invalid input: $key")
        val inputsOfTargetInput = targetInput.inputs.toSet()
        val countWhenHighPulse = mutableMapOf<String, Long>()
        while (true) {
            if (countWhenHighPulse.size == inputsOfTargetInput.size) {
                return countWhenHighPulse.values.reduce(Long::times)
            }
            val (module, pulse) = queue.removeFirst()
            val destinations = module?.process(pulse) ?: emptyList()
            val destinationsToPulses = destinations.map { (destination, pulse) -> modules[destination] to pulse }
            destinationsToPulses.forEach { (desination, pulse) ->
                if ((desination?.id ?: "") == targetInput.id)
                    if (pulse.type == PulseType.HIGH && !countWhenHighPulse.containsKey(module?.id)) {
                        countWhenHighPulse[module!!.id] = count
                    }
            }
            queue.addAll(destinationsToPulses)
            if (queue.isEmpty()) {
                queue.add(initialModule to initialPulse)
                count++
            }
        }
    }

    fun part2(input: List<String>): String {
        val modules = input.map { Module.parse(it) }.associateBy { it.id }
        return numberOfButtonPushesUntilLowState(modules, "rx").toString()
    }

}
