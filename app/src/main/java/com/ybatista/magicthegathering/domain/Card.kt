package com.ybatista.magicthegathering.domain

data class Card(
    var name: String,
    var type: String,
    var text: String,
    var imageUrl: String,
)