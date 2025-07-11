package org.babetech.borastock.ui.screens.suppliers

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Supplier(
    val id: String,
    val name: String,
    val category: String,
    val contactPerson: String,
    val email: String,
    val phone: String,
    val address: String,
    val city: String,
    val country: String,
    val productsCount: Int,
    val totalOrders: Int,
    val totalValue: Double,
    val rating: Float,
    val status: SupplierStatus,
    val reliability: SupplierReliability,
    val lastOrderDate: LocalDateTime,
    val paymentTerms: String,
    val notes: String?
)

enum class SupplierStatus(val label: String, val color: Color, val icon: ImageVector) {
    ACTIVE("Actif", Color(0xFF22c55e), Icons.Default.CheckCircle),
    INACTIVE("Inactif", Color(0xFF6b7280), Icons.Default.Pause),
    PENDING("En attente", Color(0xFFf59e0b), Icons.Default.Schedule),
    BLOCKED("Bloqué", Color(0xFFef4444), Icons.Default.Block)
}

enum class SupplierReliability(val label: String, val color: Color, val icon: ImageVector) {
    EXCELLENT("Excellent", Color(0xFF22c55e), Icons.Default.Star),
    GOOD("Bon", Color(0xFF3b82f6), Icons.Default.ThumbUp),
    AVERAGE("Moyen", Color(0xFFf59e0b), Icons.Default.Remove),
    POOR("Faible", Color(0xFFef4444), Icons.Default.ThumbDown)
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun SuppliersScreen() {
    val navigator = rememberSupportingPaneScaffoldNavigator()
    val scope = rememberCoroutineScope()
    
    var selectedSupplier by remember { mutableStateOf<Supplier?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Tous") }
    var sortBy by remember { mutableStateOf("Nom") }
    var isLoading by remember { mutableStateOf(true) }

    // Animation de chargement initial
    LaunchedEffect(Unit) {
        delay(800)
        isLoading = false
    }

    // Données d'exemple
    val suppliers = remember {
        listOf(
            Supplier(
                id = "SUP001",
                name = "Apple Inc.",
                category = "Électronique",
                contactPerson = "Jean Dupont",
                email = "contact@apple-france.com",
                phone = "+33 1 23 45 67 89",
                address = "12 Rue de Rivoli",
                city = "Paris",
                country = "France",
                productsCount = 25,
                totalOrders = 156,
                totalValue = 2500000.0,
                rating = 4.9f,
                status = SupplierStatus.ACTIVE,
                reliability = SupplierReliability.EXCELLENT,
                lastOrderDate = LocalDateTime.now().minusDays(2),
                paymentTerms = "30 jours",
                notes = "Fournisseur premium avec excellent service client"
            ),
            Supplier(
                id = "SUP002",
                name = "Samsung Electronics",
                category = "Électronique",
                contactPerson = "Marie Martin",
                email = "pro@samsung.fr",
                phone = "+33 1 34 56 78 90",
                address = "45 Avenue des Champs-Élysées",
                city = "Paris",
                country = "France",
                productsCount = 32,
                totalOrders = 203,
                totalValue = 1800000.0,
                rating = 4.7f,
                status = SupplierStatus.ACTIVE,
                reliability = SupplierReliability.EXCELLENT,
                lastOrderDate = LocalDateTime.now().minusDays(5),
                paymentTerms = "45 jours",
                notes = "Partenaire stratégique depuis 5 ans"
            ),
            Supplier(
                id = "SUP003",
                name = "Dell Technologies",
                category = "Informatique",
                contactPerson = "Pierre Dubois",
                email = "business@dell.fr",
                phone = "+33 1 45 67 89 01",
                address = "78 Boulevard Haussmann",
                city = "Paris",
                country = "France",
                productsCount = 18,
                totalOrders = 89,
                totalValue = 950000.0,
                rating = 4.3f,
                status = SupplierStatus.ACTIVE,
                reliability = SupplierReliability.GOOD,
                lastOrderDate = LocalDateTime.now().minusDays(10),
                paymentTerms = "30 jours",
                notes = "Bon rapport qualité-prix"
            ),
            Supplier(
                id = "SUP004",
                name = "Sony Corporation",
                category = "Audio/Vidéo",
                contactPerson = "Sophie Leroy",
                email = "pro@sony.fr",
                phone = "+33 1 56 78 90 12",
                address = "23 Rue de la Paix",
                city = "Lyon",
                country = "France",
                productsCount = 15,
                totalOrders = 67,
                totalValue = 720000.0,
                rating = 4.5f,
                status = SupplierStatus.ACTIVE,
                reliability = SupplierReliability.GOOD,
                lastOrderDate = LocalDateTime.now().minusDays(7),
                paymentTerms = "60 jours",
                notes = "Spécialiste audio haut de gamme"
            ),
            Supplier(
                id = "SUP005",
                name = "Microsoft France",
                category = "Logiciels",
                contactPerson = "Thomas Bernard",
                email = "enterprise@microsoft.fr",
                phone = "+33 1 67 89 01 23",
                address = "39 Quai du Président Roosevelt",
                city = "Issy-les-Moulineaux",
                country = "France",
                productsCount = 12,
                totalOrders = 45,
                totalValue = 580000.0,
                rating = 4.1f,
                status = SupplierStatus.PENDING,
                reliability = SupplierReliability.AVERAGE,
                lastOrderDate = LocalDateTime.now().minusDays(20),
                paymentTerms = "30 jours",
                notes = "En cours de négociation contrat"
            ),
            Supplier(
                id = "SUP006",
                name = "Xiaomi France",
                category = "Électronique",
                contactPerson = "Amélie Rousseau",
                email = "b2b@xiaomi.fr",
                phone = "+33 1 78 90 12 34",
                address = "15 Rue du Commerce",
                city = "Marseille",
                country = "France",
                productsCount = 28,
                totalOrders = 134,
                totalValue = 650000.0,
                rating = 3.8f,
                status = SupplierStatus.ACTIVE,
                reliability = SupplierReliability.AVERAGE,
                lastOrderDate = LocalDateTime.now().minusDays(3),
                paymentTerms = "45 jours",
                notes = "Bon rapport qualité-prix, délais parfois longs"
            ),
            Supplier(
                id = "SUP007",
                name = "HP Enterprise",
                category = "Informatique",
                contactPerson = "Nicolas Moreau",
                email = "channel@hpe.fr",
                phone = "+33 1 89 01 23 45",
                address = "92 Avenue de la Grande Armée",
                city = "Paris",
                country = "France",
                productsCount = 8,
                totalOrders = 23,
                totalValue = 180000.0,
                rating = 3.2f,
                status = SupplierStatus.BLOCKED,
                reliability = SupplierReliability.POOR,
                lastOrderDate = LocalDateTime.now().minusDays(45),
                paymentTerms = "30 jours",
                notes = "Problèmes de qualité récurrents - En révision"
            )
        )
    }

    val filteredSuppliers = suppliers.filter { supplier ->
        val matchesSearch = supplier.name.contains(searchQuery, ignoreCase = true) ||
                supplier.category.contains(searchQuery, ignoreCase = true) ||
                supplier.contactPerson.contains(searchQuery, ignoreCase = true) ||
                supplier.city.contains(searchQuery, ignoreCase = true)
        
        val matchesFilter = when (selectedFilter) {
            "Tous" -> true
            "Actifs" -> supplier.status == SupplierStatus.ACTIVE
            "Inactifs" -> supplier.status == SupplierStatus.INACTIVE
            "En attente" -> supplier.status == SupplierStatus.PENDING
            "Bloqués" -> supplier.status == SupplierStatus.BLOCKED
            else -> true
        }
        
        matchesSearch && matchesFilter
    }.let { suppliers ->
        when (sortBy) {
            "Nom" -> suppliers.sortedBy { it.name }
            "Catégorie" -> suppliers.sortedBy { it.category }
            "Note" -> suppliers.sortedByDescending { it.rating }
            "Commandes" -> suppliers.sortedByDescending { it.totalOrders }
            "Valeur" -> suppliers.sortedByDescending { it.totalValue }
            "Statut" -> suppliers.sortedBy { it.status.label }
            else -> suppliers
        }
    }

    SupportingPaneScaffold(
        value = navigator.scaffoldValue,
        directive = navigator.scaffoldDirective,
        mainPane = {
            AnimatedPane {
                SuppliersMainPane(
                    suppliers = filteredSuppliers,
                    searchQuery = searchQuery,
                    onSearchQueryChange = { searchQuery = it },
                    selectedFilter = selectedFilter,
                    onFilterChange = { selectedFilter = it },
                    sortBy = sortBy,
                    onSortChange = { sortBy = it },
                    onSupplierSelected = { supplier ->
                        selectedSupplier = supplier
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
                selectedSupplier?.let { supplier ->
                    SupplierDetailPane(
                        supplier = supplier,
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
private fun SuppliersMainPane(
    suppliers: List<Supplier>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedFilter: String,
    onFilterChange: (String) -> Unit,
    sortBy: String,
    onSortChange: (String) -> Unit,
    onSupplierSelected: (Supplier) -> Unit,
    isLoading: Boolean,
    showDetailButton: Boolean
) {
    var headerVisible by remember { mutableStateOf(false) }
    var searchVisible by remember { mutableStateOf(false) }
    var listVisible by remember { mutableStateOf(false) }

    LaunchedEffect(isLoading) {
        if (!isLoading) {
            headerVisible = true
            delay(200)
            searchVisible = true
            delay(200)
            listVisible = true
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
        // Loading indicator
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LoadingAnimation()
            }
        } else {
            // Header avec statistiques
            AnimatedVisibility(
                visible = headerVisible,
                enter = slideInVertically(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    ),
                    initialOffsetY = { -it }
                ) + fadeIn(animationSpec = tween(600))
            ) {
                SuppliersHeader(suppliers = suppliers)
            }
            
            // Barre de recherche et filtres
            AnimatedVisibility(
                visible = searchVisible,
                enter = slideInVertically(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    ),
                    initialOffsetY = { -it / 2 }
                ) + fadeIn(animationSpec = tween(600, delayMillis = 200))
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
            
            // Liste des fournisseurs
            AnimatedVisibility(
                visible = listVisible,
                enter = slideInVertically(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    ),
                    initialOffsetY = { it / 2 }
                ) + fadeIn(animationSpec = tween(600, delayMillis = 400))
            ) {
                SuppliersList(
                    suppliers = suppliers,
                    onSupplierSelected = onSupplierSelected,
                    modifier = Modifier.weight(1f)
                )
            }
            
            // FAB animé
            AnimatedVisibility(
                visible = listVisible,
                enter = scaleIn(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                ) + fadeIn(animationSpec = tween(400, delayMillis = 600))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    AnimatedFAB()
                }
            }
        }
    }
}

@Composable
private fun LoadingAnimation() {
    val infiniteTransition = rememberInfiniteTransition(label = "loading")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            Icons.Default.People,
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .rotate(rotation),
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            "Chargement des fournisseurs...",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun AnimatedFAB() {
    val infiniteTransition = rememberInfiniteTransition(label = "fab_pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    FloatingActionButton(
        onClick = { /* TODO: Ajouter nouveau fournisseur */ },
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier
            .scale(scale)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Icon(
            Icons.Default.Add,
            contentDescription = "Ajouter un fournisseur",
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
private fun SuppliersHeader(suppliers: List<Supplier>) {
    val totalSuppliers = suppliers.size
    val activeSuppliers = suppliers.count { it.status == SupplierStatus.ACTIVE }
    val pendingSuppliers = suppliers.count { it.status == SupplierStatus.PENDING }
    val totalValue = suppliers.sumOf { it.totalValue }
    val averageRating = if (suppliers.isNotEmpty()) suppliers.map { it.rating }.average() else 0.0

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
            // Titre avec animation
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
                            Icons.Default.People,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Gestion des Fournisseurs",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Partenaires et relations commerciales",
                            style = MaterialTheme.typography.bodyLarge,
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
                    Triple("Total", totalSuppliers.toString(), Icons.Default.Business),
                    Triple("Actifs", activeSuppliers.toString(), Icons.Default.CheckCircle),
                    Triple("En attente", pendingSuppliers.toString(), Icons.Default.Schedule),
                    Triple("Note moy.", String.format("%.1f", averageRating), Icons.Default.Star)
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
                        ) + fadeIn(animationSpec = tween(400))
                    ) {
                        SupplierStatCard(
                            title = title,
                            value = value,
                            icon = icon,
                            color = when (index) {
                                0 -> MaterialTheme.colorScheme.primary
                                1 -> Color(0xFF22c55e)
                                2 -> Color(0xFFf59e0b)
                                3 -> Color(0xFF8b5cf6)
                                else -> MaterialTheme.colorScheme.primary
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
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
                            text = "Valeur totale des commandes",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = "${String.format("%.0f", totalValue / 1000)}K €",
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
private fun SupplierStatCard(
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
                label = { Text("Rechercher un fournisseur...") },
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
                        listOf("Tous", "Actifs", "Inactifs", "En attente", "Bloqués").forEach { filter ->
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
                        listOf("Nom", "Catégorie", "Note", "Commandes", "Valeur", "Statut").forEach { sort ->
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
private fun SuppliersList(
    suppliers: List<Supplier>,
    onSupplierSelected: (Supplier) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        itemsIndexed(suppliers) { index, supplier ->
            var visible by remember { mutableStateOf(false) }
            
            LaunchedEffect(Unit) {
                delay(index * 100L)
                visible = true
            }
            
            AnimatedVisibility(
                visible = visible,
                enter = slideInHorizontally(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    ),
                    initialOffsetX = { it }
                ) + fadeIn(animationSpec = tween(400))
            ) {
                SupplierCard(
                    supplier = supplier,
                    onClick = { onSupplierSelected(supplier) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SupplierCard(
    supplier: Supplier,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "card_scale"
    )

    Card(
        onClick = {
            isPressed = true
            onClick()
        },
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = supplier.status.color.copy(alpha = 0.1f)
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
            // En-tête avec nom et statut
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = supplier.name,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = supplier.category,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = supplier.status.color.copy(alpha = 0.1f)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = supplier.status.icon,
                                contentDescription = null,
                                tint = supplier.status.color,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = supplier.status.label,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = supplier.status.color
                            )
                        }
                    }
                    
                    // Note avec étoiles
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFfbbf24),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = String.format("%.1f", supplier.rating),
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
            
            // Informations de contact
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                InfoItem(
                    label = "Contact",
                    value = supplier.contactPerson,
                    modifier = Modifier.weight(1f)
                )
                InfoItem(
                    label = "Ville",
                    value = supplier.city,
                    modifier = Modifier.weight(1f)
                )
            }
            
            // Métriques business
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                InfoItem(
                    label = "Produits",
                    value = "${supplier.productsCount} réf.",
                    modifier = Modifier.weight(1f)
                )
                InfoItem(
                    label = "Commandes",
                    value = supplier.totalOrders.toString(),
                    modifier = Modifier.weight(1f)
                )
                InfoItem(
                    label = "Valeur totale",
                    value = "${String.format("%.0f", supplier.totalValue / 1000)}K €",
                    modifier = Modifier.weight(1f)
                )
            }
            
            // Badge de fiabilité
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = supplier.reliability.color.copy(alpha = 0.1f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = supplier.reliability.icon,
                            contentDescription = null,
                            tint = supplier.reliability.color,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Fiabilité ${supplier.reliability.label}",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = supplier.reliability.color
                        )
                    }
                }
                
                Text(
                    text = "Dernière commande: ${supplier.lastOrderDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
    
    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(100)
            isPressed = false
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
private fun SupplierDetailPane(
    supplier: Supplier,
    onBack: () -> Unit,
    showBackButton: Boolean
) {
    var detailsVisible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        delay(200)
        detailsVisible = true
    }

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
        
        AnimatedVisibility(
            visible = detailsVisible,
            enter = slideInVertically(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                ),
                initialOffsetY = { it / 2 }
            ) + fadeIn(animationSpec = tween(600))
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    // Header du fournisseur
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
                            // En-tête avec logo et statut
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(
                                            brush = Brush.linearGradient(
                                                colors = listOf(
                                                    supplier.status.color.copy(alpha = 0.2f),
                                                    supplier.status.color.copy(alpha = 0.1f)
                                                )
                                            )
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = supplier.name.take(2).uppercase(),
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = supplier.status.color
                                    )
                                }
                                
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = supplier.name,
                                        style = MaterialTheme.typography.headlineSmall.copy(
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = supplier.category,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    
                                    // Note avec étoiles
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        modifier = Modifier.padding(top = 4.dp)
                                    ) {
                                        repeat(5) { index ->
                                            Icon(
                                                Icons.Default.Star,
                                                contentDescription = null,
                                                tint = if (index < supplier.rating.toInt()) 
                                                    Color(0xFFfbbf24) 
                                                else 
                                                    MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                        Text(
                                            text = "(${String.format("%.1f", supplier.rating)})",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                                
                                Card(
                                    shape = RoundedCornerShape(20.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = supplier.status.color.copy(alpha = 0.1f)
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(
                                            imageVector = supplier.status.icon,
                                            contentDescription = null,
                                            tint = supplier.status.color,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Text(
                                            text = supplier.status.label,
                                            style = MaterialTheme.typography.labelLarge.copy(
                                                fontWeight = FontWeight.Bold
                                            ),
                                            color = supplier.status.color
                                        )
                                    }
                                }
                            }
                            
                            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                            
                            // Métriques principales
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                MetricCard(
                                    title = "Produits",
                                    value = supplier.productsCount.toString(),
                                    subtitle = "références",
                                    icon = Icons.Default.Inventory,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.weight(1f)
                                )
                                MetricCard(
                                    title = "Commandes",
                                    value = supplier.totalOrders.toString(),
                                    subtitle = "total",
                                    icon = Icons.Default.ShoppingCart,
                                    color = Color(0xFF3b82f6),
                                    modifier = Modifier.weight(1f)
                                )
                                MetricCard(
                                    title = "Valeur",
                                    value = "${String.format("%.0f", supplier.totalValue / 1000)}K",
                                    subtitle = "euros",
                                    icon = Icons.Default.TrendingUp,
                                    color = Color(0xFF22c55e),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
                
                item {
                    // Informations de contact
                    DetailSection(
                        title = "Informations de contact",
                        icon = Icons.Default.ContactPhone,
                        items = listOf(
                            "Personne de contact" to supplier.contactPerson,
                            "Email" to supplier.email,
                            "Téléphone" to supplier.phone,
                            "Adresse" to supplier.address,
                            "Ville" to "${supplier.city}, ${supplier.country}"
                        )
                    )
                }
                
                item {
                    // Informations commerciales
                    DetailSection(
                        title = "Informations commerciales",
                        icon = Icons.Default.Business,
                        items = listOf(
                            "Conditions de paiement" to supplier.paymentTerms,
                            "Fiabilité" to supplier.reliability.label,
                            "Dernière commande" to supplier.lastOrderDate.format(
                                DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm")
                            ),
                            "Statut" to supplier.status.label
                        )
                    )
                }
                
                item {
                    // Notes
                    supplier.notes?.let { notes ->
                        DetailSection(
                            title = "Notes et commentaires",
                            icon = Icons.Default.Notes,
                            items = listOf("Commentaires" to notes)
                        )
                    }
                }
                
                item {
                    // Actions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = { /* TODO: Modifier le fournisseur */ },
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
                            onClick = { /* TODO: Nouvelle commande */ },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            enabled = supplier.status == SupplierStatus.ACTIVE
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Commander")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    subtitle: String,
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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun DetailSection(
    title: String,
    icon: ImageVector,
    items: List<Pair<String, String>>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
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
}