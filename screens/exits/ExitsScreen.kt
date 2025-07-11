package org.babetech.borastock.ui.screens.exits

import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class StockExit(
    val id: String,
    val productName: String,
    val category: String,
    val quantity: Int,
    val unitPrice: Double,
    val totalValue: Double,
    val customer: String,
    val exitDate: LocalDateTime,
    val orderNumber: String?,
    val deliveryAddress: String?,
    val status: ExitStatus,
    val notes: String?,
    val urgency: ExitUrgency
)

enum class ExitStatus(val label: String, val color: Color, val icon: ImageVector) {
    PENDING("En préparation", Color(0xFFf59e0b), Icons.Default.Schedule),
    PREPARED("Préparée", Color(0xFF3b82f6), Icons.Default.Inventory),
    SHIPPED("Expédiée", Color(0xFF8b5cf6), Icons.Default.LocalShipping),
    DELIVERED("Livrée", Color(0xFF22c55e), Icons.Default.CheckCircle),
    CANCELLED("Annulée", Color(0xFFef4444), Icons.Default.Cancel)
}

enum class ExitUrgency(val label: String, val color: Color, val icon: ImageVector) {
    LOW("Normale", Color(0xFF6b7280), Icons.Default.Remove),
    MEDIUM("Prioritaire", Color(0xFFf59e0b), Icons.Default.PriorityHigh),
    HIGH("Urgente", Color(0xFFef4444), Icons.Default.Warning)
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ExitsScreen() {
    val navigator = rememberSupportingPaneScaffoldNavigator()
    val scope = rememberCoroutineScope()
    
    var selectedExit by remember { mutableStateOf<StockExit?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Toutes") }
    var sortBy by remember { mutableStateOf("Date") }
    var isLoading by remember { mutableStateOf(true) }

    // Animation de chargement initial
    LaunchedEffect(Unit) {
        delay(800)
        isLoading = false
    }

    // Données d'exemple avec différents statuts et urgences
    val stockExits = remember {
        listOf(
            StockExit(
                id = "S001",
                productName = "iPhone 15 Pro Max",
                category = "Électronique",
                quantity = 2,
                unitPrice = 1199.99,
                totalValue = 2399.98,
                customer = "TechStore Paris",
                exitDate = LocalDateTime.now().minusHours(1),
                orderNumber = "CMD2024001",
                deliveryAddress = "123 Rue de Rivoli, 75001 Paris",
                status = ExitStatus.SHIPPED,
                notes = "Livraison express demandée",
                urgency = ExitUrgency.HIGH
            ),
            StockExit(
                id = "S002",
                productName = "Samsung Galaxy S24 Ultra",
                category = "Électronique",
                quantity = 1,
                unitPrice = 1299.99,
                totalValue = 1299.99,
                customer = "Mobile World Lyon",
                exitDate = LocalDateTime.now().minusHours(3),
                orderNumber = "CMD2024002",
                deliveryAddress = "45 Place Bellecour, 69002 Lyon",
                status = ExitStatus.DELIVERED,
                notes = "Client satisfait, livraison réussie",
                urgency = ExitUrgency.LOW
            ),
            StockExit(
                id = "S003",
                productName = "MacBook Air M3",
                category = "Informatique",
                quantity = 3,
                unitPrice = 1299.99,
                totalValue = 3899.97,
                customer = "Université de Bordeaux",
                exitDate = LocalDateTime.now().minusHours(5),
                orderNumber = "CMD2024003",
                deliveryAddress = "351 Cours de la Libération, 33405 Talence",
                status = ExitStatus.PREPARED,
                notes = "Commande institutionnelle, facture séparée",
                urgency = ExitUrgency.MEDIUM
            ),
            StockExit(
                id = "S004",
                productName = "AirPods Pro 2",
                category = "Audio",
                quantity = 10,
                unitPrice = 279.99,
                totalValue = 2799.90,
                customer = "AudioMax Marseille",
                exitDate = LocalDateTime.now().minusHours(8),
                orderNumber = "CMD2024004",
                deliveryAddress = "12 La Canebière, 13001 Marseille",
                status = ExitStatus.PENDING,
                notes = "Vérifier stock avant expédition",
                urgency = ExitUrgency.LOW
            ),
            StockExit(
                id = "S005",
                productName = "Dell XPS 13",
                category = "Informatique",
                quantity = 1,
                unitPrice = 999.99,
                totalValue = 999.99,
                customer = "StartupTech Lille",
                exitDate = LocalDateTime.now().minusHours(12),
                orderNumber = "CMD2024005",
                deliveryAddress = "78 Rue Nationale, 59000 Lille",
                status = ExitStatus.CANCELLED,
                notes = "Annulée - problème de paiement",
                urgency = ExitUrgency.LOW
            ),
            StockExit(
                id = "S006",
                productName = "Sony WH-1000XM5",
                category = "Audio",
                quantity = 5,
                unitPrice = 399.99,
                totalValue = 1999.95,
                customer = "MusicStore Toulouse",
                exitDate = LocalDateTime.now().minusDays(1),
                orderNumber = "CMD2024006",
                deliveryAddress = "25 Place du Capitole, 31000 Toulouse",
                status = ExitStatus.DELIVERED,
                notes = "Livraison parfaite, client régulier",
                urgency = ExitUrgency.MEDIUM
            ),
            StockExit(
                id = "S007",
                productName = "iPad Pro 12.9",
                category = "Tablettes",
                quantity = 2,
                unitPrice = 1099.99,
                totalValue = 2199.98,
                customer = "DesignStudio Nice",
                exitDate = LocalDateTime.now().minusDays(1).minusHours(6),
                orderNumber = "CMD2024007",
                deliveryAddress = "10 Promenade des Anglais, 06000 Nice",
                status = ExitStatus.SHIPPED,
                notes = "Matériel professionnel pour studio",
                urgency = ExitUrgency.HIGH
            )
        )
    }

    val filteredExits = stockExits.filter { exit ->
        val matchesSearch = exit.productName.contains(searchQuery, ignoreCase = true) ||
                exit.category.contains(searchQuery, ignoreCase = true) ||
                exit.customer.contains(searchQuery, ignoreCase = true) ||
                exit.orderNumber?.contains(searchQuery, ignoreCase = true) == true
        
        val matchesFilter = when (selectedFilter) {
            "Toutes" -> true
            "En préparation" -> exit.status == ExitStatus.PENDING
            "Préparées" -> exit.status == ExitStatus.PREPARED
            "Expédiées" -> exit.status == ExitStatus.SHIPPED
            "Livrées" -> exit.status == ExitStatus.DELIVERED
            "Annulées" -> exit.status == ExitStatus.CANCELLED
            else -> true
        }
        
        matchesSearch && matchesFilter
    }.let { exits ->
        when (sortBy) {
            "Date" -> exits.sortedByDescending { it.exitDate }
            "Produit" -> exits.sortedBy { it.productName }
            "Client" -> exits.sortedBy { it.customer }
            "Quantité" -> exits.sortedByDescending { it.quantity }
            "Valeur" -> exits.sortedByDescending { it.totalValue }
            "Statut" -> exits.sortedBy { it.status.label }
            "Urgence" -> exits.sortedByDescending { it.urgency.ordinal }
            else -> exits
        }
    }

    SupportingPaneScaffold(
        value = navigator.scaffoldValue,
        directive = navigator.scaffoldDirective,
        mainPane = {
            AnimatedPane {
                ExitsMainPane(
                    exits = filteredExits,
                    searchQuery = searchQuery,
                    onSearchQueryChange = { searchQuery = it },
                    selectedFilter = selectedFilter,
                    onFilterChange = { selectedFilter = it },
                    sortBy = sortBy,
                    onSortChange = { sortBy = it },
                    onExitSelected = { exit ->
                        selectedExit = exit
                        scope.launch {
                            navigator.navigateTo(SupportingPaneScaffoldRole.Supporting)
                        }
                    },
                    isLoading = isLoading,
                    showDetailButton = navigator.scaffoldValue[SupportingPaneScaffoldRole.Supporting] == PaneAdaptedValue.Hidden
                )
            }
        },
        supportingPane = {
            AnimatedPane {
                selectedExit?.let { exit ->
                    ExitDetailPane(
                        exit = exit,
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
private fun ExitsMainPane(
    exits: List<StockExit>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedFilter: String,
    onFilterChange: (String) -> Unit,
    sortBy: String,
    onSortChange: (String) -> Unit,
    onExitSelected: (StockExit) -> Unit,
    isLoading: Boolean,
    showDetailButton: Boolean
) {
    var headerVisible by remember { mutableStateOf(false) }
    var contentVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(200)
        headerVisible = true
        delay(300)
        contentVisible = true
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
        // Header avec animation d'entrée
        AnimatedVisibility(
            visible = headerVisible,
            enter = slideInVertically(
                animationSpec = tween(600, easing = EaseOutCubic),
                initialOffsetY = { -it }
            ) + fadeIn(animationSpec = tween(600))
        ) {
            ExitsHeader(exits = exits)
        }
        
        // Barre de recherche et filtres avec animation
        AnimatedVisibility(
            visible = contentVisible,
            enter = slideInVertically(
                animationSpec = tween(700, delayMillis = 200, easing = EaseOutCubic),
                initialOffsetY = { it / 2 }
            ) + fadeIn(animationSpec = tween(700, delayMillis = 200))
        ) {
            SearchAndFiltersSection(
                searchQuery = searchQuery,
                onSearchQueryChange = onSearchQueryChange,
                selectedFilter = selectedFilter,
                onFilterChange = onFilterChange,
                sortBy = sortBy,
                onSortChange = onSortChange
            )
        }
        
        // Liste des sorties avec animation de chargement
        if (isLoading) {
            LoadingAnimation(modifier = Modifier.weight(1f))
        } else {
            AnimatedVisibility(
                visible = contentVisible,
                enter = slideInVertically(
                    animationSpec = tween(800, delayMillis = 400, easing = EaseOutCubic),
                    initialOffsetY = { it }
                ) + fadeIn(animationSpec = tween(800, delayMillis = 400))
            ) {
                ExitsList(
                    exits = exits,
                    onExitSelected = onExitSelected,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        // FAB avec animation de rebond
        AnimatedVisibility(
            visible = contentVisible,
            enter = scaleIn(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                ),
                initialScale = 0f
            ) + fadeIn(animationSpec = tween(500, delayMillis = 600))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                FloatingActionButton(
                    onClick = { /* TODO: Ajouter nouvelle sortie */ },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .shadow(
                            elevation = 12.dp,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .graphicsLayer {
                            // Animation de pulsation subtile
                            val infiniteTransition = rememberInfiniteTransition(label = "fab_pulse")
                            val scale by infiniteTransition.animateFloat(
                                initialValue = 1f,
                                targetValue = 1.05f,
                                animationSpec = infiniteRepeatable(
                                    animation = tween(2000, easing = EaseInOutSine),
                                    repeatMode = RepeatMode.Reverse
                                ), label = "fab_scale"
                            )
                            scaleX = scale
                            scaleY = scale
                        }
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Ajouter une sortie",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingAnimation(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val infiniteTransition = rememberInfiniteTransition(label = "loading")
            val rotation by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                ), label = "loading_rotation"
            )
            
            Icon(
                Icons.Default.Refresh,
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .graphicsLayer { rotationZ = rotation },
                tint = MaterialTheme.colorScheme.primary
            )
            
            Text(
                "Chargement des sorties...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ExitsHeader(exits: List<StockExit>) {
    val totalExits = exits.size
    val pendingExits = exits.count { it.status == ExitStatus.PENDING }
    val shippedExits = exits.count { it.status == ExitStatus.SHIPPED }
    val deliveredExits = exits.count { it.status == ExitStatus.DELIVERED }
    val urgentExits = exits.count { it.urgency == ExitUrgency.HIGH }
    val totalValue = exits.filter { it.status != ExitStatus.CANCELLED }.sumOf { it.totalValue }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(28.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Titre avec animation de gradient
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f),
                                MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.2f)
                            )
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.colorScheme.secondary
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Output,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Sorties de Stock",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.ExtraBold
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Gestion des expéditions et livraisons",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            // Statistiques avec animations décalées
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val stats = listOf(
                    Triple("Total Sorties", totalExits.toString(), Icons.Default.Receipt),
                    Triple("En Préparation", pendingExits.toString(), Icons.Default.Schedule),
                    Triple("Expédiées", shippedExits.toString(), Icons.Default.LocalShipping),
                    Triple("Livrées", deliveredExits.toString(), Icons.Default.CheckCircle)
                )
                
                stats.forEachIndexed { index, (title, value, icon) ->
                    var visible by remember { mutableStateOf(false) }
                    
                    LaunchedEffect(Unit) {
                        delay(index * 150L)
                        visible = true
                    }
                    
                    AnimatedVisibility(
                        visible = visible,
                        enter = scaleIn(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessMedium
                            )
                        ) + fadeIn()
                    ) {
                        ExitStatCard(
                            title = title,
                            value = value,
                            icon = icon,
                            color = when (index) {
                                0 -> MaterialTheme.colorScheme.primary
                                1 -> Color(0xFFf59e0b)
                                2 -> Color(0xFF8b5cf6)
                                else -> Color(0xFF22c55e)
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            
            // Métriques supplémentaires
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Valeur totale
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                Icons.Default.TrendingDown,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = "Valeur totale",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = "${String.format("%.2f", totalValue)} €",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold
                            ),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                
                // Sorties urgentes
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFef4444).copy(alpha = 0.1f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                Icons.Default.Warning,
                                contentDescription = null,
                                tint = Color(0xFFef4444),
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = "Urgentes",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = urgentExits.toString(),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold
                            ),
                            color = Color(0xFFef4444)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ExitStatCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = color.copy(alpha = 0.15f)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
            }
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.ExtraBold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
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
                elevation = 6.dp,
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
            // Barre de recherche avec animation
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                label = { Text("Rechercher une sortie...") },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary
                )
            )
            
            // Filtres et tri
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
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
                        shape = RoundedCornerShape(16.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = filterExpanded,
                        onDismissRequest = { filterExpanded = false }
                    ) {
                        listOf("Toutes", "En préparation", "Préparées", "Expédiées", "Livrées", "Annulées").forEach { filter ->
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
                        shape = RoundedCornerShape(16.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = sortExpanded,
                        onDismissRequest = { sortExpanded = false }
                    ) {
                        listOf("Date", "Produit", "Client", "Quantité", "Valeur", "Statut", "Urgence").forEach { sort ->
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
private fun ExitsList(
    exits: List<StockExit>,
    onExitSelected: (StockExit) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 20.dp)
    ) {
        items(exits) { exit ->
            var visible by remember { mutableStateOf(false) }
            
            LaunchedEffect(exit.id) {
                delay(50)
                visible = true
            }
            
            AnimatedVisibility(
                visible = visible,
                enter = slideInHorizontally(
                    animationSpec = tween(500, easing = EaseOutCubic),
                    initialOffsetX = { it }
                ) + fadeIn(animationSpec = tween(500))
            ) {
                ExitCard(
                    exit = exit,
                    onClick = { onExitSelected(exit) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExitCard(
    exit: StockExit,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    
    Card(
        onClick = {
            isPressed = true
            onClick()
        },
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = if (isPressed) 12.dp else 8.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = exit.status.color.copy(alpha = 0.15f)
            )
            .graphicsLayer {
                scaleX = if (isPressed) 0.98f else 1f
                scaleY = if (isPressed) 0.98f else 1f
            },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        LaunchedEffect(isPressed) {
            if (isPressed) {
                delay(100)
                isPressed = false
            }
        }
        
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // En-tête avec nom, statut et urgence
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = exit.productName,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = exit.category,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Badge de statut
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = exit.status.color.copy(alpha = 0.15f)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = exit.status.icon,
                                contentDescription = null,
                                tint = exit.status.color,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = exit.status.label,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = exit.status.color
                            )
                        }
                    }
                    
                    // Badge d'urgence si nécessaire
                    if (exit.urgency != ExitUrgency.LOW) {
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = exit.urgency.color.copy(alpha = 0.15f)
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = exit.urgency.icon,
                                    contentDescription = null,
                                    tint = exit.urgency.color,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = exit.urgency.label,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = exit.urgency.color
                                )
                            }
                        }
                    }
                }
            }
            
            // Informations détaillées
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                InfoItem(
                    label = "Quantité",
                    value = "${exit.quantity} unités",
                    modifier = Modifier.weight(1f)
                )
                InfoItem(
                    label = "Valeur totale",
                    value = "${String.format("%.2f", exit.totalValue)} €",
                    modifier = Modifier.weight(1f)
                )
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                InfoItem(
                    label = "Client",
                    value = exit.customer,
                    modifier = Modifier.weight(1f)
                )
                InfoItem(
                    label = "Date de sortie",
                    value = exit.exitDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                    modifier = Modifier.weight(1f)
                )
            }
            
            // Commande et adresse
            exit.orderNumber?.let { orderNumber ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    InfoItem(
                        label = "N° de commande",
                        value = orderNumber,
                        modifier = Modifier.weight(1f)
                    )
                    exit.deliveryAddress?.let { address ->
                        InfoItem(
                            label = "Adresse de livraison",
                            value = address.split(",").first(),
                            modifier = Modifier.weight(1f)
                        )
                    }
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
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun ExitDetailPane(
    exit: StockExit,
    onBack: () -> Unit,
    showBackButton: Boolean
) {
    var detailVisible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        delay(200)
        detailVisible = true
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        if (showBackButton) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainer)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
            }
        }
        
        AnimatedVisibility(
            visible = detailVisible,
            enter = slideInVertically(
                animationSpec = tween(600, easing = EaseOutCubic),
                initialOffsetY = { it / 2 }
            ) + fadeIn(animationSpec = tween(600))
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 12.dp,
                        shape = RoundedCornerShape(24.dp)
                    ),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // En-tête avec animation
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            exit.status.color,
                                            exit.status.color.copy(alpha = 0.7f)
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = exit.status.icon,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        
                        Column {
                            Text(
                                text = exit.productName,
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.ExtraBold
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Sortie ${exit.id}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                        thickness = 1.dp
                    )
                    
                    // Détails complets avec sections
                    DetailSection(
                        title = "Informations produit",
                        items = listOf(
                            "Catégorie" to exit.category,
                            "Quantité" to "${exit.quantity} unités",
                            "Prix unitaire" to "${String.format("%.2f", exit.unitPrice)} €",
                            "Valeur totale" to "${String.format("%.2f", exit.totalValue)} €"
                        )
                    )
                    
                    DetailSection(
                        title = "Informations client",
                        items = listOfNotNull(
                            "Client" to exit.customer,
                            exit.orderNumber?.let { "N° de commande" to it },
                            exit.deliveryAddress?.let { "Adresse de livraison" to it },
                            "Date de sortie" to exit.exitDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm"))
                        )
                    )
                    
                    DetailSection(
                        title = "Statut et priorité",
                        items = listOf(
                            "Statut" to exit.status.label,
                            "Urgence" to exit.urgency.label
                        )
                    )
                    
                    exit.notes?.let { notes ->
                        DetailSection(
                            title = "Notes",
                            items = listOf("Commentaires" to notes)
                        )
                    }
                    
                    // Actions avec animations
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedButton(
                            onClick = { /* TODO: Modifier la sortie */ },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Modifier")
                        }
                        
                        Button(
                            onClick = { /* TODO: Changer le statut */ },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = exit.status.color
                            )
                        ) {
                            Icon(
                                when (exit.status) {
                                    ExitStatus.PENDING -> Icons.Default.PlayArrow
                                    ExitStatus.PREPARED -> Icons.Default.LocalShipping
                                    ExitStatus.SHIPPED -> Icons.Default.CheckCircle
                                    else -> Icons.Default.Refresh
                                },
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                when (exit.status) {
                                    ExitStatus.PENDING -> "Préparer"
                                    ExitStatus.PREPARED -> "Expédier"
                                    ExitStatus.SHIPPED -> "Confirmer"
                                    else -> "Réactiver"
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
private fun DetailSection(
    title: String,
    items: List<Pair<String, String>>
) {
    var sectionVisible by remember { mutableStateOf(false) }
    
    LaunchedEffect(title) {
        delay(100)
        sectionVisible = true
    }
    
    AnimatedVisibility(
        visible = sectionVisible,
        enter = slideInVertically(
            animationSpec = tween(400, easing = EaseOutCubic),
            initialOffsetY = { it / 4 }
        ) + fadeIn(animationSpec = tween(400))
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            
            items.forEach { (label, value) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = value,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1.5f)
                    )
                }
            }
        }
    }
}