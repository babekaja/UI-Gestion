package org.babetech.borastock.ui.screens.stock

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class StockItem(
    val id: String,
    val name: String,
    val category: String,
    val currentStock: Int,
    val minStock: Int,
    val maxStock: Int,
    val price: Double,
    val supplier: String,
    val lastUpdate: String,
    val status: StockStatus
)

enum class StockStatus(val label: String, val color: Color, val icon: ImageVector) {
    IN_STOCK("En stock", Color(0xFF22c55e), Icons.Default.CheckCircle),
    LOW_STOCK("Stock faible", Color(0xFFf59e0b), Icons.Default.Warning),
    OUT_OF_STOCK("Rupture", Color(0xFFef4444), Icons.Default.Error),
    OVERSTOCKED("Surstock", Color(0xFF3b82f6), Icons.Default.TrendingUp)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockScreen() {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Tous") }
    var sortBy by remember { mutableStateOf("Nom") }

    // Données d'exemple
    val stockItems = remember {
        listOf(
            StockItem("1", "iPhone 15 Pro", "Électronique", 25, 10, 100, 1199.99, "Apple Inc.", "Il y a 2h", StockStatus.IN_STOCK),
            StockItem("2", "Samsung Galaxy S24", "Électronique", 8, 15, 80, 899.99, "Samsung", "Il y a 1h", StockStatus.LOW_STOCK),
            StockItem("3", "MacBook Air M3", "Informatique", 0, 5, 50, 1299.99, "Apple Inc.", "Il y a 30min", StockStatus.OUT_OF_STOCK),
            StockItem("4", "AirPods Pro", "Accessoires", 150, 20, 200, 249.99, "Apple Inc.", "Il y a 3h", StockStatus.OVERSTOCKED),
            StockItem("5", "Dell XPS 13", "Informatique", 12, 8, 40, 999.99, "Dell", "Il y a 1h", StockStatus.IN_STOCK),
            StockItem("6", "Sony WH-1000XM5", "Audio", 5, 10, 60, 399.99, "Sony", "Il y a 45min", StockStatus.LOW_STOCK),
            StockItem("7", "iPad Pro 12.9", "Tablettes", 18, 12, 70, 1099.99, "Apple Inc.", "Il y a 2h", StockStatus.IN_STOCK),
            StockItem("8", "Surface Pro 9", "Tablettes", 0, 6, 35, 1199.99, "Microsoft", "Il y a 15min", StockStatus.OUT_OF_STOCK)
        )
    }

    val filteredItems = stockItems.filter { item ->
        val matchesSearch = item.name.contains(searchQuery, ignoreCase = true) ||
                item.category.contains(searchQuery, ignoreCase = true) ||
                item.supplier.contains(searchQuery, ignoreCase = true)
        
        val matchesFilter = when (selectedFilter) {
            "Tous" -> true
            "En stock" -> item.status == StockStatus.IN_STOCK
            "Stock faible" -> item.status == StockStatus.LOW_STOCK
            "Rupture" -> item.status == StockStatus.OUT_OF_STOCK
            "Surstock" -> item.status == StockStatus.OVERSTOCKED
            else -> true
        }
        
        matchesSearch && matchesFilter
    }.let { items ->
        when (sortBy) {
            "Nom" -> items.sortedBy { it.name }
            "Stock" -> items.sortedBy { it.currentStock }
            "Prix" -> items.sortedBy { it.price }
            "Statut" -> items.sortedBy { it.status.label }
            else -> items
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.3f)
                    )
                )
            )
    ) {
        // Header avec statistiques
        StockHeader(stockItems = stockItems)
        
        // Barre de recherche et filtres
        SearchAndFiltersSection(
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            selectedFilter = selectedFilter,
            onFilterChange = { selectedFilter = it },
            sortBy = sortBy,
            onSortChange = { sortBy = it }
        )
        
        // Liste des produits
        StockItemsList(
            items = filteredItems,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StockHeader(stockItems: List<StockItem>) {
    val totalItems = stockItems.size
    val inStock = stockItems.count { it.status == StockStatus.IN_STOCK }
    val lowStock = stockItems.count { it.status == StockStatus.LOW_STOCK }
    val outOfStock = stockItems.count { it.status == StockStatus.OUT_OF_STOCK }
    val totalValue = stockItems.sumOf { it.price * it.currentStock }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Titre
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.2f)
                            )
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Inventory,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Gestion des Stocks",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Vue d'ensemble de votre inventaire",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            // Statistiques
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StockStatCard(
                    title = "Total Produits",
                    value = totalItems.toString(),
                    icon = Icons.Default.Inventory,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                StockStatCard(
                    title = "En Stock",
                    value = inStock.toString(),
                    icon = Icons.Default.CheckCircle,
                    color = Color(0xFF22c55e),
                    modifier = Modifier.weight(1f)
                )
                StockStatCard(
                    title = "Stock Faible",
                    value = lowStock.toString(),
                    icon = Icons.Default.Warning,
                    color = Color(0xFFf59e0b),
                    modifier = Modifier.weight(1f)
                )
                StockStatCard(
                    title = "Ruptures",
                    value = outOfStock.toString(),
                    icon = Icons.Default.Error,
                    color = Color(0xFFef4444),
                    modifier = Modifier.weight(1f)
                )
            }
            
            // Valeur totale
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.AttachMoney,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Valeur totale du stock",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = "${String.format("%.2f", totalValue)} €",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
private fun StockStatCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(12.dp),
                spotColor = color.copy(alpha = 0.1f)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchAndFiltersSection(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedFilter: String,
    onFilterChange: (String) -> Unit,
    sortBy: String,
    onSortChange: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Barre de recherche
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                label = { Text("Rechercher un produit...") },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary
                )
            )
            
            // Filtres et tri
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Filtre par statut
                var filterExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = filterExpanded,
                    onExpandedChange = { filterExpanded = !filterExpanded },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = selectedFilter,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Filtrer") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = filterExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = filterExpanded,
                        onDismissRequest = { filterExpanded = false }
                    ) {
                        listOf("Tous", "En stock", "Stock faible", "Rupture", "Surstock").forEach { filter ->
                            DropdownMenuItem(
                                text = { Text(filter) },
                                onClick = {
                                    onFilterChange(filter)
                                    filterExpanded = false
                                }
                            )
                        }
                    }
                }
                
                // Tri
                var sortExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = sortExpanded,
                    onExpandedChange = { sortExpanded = !sortExpanded },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = sortBy,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Trier par") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = sortExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = sortExpanded,
                        onDismissRequest = { sortExpanded = false }
                    ) {
                        listOf("Nom", "Stock", "Prix", "Statut").forEach { sort ->
                            DropdownMenuItem(
                                text = { Text(sort) },
                                onClick = {
                                    onSortChange(sort)
                                    sortExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StockItemsList(
    items: List<StockItem>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(items) { item ->
            StockItemCard(item = item)
        }
    }
}

@Composable
private fun StockItemCard(item: StockItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = item.status.color.copy(alpha = 0.1f)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // En-tête avec nom et statut
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = item.category,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = item.status.color.copy(alpha = 0.1f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = item.status.icon,
                            contentDescription = null,
                            tint = item.status.color,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = item.status.label,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = item.status.color
                        )
                    }
                }
            }
            
            // Informations détaillées
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                InfoItem(
                    label = "Stock actuel",
                    value = "${item.currentStock} unités",
                    modifier = Modifier.weight(1f)
                )
                InfoItem(
                    label = "Prix unitaire",
                    value = "${String.format("%.2f", item.price)} €",
                    modifier = Modifier.weight(1f)
                )
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                InfoItem(
                    label = "Fournisseur",
                    value = item.supplier,
                    modifier = Modifier.weight(1f)
                )
                InfoItem(
                    label = "Dernière MAJ",
                    value = item.lastUpdate,
                    modifier = Modifier.weight(1f)
                )
            }
            
            // Barre de progression du stock
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Niveau de stock",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${item.currentStock}/${item.maxStock}",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                
                LinearProgressIndicator(
                    progress = { (item.currentStock.toFloat() / item.maxStock.toFloat()).coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = when {
                        item.currentStock <= item.minStock -> Color(0xFFef4444)
                        item.currentStock >= item.maxStock * 0.8 -> Color(0xFF3b82f6)
                        else -> Color(0xFF22c55e)
                    },
                    trackColor = MaterialTheme.colorScheme.surfaceContainer,
                )
            }
            
            // Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { /* TODO: Modifier le stock */ },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Modifier")
                }
                
                Button(
                    onClick = { /* TODO: Voir détails */ },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        Icons.Default.Visibility,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Détails")
                }
            }
        }
    }
}

@Composable
private fun InfoItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}