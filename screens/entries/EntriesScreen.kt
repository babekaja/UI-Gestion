package org.babetech.borastock.ui.screens.entries

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.*
import androidx.compose.material3.adaptive.navigation.rememberSupportingPaneScaffoldNavigator
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
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class StockEntry(
    val id: String,
    val productName: String,
    val category: String,
    val quantity: Int,
    val unitPrice: Double,
    val totalValue: Double,
    val supplier: String,
    val entryDate: LocalDateTime,
    val batchNumber: String?,
    val expiryDate: LocalDateTime?,
    val status: EntryStatus,
    val notes: String?
)

enum class EntryStatus(val label: String, val color: Color, val icon: ImageVector) {
    PENDING("En attente", Color(0xFFf59e0b), Icons.Default.Schedule),
    VALIDATED("Validée", Color(0xFF22c55e), Icons.Default.CheckCircle),
    RECEIVED("Reçue", Color(0xFF3b82f6), Icons.Default.Inventory),
    CANCELLED("Annulée", Color(0xFFef4444), Icons.Default.Cancel)
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun EntriesScreen() {
    val navigator = rememberSupportingPaneScaffoldNavigator()
    val scope = rememberCoroutineScope()
    
    var selectedEntry by remember { mutableStateOf<StockEntry?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Toutes") }
    var sortBy by remember { mutableStateOf("Date") }

    // Données d'exemple
    val stockEntries = remember {
        listOf(
            StockEntry(
                id = "E001",
                productName = "iPhone 15 Pro Max",
                category = "Électronique",
                quantity = 50,
                unitPrice = 1199.99,
                totalValue = 59999.50,
                supplier = "Apple Inc.",
                entryDate = LocalDateTime.now().minusHours(2),
                batchNumber = "APL2024001",
                expiryDate = null,
                status = EntryStatus.RECEIVED,
                notes = "Livraison conforme, emballage parfait"
            ),
            StockEntry(
                id = "E002",
                productName = "Samsung Galaxy S24 Ultra",
                category = "Électronique",
                quantity = 30,
                unitPrice = 1299.99,
                totalValue = 38999.70,
                supplier = "Samsung Electronics",
                entryDate = LocalDateTime.now().minusHours(4),
                batchNumber = "SAM2024002",
                expiryDate = null,
                status = EntryStatus.VALIDATED,
                notes = "En attente de réception"
            ),
            StockEntry(
                id = "E003",
                productName = "MacBook Air M3",
                category = "Informatique",
                quantity = 15,
                unitPrice = 1299.99,
                totalValue = 19499.85,
                supplier = "Apple Inc.",
                entryDate = LocalDateTime.now().minusHours(6),
                batchNumber = "APL2024003",
                expiryDate = null,
                status = EntryStatus.PENDING,
                notes = "Commande passée, livraison prévue demain"
            ),
            StockEntry(
                id = "E004",
                productName = "AirPods Pro 2",
                category = "Audio",
                quantity = 100,
                unitPrice = 279.99,
                totalValue = 27999.00,
                supplier = "Apple Inc.",
                entryDate = LocalDateTime.now().minusHours(8),
                batchNumber = "APL2024004",
                expiryDate = null,
                status = EntryStatus.RECEIVED,
                notes = "Stock complet reçu"
            ),
            StockEntry(
                id = "E005",
                productName = "Dell XPS 13",
                category = "Informatique",
                quantity = 20,
                unitPrice = 999.99,
                totalValue = 19999.80,
                supplier = "Dell Technologies",
                entryDate = LocalDateTime.now().minusHours(12),
                batchNumber = "DELL2024001",
                expiryDate = null,
                status = EntryStatus.CANCELLED,
                notes = "Annulée - problème de qualité"
            ),
            StockEntry(
                id = "E006",
                productName = "Sony WH-1000XM5",
                category = "Audio",
                quantity = 40,
                unitPrice = 399.99,
                totalValue = 15999.60,
                supplier = "Sony Corporation",
                entryDate = LocalDateTime.now().minusDays(1),
                batchNumber = "SONY2024001",
                expiryDate = null,
                status = EntryStatus.RECEIVED,
                notes = "Excellent état, emballage premium"
            )
        )
    }

    val filteredEntries = stockEntries.filter { entry ->
        val matchesSearch = entry.productName.contains(searchQuery, ignoreCase = true) ||
                entry.category.contains(searchQuery, ignoreCase = true) ||
                entry.supplier.contains(searchQuery, ignoreCase = true) ||
                entry.batchNumber?.contains(searchQuery, ignoreCase = true) == true
        
        val matchesFilter = when (selectedFilter) {
            "Toutes" -> true
            "En attente" -> entry.status == EntryStatus.PENDING
            "Validées" -> entry.status == EntryStatus.VALIDATED
            "Reçues" -> entry.status == EntryStatus.RECEIVED
            "Annulées" -> entry.status == EntryStatus.CANCELLED
            else -> true
        }
        
        matchesSearch && matchesFilter
    }.let { entries ->
        when (sortBy) {
            "Date" -> entries.sortedByDescending { it.entryDate }
            "Produit" -> entries.sortedBy { it.productName }
            "Quantité" -> entries.sortedByDescending { it.quantity }
            "Valeur" -> entries.sortedByDescending { it.totalValue }
            "Statut" -> entries.sortedBy { it.status.label }
            else -> entries
        }
    }

    SupportingPaneScaffold(
        value = navigator.scaffoldValue,
        directive = navigator.scaffoldDirective,
        mainPane = {
            AnimatedPane {
                EntriesMainPane(
                    entries = filteredEntries,
                    searchQuery = searchQuery,
                    onSearchQueryChange = { searchQuery = it },
                    selectedFilter = selectedFilter,
                    onFilterChange = { selectedFilter = it },
                    sortBy = sortBy,
                    onSortChange = { sortBy = it },
                    onEntrySelected = { entry ->
                        selectedEntry = entry
                        scope.launch {
                            navigator.navigateTo(SupportingPaneScaffoldRole.Supporting)
                        }
                    },
                    showDetailButton = navigator.scaffoldValue[SupportingPaneScaffoldRole.Supporting] == PaneAdaptedValue.Hidden
                )
            }
        },
        supportingPane = {
            AnimatedPane {
                selectedEntry?.let { entry ->
                    EntryDetailPane(
                        entry = entry,
                        onBack = {
                            scope.launch { navigator.navigateBack() }
                        },
                        showBackButton = navigator.scaffoldValue[SupportingPaneScaffoldRole.Main] == PaneAdaptedValue.Hidden
                    )
                }
            }
        }
    )
}

@Composable
private fun EntriesMainPane(
    entries: List<StockEntry>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedFilter: String,
    onFilterChange: (String) -> Unit,
    sortBy: String,
    onSortChange: (String) -> Unit,
    onEntrySelected: (StockEntry) -> Unit,
    showDetailButton: Boolean
) {
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
        EntriesHeader(entries = entries)
        
        // Barre de recherche et filtres
        SearchAndFiltersSection(
            searchQuery = searchQuery,
            onSearchQueryChange = onSearchQueryChange,
            selectedFilter = selectedFilter,
            onFilterChange = onFilterChange,
            sortBy = sortBy,
            onSortChange = onSortChange
        )
        
        // Liste des entrées
        EntriesList(
            entries = entries,
            onEntrySelected = onEntrySelected,
            modifier = Modifier.weight(1f)
        )
        
        // Bouton d'ajout flottant
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            FloatingActionButton(
                onClick = { /* TODO: Ajouter nouvelle entrée */ },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(16.dp)
                )
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Ajouter une entrée",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun EntriesHeader(entries: List<StockEntry>) {
    val totalEntries = entries.size
    val pendingEntries = entries.count { it.status == EntryStatus.PENDING }
    val receivedEntries = entries.count { it.status == EntryStatus.RECEIVED }
    val totalValue = entries.filter { it.status != EntryStatus.CANCELLED }.sumOf { it.totalValue }

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
                            Icons.Default.Input,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Entrées de Stock",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Gestion des réceptions et commandes",
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
                EntryStatCard(
                    title = "Total Entrées",
                    value = totalEntries.toString(),
                    icon = Icons.Default.Receipt,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                EntryStatCard(
                    title = "En Attente",
                    value = pendingEntries.toString(),
                    icon = Icons.Default.Schedule,
                    color = Color(0xFFf59e0b),
                    modifier = Modifier.weight(1f)
                )
                EntryStatCard(
                    title = "Reçues",
                    value = receivedEntries.toString(),
                    icon = Icons.Default.CheckCircle,
                    color = Color(0xFF22c55e),
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
                            Icons.Default.TrendingUp,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Valeur totale des entrées",
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
private fun EntryStatCard(
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
                label = { Text("Rechercher une entrée...") },
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
                        listOf("Toutes", "En attente", "Validées", "Reçues", "Annulées").forEach { filter ->
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
                        listOf("Date", "Produit", "Quantité", "Valeur", "Statut").forEach { sort ->
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
private fun EntriesList(
    entries: List<StockEntry>,
    onEntrySelected: (StockEntry) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(entries) { entry ->
            EntryCard(
                entry = entry,
                onClick = { onEntrySelected(entry) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EntryCard(
    entry: StockEntry,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = entry.status.color.copy(alpha = 0.1f)
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
                        text = entry.productName,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = entry.category,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = entry.status.color.copy(alpha = 0.1f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = entry.status.icon,
                            contentDescription = null,
                            tint = entry.status.color,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = entry.status.label,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = entry.status.color
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
                    label = "Quantité",
                    value = "${entry.quantity} unités",
                    modifier = Modifier.weight(1f)
                )
                InfoItem(
                    label = "Prix unitaire",
                    value = "${String.format("%.2f", entry.unitPrice)} €",
                    modifier = Modifier.weight(1f)
                )
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                InfoItem(
                    label = "Valeur totale",
                    value = "${String.format("%.2f", entry.totalValue)} €",
                    modifier = Modifier.weight(1f)
                )
                InfoItem(
                    label = "Date d'entrée",
                    value = entry.entryDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                    modifier = Modifier.weight(1f)
                )
            }
            
            // Fournisseur et lot
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                InfoItem(
                    label = "Fournisseur",
                    value = entry.supplier,
                    modifier = Modifier.weight(1f)
                )
                entry.batchNumber?.let { batch ->
                    InfoItem(
                        label = "N° de lot",
                        value = batch,
                        modifier = Modifier.weight(1f)
                    )
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

@Composable
private fun EntryDetailPane(
    entry: StockEntry,
    onBack: () -> Unit,
    showBackButton: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (showBackButton) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainer)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
            }
        }
        
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(20.dp)
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
                // En-tête
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(entry.status.color.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = entry.status.icon,
                            contentDescription = null,
                            tint = entry.status.color,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    
                    Column {
                        Text(
                            text = entry.productName,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Entrée ${entry.id}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                
                // Détails complets
                DetailSection(
                    title = "Informations produit",
                    items = listOf(
                        "Catégorie" to entry.category,
                        "Quantité" to "${entry.quantity} unités",
                        "Prix unitaire" to "${String.format("%.2f", entry.unitPrice)} €",
                        "Valeur totale" to "${String.format("%.2f", entry.totalValue)} €"
                    )
                )
                
                DetailSection(
                    title = "Informations fournisseur",
                    items = listOf(
                        "Fournisseur" to entry.supplier,
                        "N° de lot" to (entry.batchNumber ?: "Non spécifié"),
                        "Date d'entrée" to entry.entryDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm")),
                        "Statut" to entry.status.label
                    )
                )
                
                entry.notes?.let { notes ->
                    DetailSection(
                        title = "Notes",
                        items = listOf("Commentaires" to notes)
                    )
                }
                
                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { /* TODO: Modifier l'entrée */ },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Modifier")
                    }
                    
                    Button(
                        onClick = { /* TODO: Valider l'entrée */ },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        enabled = entry.status == EntryStatus.PENDING
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Valider")
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailSection(
    title: String,
    items: List<Pair<String, String>>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onSurface
        )
        
        items.forEach { (label, value) ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}