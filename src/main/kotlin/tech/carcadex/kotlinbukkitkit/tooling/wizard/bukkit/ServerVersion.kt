package tech.carcadex.kotlinbukkitkit.tooling.wizard.bukkit

enum class ServerVersion(val displayName: String, val maxVersion: String, val minor: Int,) {
    v1_8("1.8", "1.8.9", 8),
    v1_9("1.9", "1.9.4", 9),
    v1_12("1.12", "1.12.2", 12),
    v1_13("1.13", "1.13.2", 13),
    v1_14("1.14", "1.14.4", 14),
    v1_15("1.15", "1.15.2", 15),
    v1_16("1.16", "1.16.5", 16),
    v1_17("1.17", "1.17.1", 17),
    v1_18("1.18", "1.18.2", 18),
    v1_19("1.19", "1.19.4", 19),
    v1_20("1.20", "1.20.6", 20),
    v1_21("1.21", "1.21", 20),;


    override fun toString(): String {
        return displayName
    }
}
