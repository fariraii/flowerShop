package com.example.flowershop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.flowershop.ui.*
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlowerShopApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlowerShopApp(vm: FlowerViewModel = viewModel()) {
    val state by vm.state.collectAsState()
    LaunchedEffect(Unit) { vm.onIntent(Intent.Load) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("FlowerShop") })
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            SearchBar(state.query, onChange = { vm.onIntent(Intent.Search(it)) })
            Spacer(Modifier.height(8.dp))
            CatalogList(
                catalog = if (state.query.isBlank()) state.catalog else state.catalog.filter { it.name.contains(state.query, ignoreCase = true) },
                cart = state.cart,
                onAdd = { vm.onIntent(Intent.AddToCart(it)) },
                onRemove = { vm.onIntent(Intent.RemoveFromCart(it)) }
            )
            Spacer(Modifier.height(16.dp))
            CartSummary(
                state,
                onRemove = { id: String -> vm.onIntent(Intent.RemoveFromCart(id)) },
                onClearCart = { vm.onIntent(Intent.ClearCart) }
            )
        }
    }
}

@Composable
fun SearchBar(text: String, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = text,
        onValueChange = onChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Search flowers") }
    )
}

@Composable
fun CatalogList(
    catalog: List<com.example.flowershop.domain.Flower>,
    cart: Map<String, Int>,
    onAdd: (String) -> Unit,
    onRemove: (String) -> Unit
) {
    Column {
        catalog.forEach { item ->
            val qty = cart[item.id] ?: 0
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(item.name, style = MaterialTheme.typography.titleLarge)
                        Text("£${"%.2f".format(item.price)}", style = MaterialTheme.typography.bodyLarge)
                        Text(item.description, style = MaterialTheme.typography.bodySmall)
                    }
                    Spacer(Modifier.width(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedButton(
                            onClick = { if (qty > 0) onRemove(item.id) },
                            enabled = qty > 0
                        ) { Text("-") }
                        Text(
                            qty.toString(),
                            modifier = Modifier.padding(horizontal = 8.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Button(onClick = { onAdd(item.id) }) { Text("+") }
                    }
                }
            }
        }
    }
}

@Composable
fun CartSummary(
    state: UiState,
    onRemove: (String) -> Unit,
    onClearCart: () -> Unit
) {
    val total = state.cart.entries.sumOf { (id, qty) ->
        val item = state.catalog.firstOrNull { it.id == id }
        (item?.price ?: 0.0) * qty
    }
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            Text("Cart", style = MaterialTheme.typography.titleLarge)
            if (state.cart.isEmpty()) {
                Text("Your cart is empty.")
            } else {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onClearCart) {
                        Text("Clear Cart")
                    }
                }
                state.cart.forEach { (id, qty) ->
                    val item = state.catalog.firstOrNull { it.id == id }
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("${item?.name ?: id} x$qty")
                        Row {
                            OutlinedButton(onClick = { onRemove(id) }) { Text("-") }
                        }
                    }
                }
                Divider(Modifier.padding(vertical = 8.dp))
                Text("Total: £${"%.2f".format(total)}", style = MaterialTheme.typography.titleMedium)
                Button(onClick = {}, enabled = total > 0.0) { Text("Checkout") }
            }
        }
    }
}