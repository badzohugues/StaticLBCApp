package com.badzohugues.staticlbcapp.misc

enum class ErrorMessage(val message: String) {
    SERVER_ERROR("Erreur serveur"),
    NETWORK_ERROR("Aucun réseau"),
    DATABASE_ERROR("Erreur base de données"),
    UNKNOWN_ERROR("Erreur inconnu")
}
