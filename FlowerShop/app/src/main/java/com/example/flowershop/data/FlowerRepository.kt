package com.example.flowershop.data

import com.example.flowershop.domain.Flower

interface FlowerRepository {
    fun getCatalog(): List<Flower>
}

class InMemoryFlowerRepository : FlowerRepository {
    private val items = listOf(
        Flower("roses", "Roses Bouquet", 24.99, "Classic red roses."),
        Flower("tulips", "Tulip Bundle", 18.49, "Fresh mixed tulips."),
        Flower("orchid", "White Orchid", 32.00, "Elegant potted orchid."),
        Flower("sunflower", "Sunflower Smile", 15.00, "Bright sunflowers to cheer up.")
    )
    override fun getCatalog(): List<Flower> = items
}
