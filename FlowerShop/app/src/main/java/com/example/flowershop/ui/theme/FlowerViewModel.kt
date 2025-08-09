package com.example.flowershop.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import com.example.flowershop.domain.Flower
import com.example.flowershop.data.FlowerRepository
import com.example.flowershop.data.InMemoryFlowerRepository

data class UiState(
    val catalog: List<Flower> = emptyList(),
    val cart: Map<String, Int> = emptyMap(), // id -> qty
    val query: String = ""
)

sealed class Intent {
    object Load : Intent()
    data class AddToCart(val id: String) : Intent()
    data class RemoveFromCart(val id: String) : Intent()
    data class Search(val text: String) : Intent()
    object ClearCart : Intent()
}

class FlowerViewModel(
    private val repo: FlowerRepository = InMemoryFlowerRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state

    fun onIntent(intent: Intent) {
        when (intent) {
            Intent.Load -> {
                _state.update { it.copy(catalog = repo.getCatalog()) }
            }
            is Intent.AddToCart -> {
                _state.update { s ->
                    val qty = (s.cart[intent.id] ?: 0) + 1
                    s.copy(cart = s.cart.toMutableMap().apply { put(intent.id, qty) })
                }
            }
            is Intent.RemoveFromCart -> {
                _state.update { s ->
                    val current = s.cart[intent.id] ?: 0
                    val next = (current - 1).coerceAtLeast(0)
                    val map = s.cart.toMutableMap()
                    if (next == 0) map.remove(intent.id) else map[intent.id] = next
                    s.copy(cart = map)
                }
            }
            is Intent.Search -> {
                _state.update { it.copy(query = intent.text) }
            }
            Intent.ClearCart -> {
                _state.update { it.copy(cart = emptyMap()) }
            }
        }
    }
}
